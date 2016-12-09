package nl.tue.declare.appl.framework.engine;

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
import java.net.*;

import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.msg.*;
import nl.tue.declare.utils.sockets.*;

public class ResponseWorklistProxy
    extends ResponseSocketCommunication implements IWorklistRemoteProxy,
    ISocketConnectionListener {

  private IWorklistRemoteProxyListener listener = null;
  private IRequestListener requestListener = null;

  /**
   * DefaultWorklistRemoteProxy
   *
   * @param socket Socket
   */
  public ResponseWorklistProxy(Socket socket) {
    super(new ServerSocketConnection(socket));
  }

  public void setRequestListener(IRequestListener l) {
    this.requestListener = l;
  }

  /**
   *
   * @param msg String
   */
  public void send(IMessage msg) {}

  /**
   * close
   */
  public void close() {
    super.finish();
  }

  /**
   * addListener
   *
   * @param listener IWorklistRemoteProxyListener
   */
  public void addListener(IWorklistRemoteProxyListener listener) {
    this.listener = listener;
  }

  public void disconnected() {
    if (listener != null) {
      this.listener.disconnected(this);
    }
  }

  private String getResponse(String request) {
    String result = null;
    IMessage rq = MessageFactory.getWorklistRequest(request);
    // send response
    if (rq != null && requestListener != null) {
      IMessage response = requestListener.request(rq, this);
      if (response != null) {
        result = response.msg();
      }
    }
    return result;
  }

  protected String response(String request) {
    return this.getResponse(request);
  }

  public User getUser() {
    User user = null;
    if (listener != null) {
      user = listener.getUser();
    }
    return user;
  }

  public void initiate() {
    super.start();
  }

  public RemoteWorklist getWorklist() {
    if (listener != null) {
      return listener.getWorklist();
    }
    else {
      return null;
    }
  }
}
