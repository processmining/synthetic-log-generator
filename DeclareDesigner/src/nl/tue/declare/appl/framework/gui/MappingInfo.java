package nl.tue.declare.appl.framework.gui;

import nl.tue.declare.utils.*;

public class MappingInfo
    extends Thread {

  private boolean stop;
  private MappingInfoWindow window;
  private int counter = 0;

  public MappingInfo() {
    super();
    stop = false;
    window = new MappingInfoWindow();
  }

  public void run() {
    window.open();
    while (!stop) {
      SystemOutWriter.singleton().println("" + counter++);
    }
    window.close();
  }

  public void setStop(boolean stop) {
    this.stop = stop;
  }

}
