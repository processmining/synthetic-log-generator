package nl.tue.declare.datamanagement;

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
public abstract class Broker {

  private String connectionString;

  public Broker(String aConnectionString) {
    super();
    this.connectionString = aConnectionString;
    this.connect();
  }

  protected String getConnectionString() {
    return connectionString;
  }

  /**
   * connect
   */
  protected abstract void connect();
}
