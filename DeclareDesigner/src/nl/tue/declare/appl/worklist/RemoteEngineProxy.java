package nl.tue.declare.appl.worklist;

import java.io.*;
import java.net.*;

import nl.tue.declare.appl.worklist.IRemoteEngineListener;
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
import nl.tue.declare.utils.sockets.*;

public class RemoteEngineProxy
    implements ISocketConnectionListener {

  private static final int DEFAULT_INFO_PORT = 8193;
  private static final int DEFAULT_REQ_PORT = 8193;

  String username;
  String password;

  ChannelInformation informationChannel;
  ChannelRequest requestChannel;

  IRemoteEngineListener listener = null;

  public RemoteEngineProxy(String username, String password,
                           int portInformation, int portRequest, String host) {
    super();
    this.username = username;
    this.password = password;
    // set up information channel
    if (portInformation < 0) {
      portInformation = DEFAULT_INFO_PORT;
    }
    this.informationChannel = new ChannelInformation(new ClientSocketConnection(
        portInformation, host));
    this.informationChannel.addListener(this);
    // set up reqesting channel
    if (portRequest < 0) {
      portRequest = DEFAULT_REQ_PORT;
    }
    this.requestChannel = new ChannelRequest(new ClientSocketConnection(
        portRequest, host));
    this.requestChannel.addListener(this);

  }

  public String getUserName() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public void received(String message) {
    if (listener != null) {
      listener.received(message);
    }
  }

  public void sent(String message) {
    if (listener != null) {
      listener.sent(this, message);
    }
  }

  public void connected() {
    if (listener != null) {
      listener.connected(this);
    }
  }

  public void disconnected() {
    if (listener != null) {
      listener.disconnected(this);
    }
  }

  public void addListener(IRemoteEngineListener listener) {
    this.listener = listener;
  }

  /**
   *
   * @param message IMessage
   */
  /* public void sendMessage(IMessage message) {
   }*/

  /**
   *
   * @param message IMessage
   */
  /*public IWorklistMessageHandler request(IMessage message) {
    return this.requestChannel.request(message);
     }*/

  public IMessage request(IMessage message) {
    return this.requestChannel.request(message);
  }

  public void connect() throws UnknownHostException, IOException {
    this.informationChannel.connect();
    this.requestChannel.connect();
  }

  public void start() {

  }

  public void finish() {
    this.informationChannel.finish();
    this.requestChannel.finish();
  }

  public void monitorLogIn(IMessage msg) {
    this.informationChannel.send(msg);
    this.informationChannel.start();
  }

}

/**
 *
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
class ChannelInformation
    extends InformationSocketCommunication {
  public ChannelInformation(SocketConnection connection) {
    super(connection);
  }

  /**
   *
   * @param message IMessage
   */
  public void send(IMessage message) {
    super.send(message.msg());
  }

  public void received(String message) {
    if (message.equals(InformationSocketCommunication.CONFIRM)) {
      send(InformationSocketCommunication.CONFIRM);
    }
    else {
      listener.received(message);
    }

  }
}

/**
 *
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
class ChannelRequest
    extends RequestSocketCommunication {
  public ChannelRequest(SocketConnection connection) {
    super(connection);
  }

  /**
   *
   * @param message IMessage
   * @return IWorklistMessageHandler
   */
  /*IWorklistMessageHandler request(IMessage message) {
    String response = super.request(message.msg());
    return MessageFactory.parseEngineResponse(response);
     }*/
  IMessage request(IMessage message) {
    String response = super.request(message.msg());
    return MessageFactory.getEngineResponse(response);
  }

}
