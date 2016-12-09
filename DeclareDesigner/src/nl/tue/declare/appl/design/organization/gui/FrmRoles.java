package nl.tue.declare.appl.design.organization.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.organization.Role;

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
public class FrmRoles extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6259073198338520773L;

	private JList rolesList = new JList();

	public FrmRoles(Frame parent, String title, JInternalFrame aMonitorFrame) {
		super(parent, title, aMonitorFrame);
	}

	public void fillListRoles(List<Role> roles) {
		this.fillList(new ArrayList<Object>(roles), rolesList);
	}

	public Object[] getSelectedRoles() {
		return getSelecetdAllList(rolesList);
	}

	@Override
	protected Component getContent() {
		return new JScrollPane(rolesList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
}
