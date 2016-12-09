package nl.tue.declare.logging;

import java.util.*;

import org.deckfour.xes.model.XEvent;
import nl.tue.declare.domain.instance.*;

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
public class AssignmentLog {
  private Assignment assignment;
  private Collection<XEvent> log;

  AssignmentLog(Assignment assignment) {
    super();
    this.assignment = assignment;
    this.log = new ArrayList<XEvent>();
  }

  public Assignment getAssignment() {
    return assignment;
  }

  public void add(XEvent ate) {
    if (ate != null) {
      log.add(ate);
    }
  }

  public Iterable<XEvent> log() {
    return log;
  }
}
