package nl.tue.declare.appl.util;

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
public class LTLFileDialog
    extends DefaultFileDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = -4991259777996481277L;

public LTLFileDialog() {
    super(new String[] {"ltl"});
  }

  public LTLFileDialog(String name) {
    super(new String[] {"ltl"}, name + ".ltl");
  }
}
