package linkSimilarity.clusteringAlgorithms;

import linkSimilarity.affinityOperations.BondEnergyAlgorithm;
import linkSimilarity.affinityOperations.BondMatrixPermutation;

public class MatrixPartition {

	public MatrixPartition() {
	}
	
	/**
	 * Evaluates value for assumed point D (x == y, its on diagonal)
	 * - this value is further maximized
	 * 
	 * @param index1 - parameter p, indexing to rows of matrix
	 * @param index2 - parameter q, indexing to columns of matrix
	 * @param similarityMatrix - similarity matrix which should be divided
	 * @param chosenMiddleDIndex - assumed index value of possible D point (x == y, its on diagonal)
	 * @return value of assumed point D
	 */
	public static double evaluatePartitioningMeasurement(int index1, int index2, double[][] similarityMatrix, int chosenMiddleDIndex) {
		int matrixSize = similarityMatrix.length;
		double pointDValue = 0.0;
		index1 = index1 + 1;
		index2 = index2 + 1;
		for (int rowIndex = (index1 - 1)*chosenMiddleDIndex + 1; 
				rowIndex < chosenMiddleDIndex + (index1 - 1)*(matrixSize - chosenMiddleDIndex); rowIndex++) {
			for(int columnIndex = (index2 - 1)*chosenMiddleDIndex + 1; 
					columnIndex < chosenMiddleDIndex + (index2 - 1)*(matrixSize - chosenMiddleDIndex); columnIndex++) {
				pointDValue = pointDValue + similarityMatrix[rowIndex][columnIndex];
			}
		}
		return pointDValue;
	}
	
	/**
	 * Evaluates location of middle point - its value should be maximized according given function
	 * 
	 * @param permutedSimilarityMatrix - original permuted similarity matrix to find divisible point D
	 * @return position of chosen D point (x == y, its on diagonal)
	 */
	public static int getMiddlePointLocation(double permutedSimilarityMatrix[][]) {
		int optimalPointLocation = (int) Math.floor(permutedSimilarityMatrix.length / 2.0);
		double maximizedValue = Double.NEGATIVE_INFINITY;
		double actualPointWholeValue;
		
		for (int pointLocation = 1; pointLocation < permutedSimilarityMatrix.length; pointLocation++) {
			actualPointWholeValue = MatrixPartition.evaluatePointDWholeValue(permutedSimilarityMatrix, pointLocation);
			if (actualPointWholeValue > maximizedValue) {
				maximizedValue = actualPointWholeValue;
				optimalPointLocation = pointLocation;
			}
		}
		
		return optimalPointLocation;
	}
	
	/**
	 * Creates upper left matrix from bigger matrix from previous iteration
	 * 
	 * @param matrix - permuted similarity matrix from previous iteration
	 * @param middlePointIndex - position of chosen D point (x == y, its diagonal)
	 * @return upper left matrix including chosen point D with its row/column
	 */
	public static double[][] getUpperLeftMatrix(double matrix[][], int middlePointIndex) {
		double matrixPart[][] = new double[middlePointIndex + 1][];
		
		for (int rowIndex = 0; rowIndex <= middlePointIndex; rowIndex++) {
			matrixPart[rowIndex] = new double[middlePointIndex + 1];
			for (int columnIndex = 0; columnIndex <= middlePointIndex; columnIndex++) {
				//if (columnIndex == matrix.length || rowIndex == matrix.length) { continue; }
				matrixPart[rowIndex][columnIndex] = matrix[rowIndex][columnIndex];
			}
		}
		
		return matrixPart;
	}
	
	/**
	 * Creates bottom right matrix from bigger matrix from previous iteration
	 * 
	 * @param matrix - permuted similarity matrix from previous iteration
	 * @param middlePointIndex - position of chosen D point (x == y, its diagonal)
	 * @return bottom right matrix including chosen point D with its row/column
	 */
	public static double[][] getBottomRightMatrix(double matrix[][], int middlePointIndex) {
		int newMatrixSize = matrix.length - middlePointIndex - 1;
		double matrixPart[][] = new double[newMatrixSize][];
		int originalMatrixRow, originalMatrixColumn;
		
		originalMatrixRow = middlePointIndex + 1;
		for (int rowIndex = 0; rowIndex < newMatrixSize; rowIndex++) {
			matrixPart[rowIndex] = new double[newMatrixSize];

			originalMatrixColumn = middlePointIndex + 1;
			for (int columnIndex = 0; columnIndex < newMatrixSize; columnIndex++) {
				matrixPart[rowIndex][columnIndex] = matrix[originalMatrixRow][originalMatrixColumn];
				originalMatrixColumn = originalMatrixColumn + 1;
			}
			originalMatrixRow = originalMatrixRow + 1;
		}
		
		return matrixPart;
	}

