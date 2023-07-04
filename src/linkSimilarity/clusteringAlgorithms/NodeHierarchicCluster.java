package linkSimilarity.clusteringAlgorithms;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Session;

import graphConstruction.GraphHierarchicClustering;
import graphConstruction.GraphToMatrixProcessor;


public class NodeHierarchicCluster {
	
	private NodeHierarchicCluster parentClusterTop = null;
	private NodeHierarchicCluster parentClusterBottom = null;
	private int relativePointD = -1;
	private int pointD = -1;
	private String matrixNames[] = null;
	
	private List<Integer> associatedNodeIndexes;
	
	public NodeHierarchicCluster() {
		this.associatedNodeIndexes = new ArrayList<Integer>();
	}
	
	public void setPointD(int pointD) {
		this.pointD = pointD;
	}
	
	public void setRelativePointD(int pointD) {
		this.relativePointD = pointD;
	}
	
	public void addNodeIndex(int nodeIndex) {
		this.associatedNodeIndexes.add(nodeIndex);
	}
	
	public NodeHierarchicCluster createTopCluster() {
		this.parentClusterTop = new NodeHierarchicCluster();
		return this.parentClusterTop;
	}
	
	public NodeHierarchicCluster createBottomCluster() {
		this.parentClusterBottom = new NodeHierarchicCluster();
		return this.parentClusterBottom;
	}
	
	public void setMatrixNames(String[] matrixNames) {
		this.matrixNames = matrixNames;
	}

	private void fillSpace(int depth) {
		for (int d = 0 ; d < depth*2; d++) {
			System.out.print(' ');
		}
	}
	
	public void printHierarchy(int depth) {
		Iterator<Integer> associatedNodes = this.associatedNodeIndexes.iterator();
		System.out.println();
		this.fillSpace(depth);

		System.out.println("PointD: " + this.pointD + "; Relative pointD: " + this.relativePointD);
		this.fillSpace(depth);
		System.out.println("Elements: ");
		this.fillSpace(depth);
		while(associatedNodes.hasNext()) {
			System.out.print(associatedNodes.next());
			System.out.print(",  ");
		}
		System.out.println();
		
	
		// LETF PART
		if (this.parentClusterTop != null) {
			this.fillSpace(depth);
			System.out.println("Parsing left cluster!");
			this.parentClusterTop.printHierarchy(depth + 1);
		}
		
		// RIGHT PART
		if (this.parentClusterBottom != null) {
			this.fillSpace(depth);
			System.out.println("Parsing right cluster!");
			this.parentClusterBottom.printHierarchy(depth + 1);
		}
	}
	
	public void printHierarchyWithNames(int depth) {
		Iterator<Integer> associatedNodes = this.associatedNodeIndexes.iterator();
		System.out.println();
		this.fillSpace(depth);

		System.out.println("PointD: " + this.pointD + "; Relative pointD: " + this.relativePointD);
		this.fillSpace(depth);
		System.out.println("Elements: ");
		this.fillSpace(depth);
		while(associatedNodes.hasNext()) {
			System.out.print(this.matrixNames[associatedNodes.next()]);
			System.out.print(",  ");
		}
		System.out.println();
		
	
		// LETF PART
		if (this.parentClusterTop != null) {
			this.fillSpace(depth);
			System.out.println("Parsing left cluster!");
			this.parentClusterTop.setMatrixNames(this.matrixNames);
			this.parentClusterTop.printHierarchyWithNames(depth + 1);
		}
		
		// RIGHT PART
		if (this.parentClusterBottom != null) {
			this.fillSpace(depth);
			System.out.println("Parsing right cluster!");
			this.parentClusterBottom.setMatrixNames(this.matrixNames);
			this.parentClusterBottom.printHierarchyWithNames(depth + 1);
		}
	}
	
	public void saveHierarchyToCSV(String pathToFile, 
			String matrixColumnNames[], String delimiter) { 
		Map<Integer, String> nameMapping = new HashMap<Integer, String>();
		for(int matrixColumnIndex = 0; matrixColumnIndex < matrixColumnNames.length; matrixColumnIndex++) {
			nameMapping.put(matrixColumnIndex, matrixColumnNames[matrixColumnIndex]);
		}
		this.saveHierarchyToCSV(pathToFile, nameMapping, delimiter);
	}
	
