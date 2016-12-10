package at.wu.ac.declare.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import minerful.concept.ProcessModel;
import minerful.concept.TaskChar;
import minerful.concept.TaskCharArchive;
import minerful.concept.TaskCharFactory;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.ConstraintsBag;
import minerful.concept.constraint.MetaConstraintUtils;
import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.domain.model.AssignmentModel;
import nl.tue.declare.domain.model.ConstraintDefinition;

public class OriginalDeclareMapDecoder {
	private List<OriginalDeclareConstraintTransferObject> constraintTOs;
	private TaskCharArchive taskCharArchive = null;
	private String processModelName = null;

	public OriginalDeclareMapDecoder(AssignmentModel declareMapModel) {
		this.buildFromDeclareMapModel(declareMapModel);
	}
	
	private void buildFromDeclareMapModel(AssignmentModel declareMapModel) {
		/* Record the name of the process */
		this.processModelName = declareMapModel.getName();

		/* Create an archive of TaskChars out of the activity definitions in the Declare Map model */
		Collection<TaskChar> tasksInDeclareMap = new ArrayList<TaskChar>(declareMapModel.activityDefinitionsCount());
		TaskCharFactory tChFactory = new TaskCharFactory();

		for (ActivityDefinition ad : declareMapModel.getActivityDefinitions()) {
			tasksInDeclareMap.add(tChFactory.makeTaskChar(ad.getName()));
		}

		this.taskCharArchive = new TaskCharArchive(tasksInDeclareMap);
		
		/* Create DTOs for constraints out of the definitions in the Declare Map model */
		this.constraintTOs = new ArrayList<OriginalDeclareConstraintTransferObject>(declareMapModel.constraintDefinitionsCount());
		for (ConstraintDefinition cd : declareMapModel.getConstraintDefinitions()) {
			this.constraintTOs.add(new OriginalDeclareConstraintTransferObject(cd));
		}		
	}
	
	public ProcessModel createMinerFulProcessModel() {
		Collection<Constraint> minerFulConstraints = new ArrayList<Constraint>(this.constraintTOs.size());
		OriginalDeclareTransferObjectToConstraintTranslator miFuConMak = new OriginalDeclareTransferObjectToConstraintTranslator(this.taskCharArchive);
		Constraint auxConstraint = null;
		
		for (OriginalDeclareConstraintTransferObject conTO: constraintTOs) {
			auxConstraint = miFuConMak.createConstraint(conTO);
			if (auxConstraint != null) {
					minerFulConstraints.add(auxConstraint);
			}
		}

		MetaConstraintUtils.createHierarchicalLinks(new TreeSet<Constraint>(minerFulConstraints));
		ConstraintsBag constraintsBag = new ConstraintsBag(this.taskCharArchive.getTaskChars(), minerFulConstraints);

		return new ProcessModel(taskCharArchive, constraintsBag, this.processModelName);
	}

	public List<OriginalDeclareConstraintTransferObject> getConstraintTOs() {
		return constraintTOs;
	}
}