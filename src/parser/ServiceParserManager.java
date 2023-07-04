package parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import functionalityNodes.ComponentNode;
import functionalityNodes.FunctionalNode;
import functionalityNodes.InternalNode;
import functionalityNodes.NodeAggregator;
import functionalityNodes.ServiceNode;


public class ServiceParserManager {

	public ServiceParserManager() {
	}
	
	public void associateServiceAssociations(ComponentNode componentNode, NodeAggregator nodeAggregator) {
		String componentFilePath = componentNode.getPathInProject().replaceAll("component$", "component.ts");
		parseService(componentNode, componentFilePath, nodeAggregator);
	}
	
	private int getInjectedServiceUsageCount(String serviceIdentifier, String[] injectedServices, String serviceContent) {
		Pattern serviceNamePattern = Pattern.compile("\\s+([^:\\s]+)\\s*:");

		for (int index = 0; index < injectedServices.length; index++) {
			if (injectedServices[index].indexOf(serviceIdentifier) >= 0) {
				Matcher serviceNameMatcher = serviceNamePattern.matcher(injectedServices[index]);
				serviceNameMatcher.find();
				String serviceName = "this." + serviceNameMatcher.group(1);
				int usageCount = serviceContent.split(serviceName).length - 1;
				if (usageCount > 0) {
					return usageCount;
				}
			}
		}
		
		return -1;
	}

	private void parseService(InternalNode serviceNode, String serviceFilePath, NodeAggregator nodeAggregator) {
		List<String> importLines = FileLoader.getRegexDataFromFile(
				serviceFilePath, AngularDetectionRegexes.getImportRegexp());
		Iterator<String> importLinesIterator = importLines.iterator();
		
		String stringToSearch = AngularDetectionRegexes.getImportGroupRegexp();
		Pattern pattern = Pattern.compile(stringToSearch);

		while(importLinesIterator.hasNext()) {
			String processedLine = importLinesIterator.next();
			Matcher matcher = pattern.matcher(processedLine);

			if (matcher.matches()) {
				String importPath = matcher.group(2);
				if (importPath.endsWith(".service")) {
					String[] names = matcher.group(1).split(",");
					String processedServiceFilePath = serviceNode.getPathInProject().replaceAll("service$", "service.ts");

					String[] injectedServices = FileLoader.getMethodArguments(serviceFilePath, "constructor").split(",");
					String wholeFileContent = FileLoader.loadWholeFile(serviceFilePath);
		
					for (int i = 0; i < names.length; i++) {
						names[i] = names[i].strip();
						int serviceUsageCount = getInjectedServiceUsageCount(names[i], injectedServices, wholeFileContent);

						if (serviceUsageCount > 0) {
							serviceNode.markServiceOccurrence(names[i], serviceUsageCount);
						} else {
							serviceNode.markServiceOccurrence(names[i], 1);
						}
						
						if (!nodeAggregator.nodeExist(names[i])) {
							ServiceNode processedServiceNode = new ServiceNode(names[i], processedServiceFilePath);
							serviceNode.insertChildNode(names[i], processedServiceNode);
							nodeAggregator.addNode(names[i], processedServiceNode);
							parseService(processedServiceNode, serviceFilePath, nodeAggregator);
						}
					}
				}
			}
		}
	}
}
