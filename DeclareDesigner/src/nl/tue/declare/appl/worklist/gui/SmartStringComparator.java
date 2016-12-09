package nl.tue.declare.appl.worklist.gui;

import java.util.Comparator;

/**
 * 
 * @author mwesterg
 *
 */
public class SmartStringComparator implements Comparator<String> {
	@Override
	public int compare(String arg0, String arg1) {
		if (arg0 == null) return arg1 == null?0:-1;
		if (arg1 == null) return 1;
		
		try {
			Double d0 = Double.valueOf(arg0);
			Double d1 = Double.valueOf(arg1);
			return d0.compareTo(d1);
		} catch (NumberFormatException e) {
			// Ignore if values are not numbers
		}
		
		return arg0.compareTo(arg1);
	}

}
