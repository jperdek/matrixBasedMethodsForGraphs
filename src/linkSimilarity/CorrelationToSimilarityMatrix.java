package linkSimilarity;

import semanticAttributesAggregation.SemanticMetricsManager;

public class CorrelationToSimilarityMatrix {

	private double[][] correlationMatrix;
	
	public CorrelationToSimilarityMatrix(double[][] correlationMatrix) {
		this.correlationMatrix = correlationMatrix;
	}
	
	/**
	 * Evaluates ||col i|| =
	 * (SQRT(SUM k [POW(c k,i; 2)]) 
	 * where c i is correlation coefficient of i column and k is column index 
	 * 
	 * @param columnIndex - column index i
	 * @return value of ||col i||
	 */
	private double getAbsoluteColumnSimilarityPartialValue(int columnIndex) {
		double absoluteColumnSimilarity = 0.0;
		
		for (int rowIndex = 0; rowIndex < this.correlationMatrix.length; rowIndex++) {
			absoluteColumnSimilarity = absoluteColumnSimilarity + Math.pow(
					this.correlationMatrix[rowIndex][columnIndex], 2.0);
		}
		return Math.sqrt(absoluteColumnSimilarity);
	}
	
	/**
	 * Evaluates ||row i|| =
	 * (SQRT(SUM k [POW(c i,k; 2)]) 
	 * where c i is correlation coefficient of i column and k is column index 
	 * 
	 * @param rowIndex - row index i
	 * @return value of ||row i||
	 */
	private double getAbsoluteRowSimilarityPartialValue(int rowIndex) {
		double absoluteRowSimilarity = 0.0;
		
		for (int columnIndex = 0; columnIndex < this.correlationMatrix.length; columnIndex++) {
			absoluteRowSimilarity = absoluteRowSimilarity + Math.pow(
					this.correlationMatrix[rowIndex][columnIndex], 2.0);
		}
		return Math.sqrt(absoluteRowSimilarity);
	}
	
	/**
	 * Evaluates similarity between two correlation matrix rows
	 * = SUM k (c i1,k * c i2, k)
	 * 
	 * @param rowIndex1 - row index i1
	 * @param rowIndex2 - row index i2
	 * @return similarity value between two correlation matrix rows
	 */
	private double getPartialSimilarityValueOfTwoRows(int rowIndex1, int rowIndex2) {
		double twoRowsSimilarity = 0.0;
		
		for (int columnIndex = 0; columnIndex < this.correlationMatrix.length; columnIndex++) {
			twoRowsSimilarity = twoRowsSimilarity + 
					this.correlationMatrix[rowIndex1][columnIndex] * this.correlationMatrix[rowIndex2][columnIndex];
		}
		return twoRowsSimilarity;
	}
	
	/**
	 * Evaluates similarity between two correlation matrix columns
	 * = SUM k (c k,i1 * c k,i2)
	 * 
	 * @param columnIndex1 - column index i1
	 * @param columnIndex2 - column index i2
	 * @return similarity value between two correlation matrix columns
	 */
	private double getPartialSimilarityValueOfTwoColumns(int columnIndex1, int columnIndex2) {
		double twoColumnsSimilarity = 0.0;
		
		for (int rowIndex = 0;rowIndex < this.correlationMatrix.length; rowIndex++) {
			twoColumnsSimilarity = twoColumnsSimilarity + 
					this.correlationMatrix[rowIndex][columnIndex1] * this.correlationMatrix[rowIndex][columnIndex2];
		}
		return twoColumnsSimilarity;
	}
	
	/**
	 * Evaluates out link similarity sim OUT i,j 
	 * = (row i, row j) / (||row i|| * ||row j||)
	 * 
	 * 
	 * @param nodeIndex1 - index of the first node
	 * @param nodeIndex2 - index of the second node
	 * @return
	 */
	public double getOutLinkSimilarity(int nodeIndex1, int nodeIndex2) {
		return this.getPartialSimilarityValueOfTwoRows(nodeIndex1, nodeIndex2) / 
				(this.getAbsoluteRowSimilarityPartialValue(nodeIndex1) * 
							this.getAbsoluteRowSimilarityPartialValue(nodeIndex2));
	}
	
