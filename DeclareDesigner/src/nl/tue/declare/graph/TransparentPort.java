package nl.tue.declare.graph;

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
import org.jgraph.graph.*;

public class TransparentPort
    extends DefaultPort {
  /**
	 * 
	 */
	private static final long serialVersionUID = 4699980481567941688L;

public TransparentPort() {
    super();
  }

  public TransparentPort(Object object) {
    super(object);
  }

  public TransparentPort(Object object, Port port) {
    super(object, port);
  }
}
