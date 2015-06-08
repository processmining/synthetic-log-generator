package nl.tue.declare.execution.msg;

public class UnknownRequest
    implements IMessage {
  /**
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.IMessage method
   */
  public String msg() {
    return "Unknown request.";
  }

  public UnknownRequest() {
    super();
  }
}
