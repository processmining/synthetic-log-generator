package nl.tue.declare.appl.worklist.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import nl.tue.declare.execution.WorkItem;

public class ForwardedWorkItemPanel  extends WorkItemPanel{

	private static final long serialVersionUID = 1L;
	
	ForwardedWorkItemPanel(WorkItem workItem) {
		super(workItem);
		this.add(new JLabel(workItem.getActivity().getExternalCase().getCaseID()), BorderLayout.NORTH);
	}
}
