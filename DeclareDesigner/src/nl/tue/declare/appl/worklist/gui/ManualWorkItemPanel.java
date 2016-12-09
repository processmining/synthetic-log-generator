package nl.tue.declare.appl.worklist.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import nl.tue.declare.appl.worklist.IAssignmentExecutionListener;
import nl.tue.declare.execution.WorkItem;

public class ManualWorkItemPanel extends WorkItemPanel {

	private static final long serialVersionUID = 1L;

	private DataPanel data;
	private JButton button = new JButton("complete");
	private JButton btnCancel = new JButton("cancel");

	private IAssignmentExecutionListener listener = null;

	public ManualWorkItemPanel(WorkItem workItem) {
		super(workItem);
		data = new DataPanel(getWorkItem());
		this.add(data, BorderLayout.CENTER);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				complete();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(button);
		buttonPanel.add(btnCancel);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * buildInterface
	 * 
	 * @todo Implement this nl.tue.declare.appl.util.swing.DefaultPanel method
	 */
	protected void buildInterface() {

	}

	void setEnabledComplete(boolean enabled) {
		this.button.setEnabled(enabled);
	}

	void setEnabledCancel(boolean enabled) {
		this.btnCancel.setEnabled(enabled);
	}

	boolean getEnabledComplete() {
		return this.button.isEnabled();
	}

	/**
	 * setListener
	 */
	public void setListener(IAssignmentExecutionListener listener) {
		this.listener = listener;
	}

	private void complete() {
		if (listener != null) {
			listener.completeActivity(getWorkItem());
		}
	}

	private void cancel() {
		if (listener != null) {
			listener.cancelActivity(getWorkItem());
		}
	}

	public boolean readData() {
		return this.data.read();
	}

}
