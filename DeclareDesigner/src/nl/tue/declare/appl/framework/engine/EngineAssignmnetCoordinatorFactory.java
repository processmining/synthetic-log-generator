package nl.tue.declare.appl.framework.engine;

import java.io.*;

import nl.tue.declare.domain.instance.*;

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
public class EngineAssignmnetCoordinatorFactory
    implements IAssignmnetCoordinatorFactory {
  public EngineAssignmnetCoordinatorFactory() {
  }

  /**
   * create
   *
   * @param assignment Assignment
   * @param file File
   * @return AssignmentCoordinator
   * @todo Implement this
   *   nl.tue.declare.appl.framework.engine.coordinate.IAssignmnetCoordinatorFactory
   *   method
   */
  public EngineAssignmentCoordinator create(Assignment assignment, File file) throws
      Throwable {
    return new EngineAssignmentCoordinator(assignment, file);
  }

  public EngineCoordinator createEngine() {
    return EngineCoordinator.singleton();
  }
}
