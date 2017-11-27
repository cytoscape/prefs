package org.lib;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * JFormattedTextField for use with doubles and verification.
 */
public class RangedDoubleTextField extends JFormattedTextField {

	/**
	 * RangedDoubleTextField: A numerical entry box for decimal numbers supports
	 * arrow keys to increment / decrement the value
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final RangedDoubleVerifier dVerifier;
	private final INumberFormatter f;

	/**
	 * Ranged field. Default prefs formatting.
	 * 
	 */
	public RangedDoubleTextField(double min, double max, INumberFormatter formatter) {
		f = formatter;
		dVerifier = new RangedDoubleVerifier(this, min, max, formatter);
		init(dVerifier);
	}

	/**
	 * Un-Ranged field. Default prefs formatting.
	 * 
	 */
	public RangedDoubleTextField(INumberFormatter formatter) {
		f = formatter;
		dVerifier = new RangedDoubleVerifier(this, formatter);
		init(dVerifier);
	}

	public RangedDoubleTextField(double min, double max, INumberFormatter formatter, Dimension size) {
		this(min, max, formatter);
		GuiFactory.setSizes(this, size);
	}

	private void init(RangedDoubleVerifier dv) {
		this.setDocument(new DoubleTextFieldDocument());
		this.setInputVerifier(dv);
		setHorizontalAlignment(JTextField.RIGHT);
	}

	/**
	 * Get the cached original value as a Double
	 * 
	 */
	public double getOriginalValue() {
		return fOriginal;
	}

	public void rememberOriginal() {
		if (Double.isNaN(fOriginal))
			fOriginal = getDouble();
	}// resetBorder();

	public void setEditedBorder() {
		setBorder(Borders.red);
	}

	public void reset() {
		resetBorder();
		setValue(fOriginal);
	}

	public void resetBorder() {
		setBorder(Borders.etchedRaised);
	}

	public Double getDouble() {
		return f.parseDouble(getText());
	}

	public void setDouble(double d) {
		setValue(d);
	}

	public void setValue(double d) {
		setText(f.formattedDouble(d));
	}

	public void setMin(double d) {
		dVerifier.setMin(d);
	}

	public double getMin() {
		return dVerifier.getMin();
	}

	public void setMax(double d) {
		dVerifier.setMax(d);
	}

	public double getMax() {
		return dVerifier.getMax();
	}

	/**
	 * Get error label Use for 1:1 mapping of field to error label
	 * 
	 */
	public JLabel getErrorLabel() {
		return dVerifier.getErrorLabel();
	}

	/**
	 * Set error label Use for Many:1 mapping of field to error label
	 */
	public void setErrorLabel(JLabel label) {
		dVerifier.setErrorLabel(label);
	}

	public RangedDoubleVerifier getRangedDoubleVerifier() {
		return dVerifier;
	}

	@Override
	public boolean isEditValid() {
		return dVerifier.checkField(this);
	}

	private double fOriginal = Double.NaN;
	private double fIncrement = 1;
	private double fCtrlIncrement = .5;
	private double fAltIncrement = .01;
	private double fShiftIncrement = 4;
	private boolean fSupportArrowKeys = false;

	public void supportArrowKeyIncrement() {
		fSupportArrowKeys = true;
	}

	@Override
	public void processKeyEvent(KeyEvent ev) {
		int code = ev.getKeyCode();
		if (fSupportArrowKeys && (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN)) {
			if (ev.getID() != KeyEvent.KEY_PRESSED)
				return;
			int sign = (code == KeyEvent.VK_UP) ? 1 : -1;
			boolean altDown = ev.isAltDown();
			boolean ctrlDown = ev.isControlDown();
			boolean shiftDown = ev.isShiftDown();

			double increment = fIncrement;
			if (ctrlDown)
				increment = fCtrlIncrement;
			else if (altDown)
				increment = fAltIncrement;
			else if (shiftDown)
				increment = fShiftIncrement;
			double d = getDouble();
			d += sign * increment;
			setValue(d);
			// SoundUtil.click();
			ev.consume();
			super.processKeyEvent(ev);
			return;
		}
		if (!ev.isConsumed()) {
			if (Character.isLetter(ev.getKeyChar()))
				return;
			super.processKeyEvent(ev);
		}

	}

	public boolean hasText() {
		return getText() != null && getText().length() > 0;
	}

}
