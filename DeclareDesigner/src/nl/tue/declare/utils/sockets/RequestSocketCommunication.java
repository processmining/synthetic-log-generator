package nl.tue.declare.utils.sockets;

public class RequestSocketCommunication
    extends AbstractSocketCommunation {

  public RequestSocketCommunication(SocketConnection connection) {
    super(connection);
  }

  public String request(String request) {
    send(request);
    String r = receive();
    return r;
  }
}