	/**
	 * Evaluates in link similarity sim IN i,j 
	 * = (col i, col j) / (||col i|| * ||col j||)
	 * 
	 * 
	 * @param nodeIndex1 - index of the first node
	 * @param nodeIndex2 - index of the second node
	 * @return
	 */
	public double getInLinkSimilarity(int nodeIndex1, int nodeIndex2) {
		return this.getPartialSimilarityValueOfTwoColumns(nodeIndex1, nodeIndex2) /
				(this.getAbsoluteColumnSimilarityPartialValue(nodeIndex1) * 
						this.getAbsoluteColumnSimilarityPartialValue(nodeIndex2));
	}
	
	/**
	 * Evaluates MOD i,j = ||col i|| + ||col j|| + ||row i|| + ||row j||
	 * 
	 * 
	 * @param nodeIndex1 - index of the first node
	 * @param nodeIndex2 - index of the second node
	 * @return MOD of i, j nodes
	 */
	private double getMODofNodes(int nodeIndex1, int nodeIndex2) {
		return this.getAbsoluteColumnSimilarityPartialValue(nodeIndex1) + 
			   this.getAbsoluteColumnSimilarityPartialValue(nodeIndex2) + 
			   this.getAbsoluteRowSimilarityPartialValue(nodeIndex1) 	+ 
			   this.getAbsoluteRowSimilarityPartialValue(nodeIndex2);
	}
	
	/**
	 * Evaluates MOD of i, j nodes
	 * 
	 * @param rowPartialSum - value of ||row i|| + ||row j||
	 * @param columnPartialSum - value of ||col i|| + ||col j||
	 * @return rowPartialSum + columnPartialSum
	 */
	private double getMODofNodes(double rowPartialSum, double columnPartialSum) {
		return rowPartialSum + columnPartialSum;
	}
	
	/**
	 * Evaluates value of ||col i|| + ||col j||
	 * 
	 * @param nodeIndex1 - index of the first node
	 * @param nodeIndex2 - index of the second node
	 * @return value of ||col i|| + ||col j||
	 */
	private double getAbsoluteColumnSimilarityPartialValuesSum(int nodeIndex1, int nodeIndex2) {
		return this.getAbsoluteColumnSimilarityPartialValue(nodeIndex1) + 
			   this.getAbsoluteColumnSimilarityPartialValue(nodeIndex2);
	}
	
	/**
	 * Evaluates value of ||row i|| + ||row j||
	 * 
	 * @param nodeIndex1 - index of the first node
	 * @param nodeIndex2 - index of the second node
	 * @return value of ||row i|| + ||row j||
	 */
	private double getAbsoluteRowSimilarityPartialValuesSum(int nodeIndex1, int nodeIndex2) {
		return this.getAbsoluteRowSimilarityPartialValue(nodeIndex1) + 
			   this.getAbsoluteRowSimilarityPartialValue(nodeIndex2);
	}
	
	/**
	 * Evaluates Alfa similarity coefficient: 
	 * (||row i|| + ||row j||) / (MOD i, j)
	 * 
	 * @param rowPartialSum - ||row i|| + ||row j||
	 * @param MOD - ||col i|| + ||col j|| + ||row i|| + ||row j||
	 * @return Alfa similarity coefficient value
	 */
	private double getSimilarityAlfa(double rowPartialSum, double MOD) {
		return rowPartialSum / MOD;
	}
	
	/**
	 * Evaluates Beta similarity coefficient: 
	 * (||col i|| + ||col j||) / (MOD i, j)
	 * 
	 * @param columnPartialSum - ||col i|| + ||col j||
	 * @param MOD - ||col i|| + ||col j|| + ||row i|| + ||row j||
	 * @return Beta similarity coefficient value
	 */
	private double getSimilarityBeta(double columnPartialSum, double MOD) {
		return columnPartialSum / MOD;
	}
	
