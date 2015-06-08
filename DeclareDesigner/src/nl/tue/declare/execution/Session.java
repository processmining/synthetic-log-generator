package nl.tue.declare.execution;

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
public class Session {

  private long id = 0;

  public Session() {
    super();
  }

  private synchronized void setId(long id) {
    this.id = id;
  }

  public synchronized long getId() {
    return id;
  }

  public void next() {
    id++;
  }

  public String toString() {
    return Long.toString(getId());
  }

  private long parseString(String str) {
    return Long.parseLong(str);
  }

  public void fromString(String str) {
    setId(parseString(str));
  }

  public boolean check(String ssn) {
    return parseString(ssn) == id;
  }
}
