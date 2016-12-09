package nl.tue.declare.appl.design.model.gui;

import info.clearthought.layout.TableLayout;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.model.DataElement;

public class FrmDataElement extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3704793367225677784L;

	private JTextField name = new JTextField("");
	private JComboBox type = new JComboBox();
	private JTextField ini = new JTextField("");

	private static double p = TableLayout.PREFERRED;
	private static double size[][] = { { p, TableLayout.FILL }, { p, p, p } };

	public FrmDataElement(Frame owner, String title, Container aMonitorFrame) {
		super(owner, title, aMonitorFrame);
	}

	public void toData(DataElement el) {
		if (el != null) {
			el.setName(name.getText());
			el.setType((DataElement.Type) type.getSelectedItem());
			el.setInitial(ini.getText());
		}
	}

	public void fromData(DataElement el) {
		if (el != null) {
			name.setText(el.getName());
			type.setSelectedItem(el.getType());
			ini.setText(el.getInitial());
		} else {
			name.setText("");
			type.setSelectedItem(null);
			ini.setText("");
		}
	}

	public void setTypeModel(ComboBoxModel model) {
		this.type.setModel(model);
	}

	@Override
	protected Component getContent() {
		JPanel topPanel = new JPanel(new TableLayout(size));

		topPanel.add(new JLabel("name"), "0,0");
		topPanel.add(name, "1,0");

		topPanel.add(new JLabel("type"), "0,1");
		topPanel.add(type, "1,1");

		topPanel.add(new JLabel("initial"), "0,2");
		topPanel.add(ini, "1,2");
		return topPanel;
	}
}
