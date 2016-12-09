package nl.tue.declare.appl.framework.gui;

import yawlservice.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.appl.framework.InstanceHandler;

public class AssignmentCoordinatorTableModel
    extends TTableModel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public AssignmentCoordinatorTableModel() {
    super(5, new Object[] {"time", "external id", "specification", "case",
          "task", "decomposition", "instance id", "model"});
  }

  public void addRow(InstanceHandler manager) {
    ExternalWorkItem external = manager.getExternal();
    Assignment assignment = manager.getAssignment();
    String engineId = "";
    String specification = "";
    String caseId = "";
    String task = "";
    if (external != null) {
      engineId = external.getEngineID();
      specification = external.getSpecificationID();
      caseId = external.getCaseID();
      task = external.getTaskID();
    }
    if (assignment != null) {
      addRow(new Object[] {manager.getActivated(), engineId, specification,
             caseId, task, manager,
             Integer.toString(assignment.getId()), assignment.getName()});
    }
  }

  InstanceHandler getValueAt(int row) {
    InstanceHandler manager = null;
    if (row > -1) {
      Object value = super.getObject(row);
      if (value instanceof InstanceHandler) {
        manager = (InstanceHandler) value;
      }
    }
    return manager;
  }
}
