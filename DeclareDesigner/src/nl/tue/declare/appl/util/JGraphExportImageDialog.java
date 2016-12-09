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
public class JGraphExportImageDialog
    extends DefaultFileDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = -3908843855609064198L;

public JGraphExportImageDialog() {
    super(new String[] {"png", "jpg", "bmp"});
  }
}
