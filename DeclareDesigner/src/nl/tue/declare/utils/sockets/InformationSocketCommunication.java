package nl.tue.declare.utils.sockets;

public class InformationSocketCommunication
    extends AbstractSocketCommunation implements ISocketConnectionListener {
  public static final String CONFIRM = "confirm";

  protected ISocketConnectionListener listener;

  public InformationSocketCommunication(SocketConnection connection) {
    super(connection);
    super.addListener(this);
  }

  /**
   *
   * @param listener ISocketConnectionListener
   */
  public void addListener(ISocketConnectionListener listener) {
    this.listener = listener;
  }

  public void received(String message) {
    listener.received(message);
  }

  public boolean requestConfirm() {
    send(CONFIRM);
    return receive().equals(CONFIRM);
  }

  public void sent(String message) {
  }

  public void disconnected() {
  }

  public void connected() {
  }
}
