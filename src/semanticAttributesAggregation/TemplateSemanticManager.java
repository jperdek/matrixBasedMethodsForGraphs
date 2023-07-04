package semanticAttributesAggregation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import functionalityNodes.ComponentNode;
import functionalityNodes.ModuleNode;


public class TemplateSemanticManager {

	private AttributeSetAggregator templateClasses;
	private AttributeSetAggregator templateAttributes;
	

	public TemplateSemanticManager() {
		this.templateClasses = new AttributeSetAggregator();
		this.templateAttributes = new AttributeSetAggregator();
	}
	
	public Map<String, Double> getTemplateClassesMap() {
		return this.templateClasses.getMetricsMap();
	}
	
	public Map<String, Double> getTemplateAttributesMap() {
		return this.templateClasses.getMetricsMap();
	}
	
	public void processComponents(ModuleNode rootModule) {
		List<ComponentNode> aggregatedComponents = new ArrayList<ComponentNode>();
		rootModule.getAllComponents(0, aggregatedComponents);
		this.getMetricsForClass(aggregatedComponents);
		this.getMetricsForAttributeNames(aggregatedComponents);
	}
	
	private void getMetricsForClass(List<ComponentNode> aggregatedComponents) {
		ComponentNode componentNode1, componentNode2;
		String componentName1, componentName2;
		Map<String, Integer> classMapComponent1, classMapComponent2; 
		Iterator<ComponentNode> componentsIterator1 = aggregatedComponents.iterator();
		Iterator<ComponentNode> componentsIterator2;
		
		while(componentsIterator1.hasNext()) {
			componentNode1 = componentsIterator1.next();
			componentName1 = componentNode1.getImportName();
			classMapComponent1 = componentNode1.getAssociatedClassesMap();
			componentsIterator2 = aggregatedComponents.iterator();
	
			while(componentsIterator2.hasNext()) {
				componentNode2 = componentsIterator2.next();
				componentName2 = componentNode2.getImportName();
				classMapComponent2 = componentNode2.getAssociatedClassesMap();
				
				this.templateClasses.evaluateJackardMetrics(componentName1, componentName2,
						classMapComponent1, classMapComponent2);
			}
		}
	}
	
	private void getMetricsForAttributeNames(List<ComponentNode> aggregatedAttributes) {
		ComponentNode componentNode1, componentNode2;
		String componentName1, componentName2;
		Map<String, Integer> attributesMapComponent1, attributesMapComponent2; 
		Iterator<ComponentNode> componentsIterator1 = aggregatedAttributes.iterator();
		Iterator<ComponentNode> componentsIterator2;
		
		while(componentsIterator1.hasNext()) {
			componentNode1 = componentsIterator1.next();
			componentName1 = componentNode1.getImportName();
			attributesMapComponent1 = componentNode1.getAssociatedAttributesMap();
			componentsIterator2 = aggregatedAttributes.iterator();
	
			while(componentsIterator2.hasNext()) {
				componentNode2 = componentsIterator2.next();
				componentName2 = componentNode2.getImportName();
				attributesMapComponent2 = componentNode2.getAssociatedAttributesMap();
				
				this.templateAttributes.evaluateJackardMetrics(componentName1, componentName2,
						attributesMapComponent1, attributesMapComponent2);
			}
		}
	}
}
