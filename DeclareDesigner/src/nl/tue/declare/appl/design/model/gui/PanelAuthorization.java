package nl.tue.declare.appl.design.model.gui;

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
import info.clearthought.layout.TableLayout;

import java.util.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.Authorization;
import nl.tue.declare.domain.model.TeamRole;

public class PanelAuthorization extends TPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 221552349839975671L;
	private JList selectedList = new JList();

	private JList availableList = new JList();

	private JButton jButtonAdd = new JButton("<--");
	private JButton jButtonRemove = new JButton("-->");

	private static double p = TableLayout.PREFERRED;
	private static double size[][] = {
			{ TableLayout.FILL, 80, TableLayout.FILL }, { p } };

	public PanelAuthorization() {
		super(new TableLayout(size));
		try {
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * jbInit
	 */
	private void jbInit() {
		Box buttons = new Box(BoxLayout.Y_AXIS);

		this.jButtonAdd.addActionListener(this);
		this.jButtonRemove.addActionListener(this);
		buttons.add(this.jButtonAdd);
		buttons.add(this.jButtonRemove);
		
		add(new JScrollPane(selectedList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), "0,0");
		add(buttons, "1,0,c,t");
		add(new JScrollPane(availableList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), "2,0");
	}

	/**
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == jButtonAdd) {
			this.add();
		}
		if (source == jButtonRemove) {
			this.remove();
		}
		return;
	}

	/**
   *
   */
	private void add() {
		try {
			Object[] available = this.availableList.getSelectedValues();
			DefaultListModel availableModel = (DefaultListModel) this.availableList
					.getModel();
			DefaultListModel selectedModel = (DefaultListModel) this.selectedList
					.getModel();
			move(available, availableModel, selectedModel);
		} catch (Exception e) {
		}
		;
	}

	/**
   *
   */
	private void remove() {
		try {
			Object[] selected = this.selectedList.getSelectedValues();
			DefaultListModel availableModel = (DefaultListModel) this.availableList
					.getModel();
			DefaultListModel selectedModel = (DefaultListModel) this.selectedList
					.getModel();
			move(selected, selectedModel, availableModel);
		} catch (Exception e) {
		}
		;
	}

	/**
	 * 
	 * @param elements
	 *            Object[]
	 * @param from
	 *            DefaultListModel
	 * @param to
	 *            DefaultListModel
	 */
	private void move(Object[] elements, DefaultListModel from,
			DefaultListModel to) {
		for (int i = 0; i < elements.length; i++) {
			Object element = elements[i];
			from.removeElement(element);
			to.addElement(element);
		}
	}

	/**
	 * 
	 * @param selected
	 *            List
	 * @param available
	 *            List
	 */
	public void start(Authorization authorization) {
		FrameUtil
				.fillList(authorization.getAuthorizedList(), this.selectedList);
		FrameUtil.fillList(authorization.getUnauthorizedList(),
				this.availableList);
	}

	/**
	 * 
	 * @return List
	 */
	public Iterable<TeamRole> getSelected() {
		List<TeamRole> list = new ArrayList<TeamRole>();
		ListModel model = this.selectedList.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			Object current = model.getElementAt(i);
			if (current instanceof TeamRole)
				list.add((TeamRole) current);
		}
		return list;
	}
}
