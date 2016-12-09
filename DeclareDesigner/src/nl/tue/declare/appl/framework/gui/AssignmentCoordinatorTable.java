package nl.tue.declare.appl.framework.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.graph.assignment.*;
import nl.tue.declare.appl.framework.InstanceHandler;

public class AssignmentCoordinatorTable
    extends TTable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Map<Assignment, Color> state;

  public AssignmentCoordinatorTable(AssignmentCoordinatorTableModel dm) {
    super(dm);
    state = new HashMap<Assignment, Color>();
    setUp();
  }

  public InstanceHandler getSelected() {
    Object selected = super.getSelected();
    InstanceHandler manager = null;
    if (selected instanceof InstanceHandler) {
      manager = (InstanceHandler) selected;
    }
    return manager;
  }

  private void setState(Assignment object, Color color) {
    if (object != null && color != null) {
      state.put(object, color);
    }
  }

  public void setState(AssignmentState state) {
    if (state != null) {
      if (state.getAssignment() != null) {
        this.setState(state.getAssignment(), AssignmentView.getColor(state));
      }
    }
    this.repaint();
  }

  private Color getColor(int row) {
    Color color = null;
    AssignmentCoordinatorTableModel model = this.getAssignmentModel();
    if (model != null) {
      InstanceHandler manager = model.getValueAt(row);
      if (manager != null) {
        color = state.get(manager.getAssignment());
      }
    }
    return color;
  }

  private AssignmentCoordinatorTableModel getAssignmentModel() {
    TableModel model = this.getModel();
    AssignmentCoordinatorTableModel m = null;
    if (model instanceof AssignmentCoordinatorTableModel) {
      m = (AssignmentCoordinatorTableModel) model;
    }
    return m;
  }

  private void setUp() {
    for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
      getColumnModel().getColumn(i).setCellRenderer(new MyTableCellRenderer());
    }
  }

  public class MyTableCellRenderer
      extends DefaultTableCellRenderer { //JLabel implements TableCellRenderer
    /**
	 * 
	 */
	private static final long serialVersionUID = -5244473644292339175L;

	// This method is called each time a cell in a column
    // using this renderer needs to be rendered.
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
      JLabel comp = (JLabel)super.getTableCellRendererComponent(table, value,
          isSelected, hasFocus, rowIndex, vColIndex);
      Color color = getColor(rowIndex);
      if (color != null) {
        comp.setBackground(isSelected ? Color.lightGray : Color.white);
        comp.setForeground(color);
      }
      Font old = comp.getFont();
      comp.setFont(new Font(old.getName(), Font.BOLD, old.getSize()));
      if (value != null) {
        comp.setText(value.toString());
      }
      return comp;
    }
  }
}
