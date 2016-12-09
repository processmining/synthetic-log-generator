package nl.tue.declare.appl.framework.gui;

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

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.framework.yawl.*;

public class DataMapTableModel
    extends TTableModel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 2790370298402339294L;
private final int LOCAL_COLUMN = 3;

  public DataMapTableModel() {
    super(0, new Object[] {"in/out", "external name", "type", "local name"});
  }

  public void addRow(DataMap map) {
    String local = (map.getLocal() == null) ? "" :
        map.getLocal().getName();
    super.addRow(new Object[] {map, map.getExternal().getName(),
                 map.getExternal().getType(), local});
  }

  int getLocalColumn() {
    return LOCAL_COLUMN;
  }

  public boolean isCellEditable(int row, int col) {
    //Note that the data/cell address is constant,
    //no matter where the cell appears onscreen.
    if (col != LOCAL_COLUMN) {
      return false;
    }
    else {
      return true;
    }
  }

  public boolean isLocalColumn(int i) {
    return i == this.LOCAL_COLUMN;
  }
}
