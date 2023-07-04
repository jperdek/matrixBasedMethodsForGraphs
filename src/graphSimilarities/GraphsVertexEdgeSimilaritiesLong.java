package graphSimilarities;


public class GraphsVertexEdgeSimilaritiesLong {

	private long sourceEdgeMatrix1[][];
	private long terminusEdgeMatrix1[][];
	private long sourceEdgeMatrix2[][];
	private long terminusEdgeMatrix2[][];
	
	private double convergedMatrixX[][] = null;
	private double convergedMatrixY[][] = null;
	
	private EdgeToNodeMapping[] mappingNodesToEdgesMatrix1;
	private EdgeToNodeMapping[] mappingNodesToEdgesMatrix2;
	

	public GraphsVertexEdgeSimilaritiesLong(long matrix1[][], long matrix2[][]) {
		this(matrix1, matrix2, false);
	}
	
	public GraphsVertexEdgeSimilaritiesLong(long matrix1[][], long matrix2[][], boolean assignNodeLabels) {
		SourceAndTerminusEdgeMatricesLong sourceTerminusMatrix1 = new SourceAndTerminusEdgeMatricesLong(matrix1, assignNodeLabels);
		this.sourceEdgeMatrix1 = sourceTerminusMatrix1.getSourceEdgeMatrix();
		this.terminusEdgeMatrix1 = sourceTerminusMatrix1.getTerminusEdgeMatrix();
		
		SourceAndTerminusEdgeMatricesLong sourceTerminusMatrix2 = new SourceAndTerminusEdgeMatricesLong(matrix2, assignNodeLabels);
		this.sourceEdgeMatrix2 = sourceTerminusMatrix2.getSourceEdgeMatrix();
		this.terminusEdgeMatrix2 = sourceTerminusMatrix2.getTerminusEdgeMatrix();
		
		if (assignNodeLabels) {
			mappingNodesToEdgesMatrix1 = sourceTerminusMatrix1.getEdgeToNodesMapping();
			mappingNodesToEdgesMatrix2 = sourceTerminusMatrix2.getEdgeToNodesMapping();
		}
	}
	
	public void computeSimilarityMatrices() {
		double initialXMatrix[][] = this.initializeOnesMatrix(this.sourceEdgeMatrix1.length, this.sourceEdgeMatrix2.length);
		double initialYMatrix[][] = this.initializeOnesMatrix(this.sourceEdgeMatrix1[0].length, this.sourceEdgeMatrix2[0].length);
		this.computeSimilarityMatricesPairIterationToConvergence(initialXMatrix, initialYMatrix);
	}
	
	public double[][] getConvergedMatrixX() { return this.convergedMatrixX; }
	
	public double[][] getConvergedMatrixY() { return this.convergedMatrixY; }

	public EdgeToNodeMapping[] getMappingEdgesToNodesForMatrix1() { return this.mappingNodesToEdgesMatrix1; }
	
	public EdgeToNodeMapping[] getMappingEdgesToNodesForMatrix2() { return this.mappingNodesToEdgesMatrix2; }
	
	private double[] evaluateColumnMeans(double updateMatrix[][]) {
		double columnMeans[] = new double[updateMatrix[0].length];
		for (int colIndex = 0; colIndex < updateMatrix[0].length; colIndex++) {
			columnMeans[colIndex] = 0.0;
			
			for (int rowIndex = 0; rowIndex < updateMatrix.length; rowIndex++) {
				columnMeans[colIndex] = columnMeans[colIndex] + updateMatrix[rowIndex][colIndex];
			}
			columnMeans[colIndex] =(columnMeans[colIndex] + 0.0) / updateMatrix.length;
		}
		return columnMeans;
	}
	
	public static double[][] multiplyMatrixWithMatrixNewMatrix(double matrix1[][], double matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix2[0].length];
			
