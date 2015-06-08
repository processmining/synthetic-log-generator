package nl.tue.declare.appl.util;

import java.io.*;

import java.awt.*;
import javax.swing.*;

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

public abstract class FileDialog
    extends JFileChooser {

  /**
	 * 
	 */
	private static final long serialVersionUID = -3746339856906036488L;
private static final String APPLICATION_PATH = "user.dir";

  public FileDialog() {
    super();
    // Start in current directory
    setCurrentDirectory(new File("."));
  }

  public FileDialog(String name) {
    super();
    // Start in current dirextory, and specified file name
    setSelectedFile(new File(getApplicationPath() + File.separator + name));
  }

  /**
   *
   * @return String
   */
  private static String getApplicationPath() {
    return System.getProperty(APPLICATION_PATH);
  }

  protected abstract String[] getExt();

  /**
   * Use a JFileChooser in Save mode to select files
   * to open. Use a filter for FileFilter subclass to select
   * for "*.xml" files. If a file is selected, then write the
   * the string from the textarea into it.
   **/
  public String saveFile(Component parent) {

    File file = null;

    // Open chooser dialog
    int result = showSaveDialog(parent);

    if (result == JFileChooser.CANCEL_OPTION) {
      return null;
    }
    else if (result == JFileChooser.APPROVE_OPTION) {
      file = getSelectedFile();
      if (file.exists()) {
        int response = JOptionPane.showConfirmDialog(null,
            "Overwrite existing file?", "Confirm Overwrite",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.CANCEL_OPTION) {
          return null;
        }
      }
      String path = file.getAbsolutePath();
      //if (!path.endsWith(getExt()))
      if (!this.checkfile(path)) {
        path += "." + getExt()[0];
      }
      return path;
    }
    else {
      return null;
    }
  } // saveFile

  private boolean checkfile(String path) {
    String[] ext = this.getExt();
    boolean found = false;
    int i = 0;
    while (i < ext.length && !found) {
      found = path.endsWith(ext[i++]);
    }
    return found;
  }

  /**
   * Use a JFileChooser in Open mode to select files
   * to open. Use a filter for FileFilter subclass to select
   * for *.java files. If a file is selected then read the
   * file and place the string into the textarea.
   **/
  public String openFile(Component parent) {
    String path = null;
    // Now open chooser
    int result = showOpenDialog(parent);

    if (result == JFileChooser.CANCEL_OPTION) {
      return null;
    }
    else if (result == JFileChooser.APPROVE_OPTION) {
      File file = getSelectedFile();
      path = file.getAbsolutePath();
      //if (!path.endsWith(getExt()))
      if (!this.checkfile(path)) {
        path += "." + getExt();
      }
    }
    return path;
  } // openFile
}
