package nl.tue.declare.domain.model;

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
public class Condition {
  private String text;

  public Condition() {
    super();
    text = "";
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  /**
   * isTrue
   *
   * @return boolean
   */
  public boolean isTrue() {
    if (text.equals("")) {
      return true;
    }
    return true;
  }

  /**
   * toString
   */
  public String toString() {
    return text;
  }
}
