package nl.tue.declare.appl.util.swing;

import java.awt.*;
import javax.swing.*;

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
public class TSplitPane
    extends JSplitPane {
  /**
	 * 
	 */
	private static final long serialVersionUID = 6780278882054035567L;

public TSplitPane() {
    super();
  }

  public TSplitPane(int newOrientation) {
    super(newOrientation);
    prepare();
  }

  public TSplitPane(int newOrientation, boolean newContinuousLayout) {
    super(newOrientation, newContinuousLayout);
    prepare();
  }

  public TSplitPane(int newOrientation, Component newLeftComponent,
                    Component newRightComponent) {
    super(newOrientation, newLeftComponent, newRightComponent);
    prepare();
  }

  public TSplitPane(int newOrientation, boolean newContinuousLayout,
                    Component newLeftComponent, Component newRightComponent) {
    super(newOrientation, newContinuousLayout, newLeftComponent,
          newRightComponent);
    prepare();
  }

  private void prepare() {
    setOneTouchExpandable(true);
  }
}
