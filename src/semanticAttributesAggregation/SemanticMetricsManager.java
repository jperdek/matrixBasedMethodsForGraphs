package semanticAttributesAggregation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SemanticMetricsManager {
	List<SemanticMetricsSimilarity> semanticMetrics;
	
	public SemanticMetricsManager() {
		this.semanticMetrics = new ArrayList<SemanticMetricsSimilarity>();
	}
	
	public void addMetric(SemanticMetricsSimilarity metric) {
		this.semanticMetrics.add(metric);
	}
	
	public double evaluateMetricValueBetweenTwoNodes(int nodeIndex1, int nodeIndex2) {
		double incrementalValue = 0.0;
		Iterator<SemanticMetricsSimilarity> metricIterator = this.semanticMetrics.iterator();
		SemanticMetricsSimilarity processedMetrics;
		
		while(metricIterator.hasNext()) {
			processedMetrics = metricIterator.next();
			incrementalValue = incrementalValue + 
					processedMetrics.getCoefficient() * ((JaccardSimilarity) processedMetrics).getSimilarityValueFromMap(nodeIndex1, nodeIndex2);
		}
		return incrementalValue;
	}
}
