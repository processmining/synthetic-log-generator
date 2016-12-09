package nl.tue.declare.datamanagement.assignment;

import org.w3c.dom.*;

import nl.tue.declare.datamanagement.XMLBroker;
import nl.tue.declare.datamanagement.XMLElementFactory;
import yawlservice.*;

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
public class ExternalCaseElementFactory    extends XMLElementFactory {

   private static final String TAG_MAIN = "yawl";
   private static final String TAG_FILE_PATH = "path";
   private static final String TAG_FILE_NAME = "file";
   private static final String TAG_SPEC = "specification";
   private static final String TAG_DEC = "decomposition";
  /**
    * ElementFactory
    *
    * @param aBroker XMLBroker
    */
   public ExternalCaseElementFactory(XMLBroker aBroker) {
     super(aBroker);
   }

   public ExternalCaseElementFactory(XMLElementFactory factory) {
     super(factory);
  }

   /**
    * elementToAssignmentGraphical
    *
    * @param view model
    * @param model AssignmentModel
    * @param element Element
    */
   public Element findYawlElement(Element element) {
     return findFirstElement(element, TAG_MAIN);
   }

   /**
    * createAssignmentElement
    *
    * @param model model
    * @return Element
    */
   public Element createAssignmentElement(ExternalCase ec) {
     Element element = getDocument().createElement(TAG_MAIN);
    // Element element = getYawlElement(main);
     setAttribute(element, TAG_FILE_PATH, ec.getFilePath());
     setAttribute(element, TAG_FILE_NAME,ec.getFileName());
     setAttribute(element, TAG_SPEC, ec.getStecificationID());
     setAttribute(element, TAG_DEC, ec.getDecomopositionID());
     return element;
   }


   public void elementToExternalCase(ExternalCase ec,
                                            Element element) {
        // YAWL sub-process
     ec.setFilePath(element.getAttribute(TAG_FILE_PATH));
     ec.setFileName(element.getAttribute(TAG_FILE_NAME));
     ec.setSpecificationID(element.getAttribute(TAG_SPEC));
     ec.setDecomopositionID(element.getAttribute(TAG_DEC));
    }

}
