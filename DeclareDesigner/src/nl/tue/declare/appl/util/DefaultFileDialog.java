package nl.tue.declare.appl.util;

import java.io.*;

import javax.swing.filechooser.FileFilter;

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
public class DefaultFileDialog
    extends FileDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 4524363156377923623L;
DefaultFileFilter filter;
  public DefaultFileDialog(String[] ext) {
    super();
    // Set filter for Java source files.
    filter = new DefaultFileFilter(ext);
    setFileFilter(this.createFilter(ext));
  }

  public DefaultFileDialog(String[] ext, String name) {
    super(name);
    // Set filter for Java source files.
    filter = new DefaultFileFilter(ext);
    setFileFilter(this.createFilter(ext));
  }

  //protected abstract FileFilter createFilter();
  protected FileFilter createFilter(String[] ext) {
    return new DefaultFileFilter(ext);
  }

  protected String[] getExt() {
    return filter.getExt();
  }

  public String getExtension(File file) {
    return filter.getExtension(file);
  }
}
