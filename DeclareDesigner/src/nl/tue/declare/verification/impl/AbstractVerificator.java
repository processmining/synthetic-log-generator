package nl.tue.declare.verification.impl;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.verification.*;

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
public abstract class AbstractVerificator
    implements IVerificator {

  protected AssignmentModel model;

  public AbstractVerificator(AssignmentModel model) {
    super();
    this.model = model;
  }
}
