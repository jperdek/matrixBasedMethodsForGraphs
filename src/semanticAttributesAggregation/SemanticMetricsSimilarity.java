package semanticAttributesAggregation;

public class SemanticMetricsSimilarity {

	private String semanticVariableName;
	private double coefficient = 0.0;
	
	public SemanticMetricsSimilarity(String semanticVariableName, double coefficient) {
		this.semanticVariableName = semanticVariableName;
		this.coefficient = coefficient;
	}
	
	public double getCoefficient() { return this.coefficient; }
	
	public String getSemanticVariableName() { return this.semanticVariableName; }
}