	/**
	 * Evaluates function (and all M(SM) parts) which should be maximized:
	 * F = M(SM 1,1) * M(SM 2,2) - M(SM 1,2) * M(SM 2,1) ~= M(SM 0,0) * M(SM 1,1) - M(SM 0,1) * M(SM 1,0)
	 * 
	 * @param similarityMatrix - matrix in which function should be maximized
	 * @param chosenMiddleDIndex - position of chosen D point (x == y, its diagonal)
	 * @return function F value
	 */
	public static double evaluatePointDWholeValue(double[][] similarityMatrix, int chosenMiddleDIndex) {
		double pointDValue11 = evaluatePartitioningMeasurement(0, 0, similarityMatrix, chosenMiddleDIndex);
		double pointDValue22 = evaluatePartitioningMeasurement(1, 1, similarityMatrix, chosenMiddleDIndex);
		double pointDValue12 = evaluatePartitioningMeasurement(0, 1, similarityMatrix, chosenMiddleDIndex);
		double pointDValue21 = evaluatePartitioningMeasurement(1, 0, similarityMatrix, chosenMiddleDIndex);
		
		return pointDValue11 * pointDValue22 - pointDValue12 * pointDValue21;
	}
	
	/**
	 * Evaluates cardinality of given matrix divided by chosen point
	 * 
	 * @param chosenDPointIndex - position of chosen D point (x == y, its diagonal)
	 * @param matrixLength - length of actually processed matrix
	 * @param topCorner11 - if we are processing top left corner
	 * @return
	 */
	public static int getCardinalityAccordingChosenDPoint(int chosenDPointIndex, int matrixLength, boolean topCorner11) {
		if (topCorner11) { // LEFT TOP CORNER/MATRIX
			//cardinality includes chosen pointD and all left matrix part
			return chosenDPointIndex + 1;
		} 
		// RIGHT BOTTOM CORNER/MATRIX
		//cardinality includes all right matrix part without pointD
		return (matrixLength - chosenDPointIndex - 1);
	}
	
	/**
	 * Associates variables (matrix columns/rows) with given clusters
	 * 
	 * @param cluster - cluster where data should be stored/associated
	 * @param nodesMapping - mapping columns/rows of repvious matrix with rows/columns of maximized permuted matrix
	 * @param chosenPoint - point chosen to divide parts into separated clusters
	 * @param previousChosenPoint - previous point chosen to divide parts into separated clusters
	 * @param upperPart - if upper part is processed
	 * @param previousUpperPart - if upper part was previously processed in previous recursion iteration
	 */
	public static void insertPreviousDocumentIndexes(NodeHierarchicCluster cluster, int nodesMapping[], 
			int chosenPoint, int previousChosenPoint, boolean upperPart, boolean previousUpperPart) {
		int newPositionIndex;
		
		// TOP LEFT PART (no movements are necessary, indexing starts on 0)
		if (upperPart) {
			//original variables are indexed from 0 (before permutation)
			//includes also chosen point D
			for (int previousIndex = 0; previousIndex <= chosenPoint; previousIndex++) {
				newPositionIndex = nodesMapping[previousIndex]; //obtains permuted value
				cluster.addNodeIndex(newPositionIndex); //saves it as cluster member
			}
		
		// BOTTOM RIGHT PART
		} else {
			//original variables are indexed from 0 (before permutation)
			//starts behind chosen point D and follows to the end of the matrix
			for (int previousIndex = chosenPoint + 1; previousIndex < nodesMapping.length; previousIndex++) {
				newPositionIndex = nodesMapping[previousIndex]; //obtains permuted value
				//if we previously not processed upper part
				if (!previousUpperPart) {
					//previous chosen point must be added to newPosition index because in this
					//iteration indexing starts from 0 (not from value of previousChosenPoint)
					newPositionIndex = newPositionIndex + previousChosenPoint;
				}
				cluster.addNodeIndex(newPositionIndex); //saves it as cluster member
			}
		}
	}

	
	/**
	 * Associates variables (matrix columns/rows) with given clusters
	 * 
	 * @param cluster - cluster where data should be stored/associated
	 * @param nodesMapping - mapping columns/rows of repvious matrix with rows/columns of maximized permuted matrix
	 * @param chosenPoint - point chosen to divide parts into separated clusters
	 * @param previousChosenPoint - previous point chosen to divide parts into separated clusters
	 * @param upperPart - if upper part is processed
	 * @param previousUpperPart - if upper part was previously processed in previous recursion iteration
	 */
	public static void insertPreviousDocumentIndexesIteration(NodeHierarchicCluster cluster, int nodesMapping[], 
			int chosenPoint, boolean upperPart) {
		int newPositionIndex;
		
		// TOP LEFT PART (no movements are necessary, indexing starts on 0)
		if (upperPart) {
			//original variables are indexed from 0 (before permutation)
			//includes also chosen point D
			for (int previousIndex = 0; previousIndex <= chosenPoint; previousIndex++) {
				newPositionIndex = nodesMapping[previousIndex]; //obtains permuted value
				cluster.addNodeIndex(newPositionIndex); //saves it as cluster member
			}
		
		// BOTTOM RIGHT PART
		} else {
			//original variables are indexed from 0 (before permutation)
			//starts behind chosen point D and follows to the end of the matrix
			for (int previousIndex = chosenPoint + 1; previousIndex < nodesMapping.length; previousIndex++) {
				newPositionIndex = nodesMapping[previousIndex]; //obtains permuted value
				cluster.addNodeIndex(newPositionIndex); //saves it as cluster member
			}
		}
	}
	

