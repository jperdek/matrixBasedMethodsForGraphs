package linkSimilarity;


public class SimilarityVector {

	
	public SimilarityVector() {
	}
	
	
	/**
	 * Evaluates absolute value of vector = ||vec|| 
	 * 
	 * @param vector - vector which value should be evaluated
	 * @return absolute value of similarityVector
	 */
	private static double getAbsoluteVectorValue(double vector[]) {
		double absoluteColumnSimilarity = 0.0;
		
		for (int index = 0; index < vector.length; index++) {
			absoluteColumnSimilarity = absoluteColumnSimilarity + Math.pow(vector[index], 2.0);
		}
		return Math.sqrt(absoluteColumnSimilarity);
	}
	
	
	/**
	 * Evaluates similarity between two vectors
	 * = SUM k (vec1 k * vec2 k)
	 * 
	 * @param vector1 - vector 1
	 * @param vector2 - vector 2
	 * @return similarity value between two vectors
	 */
	private static double getPartialSimilarityValueOfTwoVectors(double vector1[], double vector2[]) {
		double twoRowsSimilarity = 0.0;
		
		for (int index = 0; index < vector1.length; index++) {
			twoRowsSimilarity = twoRowsSimilarity + vector1[index] * vector2[index];
		}
		return twoRowsSimilarity;
	}
	
	
	/**
	 * Evaluates partial similarity between two vectors
	 * SOMETIMES ZERO VALUES ARE POSSIBLE
	 * 
	 * 
	 * @param vector1 - vector 1
	 * @param vector2 - vector 2
	 * @return partial similarity between two vectors
	 */
	public static double getPartialLinkSimilarity(double vector1[], double vector2[]) {
		double absVector1 = SimilarityVector.getAbsoluteVectorValue(vector1);
		double absVector2 = SimilarityVector.getAbsoluteVectorValue(vector2);
		if (absVector1 == 0.0) {
			absVector1 = 1.0;
		}
		if (absVector2 == 0.0) {
			absVector2 = 1.0;
		}
		return SimilarityVector.getPartialSimilarityValueOfTwoVectors(vector1, vector2) / (absVector1 * absVector2);
	}

	
	/**
	 * Evaluates MOD i,j = ||col i|| + ||col j|| + ||row i|| + ||row j||
	 * 
	 * 
	 * @param vector11 - vector which belongs to rows from the first object
	 * @param vector11 - vector which belongs to rows from the second object
	 * @param vector11 - vector which belongs to columns from the first object
	 * @param vector11 - vector which belongs to columns from the second object
	 * @return MOD of i, j nodes
	 */
	private static double getMODofNodes(double vector11[], double vector12[], double vector21[], double vector22[]) {
		return SimilarityVector.getAbsoluteVectorValue(vector11) + 
			   SimilarityVector.getAbsoluteVectorValue(vector12) + 
			   SimilarityVector.getAbsoluteVectorValue(vector21) + 
			   SimilarityVector.getAbsoluteVectorValue(vector22);
	}
	
	
	/**
	 * Evaluates MOD of i, j nodes
	 * 
	 * @param rowPartialSum - value of ||row i|| + ||row j||
	 * @param columnPartialSum - value of ||col i|| + ||col j||
	 * @return rowPartialSum + columnPartialSum
	 */
	private static double getMODofNodes(double rowPartialSum, double columnPartialSum) {
		return rowPartialSum + columnPartialSum;
	}
	
	
	/**
	 * Evaluates value of ||vec i|| + ||vec j||
	 * 
	 * @param vector1 - index of the first vector
	 * @param vector2 - index of the second vector
	 * @return value of ||vec i|| + ||vec j||
	 */
	private static double getAbsoluteSimilarityPartialValuesSum(double vector1[], double vector2[]) {
		return SimilarityVector.getAbsoluteVectorValue(vector1) + 
			   SimilarityVector.getAbsoluteVectorValue(vector2);
	}

	
	/**
	 * Evaluates Alfa similarity coefficient: 
	 * (||row i|| + ||row j||) / (MOD i, j)
	 * 
	 * @param rowPartialSum - ||row i|| + ||row j||
	 * @param MOD - ||col i|| + ||col j|| + ||row i|| + ||row j||
	 * @return Alfa similarity coefficient value
	 */
	private static double getSimilarityAlfa(double rowPartialSum, double MOD) {
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
	private static double getSimilarityBeta(double columnPartialSum, double MOD) {
		return columnPartialSum / MOD;
	}
	
	
	/**
	 * Evaluates similarity between two vector pairs (sim([v1 ROW, v1 COL], [v2 ROW, v2 COL]))
	 * = Alfa v1 ROW, v2 ROW * sim OUT v1 ROW, v2 ROW + Beta v1 COL, v2 COL * sim IN v1 COL, v2 COL
	 * 
	 * @param vectorRow1 - row vector of the first node
	 * @param vectorColumn1 - column vector of the first node
	 * @param vectorRow2 - row vector of the second node
	 * @param vectorColumn2 - column vector of the second node
	 * @return similarity between two vector pairs (two nodes)
	 */
	public static double evaluateSimilarityBetweenTwoVectorPairs(
			double vectorRow1[], double vectorColumn1[], 
			double vectorRow2[], double vectorColumn2[]) {
		double columnPartialSum = SimilarityVector.getAbsoluteSimilarityPartialValuesSum(vectorColumn1, vectorColumn2);
		double rowPartialSum = SimilarityVector.getAbsoluteSimilarityPartialValuesSum(vectorRow1, vectorRow2);
		double MOD = SimilarityVector.getMODofNodes(rowPartialSum, columnPartialSum);
		// SOMETIMES IS THE CASE
		if (MOD == 0.0) { 
			MOD = 1.0; 
		}
		double alfaSimilarity = SimilarityVector.getSimilarityAlfa(rowPartialSum, MOD);
		double betaSimilarity = SimilarityVector.getSimilarityBeta(columnPartialSum, MOD);
		double similarityIN = SimilarityVector.getPartialLinkSimilarity(vectorColumn1, vectorColumn2);
		double similarityOUT = SimilarityVector.getPartialLinkSimilarity(vectorRow1, vectorRow2);
		
		return similarityOUT * alfaSimilarity + similarityIN * betaSimilarity;
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
