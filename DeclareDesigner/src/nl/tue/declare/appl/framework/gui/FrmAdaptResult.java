package nl.tue.declare.appl.framework.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.verification.*;
import nl.tue.declare.domain.model.AssignmentModel;
import nl.tue.declare.verification.*;

public class FrmAdaptResult extends CloseDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6690172337792519141L;

	private JList instances = new JList();
	private VerificationPanel verificationPanel = new VerificationPanel();
	private TPanel resultsPanel = new TPanel(new BorderLayout(), "Errors");

	/**
	 * 
	 * @param parent
	 *            Frame
	 */
	public FrmAdaptResult(Frame parent) {
		super(parent, "Dynamic change results");
		instances.setCellRenderer(new VerificationResultListRenderer());
		instances.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						VerificationResult result = (VerificationResult) instances
								.getSelectedValue();
						if (result != null) {
							AssignmentModel assignment = result.getModel();
							verificationPanel.clear();
							if (result != null) {
								verificationPanel
										.fromVerificationResult(result);
								resultsPanel.setTitle("Errors for instance \""
										+ assignment.toString() + "\"");
							}
						}
					}
				});
	}

	public void clear() {
		FrameUtil.clearList(instances);
		verificationPanel.clear();
	}

	public void add(VerificationResult result, AssignmentModel assignment) {
		FrameUtil.addToList(instances, result);
		if (instances.getModel().getSize() == 1) {
			instances.setSelectedIndex(0);
		}
	}

	@Override
	protected Component getContent() {
		JComponent instancesPanel = new TPanel(new BorderLayout(), "Instances");
		instancesPanel.add(new JScrollPane(instances), BorderLayout.CENTER);

		resultsPanel.add(verificationPanel, BorderLayout.CENTER);

		return new JSplitPane(JSplitPane.VERTICAL_SPLIT, instancesPanel,
				resultsPanel);
	}

	public class VerificationResultListRenderer extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2436631139508901502L;

		public VerificationResultListRenderer() {
			super();
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof VerificationResult) {
				VerificationResult result = (VerificationResult) value;
				if (!result.isEmpty()) {
					setForeground(Color.RED);
				}
			}
			return this;
		}

	}

}
