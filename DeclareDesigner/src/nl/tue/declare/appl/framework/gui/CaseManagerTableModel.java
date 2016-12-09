package nl.tue.declare.appl.framework.gui;

import yawlservice.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.utils.*;
import nl.tue.declare.appl.framework.InstanceHandler;

public class CaseManagerTableModel
    extends TTableModel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -5296407441305644142L;

public CaseManagerTableModel() {
    super(5, new Object[] {"time", "external id", "specification", "case",
          "task", "decomposition"});
  }

  private void addRow(PrettyTime time, String externalId, String specification,
                      String cs, String task, InstanceHandler manager) {
    addRow(new Object[] {time, externalId, specification, cs, task, manager});
  }

  public void addRow(PrettyTime time, InstanceHandler manager) {
    ExternalWorkItem external = manager.getExternal();
    if (external != null) {
      this.addRow(time, external.getEngineID(), external.getSpecificationID(),
                  external.getCaseID(), external.getTaskID(), manager);
    }
    else {
      this.addRow(time, "", "", "", "", manager);
    }
  }
}
