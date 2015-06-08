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
public interface IAssignmnetCoordinatorFactory {
  public EngineAssignmentCoordinator create(Assignment assignment, File file) throws
      Throwable;

  public EngineCoordinator createEngine();
}
