package nl.tue.declare.appl.util;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.*;

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
public class MainFrame
    extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = -3663913218745910331L;
private final String TITLE = Project.NAME;
  private String title;
  private JDesktopPane desktop;
  int inset;

  public MainFrame(String title, int inset) {
    super();
    try {
      this.title = title;
      this.inset = inset;
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public MainFrame(String title) {
    this(title, 0);
  }

  /**
   * Component initialization.
   *
   * @throws java.lang.Exception
   */
  protected void jbInit() throws Exception {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    setTitle(TITLE + " " + this.title);
    //int inset = 150;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset,
              screenSize.width - inset * 2,
              screenSize.height - inset * 2);
    start();
  }

  protected void middleSreen() {
    //int inset = 150;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = getWidth();
    int height = getHeight();
    setBounds( (screenSize.width - width) / 2, (screenSize.height - height) / 2,
              width, height);
  }

  public void start() {
    boolean packFrame = false;

    //Set up the GUI.
    desktop = new JDesktopPane(); //a specialized layered pane
    setContentPane(desktop);

    //Make dragging a little faster but perhaps uglier.
    desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    // Validate frames that have preset sizes
    // Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      pack();
    }
    else {
      validate();
    }

    // Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    setLocation( (screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
  }

  /**
   *
   */
  public void showMe() {
    setVisible(true);
  }

  protected void setSize(JComponent componenet, Dimension dimension) {
    FrameUtil.setSize(componenet, dimension);
  }
}