	public void saveHierarchyToCSV(String pathToFile, 
			Map<Integer, String> nameMapping, String delimiter) { 
		FileWriter fw = null;
		BufferedWriter bufferedWriter = null;
		try {
			fw = new FileWriter(pathToFile);
			bufferedWriter = new BufferedWriter(fw);
			
			this.saveHierarchyToCSV(0, bufferedWriter, nameMapping, "START", delimiter);
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedWriter != null) { 
				try { bufferedWriter.close(); }
				catch(IOException e) {
					e.printStackTrace();
				};
			}
			if(fw != null) { 
				try {
					fw.close();
				} catch(IOException e) {
				
				};
			}
		}
	}
	
	public void saveHierarchyToCSV(int depth, BufferedWriter bufferedWriter, Map<Integer, String> nameMapping, 
			String parentPosition, String delimiter) throws IOException {
		Iterator<Integer> associatedNodes = this.associatedNodeIndexes.iterator();
		int nodeIndex;
		String variableName;
		String clusterName;
		boolean writeRecords;
		
		if (this.parentClusterTop != null && this.parentClusterBottom == null) {
			writeRecords = false;
		} else if(this.parentClusterTop == null && this.parentClusterBottom != null) {
			writeRecords = false;
		} else if(this.parentClusterTop == null && this.parentClusterBottom == null) {
			writeRecords = true;
		} else {
			writeRecords = false;
		}
		
		clusterName = parentPosition + "_" + Integer.toString(depth);

		if (writeRecords) {
			while(associatedNodes.hasNext()) {
				nodeIndex = associatedNodes.next();
				variableName = nameMapping.get(nodeIndex);
				bufferedWriter.write(clusterName + delimiter + 
						variableName + delimiter + Integer.toString(pointD) 
						+ delimiter + Integer.toString(pointD) + "\n");
			}
		}
	
		// LETF PART
		if (this.parentClusterTop != null) {
			this.parentClusterTop.saveHierarchyToCSV(depth + 1, bufferedWriter, nameMapping, "TOP_LEFT", delimiter);
		}
		
		// RIGHT PART
		if (this.parentClusterBottom != null) {
			this.parentClusterBottom.saveHierarchyToCSV(depth + 1, bufferedWriter, nameMapping, "BOTTOM_RIGHT", delimiter);
		}
	}
	
	public String printHierarchyWithNamesToString(int depth) {
		String result = "";
		Iterator<Integer> associatedNodes = this.associatedNodeIndexes.iterator();
		while(associatedNodes.hasNext()) {
			result = result + this.matrixNames[associatedNodes.next()];
		}
		
	
		// LETF PART
		if (this.parentClusterTop != null) {
			this.parentClusterTop.setMatrixNames(this.matrixNames);
			result = result + "-[ " + this.parentClusterTop.printHierarchyWithNamesToString(depth + 1) + "] ";
		}
		
		// RIGHT PART
		if (this.parentClusterBottom != null) {
			this.parentClusterBottom.setMatrixNames(this.matrixNames);
			result = result + "-[ " + this.parentClusterBottom.printHierarchyWithNamesToString(depth + 1) + "] ";
		}
		return result;
	}
	
	public void insertClusters(GraphHierarchicClustering graphHierarchicClustering, Session session, 
			String clusteredTag, String parentCenterPointName) {
		graphHierarchicClustering.insertClusterCenterIfNotExists(session, parentCenterPointName, clusteredTag);
		this.insertClusters(graphHierarchicClustering, session, clusteredTag, parentCenterPointName, 0);
	}
	
	public void insertClusters(GraphHierarchicClustering graphHierarchicClustering, Session session, 
			String clusteredTag, String parentCenterPointName, int depth) {
		String leftClusterCenterName = parentCenterPointName + "LEFT_"+ Integer.toString(depth);
		String rightClusterCenterName = parentCenterPointName + "RIGHT_"+ Integer.toString(depth);
		
		if (this.parentClusterTop == null || this.parentClusterBottom == null) {
			String nodeName;
			Iterator<Integer> associatedNodes = this.associatedNodeIndexes.iterator();
			while(associatedNodes.hasNext()) {
				nodeName = this.matrixNames[associatedNodes.next()];
				graphHierarchicClustering.insertClusterNodeIfNotExists(session, nodeName, clusteredTag);
				graphHierarchicClustering.connectNodeToCenterIfNotExists(session, nodeName, parentCenterPointName, clusteredTag);
			}
		}
		
		// LETF PART
		if (this.parentClusterTop != null) {
			graphHierarchicClustering.insertClusterCenterIfNotExists(session, leftClusterCenterName, clusteredTag);
			graphHierarchicClustering.connectCentersIfNotExists(session, parentCenterPointName, leftClusterCenterName, clusteredTag);
			this.parentClusterTop.setMatrixNames(this.matrixNames);
			this.parentClusterTop.insertClusters(graphHierarchicClustering, session, clusteredTag, leftClusterCenterName, depth + 1);
		}
		
		// RIGHT PART
		if (this.parentClusterBottom != null) {
			graphHierarchicClustering.insertClusterCenterIfNotExists(session, rightClusterCenterName, clusteredTag);
			graphHierarchicClustering.connectCentersIfNotExists(session, parentCenterPointName, rightClusterCenterName, clusteredTag);
			this.parentClusterBottom.setMatrixNames(this.matrixNames);
			this.parentClusterBottom.insertClusters(graphHierarchicClustering, session, clusteredTag, rightClusterCenterName, depth + 1);
		}
	}
}