			for (int colIndex2 = 0; colIndex2 < matrix2[0].length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = 0.0;
				for (int k = 0; k < matrix2.length; k++) {
					//System.out.println(matrix2[k][colIndex2]);
					newMatrix[rowIndex1][colIndex2] = newMatrix[rowIndex1][colIndex2] + matrix1[rowIndex1][k] * matrix2[k][colIndex2];
				}
			}
		}
		return newMatrix;
	}
	
	public static double[][] multiplyMatrixWithMatrixNewMatrix(long matrix1[][], double matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix2[0].length];
			
			for (int colIndex2 = 0; colIndex2 < matrix2[0].length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = 0.0;
				for (int k = 0; k < matrix2.length; k++) {
					//System.out.println(matrix2[k][colIndex2]);
					newMatrix[rowIndex1][colIndex2] = newMatrix[rowIndex1][colIndex2] + matrix1[rowIndex1][k] * matrix2[k][colIndex2];
				}
			}
		}
		return newMatrix;
	}
	
	public static double[][] multiplyMatrixWithMatrixNewMatrix(double matrix1[][], long matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix2[0].length];
			
			for (int colIndex2 = 0; colIndex2 < matrix2[0].length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = 0.0;
				for (int k = 0; k < matrix2.length; k++) {
					//System.out.println(matrix2[k][colIndex2]);
					newMatrix[rowIndex1][colIndex2] = newMatrix[rowIndex1][colIndex2] + matrix1[rowIndex1][k] * matrix2[k][colIndex2];
				}
			}
		}
		return newMatrix;
	}
	
	public static double[][] sumTwoMatricesNewMatrix(double matrix1[][], double matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix1[0].length];
			
			for (int colIndex2 = 0; colIndex2 < matrix1[0].length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = matrix1[rowIndex1][colIndex2] + matrix2[rowIndex1][colIndex2];
			}
		}
		return newMatrix;
	}
	
	public static double[][] transposeMatrix(double matrix[][]) {
		double newMatrix[][] = new double[matrix[0].length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix[0].length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix.length];
		}
		
		
		for (int rowIndex1 = 0; rowIndex1 < matrix.length; rowIndex1++) {			
			for (int colIndex2 = 0; colIndex2 < matrix[0].length; colIndex2++) {
				newMatrix[colIndex2][rowIndex1] = matrix[rowIndex1][colIndex2];
			}
		}
		return newMatrix;
	}
	
	public static double[][] transposeMatrix(long matrix[][]) {
		double newMatrix[][] = new double[matrix[0].length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix[0].length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix.length];
		}
		
		
		for (int rowIndex1 = 0; rowIndex1 < matrix.length; rowIndex1++) {			
			for (int colIndex2 = 0; colIndex2 < matrix[0].length; colIndex2++) {
				newMatrix[colIndex2][rowIndex1] = matrix[rowIndex1][colIndex2];
			}
		}
		return newMatrix;
	}
	
	public static double calculateFrobeniusNorm(double matrix[][]) {
		double frobeniusNorm = 0.0;
		for (int rowIndex1 = 0; rowIndex1 < matrix.length; rowIndex1++) {
			for (int colIndex2 = 0; colIndex2 < matrix[0].length; colIndex2++) {
				frobeniusNorm = frobeniusNorm + Math.pow(matrix[rowIndex1][colIndex2], 2);
			}
		}
		return Math.sqrt(frobeniusNorm);
	}
	
	private double findMatrixColumnMaximumByAddingColumnMeans(double updateMatrix[][], double columnMeans[]) {
		double maximum = 0.0;
		double columnSize = 0.0;
		
		for (int colIndex = 0; colIndex < updateMatrix[0].length; colIndex++) {
			columnSize = 0.0;
			
			for (int rowIndex = 0; rowIndex < updateMatrix.length; rowIndex++) {
				columnSize = columnSize + Math.abs(updateMatrix[rowIndex][colIndex] + columnMeans[colIndex]);
			}
			if (columnSize > maximum) {
				maximum = columnSize;
			}
		}
		return maximum;
	}
	
	public static void divideMatrix(double[][] divideMatrix, double coefficient) {
		for (int rowIndex1 = 0; rowIndex1 < divideMatrix.length; rowIndex1++) {
			for (int colIndex2 = 0; colIndex2 < divideMatrix[0].length; colIndex2++) {
				divideMatrix[rowIndex1][colIndex2] = (divideMatrix[rowIndex1][colIndex2] + 0.0) / coefficient;
			}
		}
	}
	
	private static double[][] evaluateKroneckerProductOfMatrices(
			double matrix1[][], double matrix2[][], double updateFunctionMatrix[][]) {
		double[][] transposed1, transposed2;
		double[][] matrix2Updated, matrix2UpdatedMatrix1T, matrix2TUpdates, matrix2TUpdatesMatrix1;
		double resultUpdatedMetrix[][];
		double frobeniusNorm;
		matrix2Updated = multiplyMatrixWithMatrixNewMatrix(matrix2, updateFunctionMatrix);
		transposed1 = transposeMatrix(matrix1);
		matrix2UpdatedMatrix1T = multiplyMatrixWithMatrixNewMatrix(matrix2Updated, transposed1);
		
		
		transposed2 = transposeMatrix(matrix2);
		matrix2TUpdates = multiplyMatrixWithMatrixNewMatrix(transposed2, updateFunctionMatrix);
		matrix2TUpdatesMatrix1 = multiplyMatrixWithMatrixNewMatrix(matrix2TUpdates, matrix1);
		resultUpdatedMetrix = sumTwoMatricesNewMatrix(matrix2UpdatedMatrix1T, matrix2TUpdatesMatrix1);
		frobeniusNorm = calculateFrobeniusNorm(resultUpdatedMetrix);
		divideMatrix(resultUpdatedMetrix, frobeniusNorm);
		return resultUpdatedMetrix;
	}
	
	/**
	 * X_[>k] = B_[>S] * Y_[>k−1] * A_[<transp]_[>S] + B_[>T] * Y_[>k−1] * A_[<transp]_[>T]
	 * 
	 * @param matrix1SourceEdge - matrix A [>S] in equation above
	 * @param matrix2SourceEdge - matrix B [>S] in equation above
	 * @param matrix1TerminalEdge - matrix A [>T] in equation above
	 * @param matrix2TerminalEdge - matrix B [>T] in equation above 
	 * @param updateFunctionYMatrix - matrix Y in equation above
	 * @return matrix X as Kronecker product of matrices
	 */
	private static double[][] evaluateSumOfKroneckerProductsOfMatricesForX(
			long matrix1SourceEdge[][], long matrix2SourceEdge[][],
			long matrix1TerminusEdge[][], long matrix2TerminusEdge[][],
			double updateFunctionYMatrix[][]) {
		double[][] transposed1, transposed2;
		double[][] matrix2Updated, matrix2UpdatedMatrix1T, matrix2TUpdates, matrix2TUpdatesMatrix1;
		double resultUpdatedMetrix[][];
		double frobeniusNorm;
		matrix2Updated = multiplyMatrixWithMatrixNewMatrix(matrix2SourceEdge, updateFunctionYMatrix);
		transposed1 = transposeMatrix(matrix1SourceEdge);
		matrix2UpdatedMatrix1T = multiplyMatrixWithMatrixNewMatrix(matrix2Updated, transposed1);
		
		
		matrix2TUpdates = multiplyMatrixWithMatrixNewMatrix(matrix2TerminusEdge, updateFunctionYMatrix);
		transposed2 = transposeMatrix(matrix1TerminusEdge);
		matrix2TUpdatesMatrix1 = multiplyMatrixWithMatrixNewMatrix(matrix2TUpdates, transposed2);
		resultUpdatedMetrix = sumTwoMatricesNewMatrix(matrix2UpdatedMatrix1T, matrix2TUpdatesMatrix1);
		frobeniusNorm = calculateFrobeniusNorm(resultUpdatedMetrix);
		divideMatrix(resultUpdatedMetrix, frobeniusNorm);
		return resultUpdatedMetrix;
	}
	
	/**
	 * Y_[>k] = B_[<transp]_[>S] * X_[>k−1] * A_[>S] + B_[<transp]_[>T] * X_[>k−1] * A_[>T]
	 * 
	 * @param matrix1SourceEdge - matrix A [>S] in equation above
	 * @param matrix2SourceEdge - matrix B [>S] in equation above
	 * @param matrix1TerminalEdge - matrix A [>T] in equation above
	 * @param matrix2TerminalEdge - matrix B [>T] in equation above 
	 * @param updateFunctionXMatrix - matrix Y in equation above
	 * @return matrix Y as Kronecker product of matrices
	 */
	private static double[][] evaluateSumOfKroneckerProductsOfMatricesForY(
			long matrix1SourceEdge[][], long matrix2SourceEdge[][],
			long matrix1TerminusEdge[][], long matrix2TerminusEdge[][],
			double updateFunctionXMatrix[][]) {
		double[][] transposed1, transposed2;
		double[][] matrix2Updated, matrix2UpdatedMatrix1T, matrix2TUpdates, matrix2TUpdatesMatrix1;
		double resultUpdatedMetrix[][];
		double frobeniusNorm;
		transposed1 = transposeMatrix(matrix2SourceEdge);
		matrix2Updated = multiplyMatrixWithMatrixNewMatrix(transposed1, updateFunctionXMatrix);
		matrix2UpdatedMatrix1T = multiplyMatrixWithMatrixNewMatrix(matrix2Updated, matrix1SourceEdge);
		
		transposed2 = transposeMatrix(matrix2TerminusEdge);
		matrix2TUpdates = multiplyMatrixWithMatrixNewMatrix(transposed2, updateFunctionXMatrix);
		matrix2TUpdatesMatrix1 = multiplyMatrixWithMatrixNewMatrix(matrix2TUpdates, matrix1TerminusEdge);
		resultUpdatedMetrix = sumTwoMatricesNewMatrix(matrix2UpdatedMatrix1T, matrix2TUpdatesMatrix1);
		frobeniusNorm = calculateFrobeniusNorm(resultUpdatedMetrix);
		divideMatrix(resultUpdatedMetrix, frobeniusNorm);
		return resultUpdatedMetrix;
	}

	//The L1 norm is the maximum, absolute value of all matrix elements. When the L1 norm drops below 1e-16, convergence occurs.
	//https://stackoverflow.com/questions/38143932/how-to-find-when-a-matrix-converges-with-a-loop
	private void computeSimilarityMatricesPairIterationToConvergence(double updateXMatrix[][], double updateYMatrix[][]) {
		int iteration = 0;
		double treshold = 1e-16;
		double actualL1Value = Double.MIN_VALUE;
		double previousL1Value = Double.MIN_VALUE;
		double previous2L1Value = Double.MIN_VALUE;
		double actualL1ValueX, actualL1ValueY;
		
		double[][] newUpdatedXMatrix = null;
		double[][] newUpdatedYMatrix = null;
		
		while ((actualL1Value < previousL1Value + treshold && actualL1Value < previous2L1Value + treshold) || iteration % 2 != 0 || iteration < 1000) {
			System.out.println("Iteration " + Integer.toString(iteration));

			newUpdatedXMatrix = GraphsVertexEdgeSimilaritiesLong.evaluateSumOfKroneckerProductsOfMatricesForX(
					this.sourceEdgeMatrix1, this.sourceEdgeMatrix2, 
					this.terminusEdgeMatrix1, this.terminusEdgeMatrix2, updateYMatrix);
			newUpdatedYMatrix = GraphsVertexEdgeSimilaritiesLong.evaluateSumOfKroneckerProductsOfMatricesForY(
					this.sourceEdgeMatrix1, this.sourceEdgeMatrix2, this.terminusEdgeMatrix1, this.terminusEdgeMatrix2, updateXMatrix);
			
			if (iteration >= 2) {
				previous2L1Value = previousL1Value;
			}
			previousL1Value = actualL1Value;
			actualL1ValueX = this.findMatrixColumnMaximumByAddingColumnMeans(newUpdatedXMatrix, this.evaluateColumnMeans(newUpdatedXMatrix));
			actualL1ValueY = this.findMatrixColumnMaximumByAddingColumnMeans(newUpdatedYMatrix, this.evaluateColumnMeans(newUpdatedYMatrix));
			actualL1Value = actualL1ValueX + actualL1ValueY;
			iteration++;
			updateXMatrix = newUpdatedXMatrix;
			updateYMatrix = newUpdatedYMatrix;
		}
		System.out.println("Iteration to convergence: " + iteration);
		
		this.convergedMatrixX = updateXMatrix;
		this.convergedMatrixY = updateYMatrix;
	}
	
	private double[][] initializeOnesMatrix(int colSizeFirstMatrix, int rowSizeSecondMatrix) {
		double identityMatrix[][] = new double[rowSizeSecondMatrix][];
		for (int rowIndex = 0; rowIndex < rowSizeSecondMatrix; rowIndex++) {
			identityMatrix[rowIndex] = new double[colSizeFirstMatrix];
			for (int colIndex = 0; colIndex < colSizeFirstMatrix; colIndex++) {
				identityMatrix[rowIndex][colIndex] = 1.0;
			}
		}
		return identityMatrix;
	}
	
	private void printSimilarityMatrix(double[][] similarityMatrix) {
		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < similarityMatrix[0].length; columnIndex++) {
				System.out.print(similarityMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
}
