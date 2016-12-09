package nl.tue.declare.appl.framework.gui;

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
public interface ILoadListener {

  /**
   * onOk
   *
   * @throws Exception
   * @return boolean
   */
  public boolean onOk() throws Exception;


  public void reload();
}
