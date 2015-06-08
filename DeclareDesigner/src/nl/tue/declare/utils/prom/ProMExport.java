package nl.tue.declare.utils.prom;

import java.io.*;
import java.util.*;

import java.awt.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;

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
public class ProMExport {

  public static boolean templates(Collection<ConstraintTemplate> templates,
      Component component, String language) {
    boolean ok = false;
    // create a dilaog for LTL files
    LTLFileDialog dialog = new LTLFileDialog(language);
    // store the path of the seelcted file
    String file = dialog.saveFile(component);
    // if the path is selected
    if (file != null) {
      try {
        PrintStream out = new PrintStream(new File(file));
        TemplateCollection collection = new TemplateCollection(out);
        Iterator<ConstraintTemplate> iterator = templates.iterator();
        while (iterator.hasNext()) {
          collection.add(iterator.next());
        }
        collection.write();
        ok = true;
      }
      catch (FileNotFoundException ex) {
        ex.printStackTrace();
        // ignore
      }
    }
    return ok;
  }

  public static boolean model(AssignmentModel model, Component component) {
    boolean ok = false;
    if (model.constraintDefinitionsCount() > 0) {

      // create a dilaog for LTL files
      LTLFileDialog dialog = new LTLFileDialog(model.getName());
      // store the path of the seelcted file
      String file = dialog.saveFile(component);
      // if the path is selected
      if (file != null) {
        try {
          PrintStream out = new PrintStream(new File(file));
          ConstraintCollection collection = new ConstraintCollection(out);
          for (int i = 0; i < model.constraintDefinitionsCount(); i++) {
            collection.add(model.constraintDefinitionAt(i));
          }
          collection.write();
          ok = true;
        }
        catch (FileNotFoundException ex) {
          ex.printStackTrace();
          // ignore
        }
      }
    }
    return ok;
  }
}
