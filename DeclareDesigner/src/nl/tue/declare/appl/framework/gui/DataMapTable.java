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
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import nl.tue.declare.appl.util.swing.*;

public class DataMapTable
    extends TTable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4543469026383141153L;
protected RowEditorModel rm;

  public DataMapTable(TableModel dm) {
    super(dm);
    rm = new RowEditorModel();
  }

  public RowEditorModel getRowEditorModel() {
    return rm;
  }

  public void addLocal(Object[] item) {
    JComboBox cb = new JComboBox(item);
    DefaultCellEditor ed = new DefaultCellEditor(cb);
    rm.addEditorForRow(ed);
  }

  public TableCellEditor getCellEditor(int row, int col) {
    TableCellEditor tmpEditor = null;
    if (rm != null) {
      tmpEditor = rm.getEditor(row);
    }
    if (tmpEditor != null) {
      return tmpEditor;
    }
    return super.getCellEditor(row, col);
  }

  public class RowEditorModel {
    private Hashtable<Integer,TableCellEditor> data;
    
    public RowEditorModel() {
      data = new Hashtable<Integer,TableCellEditor>();
    }

    public void addEditorForRow(int row, TableCellEditor e) {
      data.put(new Integer(row), e);
    }

    public void addEditorForRow(TableCellEditor e) {
      data.put(new Integer(data.size()), e);
    }

    public void removeEditorForRow(int row) {
      data.remove(new Integer(row));
    }

    public TableCellEditor getEditor(int row) {
      return (TableCellEditor) data.get(new Integer(row));
    }
  }

}
