package nl.tue.declare.appl.design.model.gui;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;

public class ActivityDataTableModel
    extends TTableModel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 6799371841545528012L;

public ActivityDataTableModel() {
    super(0, new Object[] {"data element", "type", "input - output"});
  }

  /**
   *
   * @param data ActivityData
   */
  public void addRow(ActivityDataDefinition data) {
    addRow(new Object[] {data, data.getDataElement().getType(),
           data.getType().name()});
  }

  /**
   *
   * @param data ActivityData
   */
  public void updateRow(ActivityDataDefinition data) {
    int row = this.getIndexOf(data);
    this.setValueAt(data, row, 0);
    this.setValueAt(data.getDataElement().getName(), row, 1);
    this.setValueAt(data.getType().name(), row, 2);
  }
}
