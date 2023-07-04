package linkSimilarity.overlappingClusters;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import linkSimilarity.MatrixDeterminant;
import linkSimilarity.SimilarityVector;
import linkSimilarity.clusteringAlgorithms.MatrixPartition;


public class ExtendingAlgorithm {

	public ExtendingAlgorithm() {
	}

	public static double[] evaluateCentroidsRowVector(double[][] similarityMatrix, 
			int diagonalRowIndexP, double matrixDeterminantValue) {
		double rowSum;
		double rowVector[] = new double[similarityMatrix.length];
		
		if (matrixDeterminantValue == 0.0 || Double.isNaN(matrixDeterminantValue)) {
			matrixDeterminantValue = 1.0;
		}

		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			rowSum = 0.0;
			for (int columnIndex = 0; columnIndex < similarityMatrix.length; columnIndex++) {
				rowSum = rowSum + similarityMatrix[rowIndex][columnIndex];
			}
			rowVector[rowIndex] = (rowSum + 0.0) / matrixDeterminantValue;
		}
		return rowVector;
	}
	
	public static double[] evaluateCentroidsColumnVector(double[][] similarityMatrix,
			int diagonalColumnIndexP, double matrixDeterminantValue) {
		double columnSum = 0.0;
		double columnVector[] = new double[similarityMatrix.length];
		
		if (matrixDeterminantValue == 0.0 || Double.isNaN(matrixDeterminantValue)) {
			matrixDeterminantValue = 1.0;
		}
		
		for (int columnIndex = 0; columnIndex < similarityMatrix.length; columnIndex++) {
			columnSum = 0.0;
			for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
				columnSum = columnSum + similarityMatrix[rowIndex][columnIndex];
			}
			columnVector[columnIndex] = (columnSum + 0.0) / matrixDeterminantValue;
		}
		return columnVector;
	}

	public static double calculateTresholdFromUpperRightMatrix(double similarityMatrix[][], int chosenPointD) {
		int nonZeroValues = 0;
		double sumNonZeroValues = 0.0;
		
		for (int rowIndex = 0; rowIndex <= chosenPointD; rowIndex++) {
			for (int colIndex = chosenPointD + 1; colIndex < similarityMatrix.length; colIndex++) {
				if (similarityMatrix[rowIndex][colIndex] != 0.0) {
					nonZeroValues = nonZeroValues + 1;
					sumNonZeroValues = sumNonZeroValues + similarityMatrix[rowIndex][colIndex];
				}
			}
		}
		if (nonZeroValues == 0) {
			return 0;
		}
		if (nonZeroValues == 0) {
			nonZeroValues = 1;
		}
		return (sumNonZeroValues + 0.0) / nonZeroValues;
	}

	public static Set<Integer> constructSetP(double similarityMatrix[][], int chosenPointD) {
		Set<Integer> pSet = new HashSet<Integer>();
		boolean shouldBeIncluded;
		
		for (int rowIndex = 0; rowIndex <= chosenPointD; rowIndex++) {
			shouldBeIncluded = false;
			for (int colIndex = chosenPointD + 1; colIndex < similarityMatrix.length; colIndex++) {
				if (similarityMatrix[rowIndex][colIndex] != 0) {  // sm p, j != 0
					shouldBeIncluded = true;
					break;
				}
			}
			if (shouldBeIncluded) {
				pSet.add(rowIndex); //rowIndex == q
			}
		}
		return pSet;
	}
	
	/**
	 * 
	 * @param similarityMatrix
	 * @param chosenPointD
	 * @return
	 */
	public static Set<Integer> constructSetQ(double similarityMatrix[][], int chosenPointD) {
		Set<Integer> qSet = new HashSet<Integer>();
		boolean shouldBeIncluded;
		
		for (int colIndex = chosenPointD + 1; colIndex < similarityMatrix.length; colIndex++) {
			shouldBeIncluded = false;
			for (int rowIndex = 0; rowIndex <= chosenPointD; rowIndex++) {
				if (similarityMatrix[rowIndex][colIndex] != 0) { // sm i,q != 0
					shouldBeIncluded = true;
					break;
				}
			}
			if (shouldBeIncluded) {
				qSet.add(colIndex); //colIndex == q
			}
		}
		return qSet;
	}
	
	/**
	 * Chooses up to N nodes from the Set
	 * 
	 * @param set- the given set from which N nodes should be chosen
	 * @param treshold - treshold to accept given node
	 * @param matrix - smaller unextended matrix which will be extended in the end of iteration
	 * @param chosenPointD - chosen point D which divides previous matrix to unextended parts
	 * @param centroidRowVector - centroid representation of columns in smaller (evaluated [matrix]) matrix
	 * @param centroidColumnVector - centroid representation of rows in smaller (evaluated [matrix]) matrix
	 * @param upToChosenN - maximal number of nodes which should be chosen 
	 * @return
	 */
	public static Set<Integer> chooseNNodesFromSet(Set<Integer> set, double treshold, 
			double matrix[][], int chosenPointD,
			double centroidRowVector[], double centroidColumnVector[], int upToChosenN) {
		Map<Integer, Double> chosenNodes = new HashMap<Integer, Double>();
		Iterator<Integer> chosenNodesSetIterator = set.iterator();
		int chosenPageNumber, chosenPageNumberSmallMatrix;
		double similarityOfNodes;
		double vectorRowNode[], vectorColNode[];
	
		// FOR EACH NODE INDEX CHOOSE THE MOST SIMILAR ONES (UP TO N)
		while(chosenNodesSetIterator.hasNext()) {
			chosenPageNumber = chosenPageNumberSmallMatrix = chosenNodesSetIterator.next();
			
			//CONVERTION TO PARTIAL MATRIX SIZE - only to work with small matrix
			// example: 0, 1, 2, [3],| 4, 5  --->  0->0; 1->1; 2->2; 3->3; 4->0; 5->1 
			//([3] is chosen point D)
			if (chosenPageNumber > chosenPointD) {
				chosenPageNumberSmallMatrix = chosenPageNumber - chosenPointD - 1;
			}

			//getting vectors of given node for further comparisons
			vectorRowNode = matrix[chosenPageNumberSmallMatrix];
			vectorColNode = createVectorFromMatrixColumn(matrix, chosenPageNumberSmallMatrix);
			similarityOfNodes = SimilarityVector.evaluateSimilarityBetweenTwoVectorPairs(
					vectorRowNode, vectorColNode, centroidRowVector, centroidColumnVector);
			
			//System.out.println(similarityOfNodes);
			// DECIDING IF GIVEN NODE (INDEX) SHOULD BE ACCEPTED
			if (similarityOfNodes >= treshold) {
				chosenNodes.put(chosenPageNumber, similarityOfNodes);
			}
		}
		
		//CONVERT TO SET OF NODE INDEXES
		Set<Integer> topN = chosenNodes.entrySet().stream()
		   .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 	//sort descending
		   .limit(upToChosenN)	 			// getting up to N values
		   .map(Map.Entry::getKey) 			// getting all node indexes
		   .collect(Collectors.toSet());	// creting set from the keys
		return topN;
	}
	
	/**
	 * Creates vector from values stored in column (not accessible by normal indexing in java)
	 * 
	 * @param matrix - matrix which values should be extracted from the given column
	 * @param columnIndex - given column index which values should be extracted for each row
	 * @return vector of row values extracted from given row
	 */
	private static double[] createVectorFromMatrixColumn(double matrix[][], int columnIndex) {
		double vector[] = new double[matrix.length];
		
		for (int index = 0; index < matrix.length; index++) {
			vector[index] = matrix[index][columnIndex];
		}
		return vector;
	}

	/**
	 * Extends given matrix by adding additional columns and its values to fill full matrix
	 * 
	 * @param startIndex - start index of native matrix which should be fully included (always will be included)
	 * @param endIndex -end index of native matrix which should be fully included (never will be included - the last one is lastIndex - 1) 
	 * @param similarityMatrix - whole matrix which is not extended or divided by chosen point D
	 * @param nodesIndexes - the set of chosen node indexes from the not fully included matrix which should be included
	 * @return extended matrix from the first fully included one and second consisting with chosen nodes (rows and columns)
	 */
	public static double[][] extendPartitionedMatrix(int startIndex, int endIndex, 
			double similarityMatrix[][], Set<Integer> nodesIndexes) {
		int extendedSize = (endIndex - startIndex) + nodesIndexes.size();
		double extendedMatrix[][] = new double[extendedSize][];
		int originalMatrixRowIndex, originalMatrixColumnIndex;
		Iterator<Integer> nodeIndexesIterator, nodeIndexesIterator2;
		
		// ALLOCATE MATRIX
		for (int rowIndex = 0; rowIndex < extendedSize; rowIndex++) {
			extendedMatrix[rowIndex] = new double[extendedSize];
		}
		
		// COPY ORIGINAL MATRIX PART
		int columnIndex = 0;
		int rowIndex = 0;
		for (originalMatrixRowIndex = startIndex; originalMatrixRowIndex < endIndex; originalMatrixRowIndex++) {
			
			// COPY ORIGINAL MATRIX PART - COLUMNS
			columnIndex = 0;
			for (originalMatrixColumnIndex = startIndex; originalMatrixColumnIndex < endIndex; originalMatrixColumnIndex++) {
				extendedMatrix[rowIndex][columnIndex] = similarityMatrix[originalMatrixRowIndex][originalMatrixColumnIndex];
				columnIndex++;
			}
			
			// COPY ORIGINAL MATRIX PART - EXTENDED COLUMNS
			nodeIndexesIterator = nodesIndexes.iterator();
			while(nodeIndexesIterator.hasNext()) {
				originalMatrixColumnIndex = nodeIndexesIterator.next();
				extendedMatrix[rowIndex][columnIndex] = similarityMatrix[originalMatrixRowIndex][originalMatrixColumnIndex];
				columnIndex++;
			}
			rowIndex++;
		}
		
		// COPY EXTENDED MATRIX PARTS
		nodeIndexesIterator = nodesIndexes.iterator();
		while(nodeIndexesIterator.hasNext()) {
			originalMatrixRowIndex = nodeIndexesIterator.next();
			
			// COPY EXTENDED ORIGINAL MATRIX PART - COLUMNS
			columnIndex = 0;
			for (originalMatrixColumnIndex = startIndex; originalMatrixColumnIndex < endIndex; originalMatrixColumnIndex++) {
				extendedMatrix[rowIndex][columnIndex] = similarityMatrix[originalMatrixRowIndex][originalMatrixColumnIndex];
				columnIndex++;
			}
			
			nodeIndexesIterator2 = nodesIndexes.iterator();
			while(nodeIndexesIterator2.hasNext()) {
				originalMatrixColumnIndex = nodeIndexesIterator2.next();
				extendedMatrix[rowIndex][columnIndex] = similarityMatrix[originalMatrixRowIndex][originalMatrixColumnIndex];
				columnIndex++;
			}
			rowIndex++;
		}

		return extendedMatrix;
	}
	
	/**
	 * Extends the top left matrix
	 * 	- centroid vectors from the top left matrix are used here
	 *  - by constructing Q set (from the bottom right matrix)
	 *  - choosing up to N values from this Q set
	 *  
	 * @param upperLeftMatrix
	 * @param upToChosenN - number of the most valuable pages which should be moved to extended SM 11 from SM 22
	 * @param chosenPointD - chosen point D to divide matrixes before their extension
	 * @param matrixDeterminantValue11 - for evaluation of centroid for creation Q set
	 * @param similarityMatrix - similarity matrix which parts should be extended
	 * @param treshold - treshold to choose node (index)from matrix
	 * @return
	 */
	public static double[][] createExtendedUpperLeftMatrix(double bottomRightMatrix[][], int upToChosenN, int chosenPointD, 
			double matrixDeterminantValue11, double similarityMatrix[][], double treshold) {
		double centroinRowVector11[] = ExtendingAlgorithm.evaluateCentroidsRowVector(bottomRightMatrix, chosenPointD, matrixDeterminantValue11);
		double centroinColumnVector11[] = ExtendingAlgorithm.evaluateCentroidsColumnVector(bottomRightMatrix, chosenPointD, matrixDeterminantValue11);
		Set<Integer> setQ = ExtendingAlgorithm.constructSetQ(similarityMatrix, chosenPointD);
		Set<Integer> chosenNodes = ExtendingAlgorithm.chooseNNodesFromSet(setQ, treshold, bottomRightMatrix, chosenPointD, 
				centroinRowVector11, centroinColumnVector11, upToChosenN);
		
		// THE TOP LEFT MATRIX IS EXTENDED
		//0 up to chosen Point - bounds SM 1,1
		double extendedMatrix11[][] = ExtendingAlgorithm.extendPartitionedMatrix(0, chosenPointD + 1, similarityMatrix, chosenNodes);
		return extendedMatrix11;
	}
	
	/**
	 * Extends the bottom right matrix 
	 *	- centroid vectors from the bottom right matrix are used here
	 *  - by constructing P set (from the upper left matrix)
	 *  - choosing up to N values from this P set
	 * 
	 * @param bottomRightMatrix
	 * @param upToChosenN - number of the most valuable pages which should be moved to extended SM 22 from SM 11
	 * @param chosenPointD - chosen point D to divide matrixes before their extension
	 * @param matrixDeterminantValue22 - for evaluation of centroid for creation P set
	 * @param similarityMatrix - similarity matrix which parts should be extended
	 * @param treshold - treshold to choose node (index)from matrix
	 * @return
	 */
	public static double[][] createExtendedBottomRightMatrix(double upperLeftMatrix[][], int upToChosenN, int chosenPointD, 
			double matrixDeterminantValue22, double similarityMatrix[][], double treshold) {
		double centroinRowVector22[] = evaluateCentroidsRowVector(upperLeftMatrix, chosenPointD, matrixDeterminantValue22);
		double centroinColumnVector22[] = evaluateCentroidsColumnVector(upperLeftMatrix, chosenPointD, matrixDeterminantValue22);
		
		Set<Integer> setP = constructSetP(similarityMatrix, chosenPointD);
		Set<Integer> chosenNodes = chooseNNodesFromSet(setP, treshold, upperLeftMatrix, chosenPointD, centroinRowVector22, centroinColumnVector22, upToChosenN);
		
		// THE BOTTOM RIGHT MATRIX IS EXTENDED
		//chosen up to whole matrix length - bounds SM 2, 2
		double extendedMatrix22[][] = ExtendingAlgorithm.extendPartitionedMatrix(chosenPointD + 1, similarityMatrix.length, similarityMatrix, chosenNodes);
		return extendedMatrix22;
	}
	
	/**
	 * Extends chosen matrixes (upper left and bottom right) about similar parts across each matrix
	 * 
	 * @param similarityMatrix - whole similarity matrix from which two parts will be extended
	 * @param chosenPointD - chosen point D which divides matrix to four parts (before each part is extended)
	 * @return NewClusteredMatrixes containing both extended matrices
	 */
	public static NewClusteredMatrixes clusterOverlappingClustersWithoutLabels(double[][] similarityMatrix, int chosenPointD) {
		double upperLeftMatrix[][] = MatrixPartition.getUpperLeftMatrix(similarityMatrix, chosenPointD);
		double bottomRightMatrix[][] = MatrixPartition.getBottomRightMatrix(similarityMatrix, chosenPointD);
		
		double matrixDeterminantValue11 = MatrixDeterminant.determinantOfMatrix(upperLeftMatrix, upperLeftMatrix.length);
		double matrixDeterminantValue22 = MatrixDeterminant.determinantOfMatrix(bottomRightMatrix, bottomRightMatrix.length);
		double N1 = matrixDeterminantValue11 * 0.15;
		double N2 = matrixDeterminantValue22 * 0.15;
		int N = (int) Math.min(N1, N2); //the maximal number of extended parts for each matrix
		
		double treshold = ExtendingAlgorithm.calculateTresholdFromUpperRightMatrix(similarityMatrix, chosenPointD);
		double extendedMatrix11[][] = ExtendingAlgorithm.createExtendedBottomRightMatrix(upperLeftMatrix, N, chosenPointD, matrixDeterminantValue22, similarityMatrix, treshold);
		double extendedMatrix22[][] = ExtendingAlgorithm.createExtendedUpperLeftMatrix(bottomRightMatrix, N, chosenPointD, matrixDeterminantValue11, similarityMatrix, treshold);
				
		NewClusteredMatrixes newClusteredMatrixes = new NewClusteredMatrixes(extendedMatrix11, extendedMatrix22);
		return newClusteredMatrixes;
	}
	
	
	/**
	 * Selects nodes for the top left matrix (from the bototm right) 
	 * 	- centroid vectors from the top left matrix are used here
	 *  - by constructing Q set (from the bottom right matrix)
	 *  - choosing up to N values from this Q set
	 *  
	 * @param upperLeftMatrix
	 * @param upToChosenN - number of the most valuable pages which should be moved to extended SM 11 from SM 22
	 * @param chosenPointD - chosen point D to divide matrixes before their extension
	 * @param matrixDeterminantValue11 - for evaluation of centroid for creation Q set
	 * @param similarityMatrix - similarity matrix which parts should be extended
	 * @param treshold - treshold to choose node (index)from matrix
	 * @return
	 */
	public static Set<Integer> selectChosenNodesForUpperLeftMatrix(double bottomRightMatrix[][], int upToChosenN, int chosenPointD, 
			double matrixDeterminantValue11, double similarityMatrix[][], double treshold) {
		double centroinRowVector11[] = ExtendingAlgorithm.evaluateCentroidsRowVector(bottomRightMatrix, chosenPointD, matrixDeterminantValue11);
		double centroinColumnVector11[] = ExtendingAlgorithm.evaluateCentroidsColumnVector(bottomRightMatrix, chosenPointD, matrixDeterminantValue11);
		Set<Integer> setQ = ExtendingAlgorithm.constructSetQ(similarityMatrix, chosenPointD);
		return ExtendingAlgorithm.chooseNNodesFromSet(setQ, treshold, bottomRightMatrix, chosenPointD, 
				centroinRowVector11, centroinColumnVector11, upToChosenN);
	}
	
	/**
	 * Selects nodes for the bottom right matrix (from the upper left) 
	 *	- centroid vectors from the bottom right matrix are used here
	 *  - by constructing P set (from the upper left matrix)
	 *  - choosing up to N values from this P set
	 * 
	 * @param bottomRightMatrix
	 * @param upToChosenN - number of the most valuable pages which should be moved to extended SM 22 from SM 11
	 * @param chosenPointD - chosen point D to divide matrixes before their extension
	 * @param matrixDeterminantValue22 - for evaluation of centroid for creation P set
	 * @param similarityMatrix - similarity matrix which parts should be extended
	 * @param treshold - treshold to choose node (index)from matrix
	 * @return
	 */
	public static Set<Integer> selectChosenNodesForBottomRightMatrix(double upperLeftMatrix[][], int upToChosenN, int chosenPointD, 
			double matrixDeterminantValue22, double similarityMatrix[][], double treshold) {
		double centroinRowVector22[] = evaluateCentroidsRowVector(upperLeftMatrix, chosenPointD, matrixDeterminantValue22);
		double centroinColumnVector22[] = evaluateCentroidsColumnVector(upperLeftMatrix, chosenPointD, matrixDeterminantValue22);
		
		Set<Integer> setP = constructSetP(similarityMatrix, chosenPointD);
		return ExtendingAlgorithm.chooseNNodesFromSet(setP, treshold, upperLeftMatrix, chosenPointD, 
				centroinRowVector22, centroinColumnVector22, upToChosenN);
	}
	
	
	/**
	 * Extends chosen matrixes (upper left and bottom right) about similar parts across each matrix + putting duplicates
	 * 
	 * @param similarityMatrix - whole similarity matrix from which two parts will be extended
	 * @param chosenPointD - chosen point D which divides matrix to four parts (before each part is extended)
	 * @return NewClusteredMatrixes containing both extended matrices
	 */
	public static NewClusteredMatrixes clusterOverlappingClusters(double[][] similarityMatrix, int chosenPointD) {
		double upperLeftMatrix[][] = MatrixPartition.getUpperLeftMatrix(similarityMatrix, chosenPointD);
		double bottomRightMatrix[][] = MatrixPartition.getBottomRightMatrix(similarityMatrix, chosenPointD);
		
		double matrixDeterminantValue11 = MatrixDeterminant.determinantOfMatrix(upperLeftMatrix, upperLeftMatrix.length);
		double matrixDeterminantValue22 = MatrixDeterminant.determinantOfMatrix(bottomRightMatrix, bottomRightMatrix.length);
		if (Double.isNaN(matrixDeterminantValue11)) {
			matrixDeterminantValue11 = 1.0;
		}
		if (Double.isNaN(matrixDeterminantValue22)) {
			matrixDeterminantValue22 = 1.0;
		}
		double N1 = matrixDeterminantValue11 * 0.15;
		double N2 = matrixDeterminantValue22 * 0.15;
		int N = (int) Math.ceil(Math.min(N1, N2)); //the maximal number of extended parts for each matrix
		double treshold = ExtendingAlgorithm.calculateTresholdFromUpperRightMatrix(similarityMatrix, chosenPointD) / 20;
		Set<Integer> chosenNodesFromTopLeftToExtendBottomRight = ExtendingAlgorithm.selectChosenNodesForBottomRightMatrix(upperLeftMatrix, N, chosenPointD, matrixDeterminantValue22, similarityMatrix, treshold);
		
		
		Set<Integer> chosenNodesFromBottomRightToExtendUpperLeft = 
				ExtendingAlgorithm.selectChosenNodesForUpperLeftMatrix(bottomRightMatrix, N, chosenPointD, matrixDeterminantValue11, similarityMatrix, treshold);
		
		// THE BOTTOM RIGHT MATRIX IS EXTENDED
		//chosen up to whole matrix length - bounds SM 2, 2        BOTTOM RIGHT [chosenPointD + 1, chosenPointD + 1] -> [similarityMatrix.length, similarityMatrix.length]
		double extendedMatrixBottomRight[][] = 
				ExtendingAlgorithm.extendPartitionedMatrix(chosenPointD + 1, similarityMatrix.length, 
						similarityMatrix, chosenNodesFromTopLeftToExtendBottomRight);
		// THE TOP LEFT MATRIX IS EXTENDED
		//0 up to chosen Point - bounds SM 1,1					  TOP LEFT [0, 0] -> [chosenPointD, chosenPointD]
		double extendedMatrixTopLeft[][] = 
				ExtendingAlgorithm.extendPartitionedMatrix(0, chosenPointD + 1, 
						similarityMatrix, chosenNodesFromBottomRightToExtendUpperLeft);
		
		//printSet(chosenNodesFromBottomRightToExtendUpperLeft, "FROM BOTTOM RIGHT TO EXTEND UPPER LEFT");
		//printSet(chosenNodesFromTopLeftToExtendBottomRight, "FROM UPPER LETF TO EXTEND BOTTOM RIGHT");
		NewClusteredMatrixes newClusteredMatrixes = new NewClusteredMatrixes(extendedMatrixTopLeft, extendedMatrixBottomRight);
		newClusteredMatrixes.setUpperLeftExtensionIndexes(chosenNodesFromBottomRightToExtendUpperLeft);
		newClusteredMatrixes.setBottomRightExtensionIndexes(chosenNodesFromTopLeftToExtendBottomRight);
		
		return newClusteredMatrixes;
	}
	
	private static void printSet(Set<Integer> set, String name) {
		Iterator<Integer> setIterator = set.iterator();
		System.out.println("THIS: " + name);
		while(setIterator.hasNext()) {
			System.out.print(setIterator.next());
			System.out.print(" ");
		}
		System.out.println();
	}
}
