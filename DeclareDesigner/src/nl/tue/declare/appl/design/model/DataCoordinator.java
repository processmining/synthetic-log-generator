package nl.tue.declare.appl.design.model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.design.model.gui.*;
/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
import nl.tue.declare.appl.util.swing.ButtonAdd;
import nl.tue.declare.appl.util.swing.ButtonDelete;
import nl.tue.declare.appl.util.swing.ButtonEdit;
import nl.tue.declare.domain.model.*;

public class DataCoordinator extends ModelInternalCoordinator implements
		ActionListener {

	private DataTableModel tableModel = new DataTableModel();
	private DataTable table = new DataTable(tableModel);

	private JButton add = new ButtonAdd();
	private JButton edit = new ButtonEdit();
	private JButton delete = new ButtonDelete();

	private FrmDataElement frmDataElement;

	public DataCoordinator(JFrame aMainFrame, AssignmentPanel panel,
			AssignmentModel aModel) {
		super(panel, aModel);
		frmDataElement = new FrmDataElement(aMainFrame, "Data element", panel);
		prepareGUI();
		fillDataElements();
	}

	private void prepareGUI() {
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout(2, 2));

		JScrollPane scroll = new JScrollPane(table);
		main.add(scroll, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());

		buttons.add(add);
		buttons.add(edit);
		buttons.add(delete);
		main.add(buttons, BorderLayout.EAST);

		add.addActionListener(this);
		edit.addActionListener(this);
		delete.addActionListener(this);

		panel.setDataPanel(main);
	}

	/**
	 * addRole
	 */
	public void addDataElement() {
		frmDataElement.setTypeModel(new DefaultComboBoxModel(DataElement.Type
				.values()));
		frmDataElement.fromData(null);
		frmDataElement.setTitle("Add data element");
		if (frmDataElement.showCentered()) {
			addDataElementConfirmed();
		}
	}

	/**
	 * addRoleConfirmed
	 */
	public void addDataElementConfirmed() {
		DataElement element = this.model.createDataElement();
		frmDataElement.toData(element);
		if (!model.addData(element)) {
			JOptionPane.showInternalMessageDialog(panel,
					"You cannot add a data element that alreday exists.",
					"information", JOptionPane.INFORMATION_MESSAGE);
		}

		fillDataElements();
	}

	public void fillDataElements() {
		this.tableModel.getDataVector().clear();
		for (int i = 0; i < model.getDataCount(); i++) {
			DataElement element = model.dataAt(i);
			this.tableModel.addRow(element);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e != null) {
			Object object = e.getSource();
			if (object != null) {
				if (object == add) {
					this.addDataElement();
				}
				if (object == edit) {
					this.editDataElement();
				}
				if (object == delete) {
					this.deleteDataElement();
				}
			}
		}
	}

	/**
	 * deleteRole
	 */
	public void deleteDataElement() {
		DataElement element = getSelectedDataElement();
		if (element != null) {
			Object[] options = { "OK", "CANCEL" };
			int response = JOptionPane
					.showOptionDialog(panel,
							"Do you want to delete the data element - "
									+ element + "?", "confirmation",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
			if (response == JOptionPane.OK_OPTION) {
				deleteDataElementConfirmed(element);
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							panel,
							"No data element is selected. You must first select the data element you want to delete.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * deleteRoleConfirmed
	 * 
	 * @param element
	 *            Role
	 */
	public void deleteDataElementConfirmed(DataElement element) {
		boolean canRemove = model.deleteDataElement(element);
		if (canRemove) {
			fillDataElements();
		} else {
			JOptionPane
					.showInternalMessageDialog(
							panel,
							"The data element "
									+ element
									+ " cannot be deleted because it is assigned to at least one activity definition.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private DataElement getSelectedDataElement() {
		int row = table.getSelectedRow();
		return (DataElement) tableModel.getObject(row);
	}

	/**
	 * editRole
	 */
	public void editDataElement() {
		DataElement element = getSelectedDataElement();
		if (element != null) {
			frmDataElement.setTypeModel(new DefaultComboBoxModel(
					DataElement.Type.values()));
			frmDataElement.fromData(element);
			frmDataElement.setTitle("Edit data element");
			if (frmDataElement.showCentered()) {
				editDataElementConfirmed(element);
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							panel,
							"No data element is selected. You must first select a data element you want to edit.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * editRoleConfirmed
	 * 
	 * @param element
	 *            Role
	 */
	public void editDataElementConfirmed(DataElement element) {
		frmDataElement.toData(element);
		fillDataElements();
	}
}
