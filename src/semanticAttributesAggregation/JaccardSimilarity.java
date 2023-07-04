package semanticAttributesAggregation;

import java.util.Map;

public class JackardSimilarity extends SemanticMetricsSimilarity {
	
	private Map<String, Double> variableMapping;
	
	public JackardSimilarity(String semanticVariableName, double coefficient, Map<String, Double> variableMapping) {
		super(semanticVariableName, coefficient);
		this.variableMapping = variableMapping;
	}
	
	public double getSimilarityValueFromMap(int nodeIndex1, int nodeIndex2) {
		String index = Integer.toString(nodeIndex1) + AttributeSetAggregator.DELIMITER + Integer.toString(nodeIndex2);
		if (nodeIndex1 == nodeIndex2) {
			return 1.0; //SIMILARITY BETWEEN EQUAL NODES IS 1.0
		}
		
		if (!this.variableMapping.containsKey(index)) {
			return 0.0; //NONE SIMILARITY IF iNDEX IS ZERO
		}
		double result = this.variableMapping.get(index);
		if (Double.isFinite(result)) {
			return result;
		}
		
		return 0.0; //INFINITE OR NaN SHOULD ADD NOTHING
	}
}
