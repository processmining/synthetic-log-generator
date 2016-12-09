package nl.tue.declare.appl.worklist;

import nl.tue.declare.datamanagement.XMLHistoryBroker;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HistoryCoordinator
    extends Thread {
  Execution execution;
  public HistoryCoordinator(Assignment assignment) {
    super();
    execution = new Execution(assignment);
    //stop = false;
  }

  public void setAssignment(Assignment assignment) {
    this.execution.setAssignment(assignment);
  }

  public synchronized void update(String history) {
    XMLHistoryBroker broker = new XMLHistoryBroker(execution);
    broker.readDocumentString(history);
    broker.fromElement();
    notify();
  }

  /*private synchronized boolean updated() {
    SystemOutWriter.singleton().println("updated");
    try {
      wait();
    }
    catch (InterruptedException ex) {
    }
    return true;
  }*/

  public void run() {
    /*while( !stop && updated()){
         }*/
  }

  /*public void setStop(boolean stop) {
    this.stop = stop;
  }*/

 // private boolean stop;
}
