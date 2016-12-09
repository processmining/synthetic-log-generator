package nl.tue.declare.appl.worklist.gui;

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
public interface IWorklistFrameListener {
  /**
   * assignmentSelected
   *
   * @param object Object
   */
  public void assignmentSelected(Object object);

  /**
   * closeAssignment
   *
   * @param object Object
   */
  public void closeAssignment(Object object);
}
