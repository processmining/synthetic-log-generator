package nl.tue.declare.appl.framework;

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
import java.io.*;

import yawlservice.*;

import nl.tue.declare.control.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.utils.*;
import nl.tue.declare.appl.framework.yawl.*;

public class InstanceHandler {

  private static int ID = 1;

  private ExternalWorkItem external = null;
  private ExternalDataMapping mapping  = null;
  private Assignment assignment = null;
  private File file = null;
  private PrettyTime activated = null;



  public InstanceHandler(ExternalWorkItem external) {
    super();
    this.external = external;
    mapping = new ExternalDataMapping(this.external);
  }

  public InstanceHandler clone() {
    InstanceHandler clone = new InstanceHandler(this.external/*this.remoteCaseProxy*/);
    clone.file = new File(file.getAbsolutePath());
    clone.assignment = assignment.clone(nextId());
    return clone;
  }

  private static int nextId() {
    return ID++;
  }

  public void setActivated(PrettyTime time) {
    this.activated = time;
  }

  public PrettyTime getActivated() {
    return activated;
  }

  private void setAssignment(AssignmentModel model) {
    this.assignment = new Assignment(nextId(), model);
  }

  public void setAssignment(Assignment assignment) {
    this.assignment = assignment;
  }

  public void setAssignment(String path) {
    file = new File(path);
    AssignmentModel model = Control.singleton().getAssignmentModel().
        getAssignmentModel(path);
    this.setAssignment(model);
  }

  public boolean isEmpty() {
    return this.external == null;
  }

  public void finish() {
    this.writeToExternal();
  }

  public String toString() {
    if (external != null) {
      return this.external.getDecompositionID();
    }
    else {
      return "empty";
    }
  }

  public ExternalWorkItem getExternal() {
    return external;
  }

  public ExternalDataMapping getMapping() {
    return this.mapping;
  }

  public String getPath() {
    return file.getAbsolutePath();
  }

  public Assignment getAssignment() {
    return this.assignment;
  }

  private void writeToExternal() {
    this.mapping.writeOutputToExternal();
  }

  public void readFromExternal() {
    this.mapping.readInputFromExternal();
  }

  public String getExternalAssignmentName() {
    if (this.external != null) {
      return external.getDecompositionID();
    }
    return null;
  }

  public File getFile() {
    return this.file;
  }

  public boolean isEmptyData() {
    return mapping.getSize() == 0;
  }
}
