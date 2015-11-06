package com.example.buckup;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterEditText implements InputFilter {
	
	
	 double min;
	private int max;

	    public InputFilterEditText(double min, int max) {
	        this.min = min;
	        this.max = max;
	    }

	    public InputFilterEditText(String min, String max) {
	        this.min = Double.parseDouble(min);
	        this.max = Integer.parseInt(max);
	    }

	    @Override
	    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
	        try {
	            int input = Integer.parseInt(dest.toString() + source.toString());
	            if (isInRange(min, max, input))
	                return null;
	        } catch (NumberFormatException nfe) { }     
	        return "";
	    }
	    
	    public Integer filter(){
	    	
	    	
			return 0;
	    	
	    	
	    }

	    private boolean isInRange(double a, int b, double c) {
	        return b > a ? c >= a && c <= b : c >= b && c <= a;
	    }

}
