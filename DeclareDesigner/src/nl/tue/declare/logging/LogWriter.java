package nl.tue.declare.logging;

import java.io.*;

import nl.tue.declare.utils.*;

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
public class LogWriter {

  PrintStream out;

  public LogWriter(File file) {
    try {
      setOut(new PrintStream(file));
    }
    catch (FileNotFoundException ex) {
      // ignore
    }
  }

  public LogWriter(OutputStream outputStream) {
    setOut(new PrintStream(outputStream));
  }

  public LogWriter() {
    setOut(System.out);
  }

  public LogWriter(String fileName) {
    try {
      setOut(new PrintStream(new File(fileName)));
    }
    catch (FileNotFoundException ex) {
      // ignore
    }
  }

  public void setOut(PrintStream out) {
    this.out = out;
  }

  protected void write(String entry) {
    if (out != null) {
      out.println(currentTime() + " " + entry);
      out.flush();
    }
  }

  private String currentTime() {
    return new PrettyTime().toString();
  }
}