	/**
	 * Recursively divides matrix to clusters
	 * 
	 * @param processedCluster - parent cluster
	 * @param similarityMatrix - similarity matrix which should be divided
	 * @param preferredNumber - maximal number of cluster members
	 * @param previousPointD - previous chosen division point D (0 for the first iteration)
	 * @param previousTopLeft - if top left matrix part was processed in previous iteration
	 */
	public void hierarchicalMatrixPartitioning(NodeHierarchicCluster processedCluster, 
			double[][] similarityMatrix, int preferredNumber, int previousPointD, boolean previousTopLeft, int[] previousNodesMappingOriginal) {
		// similarityMatrix.length as length of m representing R (highest-ranked pages)
		BondMatrixPermutation bondMatrixPermutation =  BondEnergyAlgorithm.evaluateBondPermutationOptimizedMapping(similarityMatrix, previousNodesMappingOriginal);
		double permutedSimilarityMatrix[][] = bondMatrixPermutation.getResultMatrix();
		bondMatrixPermutation.getIndexMapping();
		int nodesMappingOriginal[] = bondMatrixPermutation.getOriginalIndexMapping();
		double[][] similarityMatrix11, similarityMatrix22;
		int chosenDPoint = MatrixPartition.getMiddlePointLocation(permutedSimilarityMatrix);
		int cardinalitySM11 = MatrixPartition.getCardinalityAccordingChosenDPoint(chosenDPoint, permutedSimilarityMatrix.length, true);
		int cardinalitySM22 = MatrixPartition.getCardinalityAccordingChosenDPoint(chosenDPoint, permutedSimilarityMatrix.length, false);

		NodeHierarchicCluster topCluster = processedCluster.createTopCluster();
		NodeHierarchicCluster bottomCluster = processedCluster.createBottomCluster();
		processedCluster.setRelativePointD(chosenDPoint);
		topCluster.setRelativePointD(chosenDPoint);
		bottomCluster.setRelativePointD(chosenDPoint);
		if (previousTopLeft) {
			processedCluster.setPointD(chosenDPoint);
			topCluster.setPointD(chosenDPoint);
			bottomCluster.setPointD(chosenDPoint);
		} else {
			processedCluster.setPointD(chosenDPoint + previousPointD);
			topCluster.setPointD(chosenDPoint + previousPointD);
			bottomCluster.setPointD(chosenDPoint + previousPointD);
		}
		
		//UPPER LEFT MATRIX PROCESSING
		if (cardinalitySM11 <= preferredNumber) {
			MatrixPartition.insertPreviousDocumentIndexesIteration(topCluster, nodesMappingOriginal, chosenDPoint, true);
		} else {
			similarityMatrix11 = MatrixPartition.getUpperLeftMatrix(permutedSimilarityMatrix, chosenDPoint);
			MatrixPartition.insertPreviousDocumentIndexesIteration(topCluster, nodesMappingOriginal, chosenDPoint, true);
			int newNodesMapping11[] = new int[similarityMatrix11.length];
			for (int index = 0; index <= chosenDPoint; index++) {
				newNodesMapping11[index] = nodesMappingOriginal[index];
			}
			this.hierarchicalMatrixPartitioning(topCluster, similarityMatrix11, preferredNumber, chosenDPoint, true, newNodesMapping11);
		}
		
		//BOTTOM RIGHT MATRIX PROCESSING
		if (cardinalitySM22 <= preferredNumber) {
			MatrixPartition.insertPreviousDocumentIndexesIteration(bottomCluster, nodesMappingOriginal, chosenDPoint, false);
		} else {
			similarityMatrix22 = MatrixPartition.getBottomRightMatrix(permutedSimilarityMatrix, chosenDPoint);
			MatrixPartition.insertPreviousDocumentIndexesIteration(bottomCluster, nodesMappingOriginal, chosenDPoint, false);
			int newNodesMapping22[] = new int[similarityMatrix22.length];
			for (int index = chosenDPoint + 1; index < permutedSimilarityMatrix.length; index++) {
				newNodesMapping22[index - 1 - chosenDPoint] = nodesMappingOriginal[index];
			}
			this.hierarchicalMatrixPartitioning(bottomCluster, similarityMatrix22, preferredNumber, chosenDPoint, false, newNodesMapping22);
		}
	}
	
	
	/**
	 * Recursively divides matrix to clusters - OLD NOT FULLY TESTED IF CONDITION WORKS
	 * 
	 * @param processedCluster - parent cluster
	 * @param similarityMatrix - similarity matrix which should be divided
	 * @param preferredNumber - maximal number of cluster members
	 * @param previousPointD - previous chosen division point D (0 for the first iteration)
	 * @param previousTopLeft - if top left matrix part was processed in previous iteration
	 */
	public void hierarchicalMatrixPartitioning22(NodeHierarchicCluster processedCluster, 
			double[][] similarityMatrix, int preferredNumber, int previousPointD, boolean previousTopLeft) {
		// similarityMatrix.length as length of m representing R (highest-ranked pages)
		BondMatrixPermutation bondMatrixPermutation =  BondEnergyAlgorithm.evaluateBondPermutationOptimizedMapping(similarityMatrix);
		double permutedSimilarityMatrix[][] = bondMatrixPermutation.getResultMatrix();
		int[] nodesMapping = bondMatrixPermutation.getIndexMapping();
		double[][] similarityMatrix11, similarityMatrix22;
		int chosenDPoint = MatrixPartition.getMiddlePointLocation(permutedSimilarityMatrix);
		int cardinalitySM11 = MatrixPartition.getCardinalityAccordingChosenDPoint(chosenDPoint, permutedSimilarityMatrix.length, true);
		int cardinalitySM22 = MatrixPartition.getCardinalityAccordingChosenDPoint(chosenDPoint, permutedSimilarityMatrix.length, false);
		
		processedCluster.setRelativePointD(chosenDPoint);
		if (previousTopLeft) {
			processedCluster.setPointD(chosenDPoint);
		} else {
			processedCluster.setPointD(chosenDPoint + previousPointD);
		}
		
		NodeHierarchicCluster topCluster = processedCluster.createTopCluster();
		if (cardinalitySM11 <= preferredNumber) {
			MatrixPartition.insertPreviousDocumentIndexes(topCluster, nodesMapping, chosenDPoint, previousPointD, true, previousTopLeft);
		} else {
			similarityMatrix11 = MatrixPartition.getUpperLeftMatrix(permutedSimilarityMatrix, chosenDPoint);
			MatrixPartition.insertPreviousDocumentIndexes(topCluster, nodesMapping, chosenDPoint, previousPointD, true, previousTopLeft);
			this.hierarchicalMatrixPartitioning22(topCluster, similarityMatrix11, preferredNumber, chosenDPoint, true);
		}
		
		NodeHierarchicCluster bottomCluster = processedCluster.createBottomCluster();
		if (cardinalitySM22 <= preferredNumber) {
			MatrixPartition.insertPreviousDocumentIndexes(bottomCluster, nodesMapping, chosenDPoint, previousPointD, false, previousTopLeft);
		} else {
			similarityMatrix22 = MatrixPartition.getBottomRightMatrix(permutedSimilarityMatrix, chosenDPoint);
			MatrixPartition.insertPreviousDocumentIndexes(bottomCluster, nodesMapping, chosenDPoint, previousPointD, false, previousTopLeft);
			this.hierarchicalMatrixPartitioning22(bottomCluster, similarityMatrix22, preferredNumber, chosenDPoint, false);
		}
		
		
		//ONLY FOR REFERENCE
		//this.insertPreviousDocumentIndexes(processedCluster, nodesMapping, chosenDPoint, previousPointD, true, previousTopLeft);
		//this.insertPreviousDocumentIndexes(processedCluster, nodesMapping, chosenDPoint, previousPointD, false, previousTopLeft);	
	}


	/**
	 * To print matrix values
	 * 
	 * @param matrix - matrix which should be printed
	 */
	private static void printMatrix(double matrix[][]) {
		System.out.println();
		for (int rowIndex=0; rowIndex < matrix.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < matrix.length; columnIndex++) {
				System.out.print(matrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
}
