package functionalityNodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import parser.FeatureNamesExtractor;


public class ServiceNode extends InternalNode {

	private Map<String, Integer> servicesOccurrences = null;
	
	public ServiceNode(String importName, String pathInProject) {
		super(importName, pathInProject);
	}
	
	public void setServicesOccurrences(Map<String, Integer> servicesOccurrences) {
		this.servicesOccurrences = servicesOccurrences;
	}
	
	public Map<String, Integer> getServicesOccurrences() {
		return this.servicesOccurrences;
	}

	public Set<Map.Entry<String, Integer>> getSelectorsOcurrences() {
		if (this.servicesOccurrences == null) { return null; }
		return this.servicesOccurrences.entrySet();
	}
	
	public void markServiceOccurrence(String serviceIdentifier, int occurrences) {
		if (occurrences > 0) {
			if (this.servicesOccurrences == null) {
				this.servicesOccurrences = new HashMap<String, Integer>();
			}
			
			Integer retrievedCount = this.servicesOccurrences.get(serviceIdentifier);
			if (retrievedCount == null) {
				this.servicesOccurrences.put(serviceIdentifier, occurrences);
			} else {
				this.servicesOccurrences.put(serviceIdentifier, retrievedCount.intValue() + occurrences);
			}
		}
	}
	
	private void tabularize(int depth) {
		for(int i = 0; i < depth; i++) {
			System.out.append(' ');
		}
	}

	public void printServicesOccurrences(int depth) {
		if (this.servicesOccurrences == null) { return; }
		
		tabularize(depth + 1);
		System.out.println("|__>Used services<__");
		for (Map.Entry<String, Integer> entry : this.servicesOccurrences.entrySet()) {
			String serviceName = FeatureNamesExtractor.makeNameReadable(entry.getKey());
			Integer occurrenceCount = entry.getValue();
			tabularize(depth + 1);
			System.out.println("|__" + serviceName + " " + occurrenceCount.toString());
		}
	}
}
