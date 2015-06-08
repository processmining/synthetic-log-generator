package nl.tue.declare.logging;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XMxmlSerializer;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.utils.*;

public class ProcessLogWriter {
	
  private static final String LOG = File.separator + "log" + File.separator;
  private static ProcessLogWriter instance = null;

  private HashMap<String, ModelLogWriter> models;
  private String directory;

  /**
   *
   */
  private ProcessLogWriter(OutputStream out) {
    super();
    models = new HashMap<String, ModelLogWriter>();
    
    PrettyTime time = new PrettyTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
 	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
 	directory = dateFormat.format(new Date(time.getTimeInMillis()));    
  }

  /**
   *
   * @return ProcessLogWriter
   */
  public static ProcessLogWriter singleton() {
    if (instance == null) {
      instance = new ProcessLogWriter(System.out);
    }
    return instance;
  }

  /**
   *
   * @param fileName String
   * @return OutputStream
   */
  private OutputStream getModelLogStream(String name) {
    OutputStream log = null;
    try {
      if (name != null) {
        File file = getFile(name);
        if (file != null) {
          log = new FileOutputStream(file);
        }
      }
    }
    catch (Exception ex) {
      log = System.out;
    }
    return log;
  }

  /**
   *
   * @param fileName String
   * @return File
   */
  private File getFile(String fileName) {
    File file = null;
    if (fileName != null) {
      try {
        (new File(URLLoader.loadURL(LOG + File.separator + directory +
                                    File.separator).toURI())).mkdirs();
        file = new File(URLLoader.loadURL(LOG + File.separator + directory +
                                          File.separator, fileName + "_log.xml").
                        toURI());
        if (!file.exists()) {
          file.createNewFile();
        }
      }
      catch (Exception ex) {
        // ex.printStackTrace();
      }
    }
    return file;
  }

  private ModelLogWriter get(Assignment assignment) {
  ModelLogWriter writer = models.get(assignment.getName());
    if (writer == null) {
        writer = new ModelLogWriter(/*this.getModelLogStream(assignment.getName()),*/
                                    assignment);
        models.put(assignment.getName(),writer);
      }       
    return writer;
  }

  /**
   *
   * @param assignment Assignment
   * @param event AbstractEvent
   */
  public void add(Assignment assignment, Event event) {
    ModelLogWriter writer = this.get(assignment);
    if (writer != null) {
      writer.add(assignment, event);
    }
  }

  public AssignmentLog getAssignmentLog(Assignment assignment) {
    AssignmentLog log = null;
    if (assignment != null) {
      ModelLogWriter writer = this.get(assignment);
      log = writer.getAssignmnetLog(assignment);
    }
    return log;
  }
  
	public static String serializeLog(XLog o) throws IOException {

		XMxmlSerializer serializer = new XMxmlSerializer();
	
		OutputStream output = new ByteArrayOutputStream();
	
		serializer.serialize(o, output);
		
		String xml = output.toString();
		return xml;
	}

  /**
   *
   */
  public void finish() {
    try {
      for (Map.Entry<String, ModelLogWriter> entry: models.entrySet()) {
       // iterator.next().finish();
    	  OutputStream out = getModelLogStream(entry.getKey());
    	  XLog log = entry.getValue().getLog();
    	  String str = serializeLog(log);
    	  out.write(str.getBytes());
    	  out.flush();
      }
    }
    catch (Exception ex) {
      //ex.printStackTrace();
    }
  }

  /**
   *
   * @param assignment Assignment
   * @param activity Activity
   */
  public void schedule(Assignment assignment, ActivityDefinition activity) {
    ModelLogWriter writer = this.get(assignment);
    if (writer != null) {
      writer.schedule(assignment, activity);
    }
  }

  /**
   *
   * @param assignment Assignment
   * @param activity Activity
   * @param user User
   */
  public void assign(Assignment assignment, ActivityDefinition activity,
                     User user) {
    ModelLogWriter writer = this.get(assignment);
    if (writer != null) {
      writer.assign(assignment, activity, user);
    }
  }
}
