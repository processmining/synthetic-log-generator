package nl.tue.declare.datamanagement.template;

import java.util.*;
import java.util.Map.Entry;

import org.w3c.dom.*;

import nl.tue.declare.datamanagement.XMLBroker;
import nl.tue.declare.datamanagement.XMLElementFactory;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.*;

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
public class TemplateElementFactory
    extends XMLElementFactory {

  private static final String TAG_LANGUAGE = "language";
  private static final String TAG_LANGUAGE_NAME = "name";

  private static final String TAG_GROUP = "group";
  private static final String TAG_GROUP_NAME = "name";

  private static final String TAG_TEMPL = "template";
  private static final String TAG_DESCRIPTION = "description";
  private static final String TAG_DISPLAY = "display";

  private static final String TAG_FORMULA_NAME = "name";
  private static final String TAG_FORMULA_TEXT = "text";

  private static final String TAG_FORMULA_PARAMETERS = "parameters";
  private static final String TAG_FORMULA_PARAMETER = "parameter";
  private static final String TAG_FORMULA_PARAMETER_NAME = "name";
  private static final String TAG_FORMULA_PARAMETER_BRANCHABLE = "branchable";
  
  
  private static final String TAG_STATE_MESSAGES = "statemessages";
  private static final String TAG_MESSAGE = "message";
  private static final String TAG_STATE = "state";

  private static final String TAG_GRAPHICAL = "graphical";
  private static final String TAG_LINE_NUMBER = "number";
  private static final String TAG_LINE_STYLE = "style";
  private static final String TAG_LINE_STYLE_ITEM = "item";
  private static final String TAG_LINE_STYLE_VALUE = "value";
  private static final String TAG_BEGIN = "begin";
  private static final String TAG_BEGIN_STYLE = "style";
  private static final String TAG_BEGIN_FILL = "fill";
  private static final String TAG_MIDDLE = "middle";
  private static final String TAG_MIDDLE_STYLE = "style";
  private static final String TAG_MIDDLE_FILL = "fill";
  private static final String TAG_END = "end";
  private static final String TAG_END_STYLE = "style";
  private static final String TAG_END_FILL = "fill";

  private static final String TAG_EVENT = "event";
  private static final String TAG_EVENT_TYPE = "type";
  private static final String TAG_EVENT_USER = "user";
  private static final String TAG_EVENT_JOB = "job";

  public TemplateElementFactory(XMLBroker broker) {
    super(broker);
  }

  public TemplateElementFactory(XMLElementFactory factory) {
    super(factory);
  }


  /**
   * parameterToElement
   *
   * @param parameter FormalParameter
   * @return Element
   */
  public Element parameterToElement(Parameter parameter) {
    // *** create a new element for the user
    Element element = baseToElement(parameter, TAG_FORMULA_PARAMETER);
    element.setAttribute(TAG_FORMULA_PARAMETER_NAME, parameter.getName());
    //element.setAttribute(TAG_FORMULA_PARAMETER_TYPE,parameter.getType().getName());
    element.setAttribute(TAG_FORMULA_PARAMETER_BRANCHABLE,
                         Boolean.toString(parameter.isBranchable()));

    // *** return the element for the user
    this.lineStyleToElement(parameter.getStyle(),getFirstElement(element, TAG_GRAPHICAL));
    return element;
  }

  public Language elementToLanguage(Element element) {
    int id = elementToBase(element).getId();
    Language lang = new Language(id,
                                 element.getAttribute(TAG_LANGUAGE_NAME));
    fillLanguage(element, lang, lang);
    /*Element el;
         ConstraintTemplate template;
         NodeList templates = element.getElementsByTagName(TAG_TEMPL);
         for (int i = 0; i < templates.getLength(); i++) {
      el = (Element) templates.item(i);
      template = elementToTemplate(lang, el);
      lang.add(template);
         }*/
    return lang;
  }

  private void fillLanguage(Element element, LanguageGroup parent,
                            Language lang) {
    NodeList children = element.getChildNodes();
    if (element.getNodeName().equals(TAG_TEMPL)) { // child is TEMPLATE
      parent.add(elementToTemplate(lang, element));
    }
    else { // child is LANGUAGEGROUP
      LanguageGroup group = null;
      if (element.getNodeName().equals(TAG_GROUP)) {
        group = elementToGroup(element); // create LANGUAGEGROUP
        parent.add(group); // add it to the parent only if this is not the language itself
      }
      else {
        group = lang;
      }
      for (int i = 0; i < children.getLength(); i++) { // loop through all children of the LANGUAGEGROUP
        Node node = children.item(i);
        if (node instanceof Element) {
          fillLanguage( (Element) node, group, lang);
        }
      }
    }
  }

  /**
   * elementToTemplate
   *
   * @param lang Language
   * @param element Element
   * @return ConstraintTemplate
   */
  public ConstraintTemplate elementToTemplate(Language lang, Element element) {

    String descrition = getSimpleElementText(element, TAG_DESCRIPTION);
    String display = getSimpleElementText(element, TAG_DISPLAY);

    String name = getSimpleElementText(element, TAG_FORMULA_NAME);
    String text = getSimpleElementText(element, TAG_FORMULA_TEXT);

    Element parametersTag = getFirstElement(element,
        TAG_FORMULA_PARAMETERS);

    NodeList parameters = parametersTag.getElementsByTagName(
        TAG_FORMULA_PARAMETER);

    List<Parameter> parametersList = new ArrayList<Parameter>();
    for (int i = 0; i < parameters.getLength(); i++) {
      Element parameterElement = (Element) parameters.item(i);

      String parameterName = parameterElement.getAttribute(TAG_FORMULA_PARAMETER_NAME);
      //String type = parameterElement.getAttribute(TAG_FORMULA_PARAMETER_TYPE);

      String branchableString = parameterElement.getAttribute(TAG_FORMULA_PARAMETER_BRANCHABLE);
      boolean branchable = Boolean.parseBoolean(branchableString);

      Parameter parameter = new Parameter(elementToBase(
          parameterElement).
          getId(),
          parameterName);
      parameter.setBranchable(branchable);

      // *** read the graphical representation
      Element graphicalTag = getFirstElement(parameterElement,
          TAG_GRAPHICAL);
      this.elementToLineStyle(graphicalTag, parameter.getStyle());

      parametersList.add(parameter);
    }
    
    int id = elementToBase(element).getId();
    ConstraintTemplate template = new ConstraintTemplate(id, lang);
    for (int i = 0; i < parametersList.size(); i++) {
      template.addParameter(parametersList.get(i));
    }

    if (template != null) {
      template.setName(name);
      template.setText(text);
    }

    if (template != null) {
      template.setDescription(descrition);
      template.setDisplay(display);
    }
    
    	// get state messages
    Element stateMessagesTag = getFirstElement(element,TAG_STATE_MESSAGES);
    NodeList stateMessages = stateMessagesTag.getElementsByTagName(TAG_MESSAGE);
    for (int i =0; i <stateMessages.getLength(); i++){
    	Element stateMessage = (Element) stateMessages.item(i);
    	String state = stateMessage.getAttribute(TAG_STATE);
    	String message = stateMessage.getFirstChild().getNodeValue();
    	template.setStateMessage(State.valueOf(state), message);
    }
    
    return template;
  }

  /**
   * getListTemplates
   *
   * @param element Element
   * @return NodeList
   */
  public NodeList getListLanguages(Element element) {
    return element.getElementsByTagName(TAG_LANGUAGE);
  }

  /**
   * getListTemplates
   *
   * @param element Element
   * @return NodeList
   */
  public NodeList getListTemplates(Element element) {
    NodeList list = element.getElementsByTagName(TAG_TEMPL);
    return list;
  }

  /**
   * elementToGraphical
   *
   * @param element Element
   * @param style DGraphical
   */
  public void elementToLineStyle(Element element, LineStyle style) {
    this.elementToLine(element, style);
  }

  /**
   * elementToLine
   *
   * @param element Element
   * @param line LineStyle
   */
  public void elementToLine(Element element, LineStyle line) {
    // *** get all tags from the line element
    Element styleTag = getFirstElement(element, TAG_LINE_STYLE);
    Element beginTag = getFirstElement(element, TAG_BEGIN);
    Element middleTag = getFirstElement(element, TAG_MIDDLE);
    Element endTag = getFirstElement(element, TAG_END);

    String number = styleTag.getAttribute(TAG_LINE_NUMBER);
    int nr = LineStyle.DEFAULT_NUMBER;
    try {
      nr = Integer.parseInt(number);
    }
    catch (NumberFormatException ex) {}
    ;

    // *** read the style tag
    NodeList items = styleTag.getElementsByTagName(TAG_LINE_STYLE_ITEM);
    float[] f = null;
    if (items.getLength() > 0) {
      f = new float[items.getLength()];
      for (int i = 0; i < items.getLength(); i++) {
        Element item = (Element) items.item(i);
        String value = item.getAttribute(TAG_LINE_STYLE_VALUE);
        if (value != null) {
          f[i] = Float.parseFloat(value);
        }
      }
    }
    // *** read the begin style
    String beginStyle = beginTag.getAttribute(TAG_BEGIN_STYLE);
    String beginFill = beginTag.getAttribute(TAG_BEGIN_FILL);
    int bs = Integer.parseInt(beginStyle);
    boolean bf = Boolean.parseBoolean(beginFill);

    // *** read the middle style
    String middleStyle = middleTag.getAttribute(TAG_MIDDLE_STYLE);
    String middleFill = middleTag.getAttribute(TAG_MIDDLE_FILL);
    int ms = LineStyle.DEFAULT_MIDDLE;
    try {
      ms = Integer.parseInt(middleStyle);
    }
    catch (NumberFormatException ex) {}
    ;

    boolean mf = Boolean.parseBoolean(middleFill);

    // *** read the end style
    String endStyle = endTag.getAttribute(TAG_END_STYLE);
    String endFill = endTag.getAttribute(TAG_END_FILL);
    int es = Integer.parseInt(endStyle);
    boolean ef = Boolean.parseBoolean(endFill);

    line.setLine(f);
    line.setNumber(nr);
    line.setBegin(bs);
    line.setBeginFill(bf);
    line.setMiddle(ms);
    line.setMiddleFill(mf);
    line.setEnd(es);
    line.setEndFill(ef);
  }

  /**
   * updateTemplateElement
   *
   * @param template ConstraintTemplate
   * @param element Element
   */
  public void templateToElement(ConstraintTemplate template, Element element) {

    updateObjectAttribute(element, TAG_DESCRIPTION, template.getDescription());
    updateObjectAttribute(element, TAG_DISPLAY, template.getDisplay());

    // *** update name and text attributes
    updateObjectAttribute(element, TAG_FORMULA_NAME, template.getName());
    updateObjectAttribute(element, TAG_FORMULA_TEXT, template.getText());

    Element parameters = getFirstElement(element,TAG_FORMULA_PARAMETERS);
    removeChildren(parameters);
    for (Parameter p: template.getParameters()) {
      Element parameter = this.parameterToElement(p);
      parameters.appendChild(parameter);
    }
    
    // write state messages
    Element stateMessages = getFirstElement(element, TAG_STATE_MESSAGES);
    removeChildren(stateMessages);
    Set<Entry<State, String>> msgs = template.getStateMessages();
    for (Entry<State, String> entry :msgs){
        // *** create a new element for the message
        Element message =  this.createObjectAttribute(TAG_MESSAGE, entry.getValue());
        message.setAttribute(TAG_STATE, entry.getKey().name());
        stateMessages.appendChild(message);
    }
  }

  /**
   * updateTemplateElement
   *
   * @param template ConstraintTemplate
   * @param element Element
   */
  public void updateTemplateElement(ConstraintTemplate template,
                                    Element element) {
    this.templateToElement(template, element);
  }

  /**
   * updateTemplateElement
   *
   * @param line ConstraintTemplate
   * @param element Element
   */
  public void lineStyleToElement(LineStyle line, Element element) {

    // *** get tags for style, begin and end
    Element styleTag = getFirstElement(element, TAG_LINE_STYLE);
    Element beginTag = getFirstElement(element, TAG_BEGIN);
    Element middleTag = getFirstElement(element, TAG_MIDDLE);
    Element endTag = getFirstElement(element, TAG_END);

    // *** create items in the styleTag
    Element styleItem;
    Float item = new Float(0);
    float[] array = line.getLine();
    this.removeChildren(styleTag);
    if (array != null) {
      for (int i = 0; i < array.length; i++) {
        item = array[i];
        styleItem = createElement(TAG_LINE_STYLE_ITEM);
        styleItem.setAttribute(TAG_LINE_STYLE_VALUE, item.toString());
        styleTag.appendChild(styleItem);
      }
    }

    styleTag.setAttribute(TAG_LINE_NUMBER, String.valueOf(line.getNumber()));

    // *** fill the element for the begin style
    beginTag.setAttribute(TAG_BEGIN_STYLE, String.valueOf(line.getBegin()));
    beginTag.setAttribute(TAG_BEGIN_FILL,
                          String.valueOf(line.isBeginFill()));

    // *** fill the element for the end style
    middleTag.setAttribute(TAG_MIDDLE_STYLE, String.valueOf(line.getMiddle()));
    middleTag.setAttribute(TAG_MIDDLE_FILL,
                           String.valueOf(line.isMiddleFill()));

    // *** fill the element for the end style
    endTag.setAttribute(TAG_END_STYLE, String.valueOf(line.getEnd()));
    endTag.setAttribute(TAG_END_FILL, String.valueOf(line.isEndFill()));
  }

  /**
   * getTemplateElement
   *
   * @param template ConstraintTemplate
   * @param element Element
   * @return Element
   */
  public Element getTemplateElement(ConstraintTemplate template,
                                    Element element) {
    NodeList templates = element.getElementsByTagName(TAG_TEMPL);
    boolean found = false;
    Element current = null;
    ConstraintTemplate currentTemplate;
    int i = 0;
    while ( (!found) && (i < templates.getLength())) {
      current = (Element) templates.item(i++);
      currentTemplate = this.elementToTemplate(template.getLanguage(), current);
      found = currentTemplate.equals(template);
    }
    return found ? current : null;
  }

  /**
   * createTemplateElement
   *
   * @param template ConstraintTemplate
   * @return Element
   */
  public Element createTemplateElement(ConstraintTemplate template) {

    // *** create a new element for the template
    Element element = baseToElement(template, TAG_TEMPL);

    // *** fill the element with template attributes
    this.updateTemplateElement(template, element);

    // *** return the template element
    return element;
  }

  /**
   * createTemplateElement
   *
   * @param group ConstraintTemplate
   * @return Element
   */
  public Element createGroupElement(LanguageGroup group) {

    // *** create a new element for the group
    Element element = baseToElement(group, TAG_GROUP);

    // *** fill the element with group attributes
    this.updateGroupElement(group, element);

    // *** return the element
    return element;
  }

  /**
   * updateGroupElement
   *
   * @param group ConstraintTemplate
   * @param element Element
   */
  public void updateGroupElement(LanguageGroup group, Element element) {
    this.groupToElement(group, element);
  }

  /**
   * updateTemplateElement
   *
   * @param group ConstraintTemplate
   * @param element Element
   */
  public void groupToElement(LanguageGroup group, Element element) {
    element.setAttribute(TAG_GROUP_NAME, group.getName());
  }

  /**
   * createTemplateElement
   *
   * @param language ConstraintTemplate
   * @return Element
   */
  public Element createLanguageElement(Language language) {

    // *** create a new element for the template
    //Element element = broker.createElement(TAG_LANGUAGE);
    Element element = baseToElement(language, TAG_LANGUAGE);

    this.setAttribute(element, TAG_LANGUAGE_NAME, language.getName());
    // add all templates
    Iterator<IItem> it = language.getChildren().iterator();
    //for (int i = 0; i < language.size(); i++ ){
    while (it.hasNext()) {
      //ConstraintTemplate tem = language.withId(i);
      IItem item = it.next();
      if (item instanceof ConstraintTemplate) {
        ConstraintTemplate tem = (ConstraintTemplate) item;
        Element newTemplate = createTemplateElement(tem);
        element.appendChild(newTemplate);
      }
    }

    // *** return the template element
    return element;
  }

  /**
   *
   * @param event AbstractEvent
   * @return Element
   */
  public Element eventToElement(Event event) {
    Element element = getDocument().createElement(TAG_EVENT);

    this.setAttribute(element, TAG_EVENT_TYPE, event.getType().name());
    this.updateObjectAttribute(element, TAG_EVENT_USER,
                               event.getUser().getIdString());
    this.updateObjectAttribute(element, TAG_EVENT_JOB,
                               event.getActivity().getName());

    //  return the event element
    return element;
  }

  /**
   *
   * @param lang Language
   * @param element Element
   * @return AbstractEvent
   */
  public Event elementToEvent(Language lang, Element element) {
    Event event = null;

    Element eventTag = getFirstElement(element, TAG_EVENT);

    String type = eventTag.getAttribute(TAG_EVENT_TYPE);
    String userId = getSimpleElementText(eventTag, TAG_EVENT_USER);
    String activityDefinitionName = getSimpleElementText(eventTag,
        TAG_EVENT_JOB);

    User user = new User(Integer.decode(userId));
    AssignmentModel model = new AssignmentModel(lang);
    ActivityDefinition activityDefinition = model.addActivityDefinition();
    activityDefinition.setName(activityDefinitionName);

    event = new Event(user, activityDefinition, Event.Type.valueOf(type));
    return event;
  }

  public Element getLanguageElement(Element element, Language lang) {
    Element langElement = null;
    NodeList languages = element.getElementsByTagName(TAG_LANGUAGE);
    boolean found = false;
    int i = 0;
    while ( (i < languages.getLength()) && !found) {
      langElement = (Element) languages.item(i++);
      found = langElement.getAttribute(TAG_LANGUAGE_NAME).equals(lang.
          getName());
    }
    return found ? langElement : null;
  }

  public LanguageGroup elementToGroup(Element element) {
    int id = elementToBase(element).getId();
    LanguageGroup group = new LanguageGroup(id);
    group.setName(element.getAttribute(TAG_GROUP_NAME));
    return group;
  }

  public Element getGroupElement(Element element, LanguageGroup group) {
    Element groupElement = null;
    Iterator<Element> groups = getAllSubElements(element, TAG_GROUP).
        iterator();
    LanguageGroup temp = null;
    boolean found = false;
    while (groups.hasNext() && !found) {
      groupElement = groups.next();
      temp = this.elementToGroup(groupElement);
      found = temp.equals(group);
    }
    return found ? groupElement : null;
  }
}
