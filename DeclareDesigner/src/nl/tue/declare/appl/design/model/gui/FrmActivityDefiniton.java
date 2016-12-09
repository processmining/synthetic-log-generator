package nl.tue.declare.appl.design.model.gui;

import info.clearthought.layout.TableLayout;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import nl.tue.declare.appl.util.*;

import nl.tue.declare.appl.util.swing.TPanel;
import nl.tue.declare.domain.model.ActivityDefinition;

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
public class FrmActivityDefiniton extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8434758504844770907L;

	private JTextField name = new JTextField("");

	private PanelAuthorization authorization = new PanelAuthorization();

	private ActivityDataPanel data = new ActivityDataPanel();

	private JCheckBox external = new JCheckBox("trigger YAWL process");

	private ExternalCasePanel extPanel = new ExternalCasePanel();

	private static double p = TableLayout.PREFERRED;
	private static double size[][] = { { 80, TableLayout.FILL },
			{ p, p, 200, p } };

	private TableLayout layout = new TableLayout(size);

	public FrmActivityDefiniton(Frame owner, JComponent aMonitorFrame) {
		super(owner, "Activity definition", aMonitorFrame);
			name.setToolTipText("Enter activity name.");
			name.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						FrmActivityDefiniton.this.positive.actionPerformed(null);
					}
				}

				public void keyReleased(KeyEvent e) {/* ignore */
				}

				public void keyTyped(KeyEvent e) {/* ignore */
				}

			});
	}
	private void setExternalPanel(boolean visible) {
		FrmActivityDefiniton.this.extPanel.setVisible(visible);
		pack();
	}

	private JPanel getExternalPanel() {
		JPanel main = new TPanel(new BorderLayout(), "YAWL");
		main.add(this.external, BorderLayout.NORTH);
		main.add(extPanel, BorderLayout.CENTER);
		return main;
	}


	/**
	 * fillFormFromRole
	 * 
	 * @param activity
	 *            Role
	 * @param anForm
	 *            FrmRole
	 */
	public void fromActivity(ActivityDefinition activity) {
		String name = "";
		if (activity != null) {
			name = activity.getName();
		}
		this.name.setText(name);

		if (activity.isExternal()) {
			extPanel.fromExternalCase(activity.getExternalCase());
		}
		external.setSelected(activity.isExternal());
		setExternalPanel(activity.isExternal());
		authorization.start(activity.getAuthorization());

		data.init(activity);
		//this.validate();
		//this.repaint();
		pack();
	}

	/**
	 * fillActivityDefinitionFromForm
	 * 
	 * @param activity
	 *            ActivityDefinition
	 * @param anForm
	 *            FrmActivityDefinition
	 */
	public void toActivity(ActivityDefinition activity) {
		if (activity != null) {
			activity.setName(name.getText());
			activity.getAuthorization().clear();
			activity.getAuthorization().authorize(
					authorization.getSelected());
			activity.setExternal(external.isSelected());
			if (activity.isExternal()) {
				extPanel.toExternalCase(activity.getExternalCase());
			}
		}
	}

	public ActivityDataPanel getDataPanel() {
		return this.data;
	}

	@Override
	protected Component getContent() {

		// *** prepare the bottom part of the screen with buttons
		JPanel content = new JPanel(layout);
	

		this.external.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				setExternalPanel(e.getStateChange() == ItemEvent.SELECTED);
			}

		});

		content.add(new JLabel("name"), "0,0");
		content.add(name, "1,0");

		content.add(authorization, "0,1,1,1");
		content.add(data, "0,2,1,2");
		content.add(this.getExternalPanel(), "0,3,1,3");
		return content;
	}
}
