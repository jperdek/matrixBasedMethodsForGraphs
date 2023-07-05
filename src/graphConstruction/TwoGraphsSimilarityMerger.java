package graphConstruction;

import static org.neo4j.driver.Values.parameters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;


public class TwoGraphsSimilarityMerger extends GraphCreator {

	private String graph1Tag, graph2Tag;
	private String newGraphTag;
	
	public TwoGraphsSimilarityMerger(String graph1Tag, String graph2Tag, 
			String newGraphTag, String uri, String user, String password) {
		super(uri, user, password);
		this.graph1Tag = graph1Tag;
		this.graph2Tag = graph2Tag;
		this.newGraphTag = newGraphTag;
	}
	
	public void insertClusterCenterIfNotExists(Session session, String clusterCenterName, String tag, String tagName) {
		if (AppliedGraphNames.ALLOWED_NAMES.contains(tagName)) {
			session.writeTransaction( tx -> {
					Result result = tx.run( "MERGE (a:" + tagName + " {name: $name, tag: $tag})",
									parameters( "name", clusterCenterName, "tag", tag ) );
					return result;
	        } );
		}
	}

	public void clonemNodeIfNotExist(Session session, String clusterCenterName, String tag, String tagName) {
		if (AppliedGraphNames.ALLOWED_NAMES.contains(tagName)) {
			session.writeTransaction( tx -> {
					Result result = tx.run( "MERGE (a:" + tagName + " {name: $name, tag: $tag})",
									parameters( "name", clusterCenterName, "tag", tag ) );
					return result;
	        } );
		}
	}
	
	public void connectNodeToCenterIfNotExists(Session session, String clusterNodeName, String clusterCenterName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:ClusterNode {name: $clusterNodeName, tag: $tag}), (b:ClusterCenter {name: $clusterCenterName, tag: $tag}) "
										+ "	MERGE (a)-[:Assigned {tag: $tag }]->(b)",
								parameters( "clusterNodeName", clusterNodeName, "clusterCenterName", clusterCenterName, "tag", tag ) );
				return result;
        } );
	}
	
	public void mergeGraphs(double similarityMatrix[][], String graph1Names[], 
			String graph2Names[], double groupTreshold, double mergeTreshold, boolean unionOfGraphs, 
			String newGraphTag, String graph1Tag, String graph2Tag) throws Exception {
		if (mergeTreshold > groupTreshold) {
			throw new Exception("Merge treshold is greater than group threshold! This should be not possible!");
		}
		
		String node1Name, node2Name;
		Map<String, Set<String>> merged = new HashMap<String, Set<String>>();
		Map<String, String> grouped = new HashMap<String, String>();
		Set<String> newNodes = new HashSet<String>();
		Set<String> help;
		
		for (int graph1VerticeIndex = 0; graph1VerticeIndex < graph1Names.length; graph1VerticeIndex++) {
			node1Name = graph1Names[graph1VerticeIndex];
			for (int graph2VerticeIndex = 0; graph2VerticeIndex < graph2Names.length; graph2VerticeIndex++) {
				if (graph1VerticeIndex != graph2VerticeIndex) {
					node2Name = graph2Names[graph2VerticeIndex];
					
					if (similarityMatrix[graph1VerticeIndex][graph2VerticeIndex] < groupTreshold) {
						if (unionOfGraphs) {
							newNodes.add(node1Name);
							newNodes.add(node2Name);
						}
					}
					else if (similarityMatrix[graph1VerticeIndex][graph2VerticeIndex] > mergeTreshold) {
						//group node1Name with node2Name
						grouped.put(node1Name, node2Name);
					// groupTreshold <= similarityMatrix[graph1VerticeIndex][graph2VerticeIndex] <= mergeTreshold
					} else {
						//merge node1Name with node2Name
						help = merged.get(node1Name);
						help.add(node2Name);
						merged.put(node1Name, help);
					}
				}
			}
		}
		
		Set<String> mergedRecords;
		String recordName;
		for (Map.Entry<String,Set<String>> mergedEntry : merged.entrySet()) {
			mergedRecords = mergedEntry.getValue();
			recordName = mergedEntry.getKey();
			//this.insertClusterCenterIfNotExists(recordName, 
		}
		
	}
}
