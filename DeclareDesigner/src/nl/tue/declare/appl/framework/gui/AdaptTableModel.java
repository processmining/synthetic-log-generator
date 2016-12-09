package nl.tue.declare.appl.framework.gui;

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
public class AdaptTableModel
    extends TTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1250474648503139423L;
	private static final int ERROR_COLUMN = 0;
	private static final int INSTANCE_COLUMN = 1;
  

  public AdaptTableModel() {
    super(INSTANCE_COLUMN, new Object[] {"result", "instance"});
  }

  public void addRow(VerificationResult error, Assignment assignment) {
    if (assignment != null) {
      addRow(new Object[] {error, assignment});
    }
  }

  public VerificationResult getVerification(int row) {
    if (0 <= row && row < getRowCount()) {
      return (VerificationResult)this.getValueAt(row, ERROR_COLUMN);
    }
    else {
      return null;
    }
  }

  public Assignment getAssignment(int row) {
    if (0 <= row && row < getRowCount()) {
      return (Assignment)this.getValueAt(row, INSTANCE_COLUMN);
    }
    else {
      return null;
    }
  }

}
