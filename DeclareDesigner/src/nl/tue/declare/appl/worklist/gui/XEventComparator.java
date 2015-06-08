package nl.tue.declare.appl.worklist.gui;

import java.util.Comparator;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XOrganizationalExtension;
import org.deckfour.xes.model.XEvent;

/**
 * 
 * @author mwesterg
 *
 */
public class XEventComparator implements Comparator<XEvent> {

	@Override
	public int compare(XEvent arg0, XEvent arg1) {
		if (arg0 == null) return arg1 == null?0:-1;
		if (arg1 == null) return 1;
		
		String name0 = XConceptExtension.instance().extractName(arg0);
		String name1 = XConceptExtension.instance().extractName(arg1);
		if (name0 == null && name1 != null) return -1;
		if (name1 == null && name0 != null) return 1;
		int first = name0 == null?0:name0.compareTo(name1);
		if (first != 0) return first;
		
		String instance0 = XConceptExtension.instance().extractInstance(arg0);
		String instance1 = XConceptExtension.instance().extractInstance(arg1);
		if (instance0 == null && instance1 != null) return -1;
		if (instance1 == null && instance0 != null) return 1;
		int second = instance0 == null?0:instance0.compareTo(instance1);
		if (second != 0) return second;

		String transition0 = XLifecycleExtension.instance().extractTransition(arg0);
		String transition1 = XLifecycleExtension.instance().extractTransition(arg1);
		if (transition0 == null && transition1 != null) return -1;
		if (transition1 == null && transition0 != null) return 1;
		int third = transition0 == null?0:transition0.compareTo(transition1);
		if (third != 0) return second;

		String resource0 = XOrganizationalExtension.instance().extractResource(arg0);
		String resource1 = XOrganizationalExtension.instance().extractResource(arg1);
		if (resource0 == null && resource1 != null) return -1;
		if (resource1 == null && resource0 != null) return 1;
		int fourth = resource0 == null?0:resource0.compareTo(resource1);
		if (fourth != 0) return second;

		return 0;
	}

}
