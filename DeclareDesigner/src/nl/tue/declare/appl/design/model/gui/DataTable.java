package nl.tue.declare.appl.design.model.gui;

import javax.swing.table.*;

import nl.tue.declare.appl.util.swing.*;

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
public class DataTable
    extends TTable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -5122516184024431355L;

public DataTable(TableModel dm) {
    super(dm);
  }
}
