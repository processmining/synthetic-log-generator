package nl.tue.declare.graph.model;

import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.*;

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
public class ActivityDefinitionPort
    extends DPort {
  /**
	 * 
	 */
	private static final long serialVersionUID = 8938543148946161607L;

public ActivityDefinitionPort(DVertex aVertex,
                                Parameter parameter) {
    super(aVertex, parameter);
  }

  public ActivityDefinitionPort(DVertex aVertex) {
    super(aVertex);
  }

  /**
   * converts the owner vertex into the ActivityDefinitionView type
   * @return ActivityDefinitionView
   */
  public ActivityDefinitonCell ActivityDefinitionView() {
    DVertex vertex = this.getVertex();
    ActivityDefinitonCell view = null;
    if (vertex != null) {
      if (vertex instanceof ActivityDefinitonCell) {
        view = (ActivityDefinitonCell) vertex;
      }
    }
    return view;
  }
}
