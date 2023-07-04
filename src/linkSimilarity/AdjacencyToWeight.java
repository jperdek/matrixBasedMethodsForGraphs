package linkSimilarity;

import java.util.Arrays;
import java.util.Map;

import graphSimilarities.EdgeToNodeMapping;


public class AdjacencyToWeight {

	private double[] nodesIN; 	// P i,IN - column //incoming 
	private double[] nodesOUT; 	// P i,OUT - row
	private int matrixSize = 5; // sizeIN, sizeOUT
	private double normalizedIN = 0.0;
	private double normalizedOUT = 0.0;
	private double maxIN = 0.0; 	//M IN
	private double minIN = 0.0; 	//m IN
	private double maxOUT = 0.0; 	//M OUT
	private double minOUT = 0.0;	//m OUT
	private double[] weights;

	public AdjacencyToWeight(long[][] adjacencyMatrix) {
		this(adjacencyMatrix, true);
	}
	
	public AdjacencyToWeight(long[][] adjacencyMatrix, boolean rowsIncoming) {
		this.prepareWeightsData(adjacencyMatrix, rowsIncoming);
	}

	public AdjacencyToWeight(long[][] adjacencyMatrix, 
			double similarityEdgeMatrix[][], Map<String, EdgeToNodeMapping> mergedNodeMapping) {
		this(adjacencyMatrix, true, similarityEdgeMatrix, mergedNodeMapping);
	}
	
	public AdjacencyToWeight(long[][] adjacencyMatrix, boolean rowsIncoming, 
			double similarityEdgeMatrix[][], Map<String, EdgeToNodeMapping> mergedNodeMapping) {
		this.prepareWeightsData(adjacencyMatrix, rowsIncoming, similarityEdgeMatrix, mergedNodeMapping);
	}
	
	private void prepareWeightsData(long[][] adjacencyMatrix, boolean rowsIncoming) {
		this.matrixSize = adjacencyMatrix.length;
		if (rowsIncoming) {
			this.nodesOUT = this.obtainOutcomingLinks(adjacencyMatrix);
			this.nodesIN = this.obtainIncomingLinks(adjacencyMatrix);
		} else {
			this.nodesIN = this.obtainOutcomingLinks(adjacencyMatrix);
			this.nodesOUT = this.obtainIncomingLinks(adjacencyMatrix);
		}
		this.normalizedIN = this.normalizeAndCreateSum(this.nodesIN, matrixSize);
		this.normalizedOUT = this.normalizeAndCreateSum(this.nodesOUT, matrixSize);
		this.maxIN = Arrays.stream(this.nodesIN).max().getAsDouble();;
		this.maxOUT = Arrays.stream(this.nodesOUT).max().getAsDouble();
		this.minIN = Arrays.stream(this.nodesIN).min().getAsDouble();
		this.minOUT = Arrays.stream(this.nodesOUT).min().getAsDouble();
		
		this.weights = this.evaluateWeights(this.nodesIN, this.nodesOUT,
				this.maxIN, this.minIN, this.normalizedIN, 
				this.maxOUT, this.minOUT, this.normalizedOUT);
	}
		
	private void prepareWeightsData(long[][] adjacencyMatrix, boolean rowsIncoming, 
			double similarityEdgeMatrix[][], Map<String, EdgeToNodeMapping> mergedNodeMapping) {
		this.matrixSize = adjacencyMatrix.length;
		if (rowsIncoming) {
			this.nodesOUT = this.obtainOutcomingLinks(adjacencyMatrix, similarityEdgeMatrix, mergedNodeMapping);
			this.nodesIN = this.obtainIncomingLinks(adjacencyMatrix, similarityEdgeMatrix, mergedNodeMapping);
		} else {
			this.nodesIN = this.obtainOutcomingLinks(adjacencyMatrix, similarityEdgeMatrix, mergedNodeMapping);
			this.nodesOUT = this.obtainIncomingLinks(adjacencyMatrix, similarityEdgeMatrix, mergedNodeMapping);
		}
		
		this.normalizedIN = this.normalizeAndCreateSum(this.nodesIN, matrixSize);
		this.normalizedOUT = this.normalizeAndCreateSum(this.nodesOUT, matrixSize);
		this.maxIN = Arrays.stream(this.nodesIN).max().getAsDouble();;
		this.maxOUT = Arrays.stream(this.nodesOUT).max().getAsDouble();
		this.minIN = Arrays.stream(this.nodesIN).min().getAsDouble();
		this.minOUT = Arrays.stream(this.nodesOUT).min().getAsDouble();
		
		this.weights = this.evaluateWeights(this.nodesIN, this.nodesOUT,
				this.maxIN, this.minIN, this.normalizedIN, 
				this.maxOUT, this.minOUT, this.normalizedOUT);
	}

