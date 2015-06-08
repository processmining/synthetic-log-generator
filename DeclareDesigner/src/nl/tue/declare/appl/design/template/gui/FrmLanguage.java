package nl.tue.declare.appl.design.template.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.template.Language;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class FrmLanguage extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8627121441926297112L;

	private JTextField name = new JTextField("");

	public FrmLanguage(Frame owner, String title, JInternalFrame aMonitorFrame) {
		super(owner, title, aMonitorFrame);
	}

	public void fromLanguage(Language lang) {
		if (lang != null) {
			name.setText(lang.getName());
		} else {
			name.setText("");
		}
	}

	public void toLanguage(Language lang) {
		if (lang != null) {
			lang.setName(name.getText());
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
