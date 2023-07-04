package graphConstruction;

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class GraphHierarchicClustering extends GraphCreator {

	public static final String DELIMITER = "|&&&|";
	

	public GraphHierarchicClustering(String uri, String user, String password) {
		super(uri, user, password);
	}
	
	public void insertClusterNodeIfNotExists(Session session, String clusterNodeName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:ClusterNode {name: $name, tag: $tag})",
								parameters( "name", clusterNodeName, "tag", tag ) );
				return result;
        } );
	}
	
	public void insertClusterCenterIfNotExists(Session session, String clusterCenterName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:ClusterCenter {name: $name, tag: $tag})",
								parameters( "name", clusterCenterName, "tag", tag ) );
				return result;
        } );
	}

	public void connectNodeToCenterIfNotExists(Session session, String clusterNodeName, String clusterCenterName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:ClusterNode {name: $clusterNodeName, tag: $tag}), (b:ClusterCenter {name: $clusterCenterName, tag: $tag}) "
										+ "	MERGE (a)-[:Assigned {tag: $tag }]->(b)",
								parameters( "clusterNodeName", clusterNodeName, "clusterCenterName", clusterCenterName, "tag", tag ) );
				return result;
        } );
	}
	
	public void connectCentersIfNotExists(Session session, String clusterCenterName1, String clusterCenterName2, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:ClusterCenter {name: $clusterCenterName1, tag: $tag}), (b:ClusterCenter {name: $clusterCenterName2, tag: $tag}) "
										+ "	MERGE (a)-[:AssignedCenter {tag: $tag }]->(b)",
								parameters( "clusterCenterName1", clusterCenterName1, "clusterCenterName2", clusterCenterName2, "tag", tag ) );
				return result;
        } );
	}
	
	public void clearClusters(Session session, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a {tag: $tag})-[r {tag: $tag }]-(b {tag: $tag})	DELETE r",
						parameters("tag", tag ) );
				result = tx.run( "MATCH (a {tag: $tag})	DELETE a", parameters("tag", tag ) );
				return result;
        } );
	}
}
