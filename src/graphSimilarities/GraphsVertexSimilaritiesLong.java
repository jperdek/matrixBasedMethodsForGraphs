package graphSimilarities;

public class GraphsVertexSimilaritiesLong {

	private long matrix1[][];
	private long matrix2[][];
	
	public GraphsVertexSimilaritiesLong(long matrix1[][], long matrix2[][]) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
	}
	
	public double[][] computeSimilarityMatrices() {
		double initialMatrix[][] = this.initializeOnesMatrix(this.matrix1[0].length, this.matrix2.length);
		double resultMatrix[][] = this.computeSimilarityMatricesPairIterationToConvergence(initialMatrix);
		return resultMatrix;
	}
	
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
	
	public static double[][] multiplyMatrixWithTransposedMatrixNewMatrix(double matrix1[][], double matrix2Transposed[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix1.length];
			
			for (int rowIndex2 = 0; rowIndex2 < matrix1.length; rowIndex2++) {
				newMatrix[rowIndex1][rowIndex2] = 0.0;
				for (int k = 0; k < matrix1.length; k++) {
					newMatrix[rowIndex1][rowIndex2] = newMatrix[rowIndex1][rowIndex2] + matrix1[rowIndex1][k] * matrix2Transposed[rowIndex2][k];
				}
			}
		}
		return newMatrix;
	}
	
	public static double[][] multiplyMatrixWithMatrixNewMatrixSizeN(double matrix1[][], double matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix1.length];
			
			for (int colIndex2 = 0; colIndex2 < matrix1.length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = 0.0;
				for (int k = 0; k < matrix1.length; k++) {
					newMatrix[rowIndex1][colIndex2] = newMatrix[rowIndex1][colIndex2] + matrix1[rowIndex1][k] * matrix2[k][colIndex2];
				}
			}
		}
		return newMatrix;
	}
	
	public static double[][] multiplyMatrixWithMatrixNewMatrix(double matrix1[][], double matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix2[0].length];
			
			for (int colIndex2 = 0; colIndex2 < matrix2[0].length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = 0.0;
				for (int k = 0; k < matrix2.length; k++) {
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
					newMatrix[rowIndex1][colIndex2] = newMatrix[rowIndex1][colIndex2] + matrix1[rowIndex1][k] * matrix2[k][colIndex2];
				}
			}
		}
		return newMatrix;
	}
	
	public static double[][] multiplyMatrixTransposedWithMatrixNewMatrix(long matrix1[][], double matrix2[][]) {
		double newMatrix[][] = new double[matrix1.length][];
		for (int rowIndex1 = 0; rowIndex1 < matrix1.length; rowIndex1++) {
			newMatrix[rowIndex1] = new double[matrix1.length];
			
			for (int colIndex2 = 0; colIndex2 < matrix1.length; colIndex2++) {
				newMatrix[rowIndex1][colIndex2] = 0.0;
				for (int k = 0; k < matrix1.length; k++) {
					newMatrix[rowIndex1][colIndex2] = newMatrix[rowIndex1][colIndex2] + matrix1[k][rowIndex1] * matrix2[k][colIndex2];
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
	
	//The L1 norm is the maximum, absolute value of all matrix elements. When the L1 norm drops below 1e-16, convergence occurs.
	//https://stackoverflow.com/questions/38143932/how-to-find-when-a-matrix-converges-with-a-loop
	private double[][] computeSimilarityMatricesPairIterationToConvergence(double updateMatrix[][]) {
		int iteration = 0;
		double treshold = 1e-16;
		double actualL1Value = Double.MIN_VALUE;
		double previousL1Value = Double.MIN_VALUE;
		double previous2L1Value = Double.MIN_VALUE;
		
		double frobeniusNorm;
		double[][] matrix2Updated, matrix2UpdatedMatrix1T, matrix2TUpdates, matrix2TUpdatesMatrix1;
		double[][] newUpdatedMatrix = null;
		double[][] transposed1, transposed2;
		
		while ((actualL1Value < previousL1Value + treshold && actualL1Value < previous2L1Value + treshold) || iteration % 2 != 0 || iteration < 5000) {
			System.out.println("Iteration " + Integer.toString(iteration));
			matrix2Updated = multiplyMatrixWithMatrixNewMatrix(this.matrix2, updateMatrix);
			transposed1 = transposeMatrix(this.matrix1);
			matrix2UpdatedMatrix1T = multiplyMatrixWithMatrixNewMatrix(matrix2Updated, transposed1);
			//matrix2UpdatedMatrix1T = multiplyMatrixWithTransposedMatrixNewMatrix(matrix2Updated, this.matrix1);
			
			transposed2 = transposeMatrix(this.matrix2);
			matrix2TUpdates = multiplyMatrixWithMatrixNewMatrix(transposed2, updateMatrix);
			//matrix2TUpdates = multiplyMatrixTransposedWithMatrixNewMatrix(this.matrix2, updateMatrix);
			matrix2TUpdatesMatrix1 = multiplyMatrixWithMatrixNewMatrix(matrix2TUpdates, this.matrix1);
			newUpdatedMatrix = sumTwoMatricesNewMatrix(matrix2UpdatedMatrix1T, matrix2TUpdatesMatrix1);
			frobeniusNorm = calculateFrobeniusNorm(newUpdatedMatrix);
			divideMatrix(newUpdatedMatrix, frobeniusNorm);
			
			if (iteration >= 2) {
				previous2L1Value = previousL1Value;
			}
			previousL1Value = actualL1Value;
			actualL1Value = this.findMatrixColumnMaximumByAddingColumnMeans(newUpdatedMatrix, this.evaluateColumnMeans(newUpdatedMatrix));
			iteration++;
			updateMatrix = newUpdatedMatrix;
			
		}
		System.out.println("Iteration to convergence: " + iteration);
		return newUpdatedMatrix;
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
}
