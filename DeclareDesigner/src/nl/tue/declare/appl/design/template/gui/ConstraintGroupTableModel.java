package nl.tue.declare.appl.design.template.gui;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.template.*;

public class ConstraintGroupTableModel
    extends TTableModel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 5947271933343032811L;

public ConstraintGroupTableModel() {
    super(0, new Object[] {"name", "description"});
  }

  public void addRow(ConstraintGroup group) {
    addRow(new Object[] {group, group.getDescription()});
  }

  public void updateRow(ConstraintGroup group) {
    int row = this.getIndexOf(group);
    this.setValueAt(group, row, 0);
    this.setValueAt(group.getDescription(), row, 1);
  }
}
