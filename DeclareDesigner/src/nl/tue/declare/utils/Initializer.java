package nl.tue.declare.utils;

import java.io.*;
import java.net.*;
import java.util.*;

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
public class Initializer {

  private final String EQ = "=";
  private final String fileName;
  private final String filePath;
  // private Vector<String> vector;
  private HashMap<String, String> values;

  /**
   * Initializer
   */
  protected Initializer(String fileName, String path) {
    super();
    this.fileName = fileName;
    this.filePath = path;
    values = new HashMap<String, String> ();
    restore();
  }

  private File getFile() {
    File file = null;
    try {
      URL url = null;
      if (this.filePath != null) {
        url = URLLoader.loadURL(this.filePath, fileName);
      }
      else {
        url = URLLoader.loadURL(fileName);
      }
      URI uri = url.toURI();
      file = new File(uri);
    }
    catch (URISyntaxException ex1) {
      ex1.printStackTrace();
    }
    return file;
  }

  protected String getValue(String variable) {
    return values.get(variable);
  }

  /**
   *
   * @param line String
   * @return String
   */
  private String variable(String line) {
    StringTokenizer st = new StringTokenizer(line, EQ);
    if (st.hasMoreTokens()) {
      return st.nextToken();
    }
    else {
      return null;
    }
  }

  /**
   *
   * @param line String
   * @return String
   */
  private String value(String line) {
    StringTokenizer st = new StringTokenizer(line, EQ);
    st.nextToken();
    String value = null;
    try {
      value = st.nextToken();
    }
    catch (Exception e) {
      // ignore
    }
    return value;
  }

  private String line(String variable, String value) {
    return new String(variable + EQ + value);
  }
 // reads a list of strings from file
  private void readFromFile() {
    BufferedReader infile = null;
    String inLine;
    values.clear();
    try {
      // Create a buffered stream
      infile = new BufferedReader(new FileReader(this.getFile()));

      // Read a line and append the line to the text area

      while ( (inLine = infile.readLine()) != null) {
        if (inLine.length() > 0) {
          String key = variable(inLine);
          String value = value(inLine);
          values.put(key, value);
        }
      }
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        if (infile != null) {
          infile.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void write(File aFile) throws FileNotFoundException, IOException {
    if (aFile == null) {
      throw new IllegalArgumentException("File should not be null.");
    }
    if (!aFile.exists()) {
      throw new FileNotFoundException("File does not exist: " + aFile);
    }
    if (!aFile.isFile()) {
      throw new IllegalArgumentException("Should not be a directory: " + aFile);
    }
    if (!aFile.canWrite()) {
      throw new IllegalArgumentException("File cannot be written: " + aFile);
    }

    //declared here only to make visible to finally clause; generic reference
    Writer output = null;
    try {
      //use buffering
      //FileWriter always assumes default encoding is OK!
      output = new BufferedWriter(new FileWriter(aFile));
      Iterator<Map.Entry<String, String>> entries = values.entrySet().iterator();
      while (entries.hasNext()) {
        Map.Entry<String, String> entry = entries.next();
        String key = entry.getKey();
        String value = entry.getValue();
        output.write(line(key, value) + '\n');
      }
    }
    finally {
      //flush and close both "output" and its underlying FileWriter
      if (output != null) {
        output.close();
      }
    }
  }

  public void save() {
    try {
      write(getFile());
    }
    catch (FileNotFoundException ex) {
      // ignore
    }
    catch (IOException ex) {
      // ignore
    }
  }

  public void restore() {
    readFromFile();
  }

  protected void setValue(String variable, String value) {
    values.put(variable, value);
  }
}
