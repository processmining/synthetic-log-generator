package at.wu.ac.declare.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;

import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.domain.model.ConstraintDefinition;
import nl.tue.declare.domain.template.Parameter;

import minerful.concept.TaskChar;
import minerful.concept.TaskCharSet;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.MetaConstraintUtils;
import minerful.io.encdec.DeclareConstraintTransferObject;
import minerful.io.encdec.StringToLowerCaseAlphanumToTemplateTranslator;
import minerful.io.encdec.declaremap.DeclareMapEncoderDecoder;
import minerful.io.encdec.declaremap.DeclareMapTemplate;
import minerful.io.encdec.declaremap.DeclareMapToMinerFulTemplatesTranslator;
import minerful.io.encdec.pojo.ConstraintPojo;

public class OriginalDeclareConstraintTransferObject implements Comparable<OriginalDeclareConstraintTransferObject> {
	public final DeclareMapTemplate declareMapTemplate;
	public final Class<? extends Constraint> minerFulTemplate;
	public final List<Set<String>> parameters;
	
	public OriginalDeclareConstraintTransferObject(ConstraintDefinition declareMapConstraint) {
		this.declareMapTemplate = DeclareMapTemplate.fromName(declareMapConstraint.getName());
		this.minerFulTemplate = DeclareMapToMinerFulTemplatesTranslator.translateTemplateName(this.declareMapTemplate);
		this.parameters = new ArrayList<Set<String>>();
		
		Set<String> auxParamSet = null;
		for(Parameter p : declareMapConstraint.getParameters()){
			auxParamSet = new TreeSet<String>();
			for (ActivityDefinition ad : declareMapConstraint.getBranches(p)) {
				auxParamSet.add(ad.getName());
			}
			this.parameters.add(auxParamSet);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeclareConstraintTransferObject [declareMapTemplate=");
		builder.append(declareMapTemplate);
		builder.append(", minerfulTemplate=");
		builder.append(minerFulTemplate);
		builder.append(", parameters=");
		builder.append(parameters);
		builder.append("]");
		return builder.toString();
	}

	public Set<String> getAllParamsTasks() {
		Set<String> allParamsTasks = new TreeSet<String>();

		if (this.parameters != null) {
			for (Set<String> paramTasks : this.parameters) {
				allParamsTasks.addAll(paramTasks);
			}
		}

		return allParamsTasks;
	}

	@Override
	public int compareTo(OriginalDeclareConstraintTransferObject o) {
		int result = 0;
		
		result = this.declareMapTemplate.compareTo(o.declareMapTemplate);
		if (result == 0) {
			/* Compare the parameters' sizes */
    		for (int i = 0; i < this.parameters.size() && result == 0; i++) {
    			if (this.parameters.get(i) == null) {
    				if (o.parameters.get(i) != null) {
    					return 1;
    				}
    			} else {
    				if (o.parameters.get(i) == null) {
    					return -1;
    				}
    			}
    			result = new Integer(this.parameters.get(i).size()).compareTo(o.parameters.get(i).size());
    			/* Compare the respective parameters' tasks */
    			if (result == 0) {
    				Iterator<String>
    					thisParamsIterator = this.parameters.get(i).iterator(),
    					oParamsIterator = o.parameters.get(i).iterator();
    				while (thisParamsIterator.hasNext() && result == 0) {
    					result = thisParamsIterator.next().compareTo(oParamsIterator.next());
    				}
    			}
    		}
		}
		
		return result;
	}

}