	private double[] evaluateWeights(double[] nodesIN, double[] nodesOUT, 
			double maxIN, double minIN, double normalizedIN, 
			double maxOUT, double minOUT, double normalizedOUT) {
		double[] weights = new double[nodesIN.length];
		double substractionIN, substractionOUT; 
		
		for (int index = 0; index < nodesOUT.length; index++) {
			if (nodesIN[index] >= Long.MAX_VALUE || nodesOUT[index] >= Long.MAX_VALUE) {
				weights[index] = 0.0;
			} else {
				// CHECKING IF DIVISION WITH ZERO NOT OCCUR
				substractionIN = maxIN - minIN;
				substractionOUT = maxOUT - minOUT;
				
				if (substractionIN == 0.0 || substractionOUT == 0.0) {
					if (substractionIN == 0 && substractionOUT == 0.0) {
						weights[index] = 0.5;
					} else if (substractionIN == 0.0) {
						weights[index] = 1.0 + (nodesOUT[index] - normalizedOUT + 0.0) / (substractionOUT + 0.0);
					} else {
						weights[index] = 1.0 + (nodesIN[index] - normalizedIN + 0.0) / (substractionIN + 0.0);
					}
				} else {
					weights[index] = 1.0 + Math.max(
							(nodesIN[index] - normalizedIN + 0.0) / (substractionIN + 0.0), 
							(nodesOUT[index] - normalizedOUT + 0.0) / (substractionOUT + 0.0)); 
				}
			}
		}
		
		return weights;
	}

	private double[] obtainOutcomingLinks(long[][] adjacencyMatrix, 
			double similarityEdgeMatrix[][], Map<String, EdgeToNodeMapping> mergedNodeMapping) {
		double[] nodesOUT = new double[this.matrixSize];
		String previousMappingIdentifier;
		EdgeToNodeMapping mappedEdge;
		int edgeMatrix1Identifier, edgeMatrix2Identifier;

		for (int columnIndex = 0; columnIndex < this.matrixSize; columnIndex++) {
			nodesOUT[columnIndex] = 0;
			for (int rowIndex = 0; rowIndex < this.matrixSize; rowIndex++) {
				//previousMappingIdentifier = Integer.toString(rowIndex) + "," + Integer.toString(columnIndex);
				previousMappingIdentifier = Integer.toString(columnIndex) + "," + Integer.toString(rowIndex);
				mappedEdge = mergedNodeMapping.get(previousMappingIdentifier);
				if (mappedEdge != null) {
					edgeMatrix1Identifier = (int) mappedEdge.getStartNodeIndex();
					edgeMatrix2Identifier = (int) mappedEdge.getEndNodeIndex();
					
					nodesOUT[columnIndex] = nodesOUT[columnIndex] + adjacencyMatrix[columnIndex][rowIndex] * 
							(1.0 + similarityEdgeMatrix[edgeMatrix1Identifier][edgeMatrix2Identifier]);
				
				//EDGE NOT EXISTS, THUS SIMILARITY CANNOT BE MEASURED - also adjacencyMatrix[columnIndex][rowIndex] should be zero
				} else { 
					nodesOUT[columnIndex] = nodesOUT[columnIndex] + adjacencyMatrix[columnIndex][rowIndex];
				}
			}
		}
		return nodesOUT;
	}
	
