package nl.tue.declare.datamanagement.assignment;

import org.w3c.dom.*;
import nl.tue.declare.datamanagement.*;
import nl.tue.declare.domain.model.*;

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
public class XMLAssignmentBroker
    extends XMLBroker implements AssignmentBroker {

  private AssignmentElementFactory factory;

  public XMLAssignmentBroker(String aConnectionString, String name) {
    super(aConnectionString, name);
    factory = new AssignmentElementFactory(this);
  }

  /**
   *
   * @param model AssignmentModel
   * @param path String
   * @todo Implement this nl.tue.declare.datamanagement.AssignmentBroker method
   */
  public void addAssignment(AssignmentModel model) {
    Element newAssignment = this.factory.createAssignmentElement(model);

    Element root = this.getAssignmentElement();
    root.appendChild(newAssignment);
    writeDocument();
  }

  /**
   *
   * @return AssignmentModel
   * @todo Implement this nl.tue.declare.datamanagement.AssignmentBroker method
   * @param path String
   */
  public AssignmentModel readAssignment( /*String path*/) {
    readDocument( /*path*/);
    Element root = this.getDocumentRoot();
    AssignmentModel model = factory.elementToAssignmentModel(root);
    return model;
  }

  /**
   *
   * @param documentString Node
   * @return AssignmentModel
   */
  public AssignmentModel readAssignmentfromString(String documentString) {
    readDocumentString(documentString);
    Element root = this.getDocumentRoot();
    AssignmentModel model = factory.elementToAssignmentModel(root);
    return model;
  }

  /**
   * getAssignmentElement
   *
   * @return Element
   */
  public Element getAssignmentElement() {
    return this.getDocumentRoot();
  }
}
