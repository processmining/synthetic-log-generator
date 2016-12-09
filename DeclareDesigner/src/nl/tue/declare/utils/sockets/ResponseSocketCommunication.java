package nl.tue.declare.utils.sockets;

public abstract class ResponseSocketCommunication
    extends AbstractSocketCommunation implements ISocketConnectionListener {
  public ResponseSocketCommunication(SocketConnection connection) {
    super(connection);
    //connection.addListener(this);
    super.addListener(this);
  }

  public void received(String message) {
    String response = response(message);
    send(response);
  }

  protected abstract String response(String request);

  public void sent(String message) {
  }

  public void disconnected() {

  }

  public void connected() {
  }

  /**
   *
   * @param msg String
   */
  public void send(String msg) {
    if (msg != null) {
      connection.send(msg);
    }
  }
}
