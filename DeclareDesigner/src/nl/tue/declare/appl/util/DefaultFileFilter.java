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
import java.io.*;

import javax.swing.filechooser.FileFilter;

public class DefaultFileFilter
    extends FileFilter {

  //private String ext;
  private String[] extensions;

  public DefaultFileFilter(String[] ext) {
    super();
    this.extensions = ext;
    //this.ext = ext;
  }

  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }
    String extension = getExtension(f);
    if (extension != null) {
      // if (extension.equals(ext)) {
      if (this.contains(extension)) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }

  private boolean contains(String ext) {
    boolean found = false;
    int i = 0;
    while (i < extensions.length && !found) {
      found = extensions[i++].equals(ext);
    }
    return found;
  }

  public String getDescription() {
    String[] ext = this.getExt();
    String msg = "";
    for (int i = 0; i < ext.length; i++) {
      msg += (i > 0 ? ", " : "") + ext[i];
    }
    return msg + " files";
  }

  /*
   * Get the extension of a file.
   */
  public String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }

  /**
   * getExt
   */
  public String[] getExt() {
    return extensions;
  }
}
