package org.lib;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.NumberFormatter;
/**
 * For use with RangedDoubleTextField.java
 * Verifies a ranged double text field.  Replaces field with last value if new
 * input isn't verifiable.  Colors text red if it is unverifiable as it is typed in.
 * 
 * @author Seth
 *
 */

public class RangedDoubleVerifier extends InputVerifier implements FocusListener, KeyListener{
	

	
	static DictKey INVALID = new DictKey("string.validnumber");		 
	static DictKey AND = new DictKey("plat.bool.and");
	static DictKey INVALID_INPUT = new DictKey("string.invalidinput");
	
	
	private RangedDoubleTextField myField;
	private String lastGoodValue;
	private double min,max;
	private INumberFormatter f;
	private Color dFgColor;
	private JLabel error;
	
	/**
	 * Use with ranged doubles.  
	 * Default prefs formattings.
	 * Includes Error JLabel.
	 * 
	 */
	public RangedDoubleVerifier(RangedDoubleTextField field, double minimum, double maximum, INumberFormatter formatter){
		super();
		min = minimum;
		max = maximum;
		f = formatter;
		init(field);
	}
	
	/**
	 * Use with un-ranged doubles. (sets range as neg-infinity<->pos-infinity)
	 * Default prefs formattings.
	 * 
	 */
	public RangedDoubleVerifier(RangedDoubleTextField field, INumberFormatter formatter){
		this(field, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, formatter);
		init(field);
	}
	
	private void init(RangedDoubleTextField field){
		dFgColor = field.getForeground();
		myField=field;
		myField.addFocusListener(this);
		myField.addKeyListener(this);
		lastGoodValue="";
		error = new JLabel();
		setErrorLabel(error);
	}
	
	public void setMin(double d)	{	min = d;	}
	public double getMin()		{	return min;		}
	public void setMax(double d)	{	max = d;	}
	public double getMax()		{	return max;		}
//	public void setRange(Range r)
//	{
//		setMin(r.getMin());
//		setMax(r.getMax());
//	}
	
	@Override public boolean verify(JComponent input) {
        boolean good = checkField(input);
        if(good){
        	lastGoodValue=myField.getText();
        }
        else{
        	myField.setText(lastGoodValue);
        	myField.setForeground(dFgColor);
        	setErrorLabel(error);
    		error.setVisible(false);
        }
        return good;
    }
	
	/**
	 * Checks validity of field entry as double within range
	 *
	 */
	public boolean checkField(JComponent input){
	
		String inText = myField.getText();
		double myNum;
		myNum = f.parseDouble(inText);
		if (Double.isNaN(myNum))
			return false;

		if(myNum<min || myNum>max)
			return false;
		return true;		
	}
	
	public JLabel getErrorLabel()	{		return error;			}
	public void setErrorLabel(JLabel label)
	{
		error=label;
		if(max==Double.MAX_VALUE && min==Double.MIN_VALUE)
			error.setText(INVALID_INPUT.lookup()); 						//default error message if unranged values
		else if(max==Double.MAX_VALUE)
			error.setText(INVALID.lookup() + " >= "+ f.formattedDouble(min));
		else if(min==Double.MIN_VALUE)
			error.setText(INVALID.lookup() + " <= "+ f.formattedDouble(max));
		else error.setText(INVALID.lookup() + " >= " + f.formattedDouble(min)
				+" " + AND.lookup() + " <= "+	f.formattedDouble(max));
		error.setVisible(false);
		error.setForeground(Color.RED);
	}
	//--------------------------------------------------------------------------------------------

	@Override	public void focusGained(FocusEvent e) {		lastGoodValue=myField.getText();	}
	@Override	public void focusLost(FocusEvent e) {
			double value = myField.getDouble();
			if (!Double.isNaN(value))
				myField.setText(f.formattedDouble(value));		//TODO AM 12/26/12 check behavior for loosing focus with invalid content
			myField.setForeground(dFgColor);
			setErrorLabel(error);
			error.setVisible(false);
//			Action action = (myField.getAction() instanceof Action) ? (Action)myField.getAction() : null;
//			if(action!=null)
//				action.actionPerformed(new ActionEvent(myField, 0, action.getActionCmd()));
	}
//--------------------------------------------------------------------------------------------
	@Override	public void keyPressed(KeyEvent e) {}
	@Override	public void keyTyped(KeyEvent e){}

	@Override	public void keyReleased(KeyEvent e)
	{
		setErrorLabel(error);
		boolean ok = checkField(myField);
		myField.setForeground(ok ? dFgColor : Color.RED);
		error.setVisible(!ok);
	}

}