package at.wu.ac.declare.util;

import minerful.concept.TaskCharArchive;
import minerful.concept.TaskCharSetFactory;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.MetaConstraintUtils;
import at.wu.ac.declare.util.OriginalDeclareConstraintTransferObject;

public class OriginalDeclareTransferObjectToConstraintTranslator {
	private TaskCharArchive taskCharArchive;
	private TaskCharSetFactory taskCharSetFactory;
	
	public OriginalDeclareTransferObjectToConstraintTranslator(TaskCharArchive taskCharArchive) {
		this.taskCharArchive = taskCharArchive;
		this.taskCharSetFactory = new TaskCharSetFactory(taskCharArchive);
	}
	
	public Constraint createConstraint(OriginalDeclareConstraintTransferObject conTO) {
		Constraint minerFulConstraint = null;
		if (conTO.minerFulTemplate != null) {
			MetaConstraintUtils.makeConstraint(
					conTO.minerFulTemplate,
					this.taskCharSetFactory.createSetsFromTaskStringsCollection(
							conTO.parameters
							)
					);
		}
		return minerFulConstraint;
	}

}