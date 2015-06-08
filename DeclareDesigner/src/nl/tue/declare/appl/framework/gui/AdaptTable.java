package nl.tue.declare.appl.framework.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.verification.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class AdaptTable
    extends TTable {
	
	
  /**
	 * 
	 */
	private static final long serialVersionUID = 4243968300185570798L;

public AdaptTable() {
    super(new AdaptTableModel());
    getColumnModel().getColumn(0).setCellRenderer(new MyTableCellRenderer());
  }

  public AdaptTableModel getModel() {
    return (AdaptTableModel)super.getModel();
  }

  public VerificationResult getVerification() {
    return getModel().getVerification(getSelectedRow());
  }

  public Assignment getAssignment() {
    return getModel().getAssignment(getSelectedRow());
  }

  public void addRow(VerificationResult error, Assignment assignment) {
    getModel().addRow(error, assignment);
  }

  public void clear() {
    getModel().clear();
  }

  public class MyTableCellRenderer
      extends DefaultTableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6620717304170487968L;

	public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
      JLabel comp = (JLabel)super.getTableCellRendererComponent(table, value,
          isSelected, hasFocus, rowIndex, vColIndex);
      Font old = comp.getFont();
      comp.setFont(new Font(old.getName(), Font.BOLD, old.getSize()));
      if (value != null) {
        comp.setText(value.toString());
      }
      return comp;
    }
  }
}
