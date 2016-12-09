package nl.tue.declare.appl.design.model.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;

public class FrmAssignmentProperties extends OkCancelDialog {

	private static final long serialVersionUID = 5523516099718565617L;
	private ModelPropertiesPanel modelPanel;

	public FrmAssignmentProperties(Frame owner, String title,
			JInternalFrame aMonitorFrame) {
		super(owner, title, aMonitorFrame);
		modelPanel = new ModelPropertiesPanel();
	}

	public ModelPropertiesPanel getModelPanel() {
		return modelPanel;
	}

	@Override
	protected Component getContent() {
		return modelPanel;
	}
}
