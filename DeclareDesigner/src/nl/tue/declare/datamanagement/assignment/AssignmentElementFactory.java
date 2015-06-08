package nl.tue.declare.datamanagement.assignment;

import java.util.*;

import org.w3c.dom.*;
import nl.tue.declare.control.*;
import nl.tue.declare.datamanagement.XMLBroker;
import nl.tue.declare.datamanagement.XMLElementFactory;
import nl.tue.declare.datamanagement.organization.*;
import nl.tue.declare.datamanagement.template.*;
import nl.tue.declare.domain.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.domain.template.*;

/**
  * <p>Title: DECLARE</p>
  *
  * <p>Description: </p>
  *
  * <p>Copyright: Copyright (c) 2006</p>
  *
  * <p>Company: TU/e</p>
  * @author Maja Pesic
  * @version 1.0
  */
 public class AssignmentElementFactory
     extends XMLElementFactory {

   private static final String TAG_ASSIGNMENT = "assignment";
   private static final String TAG_ASSIGNMENT_NAME = "name";
   private static final String TAG_ASSIGNMENT_LANGUAGE = "language";

   private static final String TAG_ACTIVITY_DEFINITIONS =
       "activitydefinitions";
   private static final String TAG_ACTIVITY_DEFINITION = "activity";
   private static final String TAG_ACTIVITY_DEFINITION_NAME = "name";
   private static final String TAG_ACTIVITY_DEFINITION_AUTHORIZATIONS =
       "authorization";

   private static final String TAG_ACTIVITY_DEFINITION_DATA_MODEL =
       "datamodel";
   private static final String TAG_ACTIVITY_DEFINITION_DATA_DEFINITION =
       "data";
   private static final String TAG_ACTIVITY_DEFINITION_DATA_ELEMENT =
       "element";
   private static final String TAG_ACTIVITY_DEFINITION_DATA_TYPE =
       "type";
   private static final String TAG_CONSTRAINT_DEFINITIONS =
       "constraintdefinitions";
   private static final String TAG_CONSTRAINT_DEFINITION = "constraint";
   private static final String TAG_CONSTRAINT_DEFINITION_NAME = "name";
   private static final String TAG_CONSTRAINT_DEFINITION_MANDATORY =
       "mandatory";
   private static final String TAG_CONSTRAINT_DEFINITION_CONDITION =
       "condition";
   private static final String TAG_CONSTRAINT_DEFINITION_TEMPLATE = "template";
   private static final String TAG_CONSTRAINT_DEFINITION_PARAMETERS =
       "constraintparameters";
   private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER =
       "parameter";
   private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_LTL =
       "templateparameter";
   private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCHES =
       "branches";
   private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH =
       "branch";
   private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH_NAME =
       "name";

   private static final String TAG_CONSTRAINT_DEFINITION_LEVEL = "level";
   private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP = "group";
   private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_NAME =
       "name";
   private static final String
       TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_DESCRIPTION = "description";
   private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_PRIORITY =
       "priority";
   private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_MESSAGE =
       "message";

   private static final String TAG_DATA = "data";
   private static final String TAG_DATA_ELEMENT = "dataelement";
   private static final String TAG_DATA_ELEMENT_NAME = "name";
   private static final String TAG_DATA_ELEMENT_TYPE = "type";
   private static final String TAG_DATA_ELEMENT_INITIAL = "initial";

   private static final String TAG_TEAM = "team";
   private static final String TAG_TEAM_ROLE = "teamrole";
   private static final String TAG_TEAM_ROLE_NAME = "name";
   private static final String TAG_TEAM_ROLE_ROLE = "role";

   /**
    * ElementFactory
    *
    * @param aBroker XMLBroker
    */
   public AssignmentElementFactory(XMLBroker aBroker) {
     super(aBroker);
   }

   /**
    * elementToAssignmentGraphical
    *
    * @param view model
    * @param model AssignmentModel
    * @param element Element
    */
   protected Element getAssignmentElement(Element element) {
     return getFirstElement(element, TAG_ASSIGNMENT);
   }

   /**
    * createAssignmentElement
    *
    * @param model model
    * @return Element
    */
   public Element createAssignmentElement(AssignmentModel model) {

     // *** create a new element for the template
     Element element = getDocument().createElement(TAG_ASSIGNMENT);

     // *** fill the element with template attributes
     this.assignmentToElement(model, element);

     // *** return the template element
     return element;
   }

   /**
    * assignmentToElement
    *
    * @param model model
    * @param element Element
    */
   public void assignmentToElement(AssignmentModel model, Element element) {
     // *** update name and text attributes
     setAttribute(element, TAG_ASSIGNMENT_NAME, model.getName());
     setAttribute(element, TAG_ASSIGNMENT_LANGUAGE,
                  model.getLanguage().getName());

     this.attributesToElement(model.getAttributes(),element);

     // handle job descriptions
     activitiesToElement(model, element);
     // handle constraint defitions
     constraintsToElement(model, element);
     dataToElement(model, element);
     rolesToElement(model.getTeamModel(), element);
   }

   /**
    *
    * @param model AssignmentModel
    * @param element Element
    */
   private void activitiesToElement(AssignmentModel model, Element element) {
     // get the element containing the list of activities
     Element activities = getFirstElement(element,TAG_ACTIVITY_DEFINITIONS);
     // clear all activities because we will write them again
     removeChildren(activities);
     // loop through the all jobs in the model
     for (int i = 0; i < model.activityDefinitionsCount(); i++) {
       // create the element for every activity
       Element job = this.activityDefinitionToElement(model.
           activityDefinitionAt(i));
       // add the element to the list of activities
       activities.appendChild(job);
     }
   }

   /**
    *
    * @param model AssignmentModel
    * @param element Element
    */
   private void constraintsToElement(AssignmentModel model, Element element) {
     // get the element containing the list of constraint definitons
     Element constraints = getFirstElement(element,TAG_CONSTRAINT_DEFINITIONS);
     // clear all constraint definitonsbecause we will write them again
     removeChildren(constraints);
     // loop through the all constraint definitons in the model
     for (int i = 0; i < model.constraintDefinitionsCount(); i++) {
       // create the element for every constraint definiton
       Element constraint = this.constraintDefintionToElement(model.
           constraintDefinitionAt(i));
       // add the element to the list of constraint definitons
       constraints.appendChild(constraint);
     }
   }

   /**
    *
    * @param activity ActivityDefinition
    * @return Element
    */
   private Element activityDefinitionToElement(ActivityDefinition activity) {
     Element element = baseToElement(activity, TAG_ACTIVITY_DEFINITION);
     setAttribute(element, TAG_ACTIVITY_DEFINITION_NAME, activity.getName());

     if (activity.isExternal()){
       ExternalCaseElementFactory temp = new ExternalCaseElementFactory(this);
       element.appendChild(temp.createAssignmentElement(activity.getExternalCase()));
       /*
       if (activity.getExternalFilePath() != null){
         setAttribute(element, TAG_ACTIVITY_DEFINITION_YAWL_FILE_PATH,
                      activity.getExternalFilePath());
       }

       if (activity.getExternalFileName() != null){
         setAttribute(element, TAG_ACTIVITY_DEFINITION_YAWL_FILE_NAME,
                      activity.getExternalFileName());
       }
       setAttribute(element, TAG_ACTIVITY_DEFINITION_YAWL_SPEC, activity.getExternalSpecification());
       setAttribute(element, TAG_ACTIVITY_DEFINITION_YAWL_DEC, activity.getExternalDecomposition());*/
     }

     element.appendChild(authorizationToElement(activity.getAuthorization()));
     element.appendChild(dataModelToElement(activity));
     return element;
   }

   private Element authorizationToElement(Authorization authorization) {
     Element element = createElement(TAG_ACTIVITY_DEFINITION_AUTHORIZATIONS);
     for (int i = 0; i < authorization.count(); i++) {
       TeamRole role = authorization.authorizedAt(i);
       Element roleElement = createObjectAttribute(TAG_TEAM_ROLE,
           role.getIdString());
       element.appendChild(roleElement);
     }
     // *** return the authorization element
     return element;
   }

   private Element dataModelToElement(ActivityDefinition activity) {
     Element element = createElement(TAG_ACTIVITY_DEFINITION_DATA_MODEL);
     Iterator<ActivityDataDefinition> iterator = activity.data();
     while (iterator.hasNext()) {
       ActivityDataDefinition data = iterator.next();
       Element dataElement = this.activityDataDefinitionToElement(data);
       element.appendChild(dataElement);
     }
     // *** return the data model element
     return element;
   }

   private Element activityDataDefinitionToElement(ActivityDataDefinition data) {
     Element element = createElement( TAG_ACTIVITY_DEFINITION_DATA_DEFINITION);
     setAttribute(element, TAG_ACTIVITY_DEFINITION_DATA_ELEMENT,
                       data.getDataElement().getIdString());
     String type = Integer.toString(data.getType().code());
     setAttribute(element, TAG_ACTIVITY_DEFINITION_DATA_TYPE, type);
     return element;
   }

   /**
    *
    * @param constraint ConstraintDefinition
    * @return Element
    */
   private Element constraintDefintionToElement(ConstraintDefinition
                                                constraint) {
     Element element = baseToElement(constraint, TAG_CONSTRAINT_DEFINITION);
     Boolean mandatory = constraint.getMandatory();
     this.setAttribute(element, TAG_CONSTRAINT_DEFINITION_MANDATORY,
                       mandatory.toString());
     Element levelElement = this.constraintLevetToElement(constraint.getLevel());
     if (levelElement != null) {
       element.appendChild(levelElement);
     }

     this.updateObjectAttribute(element,
                                TAG_CONSTRAINT_DEFINITION_CONDITION,
                                constraint.getCondition().getText());
     this.updateObjectAttribute(element,
                                TAG_CONSTRAINT_DEFINITION_NAME,
                                constraint.getName());

     TemplateElementFactory templateFactory = new TemplateElementFactory(this);

     Element template = getFirstElement(element,TAG_CONSTRAINT_DEFINITION_TEMPLATE);
     templateFactory.templateToElement(constraint, template);

     this.parametersToElement(constraint, element);

     // *** return the constraint defintion element
     return element;
   }

   private Element constraintLevetToElement(ConstraintLevel level) {
     Element element = null;
     if (level != null) {
       element = createElement(TAG_CONSTRAINT_DEFINITION_LEVEL);

       Element group = this.constraintGroupToElement(level.getGroup());
       element.appendChild(group);

       updateObjectAttribute(element,TAG_CONSTRAINT_DEFINITION_LEVEL_PRIORITY,
                                  Integer.toString(level.getLevel()));
       updateObjectAttribute(element,TAG_CONSTRAINT_DEFINITION_LEVEL_MESSAGE,
                                  level.getMessage());
     }
     return element;
   }

   private Element constraintGroupToElement(ConstraintGroup group) {
     Element element = baseToElement(group,TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP);
     updateObjectAttribute(element,TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_NAME,
                                group.getName());
     updateObjectAttribute(element,TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_DESCRIPTION,
                                group.getDescription());
     return element;
   }

   /**
    * parametersToElement
    *
    * @param constraint ConstraintDefinition
    * @param element Element
    */
   private void parametersToElement(ConstraintDefinition constraint,
                                    Element element) {
     // get the element containing the list of constraint definitons
     Element parameters = getFirstElement(element,TAG_CONSTRAINT_DEFINITION_PARAMETERS);
     // clear all constraint definitons because we will write them again
     removeChildren(parameters);
     // loop through the all constraint definitons in the model
     for (Parameter  p: constraint.getParameters()) {
       // create the element for every constraint definiton
       Element parameter = this.parameterToElement(constraint,p);
       // add the element to the list of constraint definitons
       parameters.appendChild(parameter);
     }
   }

   private Element parameterToElement(ConstraintDefinition constraintDefinition, Parameter parameter) {
     Element element = getDocument().createElement(
         TAG_CONSTRAINT_DEFINITION_PARAMETER);

     this.setAttribute(element, TAG_CONSTRAINT_DEFINITION_PARAMETER_LTL,
                       parameter.getIdString());

     this.branchesToElement(constraintDefinition, parameter, element);

     return element;
   }

   /**
    *
    * @param parameter Parameter
    * @param element Element
    */
   private void branchesToElement(ConstraintDefinition constraintDefinition, Parameter parameter, Element element) {
     // get the element containing the list of parameter branches
     Element branches = getFirstElement(element,TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCHES);     
     removeChildren(branches); // clear all parameter branches because we will write them again  
     
     for (ActivityDefinition real: constraintDefinition.getBranches(parameter)) { // loop through the all parameter branches in the model
       // create the element for every branch
       Element branch = this.branchToElement(real);
       // add the element to the list of parameter branches
       branches.appendChild(branch);

     }
   }

   /**
    *
    * @param branch ParameterBranch
    * @return Element
    */
   private Element branchToElement(ActivityDefinition branch) {
     Element element = createElement(TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH);
     this.setAttribute(element,
                       TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH_NAME,
                       branch.getName());

     // *** return the template element
     return element;
   }

  /**
    * elementToAssignmentModel
    *
    * @param element Element
    * @return AssignmentModel
    */
   public AssignmentModel elementToAssignmentModel(Element element) {
     AssignmentModel model = null;
     Element modelElement = getFirstElement(element,
         TAG_ASSIGNMENT);
     if (modelElement != null) {
       Language lang = Control.singleton().getConstraintTemplate().getLanguage(
           modelElement.getAttribute(TAG_ASSIGNMENT_LANGUAGE));
       if (lang != null) {
         model = new AssignmentModel(lang);

         // get attributes for nam eand external
         String name = modelElement.getAttribute(TAG_ASSIGNMENT_NAME);

         // set attributes for name and external
         model.setName(name);

        elementToAttributes(modelElement,model.getAttributes());


         // get team roles
         Element rolesTag = getFirstElement(modelElement, TAG_TEAM);
         NodeList roleElements = rolesTag.getElementsByTagName(TAG_TEAM_ROLE);
         for (int i = 0; i < roleElements.getLength(); i++) {
           Element roleElement = (Element) roleElements.item(i);
           TeamRole teamRole = elementToTeamRole(roleElement);
           model.getTeamModel().add(teamRole);
         }

         // get data elements
         Element dataTag = getFirstElement(modelElement,TAG_DATA);
         NodeList dataElements = dataTag.getElementsByTagName(TAG_DATA_ELEMENT);
         for (int i = 0; i < dataElements.getLength(); i++) {
           Element dataElement = (Element) dataElements.item(i);
           this.elementToDataElement(model, dataElement);
         }

         // get activities
         Element jobsTag = getFirstElement(modelElement,TAG_ACTIVITY_DEFINITIONS);
         NodeList jobs = jobsTag.getElementsByTagName(TAG_ACTIVITY_DEFINITION);
         for (int i = 0; i < jobs.getLength(); i++) {
           Element job = (Element) jobs.item(i);
           this.elementToActivityDefinition(model, job);
         }

         // get constraint definitions
         Element constraintsTag = getFirstElement(modelElement,TAG_CONSTRAINT_DEFINITIONS);
         NodeList constraints = constraintsTag.getElementsByTagName(TAG_CONSTRAINT_DEFINITION);
         for (int i = 0; i < constraints.getLength(); i++) {
           Element constraint = (Element) constraints.item(i);
           this.elementToConstraintDeintion(model, constraint);
         }
       }
     }
     return model;
   }

   /**
    *
    * @param element Element
    * @return TeamRole
    */
   private TeamRole elementToTeamRole(Element element) {
     // get the orgatizational role
     OrganizationElementFactory factory = new OrganizationElementFactory(this);
     Element roleElement = getFirstElement(element,TAG_TEAM_ROLE_ROLE);
     Role role = factory.elementToRole(roleElement);

     // get team role
     TeamRole teamRole = new TeamRole(elementToBase(element).getId(), role);
     teamRole.setName(getSimpleElementText(element, TAG_TEAM_ROLE_NAME));

     return teamRole;

   }

   /**
    *
    * @param model AssignmentModel
    * @param element Element
    * @return ConstraintDefinition
    */
   private ConstraintDefinition elementToConstraintDeintion(AssignmentModel
       model, Element element) {
     // get base for the ID
     Base base = this.elementToBase(element);

     String conditionText = getSimpleElementText(element,
         TAG_CONSTRAINT_DEFINITION_CONDITION);
     String mandatoryText = element.getAttribute(TAG_CONSTRAINT_DEFINITION_MANDATORY);
     Boolean mandatory = new Boolean(mandatoryText);

     String name = getSimpleElementText(element,TAG_CONSTRAINT_DEFINITION_NAME);

     ConstraintLevel level = this.elementToConstraintLevel(element);

     TemplateElementFactory templateFactory = new TemplateElementFactory(this);
     // get template
     Element templateElement = getFirstElement(element,TAG_CONSTRAINT_DEFINITION_TEMPLATE);
     // *** ConstraintTemplate template = templateFactory.elementToTemplate(element);
     ConstraintTemplate template = templateFactory.elementToTemplate(model.
         getLanguage(), templateElement);
     
     ConstraintDefinition constraint = new ConstraintDefinition(base.getId(),
             model, template);
         model.addConstraintDefiniton(constraint);     

     // get constraint parameters
     // first get the parameters tag
     Element parametersTag = getFirstElement(element,
         TAG_CONSTRAINT_DEFINITION_PARAMETERS);
     // get all parameters
     NodeList parameters = parametersTag.getElementsByTagName(TAG_CONSTRAINT_DEFINITION_PARAMETER);

     // a temp list for parameters
     List<Parameter> params = new ArrayList<Parameter>();
     for (int i = 0; i < parameters.getLength(); i++) {
       Element parameterElement = (Element) parameters.item(i);
       Parameter parameter = this.elementToParameter(model, constraint,parameterElement);
       params.add(parameter);
     }


     for (int i = 0; i < params.size(); i++) {
       Parameter parameter = params.get(i);
       for ( ActivityDefinition real: constraint.getBranches(parameter)) {
         constraint.addBranch(parameter,real);
       }
     }

     if (constraint != null) {
       constraint.getCondition().setText(conditionText);
       constraint.setName(name);
       constraint.setMandatory(mandatory);
       if (!mandatory) {
         constraint.setLevel(level);
       }
     }

     return constraint;
   }

   /**
    *
    * @param element Element
    * @return ConstraintLevel
    */
   private ConstraintLevel elementToConstraintLevel(Element element) {
     ConstraintLevel level = null;
     if (element != null) {
       Element levelElement = getFirstElement(element,
           TAG_CONSTRAINT_DEFINITION_LEVEL);
       if (levelElement != null) {
         ConstraintGroup group = this.elementToConstraintGroup(levelElement);
         if (group != null) {
           level = new ConstraintLevel(group);
           String priority = getSimpleElementText(levelElement,
               TAG_CONSTRAINT_DEFINITION_LEVEL_PRIORITY);
           String message = getSimpleElementText(levelElement,
               TAG_CONSTRAINT_DEFINITION_LEVEL_MESSAGE);
           int pr = ConstraintWarningLevel.possible()[0];
           try {
             pr = Integer.parseInt(priority);
           }
           catch (Exception e) {}
           level.setLevel(pr);
           level.setMessage(message);
         }
       }
     }
     return level;
   }

   /**
    *
    * @param element Element
    * @return ConstraintGroup
    */
   private ConstraintGroup elementToConstraintGroup(Element element) {
     ConstraintGroup group = null;
     if (element != null) {
       Element groupElement = getFirstElement(element,
           TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP);
       if (groupElement != null) {
         Base base = this.elementToBase(groupElement);
         group = new ConstraintGroup(base.getId());

         String name = getSimpleElementText(groupElement,
             TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_NAME);
         String description = getSimpleElementText(groupElement,
             TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_DESCRIPTION);
         group.setName(name);
         group.setDescription(description);
       }
     }
     return group;
   }

   /**
     *
     * @param model AssignmentModel
     * @param template ConstraintTemplate
     * @param element Element
     * @return Parameter
     */
    private Parameter elementToParameter(AssignmentModel model,
                                         ConstraintDefinition constraintDefinition,
                                         Element element) {
      String templateParam = element.getAttribute(
          TAG_CONSTRAINT_DEFINITION_PARAMETER_LTL);

      Parameter parameter = constraintDefinition.getParameterWithId(Integer.decode(templateParam));

      //Parameter parameter = new Parameter(null, FormalParameter);

      Element branchesTag = getFirstElement(element,
          TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCHES);
      NodeList branches = branchesTag.getElementsByTagName(
          TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH);

      for (int i = 0; i < branches.getLength(); i++) {
        Element branch = (Element) branches.item(i);

        String name = branch.getAttribute(TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH_NAME);

        ActivityDefinition activityDefinition = model.
            activityDefinitionWithName(name);

        constraintDefinition.addBranch(parameter,activityDefinition);
      }
      return parameter;
    }

   /**
    *
    * @param model ActivityDefinition
    * @param element Element
    */
   private void elementToActivityDefinition(AssignmentModel model,
                                            Element element) {
     Base base = this.elementToBase(element);
     ActivityDefinition activityDefinition = model.addActivityDefinition(base.
         getId());
     String name = element.getAttribute(TAG_ACTIVITY_DEFINITION_NAME);
     activityDefinition.setName(name);
        // YAWL sub-process
     ExternalCaseElementFactory temp = new ExternalCaseElementFactory(this);
     Element ec = temp.findYawlElement(element);
     if (ec != null){
      activityDefinition.setExternal(true);
      temp.elementToExternalCase(activityDefinition.getExternalCase(), ec);
     }
     // get authorization
     Element authorization = getFirstElement(element,
         TAG_ACTIVITY_DEFINITION_AUTHORIZATIONS);
     this.elementToAuthorization(activityDefinition.getAuthorization(),
                                 authorization, model.getTeamModel());
     // get activity data model
     Element data = getFirstElement(element,TAG_ACTIVITY_DEFINITION_DATA_MODEL);
     this.elementToDataModel(activityDefinition, data, model);

   }

   /**
    *
    * @param authorization Authorization
    * @param element Element
    * @param team TeamModel
    */
   private void elementToAuthorization(Authorization authorization,
                                       Element element, TeamModel team) {
     NodeList cells = element.getElementsByTagName(TAG_TEAM_ROLE);
     for (int i = 0; i < cells.getLength(); i++) {
       Element cell = (Element) cells.item(i);
       String idString = getSimpleElementText(cell);
       Integer id = Integer.decode(idString);
       // get the team role
       TeamRole role = team.getId(id.intValue());
       authorization.authorize(role);
     }
   }

   /**
    *
    * @param activity Authorization
    * @param element Element
    * @param data TeamModel
    */
   private void elementToDataModel(ActivityDefinition activity,
                                   Element element, AssignmentModel data) {
     NodeList cells = element.getElementsByTagName(TAG_ACTIVITY_DEFINITION_DATA_DEFINITION);
     for (int i = 0; i < cells.getLength(); i++) {
       Element cell = (Element) cells.item(i);
       String idElementString = cell.getAttribute(TAG_ACTIVITY_DEFINITION_DATA_ELEMENT);
       String idTypeString = cell.getAttribute(TAG_ACTIVITY_DEFINITION_DATA_TYPE);
       int idElement = Integer.decode(idElementString).intValue();
       int idType = Integer.decode(idTypeString).intValue();
       // get the team role
       DataElement dataElement = data.getDataWithId(idElement);
       ActivityDataDefinition.Type type = ActivityDataDefinition.Type.getCode(
           idType);
       activity.addDataElement(dataElement).setType(type);
     }
   }

   /**
    * dataToElement
    *
    * @param model DataModel
    * @param element Element
    * @return Element
    */
   private Element dataToElement(AssignmentModel model,
                                 Element element) {
     Element dataXML = getFirstElement(element, TAG_DATA);
     for (int i = 0; i < model.getDataCount(); i++) {
       DataElement dataElement = model.dataAt(i);
       Element dataElementElement = this.dataElementToElement(dataElement,
           dataXML);
       dataXML.appendChild(dataElementElement);
     }
     return dataXML;
   }

   /**
    *
    * @param dataElement DataElement
    * @param element Element
    * @return Element
    */
   private Element dataElementToElement(DataElement dataElement,
                                        Element element) {
     Element XML = baseToElement(dataElement, TAG_DATA_ELEMENT);
     this.setAttribute(XML, TAG_DATA_ELEMENT_NAME, dataElement.getName());
     this.setAttribute(XML, TAG_DATA_ELEMENT_TYPE,
                       dataElement.getType().getName());
     this.setAttribute(XML, TAG_DATA_ELEMENT_INITIAL,
                       dataElement.getInitial());
     return XML;
   }

   private void elementToDataElement(AssignmentModel model,
                                     Element element) {
     Base base = this.elementToBase(element);
     String name = element.getAttribute(TAG_DATA_ELEMENT_NAME);
     String type = element.getAttribute(TAG_DATA_ELEMENT_TYPE);
     String initial = element.getAttribute(TAG_DATA_ELEMENT_INITIAL);

     model.addData(base.getId(), name, DataElement.Type.getName(type), initial);
   }

   /**
    * rolesToElement
    *
    * @param teamModel DataModel
    * @param element Element
    * @return Element
    */
   private Element rolesToElement(TeamModel teamModel, Element element) {
     Element teamXML = getFirstElement(element, TAG_TEAM);
     for (int i = 0; i < teamModel.getSize(); i++) {
       TeamRole teamRole = teamModel.get(i);
       Element roleElement = this.teamRoleToElement(teamRole);
       teamXML.appendChild(roleElement);
     }
     return teamXML;
   }

   private Element teamRoleToElement(TeamRole teamRole) {
     // *** create an element for the role
     Element element = baseToElement(teamRole, TAG_TEAM_ROLE);

     // *** create tags for all attributes of the role
     Element nameTag = createObjectAttribute(TAG_TEAM_ROLE_NAME,teamRole.getName());

     OrganizationElementFactory factory = new OrganizationElementFactory(this);
     Element roleElementTag = factory.roleToElement(teamRole.getRole());

     // *** add all attribute tags to the role element
     element.appendChild(nameTag);
     element.appendChild(roleElementTag);

     // *** return the element for the role
     return element;
   }

  }
