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
import nl.tue.declare.appl.*;
import nl.tue.declare.appl.framework.engine.EngineAssignmnetCoordinatorFactory;
import nl.tue.declare.appl.framework.engine.IAssignmnetCoordinatorFactory;


public class Framework {

  public static void main(String[] args) {
    Project.ini(args);
    IAssignmnetCoordinatorFactory factory =  new EngineAssignmnetCoordinatorFactory();

    if (!FrameworkCoordinator.singleton().initiate(factory)) {
      System.exit( -1);
    }
  }
}
