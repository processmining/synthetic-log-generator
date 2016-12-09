package nl.tue.declare.appl.design.template.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.template.*;

public class FrmGroup extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377907087297027363L;

	private JTextField name = new JTextField("");

	public FrmGroup(Frame owner, JInternalFrame aMonitorFrame) {
		super(owner, "Group", aMonitorFrame);
	}

	public void fromGroup(LanguageGroup group) {
		if (group != null) {
			name.setText(group.getName());
		} else {
			name.setText("");
		}
	}

	public void toGroup(LanguageGroup group) {
		if (group != null) {
			group.setName(name.getText());
		}
	}

	@Override
	protected Component getContent() {
		JPanel topPanel = new JPanel(new GridLayout(1, 2, 2, 2));
		topPanel.add(new JLabel("name"));
		topPanel.add(name);
		return topPanel;
	}

}
