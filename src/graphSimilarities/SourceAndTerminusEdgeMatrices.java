package graphSimilarities;

import java.util.HashMap;
import java.util.Map;

public class SourceAndTerminusEdgeMatrices {

	private double[][] sourceEdgeMatrix;
	private double[][] terminusEdgeMatrix;
	private EdgeToNodeMapping[] mappingNodesToEdges;
	
	public SourceAndTerminusEdgeMatrices(double adjacencyMatrix[][]) {
		this(adjacencyMatrix, false);
	}
	
	public SourceAndTerminusEdgeMatrices(double adjacencyMatrix[][], boolean assignLabels) {
		if (assignLabels) {
			this.createSourceAndTerminusEdgeMatrixFromAdjacencyMatrixWithLabels(adjacencyMatrix);
		} else {
			this.createSourceAndTerminusEdgeMatrixFromAdjacencyMatrix(adjacencyMatrix);
		}
	}
	
	public double[][] getSourceEdgeMatrix() { return this.sourceEdgeMatrix; }
	
	public double[][] getTerminusEdgeMatrix() { return this.terminusEdgeMatrix; }
	
	public EdgeToNodeMapping[] getEdgeToNodesMapping() { return this.mappingNodesToEdges; }

	private int countEdgesInAdjacencyMatrix(double adjacencyMatrix[][]) {
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
	
	private void createSourceAndTerminusEdgeMatrixFromAdjacencyMatrixWithLabels(double adjacencyMatrix[][]) {
		int countEdges = this.countEdgesInAdjacencyMatrix(adjacencyMatrix);
		double sourceEdgeMatrix[][] = new double[countEdges][];
		double terminusEdgeMatrix[][] = new double[countEdges][];
		int  edgeIndex;
		
		for (edgeIndex = 0; edgeIndex < countEdges; edgeIndex++) {
			sourceEdgeMatrix[edgeIndex] = new double[adjacencyMatrix.length];
			terminusEdgeMatrix[edgeIndex] = new double[adjacencyMatrix.length];

			for (int vertexIndex = 0; vertexIndex < adjacencyMatrix.length; vertexIndex++) {
				sourceEdgeMatrix[edgeIndex][vertexIndex] = 0.0;
				terminusEdgeMatrix[edgeIndex][vertexIndex] = 0.0;
			}
		}

		this.mappingNodesToEdges = new EdgeToNodeMapping[countEdges];
		edgeIndex = 0;
		for (int startEdgeIndex = 0; startEdgeIndex < adjacencyMatrix.length; startEdgeIndex++) {
			for (int endEdgeIndex = 0; endEdgeIndex < adjacencyMatrix.length; endEdgeIndex++) {
				if (adjacencyMatrix[startEdgeIndex][endEdgeIndex] > 0.0) {
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

	private void createSourceAndTerminusEdgeMatrixFromAdjacencyMatrix(double adjacencyMatrix[][]) {
		int countEdges = this.countEdgesInAdjacencyMatrix(adjacencyMatrix);
		double sourceEdgeMatrix[][] = new double[countEdges][];
		double terminusEdgeMatrix[][] = new double[countEdges][];
		int  edgeIndex;
		
		for (edgeIndex = 0; edgeIndex < countEdges; edgeIndex++) {
			sourceEdgeMatrix[edgeIndex] = new double[adjacencyMatrix.length];
			terminusEdgeMatrix[edgeIndex] = new double[adjacencyMatrix.length];

			for (int vertexIndex = 0; vertexIndex < adjacencyMatrix.length; vertexIndex++) {
				sourceEdgeMatrix[edgeIndex][vertexIndex] = 0.0;
				terminusEdgeMatrix[edgeIndex][vertexIndex] = 0.0;
			}
		}

		
		edgeIndex = 0;
		for (int startEdgeIndex = 0; startEdgeIndex < adjacencyMatrix.length; startEdgeIndex++) {
			for (int endEdgeIndex = 0; endEdgeIndex < adjacencyMatrix.length; endEdgeIndex++) {
				if (adjacencyMatrix[startEdgeIndex][endEdgeIndex] > 0.0) {
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
