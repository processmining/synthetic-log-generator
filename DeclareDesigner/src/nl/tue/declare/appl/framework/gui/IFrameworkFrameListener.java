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
public interface IFrameworkFrameListener {
  /**
   * load
   */
  public void load();

  /**
   * complete
   */
  public void complete();

  /**
   * loadEmpty
   */
  public void loadEmpty();

  public void finish();

  void saveSettings();

  void restoreSettings();

  void changeModel();

  void changeTeam();

  void arrivedSelected();

  void activeSelected();

}
