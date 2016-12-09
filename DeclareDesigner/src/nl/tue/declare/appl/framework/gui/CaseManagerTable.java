package nl.tue.declare.appl.framework.gui;

import javax.swing.table.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.framework.InstanceHandler;

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
public class CaseManagerTable
    extends TTable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4389937785889654237L;

public CaseManagerTable(TableModel dm) {
    super(dm);
    getColumnModel().getColumn(0).setPreferredWidth(100);
  }

  public InstanceHandler getSelected() {
    Object selected = super.getSelected();
    InstanceHandler manager = null;
    if (selected instanceof InstanceHandler) {
      manager = (InstanceHandler) selected;
    }
    return manager;
  }
}
