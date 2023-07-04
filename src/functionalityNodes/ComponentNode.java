package functionalityNodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import parser.FeatureNamesExtractor;


public class ComponentNode extends InternalNode {

	private String type = null;
	private String selector = null;
	private String templateUrlPath = null;
	private List<String> styleUrls = null;
	private Map<String, Integer> selectorsOccurrences = null;
	private Map<String, Integer> servicesOccurrences = null;

	private Map<String, Integer> classOccurences = null;
	private Map<String, Integer> attributeOccurences = null;
	

	public ComponentNode(String importName, String pathInProject) {
		super(importName, pathInProject);
	}
	
	public void setUIType() {
		this.type = "UI";
	}
	
	public void setComponentType() {
		this.type = "Component";
	}
	
	public void setSelector(String selector) {
		this.selector = selector;
	}
	
	public String getSelector() {
		return this.selector;
	}
	
	public void setTemplateUrlPath(String templateUrlPath) {
		this.templateUrlPath = templateUrlPath;
	}
	
	public String getTemplateUrlPath() {
		return this.templateUrlPath;
	}
	
	public void setStyleUrls(List<String> styleUrls) {
		this.styleUrls = styleUrls;
	}
	
	public List<String> getStyleUrls() {
		return this.styleUrls;
	}

	public void setSelectorsOccurrences(Map<String, Integer> selectorsOccurrences) {
		this.selectorsOccurrences = selectorsOccurrences;
	}
	
	public void setServicesOccurrences(Map<String, Integer> servicesOccurrences) {
		this.servicesOccurrences = servicesOccurrences;
	}

	public Map<String, Integer> getSelectorsOccurrences() {
		return this.selectorsOccurrences;
	}
	
	public Map<String, Integer> getServicesOccurrences() {
		return this.servicesOccurrences;
	}

	public Set<Map.Entry<String, Integer>> getSelectorsOcurrences() {
		if (this.selectorsOccurrences == null) { return null; }
		return this.selectorsOccurrences.entrySet();
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
	
	public void printSelectorOccurrences(int depth) {
		if (this.selectorsOccurrences == null) { return; }
		
		tabularize(depth + 1);
		System.out.println("|__>Used in template<__");
		for (Map.Entry<String, Integer> entry : this.selectorsOccurrences.entrySet()) {
			String selectorName = entry.getKey();
			Integer occurrenceCount = entry.getValue();
			tabularize(depth + 1);
			System.out.println("|__" + selectorName + " " + occurrenceCount.toString());
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
	
	public void createSemanticMaps() {
		this.classOccurences = new HashMap<String, Integer>();
		this.attributeOccurences = new HashMap<String, Integer>();
	}
	
	public Map<String, Integer> getAssociatedClassesMap() { return this.classOccurences; }
	
	public Map<String, Integer> getAssociatedAttributesMap() { return this.attributeOccurences; }
}
