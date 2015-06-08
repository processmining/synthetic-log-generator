package nl.tue.declare.appl.worklist.gui;

import java.util.Comparator;

import org.processmining.operationalsupport.messages.reply.DiscretePrediction;
import org.processmining.operationalsupport.messages.reply.NumericalPrediction;
import org.processmining.operationalsupport.messages.reply.Prediction;

/**
 * 
 * @author mwesterg
 *
 */
public class PredictionComparator implements Comparator<Prediction<?>> {
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Prediction<?> o1, Prediction<?> o2) {
		if (o1 == null) return o2==null?0:-1;
		if (o2 == null) return 1;
		
		if ((o1 instanceof DiscretePrediction<?>) && (o2 instanceof DiscretePrediction<?>)) {
			DiscretePrediction dp1 = (DiscretePrediction) o1;
			DiscretePrediction dp2 = (DiscretePrediction) o2;
			Comparable value1 = (Comparable) dp1.getPredictedValue();
			Comparable value2 = (Comparable) dp2.getPredictedValue();
			if (value1 == null) return value2 == null?0:-1;
			if (value2 == null) return 1;
			int result = value1.compareTo(value2);
			if (result != 0) return result;
			
			double probability1 = dp1.getProbability(value1);
			double probability2 = dp2.getProbability(value2);
			return Double.valueOf(probability1).compareTo(probability2);
		}
		
		if ((o1 instanceof NumericalPrediction<?>) && (o2 instanceof NumericalPrediction<?>)) {
			NumericalPrediction<?> dp1 = (NumericalPrediction<?>) o1;
			NumericalPrediction<?> dp2 = (NumericalPrediction<?>) o2;

			int result = Double.valueOf(dp1.getAverage()).compareTo(dp2.getAverage());
			if (result != 0) return result;
			
			return Double.valueOf(dp1.getConfidence95()).compareTo(dp2.getConfidence95());
		}
		
		return 0;
	}

}
