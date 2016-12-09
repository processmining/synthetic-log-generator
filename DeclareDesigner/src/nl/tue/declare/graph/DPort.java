package nl.tue.declare.graph;

import org.jgraph.graph.*;
import nl.tue.declare.domain.template.*;

public class DPort
    extends DefaultPort {

  /**
	 * 
	 */
	private static final long serialVersionUID = -4577240013297646609L;
private DVertex vertex;


  public DPort(DVertex aVertex, Parameter parameter) {
    super(parameter);
    vertex = aVertex;
  }

  public DPort(DVertex aVertex) {
    super();
    vertex = aVertex;
  }


  protected DVertex getVertex() {
    return vertex;
  }
}
