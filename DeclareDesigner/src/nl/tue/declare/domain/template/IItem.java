package nl.tue.declare.domain.template;

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
public interface IItem {
  public int getMaxId();

  public IItem withId(int id);

  public boolean exists(IItem item);
}
