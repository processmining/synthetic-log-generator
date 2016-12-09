package nl.tue.declare.appl.design.model.gui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;

public class ActivityDataPanel extends TPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1650762790680354612L;

	private ActivityDefinition activity;

	private ActivityDataTableModel model = new ActivityDataTableModel();
	private ActivityDataTable table = new ActivityDataTable(model);
	private JButton add = new JButton("add");
	private JButton edit = new JButton("edit");
	private JButton delete = new JButton("delete");

	private IActivityDataPanelListener listener;

	public ActivityDataPanel() {
		super(new BorderLayout(), "data");
		this.gui();
		this.add.addActionListener(this);
		this.edit.addActionListener(this);
		this.delete.addActionListener(this);
	}

	private void gui() {

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(this.add);
		buttons.add(this.edit);
		buttons.add(this.delete);

		this.add(new JScrollPane(this.table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		this.add(buttons, BorderLayout.SOUTH);
	}

	public void add(ActivityDataDefinition data) {
		if (data != null) {
			this.model.addRow(data);
		}
	}

	private void edit(ActivityDataDefinition data) {
		if (data != null) {
			this.model.updateRow(data);
		}
	}

	private void delete(ActivityDataDefinition data) {
		this.model.remove(data);
	}

	private ActivityDataDefinition get() {
		return (ActivityDataDefinition) this.table.getSelected();
	}

	public void actionPerformed(ActionEvent e) {
		if (e != null) {
			Object source = e.getSource();
			if (source == add) {
				this.add();
			}
			if (source == edit) {
				this.edit();
			}
			if (source == delete) {
				this.delete();
			}
		}
	}

	private void add() {
		if (this.listener != null) {
			ActivityDataDefinition data = this.listener.add(this.activity);
			this.add(data);
		}
	}

	private void edit() {
		if (this.listener != null) {
			ActivityDataDefinition data = this.get();
			if (data != null) {
				if (listener.edit(data, this.activity)) {
					this.edit(data);
				}
			}
		}
	}

	private void delete() {
		if (this.listener != null) {
			ActivityDataDefinition data = this.get();
			if (data != null) {
				if (listener.delete(data, this.activity)) {
					this.delete(data);
				}
			}
		}
	}

	public void addListener(IActivityDataPanelListener listener) {
		this.listener = listener;
	}

	public void init(ActivityDefinition activity) {
		this.activity = activity;
		Iterator<ActivityDataDefinition> i = activity.data();
		this.model.clear();
		if (i != null) {
			while (i.hasNext()) {
				this.model.addRow(i.next());
			}
		}
	}
}
