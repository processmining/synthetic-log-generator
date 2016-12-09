package nl.tue.declare.appl.design.model.gui;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;

public class DataTableModel
    extends TTableModel {
  /**
	 * 
	 */
	private static final long serialVersionUID = -4862574948581894977L;

public DataTableModel() {
    super(0, new Object[] {"name", "type", "initial value"});
  }

  public void addRow(DataElement element) {
    addRow(new Object[] {element, element.getType(), element.getInitial()});
  }

}
