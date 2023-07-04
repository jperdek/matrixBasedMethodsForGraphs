package functionalityNodes;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class SelectorAggregator {
	Map<String, FunctionalNode> selectorMapping = new HashMap<String, FunctionalNode>();
	
	public SelectorAggregator() {
	}
	
	public void add(String selectorName, FunctionalNode selectedNode) {
		this.selectorMapping.put(selectorName, selectedNode);
	}
	
	public FunctionalNode getSelectedNode(String selectorName) {
		return this.selectorMapping.get(selectorName);
	}
	
	public List<String> getAllSelectors() {
		return new ArrayList<String>(this.selectorMapping.keySet());
	}
}
