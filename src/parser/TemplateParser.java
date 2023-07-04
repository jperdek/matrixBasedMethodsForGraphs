package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import functionalityNodes.ComponentNode;
import functionalityNodes.SelectorAggregator;

import java.util.Map;

import java.util.HashMap;
import java.util.Iterator;


public class TemplateParser {

	private SelectorAggregator selectorAggregator;
	
	public TemplateParser(SelectorAggregator selectorAggregator) {
		this.selectorAggregator = selectorAggregator;
	}
	
	public Map<String, Integer> findAssociatedComponents(String templateContent) {
		Document document = Jsoup.parse(templateContent);
		Map<String, Integer> associatedTemplates = null;
		Iterator<String> selectors = this.selectorAggregator.getAllSelectors().iterator();
		
		while(selectors.hasNext()) {
			String actualSelector = selectors.next();
			Elements elementsInDocument = document.select(actualSelector);
			Object[] elementArray = elementsInDocument.toArray();
			if (elementArray.length > 0) {
				if (associatedTemplates == null) {
					associatedTemplates = new HashMap<String, Integer>();
				}
				associatedTemplates.put(actualSelector, elementArray.length);
			}
		}
		
		for (String actualSelector: this.getHTML5MainSelectors()) {
			Elements elementsInDocument = document.select(actualSelector);
			Object[] elementArray = elementsInDocument.toArray();
			if (elementArray.length > 0) {
				if (associatedTemplates == null) {
					associatedTemplates = new HashMap<String, Integer>();
				}
				associatedTemplates.put(actualSelector, elementArray.length);
			}
		}
		
		return associatedTemplates;
	}
	
	public void collectSemanticInformation(String templateContent, ComponentNode componentNode) {
		componentNode.createSemanticMaps();
		Document document = Jsoup.parse(templateContent);
		
		for(Element child: document.children()) {
			this.processChild(child, componentNode);
		}
	}

	private void processChild(Element child, ComponentNode componentNode) {
		this.collectClassOccurrences(child, componentNode.getAssociatedClassesMap());
		this.collectAttributeOccurrences(child, componentNode.getAssociatedAttributesMap());
		
		for(Element newChild: child.children()) {
			this.processChild(newChild, componentNode);
		}
	}

	private void collectClassOccurrences(Element child, Map<String, Integer> associatedClasses) {
		Iterator<String> classIterator = child.classNames().iterator();
		String className;
		int count;
		
		while(classIterator.hasNext()) {
			className = classIterator.next();
			if (associatedClasses.containsKey(className)) {
				count = associatedClasses.get(className);
				associatedClasses.put(className, count + 1);
			} else {
				associatedClasses.put(className, 1);
			}
		}
	}
	
	private void collectAttributeOccurrences(
			Element child, Map<String, Integer> associatedAttributes) {
		Iterator<Attribute> attributeIterator = child.attributes().iterator();
		Attribute attribute;
		String attributeName;
		int count;
		
		while(attributeIterator.hasNext()) {
			attribute = attributeIterator.next();
			attributeName = attribute.getKey();
			if (associatedAttributes.containsKey(attributeName)) {
				count = associatedAttributes.get(attributeName);
				associatedAttributes.put(attributeName, count + 1);
			} else {
				associatedAttributes.put(attributeName, 1);
			}
		}
	}

	public String[] getHTML5MainSelectors() {
		return new String[] {"main", "nav", "address", "section", "article", "header", "footer"};
	}
}