	private double[] obtainIncomingLinks(long[][] adjacencyMatrix,
			double similarityEdgeMatrix[][], Map<String, EdgeToNodeMapping> mergedNodeMapping) {
		double[] nodesIN = new double[this.matrixSize];
		String previousMappingIdentifier;
		EdgeToNodeMapping mappedEdge;
		int edgeMatrix1Identifier, edgeMatrix2Identifier;
		
		for (int rowIndex = 0; rowIndex < this.matrixSize; rowIndex++) {
			nodesIN[rowIndex] = 0;
			for (int columnIndex = 0; columnIndex < this.matrixSize; columnIndex++) {
				//previousMappingIdentifier = Integer.toString(rowIndex) + "," + Integer.toString(columnIndex);
				previousMappingIdentifier = Integer.toString(columnIndex) + "," + Integer.toString(rowIndex);
				mappedEdge = mergedNodeMapping.get(previousMappingIdentifier);
				if (mappedEdge != null) {
					edgeMatrix1Identifier = (int) mappedEdge.getStartNodeIndex();
					edgeMatrix2Identifier = (int) mappedEdge.getEndNodeIndex();
	
					nodesIN[rowIndex] = nodesIN[rowIndex] + adjacencyMatrix[columnIndex][rowIndex] * 
							(1.0 + similarityEdgeMatrix[edgeMatrix1Identifier][edgeMatrix2Identifier]);
				
				//EDGE NOT EXISTS, THUS SIMILARITY CANNOT BE MEASURED - also adjacencyMatrix[columnIndex][rowIndex] should be zero
				} else {
					nodesIN[rowIndex] = nodesIN[rowIndex] + adjacencyMatrix[columnIndex][rowIndex];
				}
			}
		}
		return nodesIN;
	}
	
	private double[] obtainOutcomingLinks(long[][] adjacencyMatrix) {
		double[] nodesOUT = new double[this.matrixSize];
		
		for (int columnIndex = 0; columnIndex < this.matrixSize; columnIndex++) {
			nodesOUT[columnIndex] = 0;
			for (int rowIndex = 0; rowIndex < this.matrixSize; rowIndex++) {
				nodesOUT[columnIndex] = nodesOUT[columnIndex] + adjacencyMatrix[columnIndex][rowIndex];
			}
		}
		return nodesOUT;
	}
	
	private double[] obtainIncomingLinks(long[][] adjacencyMatrix) {
		double[] nodesIN = new double[this.matrixSize];
		
		for (int rowIndex = 0; rowIndex < this.matrixSize; rowIndex++) {
			nodesIN[rowIndex] = 0;
			for (int columnIndex = 0; columnIndex < this.matrixSize; columnIndex++) {
				nodesIN[rowIndex] = nodesIN[rowIndex] + adjacencyMatrix[columnIndex][rowIndex];
			}
		}
		return nodesIN;
	}

	private double normalizeAndCreateSum(double[] array, int value) {
		double normalizedSum = 0.0;
		for (int index = 0; index < array.length; index++) {
			normalizedSum = normalizedSum + (array[index] + 0.0) / (value + 0.0);
		}
		return normalizedSum;
	}
	
	public double[] getOutcomingValues() { return this.nodesOUT; }
	
	public double[] getIncomingValues() { return this.nodesIN; }
	
	public double getNormalizedSumIN() { return this.normalizedIN; }
	
	public double getNormalizedSumOUT() { return this.normalizedOUT; }
	
	public double[] getWeights() { return this.weights; }
	
	public double[][] convertToWeightMatrix(long[][] adjacencyMatrix) {
		double[][] weightMatrix = new double[this.matrixSize][];
		for (int rowIndex = 0; rowIndex < this.matrixSize; rowIndex++) {
			weightMatrix[rowIndex] = new double[this.matrixSize];
			
			for (int columnIndex = 0; columnIndex < this.matrixSize; columnIndex++) {
				if (columnIndex == rowIndex) {
					weightMatrix[rowIndex][columnIndex] = 1.0;	
				} else {
					weightMatrix[rowIndex][columnIndex] = Math.max(this.weights[rowIndex], this.weights[columnIndex]);
				}
			}
		}
		return weightMatrix;
	}
	
	public void printWeightMatrix(double[][] weightMatrix) {
		for (int rowIndex = 0; rowIndex < this.matrixSize; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < this.matrixSize; columnIndex++) {
				System.out.print(weightMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
}
