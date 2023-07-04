package graphSimilarities;

import java.util.HashMap;
import java.util.Map;


public class SourceAndTerminusEdgeMatricesLong {

	private long[][] sourceEdgeMatrix;
	private long[][] terminusEdgeMatrix;
	private EdgeToNodeMapping[] mappingNodesToEdges;
	
	public SourceAndTerminusEdgeMatricesLong(long adjacencyMatrix[][]) {
		this(adjacencyMatrix, false);
	}
	
	public SourceAndTerminusEdgeMatricesLong(long adjacencyMatrix[][], boolean assignLabels) {
		if (assignLabels) {
			this.createSourceAndTerminusEdgeMatrixFromAdjacencyMatrixWithLabels(adjacencyMatrix);
		} else {
			this.createSourceAndTerminusEdgeMatrixFromAdjacencyMatrix(adjacencyMatrix);
		}
	}
	
	public long[][] getSourceEdgeMatrix() { return this.sourceEdgeMatrix; }
	
	public long[][] getTerminusEdgeMatrix() { return this.terminusEdgeMatrix; }
	
	public EdgeToNodeMapping[] getEdgeToNodesMapping() { return this.mappingNodesToEdges; }

	private int countEdgesInAdjacencyMatrix(long adjacencyMatrix[][]) {
		int numberEdges = 0;
		for (int rowIndex = 0; rowIndex < adjacencyMatrix.length; rowIndex++) {
			for (int colIndex = 0; colIndex < adjacencyMatrix[0].length; colIndex++) {
				if (adjacencyMatrix[rowIndex][colIndex] > 0.0) {
					numberEdges = numberEdges + 1;
				}
			}
		}
		return numberEdges;
	}
	
	private void createSourceAndTerminusEdgeMatrixFromAdjacencyMatrixWithLabels(long adjacencyMatrix[][]) {
		int countEdges = this.countEdgesInAdjacencyMatrix(adjacencyMatrix);
		long sourceEdgeMatrix[][] = new long[countEdges][];
		long terminusEdgeMatrix[][] = new long[countEdges][];
		int  edgeIndex;
		
		for (edgeIndex = 0; edgeIndex < countEdges; edgeIndex++) {
			sourceEdgeMatrix[edgeIndex] = new long[adjacencyMatrix.length];
			terminusEdgeMatrix[edgeIndex] = new long[adjacencyMatrix.length];

			for (int vertexIndex = 0; vertexIndex < adjacencyMatrix.length; vertexIndex++) {
				sourceEdgeMatrix[edgeIndex][vertexIndex] = 0;
				terminusEdgeMatrix[edgeIndex][vertexIndex] = 0;
			}
		}

		this.mappingNodesToEdges = new EdgeToNodeMapping[countEdges];
		edgeIndex = 0;
		for (int startEdgeIndex = 0; startEdgeIndex < adjacencyMatrix.length; startEdgeIndex++) {
			for (int endEdgeIndex = 0; endEdgeIndex < adjacencyMatrix.length; endEdgeIndex++) {
				if (adjacencyMatrix[startEdgeIndex][endEdgeIndex] > 0) {
					sourceEdgeMatrix[edgeIndex][startEdgeIndex] = adjacencyMatrix[startEdgeIndex][endEdgeIndex];
					terminusEdgeMatrix[edgeIndex][endEdgeIndex] = adjacencyMatrix[startEdgeIndex][endEdgeIndex];
					this.mappingNodesToEdges[edgeIndex] = new EdgeToNodeMapping(startEdgeIndex, endEdgeIndex,  edgeIndex);
					edgeIndex++;
				}
			}
		}
		
		this.sourceEdgeMatrix = sourceEdgeMatrix;
		this.terminusEdgeMatrix = terminusEdgeMatrix;
	}

	private void createSourceAndTerminusEdgeMatrixFromAdjacencyMatrix(long adjacencyMatrix[][]) {
		int countEdges = this.countEdgesInAdjacencyMatrix(adjacencyMatrix);
		long sourceEdgeMatrix[][] = new long[countEdges][];
		long terminusEdgeMatrix[][] = new long[countEdges][];
		int  edgeIndex;
		
		for (edgeIndex = 0; edgeIndex < countEdges; edgeIndex++) {
			sourceEdgeMatrix[edgeIndex] = new long[adjacencyMatrix.length];
			terminusEdgeMatrix[edgeIndex] = new long[adjacencyMatrix.length];

			for (int vertexIndex = 0; vertexIndex < adjacencyMatrix.length; vertexIndex++) {
				sourceEdgeMatrix[edgeIndex][vertexIndex] = 0;
				terminusEdgeMatrix[edgeIndex][vertexIndex] = 0;
			}
		}

		
		edgeIndex = 0;
		for (int startEdgeIndex = 0; startEdgeIndex < adjacencyMatrix.length; startEdgeIndex++) {
			for (int endEdgeIndex = 0; endEdgeIndex < adjacencyMatrix.length; endEdgeIndex++) {
				if (adjacencyMatrix[startEdgeIndex][endEdgeIndex] > 0) {
					sourceEdgeMatrix[edgeIndex][startEdgeIndex] = adjacencyMatrix[startEdgeIndex][endEdgeIndex];
					terminusEdgeMatrix[edgeIndex][endEdgeIndex] = adjacencyMatrix[startEdgeIndex][endEdgeIndex];
					edgeIndex++;
				}
			}
		}
		
		this.sourceEdgeMatrix = sourceEdgeMatrix;
		this.terminusEdgeMatrix = terminusEdgeMatrix;
	}
}
