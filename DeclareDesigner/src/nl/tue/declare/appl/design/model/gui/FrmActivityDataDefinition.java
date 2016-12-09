package nl.tue.declare.appl.design.model.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;

public class FrmActivityDataDefinition extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1815241106395851010L;

	private JLabel labeldata = new JLabel("data element");
	private JComboBox data = new JComboBox();
	private JComboBox type = new JComboBox();

	public FrmActivityDataDefinition(Frame parent, Container monitor) {
		super(parent, "", monitor);

	}

	/**
	 * 
	 * @param box
	 *            JComboBox
	 * @param items
	 *            Collection
	 */
	private void fillComboBox(JComboBox box, Object[] items) {
		if (box != null) {
			box.removeAllItems();
			if (items != null) {
				for (int i = 0; i < items.length; i++) {
					box.addItem(items[i]);
				}
			}
		}
	}

	/**
	 * 
	 * @param items
	 *            Collection
	 */
	public void fillData(Object[] items) {
		this.fillComboBox(this.data, items);
	}

	/**
	 * 
	 * @param items
	 *            Collection
	 */
	public void fillType(Object[] items) {
		this.fillComboBox(this.type, items);
	}

	private void setComboBox(JComboBox box, Object o) {
		if (box != null) {
			box.setSelectedItem(o);
		}
	}

	public void setData(Object object) {
		this.setComboBox(this.data, object);
	}

	public void setType(Object object) {
		this.setComboBox(this.type, object);
	}

	private Object getComboBox(JComboBox box) {
		Object o = null;
		if (box != null) {
			o = box.getSelectedItem();
		}
		return o;
	}

	public Object getSelectedData() {
		return this.getComboBox(this.data);
	}

	public Object getSelectedType() {
		return this.getComboBox(this.type);
	}

	public void setDataElementEnabled(boolean enabled) {
		this.labeldata.setVisible(enabled);
		this.data.setEnabled(enabled);
		this.data.setVisible(enabled);
	}

	public void setTitle(String title) {
		super.setTitle("data" + " - " + title);
	}

	@Override
	protected Component getContent() {
		JPanel panel = new JPanel(new GridLayout(2, 1));

		JPanel data = new JPanel(new BorderLayout());
		data.add(this.labeldata, BorderLayout.WEST);
		data.add(this.data, BorderLayout.CENTER);

		JPanel type = new JPanel(new BorderLayout());
		type.add(new JLabel("type"), BorderLayout.WEST);
		type.add(this.type, BorderLayout.CENTER);

		panel.add(data);
		panel.add(type);

		return panel;
	}
}
