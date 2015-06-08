package nl.tue.declare.execution.msg.worklist;

import nl.tue.declare.execution.msg.*;

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
public abstract class AbstractLoggingMessage
    extends UserMessage {

  String id;
  String username;
  String password;

  public AbstractLoggingMessage(String id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  protected String msgBody() {
    return id + SEPARATOR + username + SEPARATOR + password;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public String getId() {
    return this.id;
  }

}
