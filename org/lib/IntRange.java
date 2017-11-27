package org.lib;



public class IntRange
{
	private static final String RANGE_STR_FORMAT_ERR = "Integer range format did not match min:max";
    private int fMin, fMax;
    public IntRange(String specStr) throws ValidationException
    {
    	String[] parts = specStr.split(":");
    	if (parts.length != 2)
    		throw new ValidationException(RANGE_STR_FORMAT_ERR);
    	try 
    	{
    		fMin = Integer.parseInt(parts[0].trim());
    		fMax = Integer.parseInt(parts[1].trim());
    	} catch (NumberFormatException ex)
    	{
    		throw new ValidationException(RANGE_STR_FORMAT_ERR);
    	}
    }
	public IntRange(int min, int max)	{		set(min, max);	}
	
	public class ValidationException extends Exception 
	{
		ValidationException(String s) {}
	}
	
	public IntRange(int min, int max, boolean dontRearrange)
	{
		if (dontRearrange)				{	fMin = min;		fMax = max;		}
		else set(min, max);
	}

// 	public IntRange(Range inRange)		{		this((int)Math.round(inRange.getMin()), (int)Math.round(inRange.getMax()));	}

 	final static String RANGE = "Range";
 	final static String MIN = "min";
 	final static String MAX = "max";
  	public IntRange(IntRange inRange)	{		this(inRange.getMin(), inRange.getMax());	}

    public IntRange(SElement element)
    {
//        fMin = element.getInt("x1");		// not the same as getElement produces, but this is called by NamedRange, which is unused
//        fMax = element.getInt("x2");
        fMin = element.getInt(MIN);
        fMax = element.getInt(MAX);
    }
//---------------------------------------------------------------------------------------------
   public static IntRange intersection(IntRange a, IntRange b)
    {
        return new IntRange(Math.max(a.getMin(), b.getMin()), Math.min(a.getMax(), b.getMax()));
    }

//  public static IntRange intersection(IntRange a, Range b)
//    {
//        return new IntRange((int) Math.max(a.getMin(), b.getMin()), (int) Math.min(a.getMax(), b.getMax()));
//    }

//---------------------------------------------------------------------------------------------
    public SElement getElement()
    {
        SElement result = new SElement(RANGE);
        result.setInt(MIN, getMin());
        result.setInt(MAX, getMax());
        return result;
    }
    
    public String getSpecString()		{	return getMin() + ":" + getMax();	}

	public void setMin(int min) { 		fMin = min;   	}
	public void setMax(int max) {		fMax = max;   	}
	public void set(int min, int max) {		fMin = Math.min(min, max); 		fMax = Math.max(min, max);
	}
	public int getMin() {		return fMin; 	}
	public int getMax() {		return fMax;  	}

	public int pin(int inVal)
	{
		if (inVal < fMin) return fMin;
		if (inVal > fMax) return fMax;
		return inVal;
	}
	/** Ensure this range is between min and max */
	public void pin(int min, int max)
	{
		fMin = Math.max(min, fMin);
		fMax = Math.min(max, fMax);
	}
	public boolean isEmpty()     	{  		return fMax <= fMin;   	}
	public int width()        		{       return fMax - fMin;     	}
	@Override public String toString()  {  	return fMin + " - " + fMax;     	}
	@Override public boolean equals(Object o)
	{
		if (super.equals(o)) return true;
		if (!(o instanceof IntRange)) return false;
		IntRange or = (IntRange) o;
		return getMin() == or.getMin() && getMax() == or.getMax();
	}
	public boolean overlap(IntRange other)	{	return getMin() < other.getMax() && getMax() > other.getMin();		}
	public boolean engulfs(IntRange other)	{	return getMin() <= other.getMin() && getMax() >= other.getMax();	}
	public boolean engulfs(int value)		{	return getMin() <= value && value <= getMax();						}
	public boolean engulfs(double value)	{	return engulfs((int) value);						}

	public IntRange subrange(int i, int nSubs)
	{
	       
        int step = Math.round(width() / nSubs);		
        int min = getMin();
        int top = Math.min(min + i + step + step, getMax());
        return new IntRange(min + i * step, top);
//    CHANGED ROUNDING CONSEQUENCES.    
//        	// rounding favors S3 as biggest subphase
//        if (i < nSubs/2)	set(lo + i * step, lo + (i + 1) * step); 	
//        else 				set(hi - (sSubs - i) * step, lo + (i + 1) * step); 	
//        switch (i)
//        {
//            case 0:     range.setMax(lo + step);              break;
//            case 1:     range.set(lo + step, lo + 2 * step);  break;
//            case kS3:     range.set(lo + 2 * step, hi - step);  break;
//            case kS4:     range.setMin(hi - step);              break;
//            case kS:
//            case k2S:
//        }
//		return null;
	}
}
