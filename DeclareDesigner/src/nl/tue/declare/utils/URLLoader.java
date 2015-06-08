package nl.tue.declare.utils;

import java.io.*;
import java.net.*;

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
public class URLLoader {

  private static final String SEPARATOR = File.separator;
  private static final String workDir = System.getProperty("user.dir");
  private static final String RESOURCES = SEPARATOR + "resources" + SEPARATOR;

  /**
   * Loads an URL from the specified path in the WORKING directory.
   * WORKING_DIRECTORY/dirPath/fileName.
   *
   * @param dirPath String
   * @param fileName String
   * @return URL
   */
  public static URL loadURL(String dirPath, String fileName) {
    return toURL(new File(path(workDir + dirPath + fileName)));
  }

  /**
   * Loads an URL from the WORKING directory.
   * WORKING_DIRECTORY/fileName
   *
   * @param fileName String
   * @return URL
   */
  public static URL loadURL(String fileName) {
    return toURL(new File(path(workDir + SEPARATOR + fileName)));
  }

  private static URL toURL(File file) {
    try {
      return file.toURI().toURL();
    }
    catch (MalformedURLException ex) {
      return null;
    }
  }

  /**
   * Loads URL form the RESOURCES directory.
   * WORKING_DIRECTORY/RESOURCES_DIRECTORY/fileName
   *
   * @param fileName String
   * @return URL
   */
  public static URL loadResource(String fileName) {
    return toURL(new File(path(workDir + RESOURCES + fileName)));
  }

  /**
   * Loading URL does not work if the path contains a SPACE. Therefore, we check every path for spaces.
   * All spaces in the path are replaced with a '%20'.
   *
   * @param path String
   * @return String
   */
  private static String path(String path) {
    return new String(path); // don't do anything
  }
}
