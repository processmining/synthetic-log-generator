package nl.tue.declare.appl.framework.engine;

import java.net.*;

import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.msg.*;
import nl.tue.declare.utils.sockets.*;

public class InformationWorklistProxy
    extends InformationSocketCommunication implements IWorklistRemoteProxy,
    ISocketConnectionListener {

  private IWorklistRemoteProxyListener listener = null;
  private IRequestListener requestListener = null;

  public InformationWorklistProxy(Socket socket) {
    super(new ServerSocketConnection(socket));
    super.addListener(this);
  }

  public void setRequestListener(IRequestListener l) {
    this.requestListener = l;
  }

  public void send(IMessage message) {
    if (message != null) {
      super.send(message.msg());
    }
  }

  public void close() {
  }

  public void addListener(IWorklistRemoteProxyListener listener) {
    this.listener = listener;
  }

  public void received(String message) {
    if (!message.equals(InformationSocketCommunication.CONFIRM)) {
      IMessage msg = MessageFactory.getWorklistRequest(message);
      if (requestListener != null) {
        requestListener.request(msg, this);
      }
    }
  }

  public void sent(String message) {
  }

  public void disconnected() {
    if (this.listener != null) {
      this.listener.disconnected(this);
    }
  }

  public void connected() {
  }

  public User getUser() {
    User user = null;
    if (listener != null) {
      user = listener.getUser();
    }
    return user;
  }

  public void initiate() {
    super.receive();
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
