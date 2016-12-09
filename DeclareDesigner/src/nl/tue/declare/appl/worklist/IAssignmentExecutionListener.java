package nl.tue.declare.appl.worklist;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;

public interface IAssignmentExecutionListener {

  public void startActivity(Activity activity);
  public void completeActivity(WorkItem workItem);
  public void cancelActivity(WorkItem workItem);
}
