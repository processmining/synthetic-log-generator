package nl.tue.declare.appl.util.swing;

import javax.swing.*;
import javax.swing.table.*;

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
public class TTable
    extends JTable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -8391157208955501539L;

  public TTable(TableModel dm) {
    super(dm);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }
  
  public TTable() {
	    super();
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  }

  private TTableModel getTableModel() {
    if (getModel() instanceof TTableModel) {
      return (TTableModel) getModel();
    }
    return null;
  }

  public Object getSelected() {
    TTableModel model = this.getTableModel();
    if (model != null) {
      int row = this.getSelectedRow();
      if (row >= 0) {
        return model.getObject(row);
      }
    }
    return null;
  }

  public int getIndexOf(Object object) {
    return this.getTableModel().getIndexOf(object);
  }

  public void setSelected(Object object) {
    int row = this.getIndexOf(object);
    if (row >= 0) {
      this.getSelectionModel().setSelectionInterval(row, row);
    }
  }
}