	/**
	 * Evaluates similarity between two nodes i, j (sim(i, j))
	 * = Alfa i,j * sim OUT i,j + Beta i,j * sim IN i,j
	 * 
	 * @param nodeIndex1 - index of the first node
	 * @param nodeIndex2 - index of the second node
	 * @return
	 */
	public double evaluateSimilarityBetweenTwoNodes(int nodeIndex1, int nodeIndex2) {
		double columnPartialSum = this.getAbsoluteColumnSimilarityPartialValuesSum(nodeIndex1, nodeIndex2);
		double rowPartialSum = this. getAbsoluteRowSimilarityPartialValuesSum(nodeIndex1, nodeIndex2);
		double MOD = this.getMODofNodes(rowPartialSum, columnPartialSum);
		double alfaSimilarity = this.getSimilarityAlfa(rowPartialSum, MOD);
		double betaSimilarity = this.getSimilarityBeta(columnPartialSum, MOD);
		double similarityIN = this.getInLinkSimilarity(nodeIndex1, nodeIndex2);
		double similarityOUT = this.getOutLinkSimilarity(nodeIndex1, nodeIndex2);
		
		return similarityOUT * alfaSimilarity + similarityIN * betaSimilarity;
	}
	
	/**
	 * Constructs similarity matrix according correlation matrix
	 * i != j then (i, j) ->  sim(i, j) = Alfa i,j * sim OUT i,j + Beta i,j * sim IN i,j
	 * i == j then (i, j) ->  1.0
	 * 
	 * @return similarity matrix
	 */
	public double[][] constructSimilarityMatrix() {
		double similarityMatrix[][] = new double[this.correlationMatrix.length][];
		
		for (int columnIndex = 0; columnIndex < this.correlationMatrix.length; columnIndex++) {
			similarityMatrix[columnIndex] = new double[this.correlationMatrix.length];
			
			for (int rowIndex = 0; rowIndex < this.correlationMatrix[columnIndex].length; rowIndex++) {
				if (rowIndex != columnIndex) {
					similarityMatrix[columnIndex][rowIndex] = 
							this.evaluateSimilarityBetweenTwoNodes(rowIndex, columnIndex);
				} else {
					similarityMatrix[columnIndex][rowIndex] = 1.0;
				}
			}
		}
		return similarityMatrix;
	}
	
	/**
	 * Constructs similarity matrix according correlation matrix
	 * i != j then (i, j) ->  sim(i, j) = Alfa i,j * sim OUT i,j + Beta i,j * sim IN i,j
	 * i == j then (i, j) ->  1.0
	 * 
	 * @return similarity matrix
	 */
	public double[][] constructSimilarityMatrix(SemanticMetricsManager semanticMetricsManager) {
		double similarityMatrix[][] = new double[this.correlationMatrix.length][];
		
		for (int columnIndex = 0; columnIndex < this.correlationMatrix.length; columnIndex++) {
			similarityMatrix[columnIndex] = new double[this.correlationMatrix.length];
			
			for (int rowIndex = 0; rowIndex < this.correlationMatrix[columnIndex].length; rowIndex++) {
				if (rowIndex != columnIndex) {
					similarityMatrix[columnIndex][rowIndex] = 
							this.evaluateSimilarityBetweenTwoNodes(rowIndex, columnIndex) 
							+ semanticMetricsManager.evaluateMetricValueBetweenTwoNodes(rowIndex, columnIndex);
				} else {
					similarityMatrix[columnIndex][rowIndex] = 1.0;
				}
			}
		}
		return similarityMatrix;
	}
	
	public void printSimilarityMatrix(double[][] similarityMatrix) {
		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < similarityMatrix.length; columnIndex++) {
				System.out.print(similarityMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
}
