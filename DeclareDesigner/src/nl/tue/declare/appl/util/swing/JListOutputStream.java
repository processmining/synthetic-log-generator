package nl.tue.declare.appl.util.swing;

import java.io.*;

import javax.swing.*;

import nl.tue.declare.appl.util.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JListOutputStream
    extends OutputStream {
  private ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
  private JList list;

  public JListOutputStream(JList list) {
    super();
    this.list = list;
  }

  /**
   * Writes the specified byte to this output stream.
   *
   * @param b the <code>byte</code>.
   * @throws IOException if an I/O error occurs. In particular, an
   *   <code>IOException</code> may be thrown if the output stream has been
   *   closed.
   * @todo Implement this java.io.OutputStream method
   */
  public void write(int b) throws IOException {
    outputStream.write(b);
  }

  public void flush() throws IOException {
    super.flush();
    try {
      if (list != null) {
        FrameUtil.addToList(list, new String(outputStream.toByteArray()));
      }
      outputStream.reset();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
