package nl.tue.declare.appl.framework.gui;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;

public class TeamTableModel
    extends TTableModel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1109077243016965864L;

public TeamTableModel() {
    super(0, new Object[] {"model role", "assigned users"});
  }

  public void addRow(TeamRole role, String members) {
    super.addRow(new Object[] {role, members});
  }
}
