package functionalityNodes;
import parser.AngularDetectionRegexes;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


public class NodeAggregator {
	Map<String, FunctionalNode> nodes = new HashMap<String, FunctionalNode>();
 
	public NodeAggregator( ) {	
	}
	
	public boolean nodeExist(String nodeIdentifier) {
		if (nodes.containsKey(nodeIdentifier)) {
			return true;
		}
		return false;
	}
	
	public void addNode(String nodeIdentifier, FunctionalNode functionalNode) {
		nodes.put(nodeIdentifier, functionalNode);
	}
	
	public FunctionalNode getNode(String nodeIdentifier) {
		return nodes.get(nodeIdentifier);
	}
	
	public List<String> getNodeNames() {
		return new ArrayList<String>(this.nodes.keySet());
	}

	public Set<Map.Entry<String,FunctionalNode>> getAllNodeNames() {
		return this.nodes.entrySet();
	}

	public FunctionalNode createNode(String processedName, String importPart) {
		if (!importPart.startsWith("@") && !importPart.startsWith("node_modules")) {
			if (importPart.endsWith("odule")) {
				return new ModuleNode(processedName, importPart);
			} else if (importPart.endsWith("omponent")) {
				return new ComponentNode(processedName, importPart);
			}
			return new InternalNode(processedName, importPart);
		}
		return new ExternalNode(processedName, importPart);
	}
	
	public List<FunctionalNode> insertImports(List<String> importLines, String basePath) {
		List<FunctionalNode> insertedNodes = new ArrayList<FunctionalNode>();
		
		Iterator<String> importLinesIterator = importLines.iterator();
		
		String stringToSearch = AngularDetectionRegexes.getImportGroupRegexp();
		Pattern pattern = Pattern.compile(stringToSearch);
		while(importLinesIterator.hasNext()) {
			String processedLine = importLinesIterator.next();
			Matcher matcher = pattern.matcher(processedLine);
			if (matcher.matches()) {
				String[] names = matcher.group(1).split(",");
				String importPath = matcher.group(2);

				FunctionalNode node;
				for (int i = 0; i < names.length; i++) {
					String processedName = names[i].strip();
					if (importPath.startsWith(".")) {
						if (importPath.startsWith("./")) {
							importPath = importPath.replaceAll("^\\.\\/", basePath + "/");
						} else if (importPath.startsWith("../")) {
							String basePath2 = basePath;
							while (importPath.startsWith("../")) {
								basePath2 = basePath2.substring(0, basePath2.lastIndexOf("/"));
								importPath = importPath.replaceAll("^\\.\\.\\/", "");
							}
							importPath = importPath.replaceAll("^", basePath2 + "/");
						}
		
					}
					if (nodeExist(processedName)) {
						node = getNode(processedName);
					} else {
						node = createNode(processedName, importPath);
						this.addNode(processedName, node);
					}
					insertedNodes.add(node);
				}
			} else {
				System.out.println("Matched line:");
				System.out.println(processedLine);
			}
		}
		return insertedNodes;
	}
}
