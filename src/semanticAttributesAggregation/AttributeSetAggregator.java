package semanticAttributesAggregation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class AttributeSetAggregator {

	public static final String DELIMITER = "|&&&|";
	
	private Map<String, Double> metricValue = new HashMap<String, Double>();
	
	public AttributeSetAggregator() {	
	}
	
	public void evaluateJackardMetrics(String name1, String name2, 
			Map<String, ?> map1, Map<String, ?> map2) {
		this.evaluateJackardMetrics(name1, name2, map1.keySet(), map2.keySet());
	}
	
	public Map<String, Double> getMetricsMap() { return this.metricValue; }
	
	public double evaluateJackardMetrics(String name1, String name2, 
			Set<String> set1, Set<String> set2) {
		String key = name1 + AttributeSetAggregator.DELIMITER + name2;
		String mirrorKey = name2 + AttributeSetAggregator.DELIMITER + name1;
		double resultMetricsValue = 0.0;
		int unionSetCardinality;
		Set<String> unionSet = new HashSet<String>(set1);
		unionSet.addAll(set2);
		Set<String> intersectionSet = new HashSet<String>(set1);
		intersectionSet.retainAll(set2);
		unionSetCardinality = unionSet.size();
		if (unionSetCardinality == 0.0) {
			return Double.NaN;
		}
		resultMetricsValue = (intersectionSet.size() + 0.0) / unionSet.size();
		if (resultMetricsValue != 0.0 && resultMetricsValue != 1.0) {
			this.metricValue.put(key, resultMetricsValue);
			this.metricValue.put(mirrorKey, resultMetricsValue);
			System.out.println(key + ": " + Double.toString(resultMetricsValue));
			return resultMetricsValue;
		}
		return 0.0;
	}
}
