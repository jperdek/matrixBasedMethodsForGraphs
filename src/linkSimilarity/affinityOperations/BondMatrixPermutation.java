package linkSimilarity.affinityOperations;

import java.util.HashSet;
import java.util.Set;

public class BondMatrixPermutation {

	private int indexArray[];
	private int originalIndexArray[];
	private double[][] similarityMatrix;
	private double resultMatrix[][] = null;

	/**
	 * Initializes values required for performing given permutation
	 * 
	 * @param similarityMatrix (or affinityMatrix which should be permuted)
	 */
	public BondMatrixPermutation(double similarityMatrix[][]) {
		this.indexArray = new int[similarityMatrix.length];
		this.similarityMatrix = similarityMatrix;
	}
	
	/**
	 * Initializes values required for performing given permutation
	 * 
	 * @param similarityMatrix (or affinityMatrix which should be permuted)
	 */
	public BondMatrixPermutation(double similarityMatrix[][], int originalIndexArray[]) {
		this.indexArray = new int[similarityMatrix.length];
		this.originalIndexArray = originalIndexArray;
		this.similarityMatrix = similarityMatrix;
	}
	
	/**
	 * Initialized indexes and whole permuted matrix for further permutation
	 *  
	 * @return permuted matrix - matrix similar to affinity matrix will be further permuted
	 */
	public double[][] initializeFirstIndexesAndPermutedMatrix() {
		this.indexArray[0] = 0;
		this.indexArray[1] = 1;
		
		double permutedMatrix[][] = new double[this.similarityMatrix.length][];
		for (int rowIndex = 0; rowIndex < this.similarityMatrix.length; rowIndex++) {
			permutedMatrix[rowIndex] = new double[this.similarityMatrix.length];
			for (int columnIndex = 0; columnIndex < this.similarityMatrix.length; columnIndex++) {
				permutedMatrix[rowIndex][columnIndex] = this.similarityMatrix[rowIndex][columnIndex];
			}
		}
		return permutedMatrix;
	}
	
	/**
	 * Initializes whole permuted matrix for further permutation
	 *  
	 * @return permuted matrix - matrix similar to affinity matrix will be further permuted
	 */
	public double[][] initializePermutedMatrix() {		
		double permutedMatrix[][] = new double[this.similarityMatrix.length][];
		for (int rowIndex = 0; rowIndex < this.similarityMatrix.length; rowIndex++) {
			permutedMatrix[rowIndex] = new double[this.similarityMatrix.length];
			for (int columnIndex = 0; columnIndex < this.similarityMatrix.length; columnIndex++) {
				permutedMatrix[rowIndex][columnIndex] = this.similarityMatrix[rowIndex][columnIndex];
			}
		}
		return permutedMatrix;
	}
	
	/**
	 * Evaluates boundary value from permuted matrix:
	 * bond(Ax, Ay) = SUM 1 to n [AFF(Az, Ax) * AFF(Az, Ay)]
	 * 
	 * @param columnIndex1 - identifier of first requested column column
	 * @param columnIndex2 - identifier of second requested column column
	 * @param permutedMatrix - previously permuted matrix
	 * @return
	 */
	public double evaluateBoundary(int columnIndex1, int columnIndex2, double permutedMatrix[][]) {
		double boundaryValue = 0.0;
		
		for (int rowIndex = 0; rowIndex < this.similarityMatrix.length; rowIndex++) {
			boundaryValue = boundaryValue + 
					permutedMatrix[rowIndex][columnIndex1] * permutedMatrix[rowIndex][columnIndex2]; 
		}
		return boundaryValue;
	}
	
	/**
	 * cont (Ai, Ak, Aj ) = AMnew − AMold = 2bond(Ai, Ak) + 2bond(Ak, Aj ) − 2bond(Ai, Aj )
	 * 
	 * @param proposedLeftColumn
	 * @param proposedMiddleColumn
	 * @param proposedRightColumn
	 * @param actualLength
	 * @param permutedMatrix
	 * @return evaluated value which should be maximized
	 */
	public double evaluateCont(int proposedLeftColumn, int proposedMiddleColumn, 
			int proposedRightColumn, int actualLength, double[][] permutedMatrix) {
		double leftNew;
		double newRight;
		double leftRight;
		
		if (proposedLeftColumn == -1) { //column is on the left
			leftNew = 0.0;
		} else {
			leftNew = 2.0 * this.evaluateBoundary(proposedLeftColumn, proposedMiddleColumn, permutedMatrix);
		}
		
		if (proposedRightColumn == actualLength) { //column is on the right
			newRight = 0.0;
		} else {
			newRight = 2.0 * this.evaluateBoundary(proposedMiddleColumn, proposedRightColumn, permutedMatrix);
		}
		
		if (proposedLeftColumn != -1 && proposedRightColumn != actualLength) { //column is in the middle
			leftRight = 2.0 * this.evaluateBoundary(proposedLeftColumn, proposedRightColumn, permutedMatrix);
		} else {
			leftRight = 0.0;
		}
		return leftNew + newRight - leftRight;
	}
	
	/**
	 * Column indices need to be moved to right when new column is added 
	 * - also native values are moved from similarity to permuted matrix according them
	 * 
	 * @param actualPosition - actual processed index (number of added columns from affinity matrix)
	 * @param newIndexLocation - new location somewhere below actualPosition index position
	 * @param permutedMatrix - permuted matrix which needs to be updated
	 */
	public void setNewIndexLocationWithMatrix(int actualPosition, int newIndexLocation, double permutedMatrix[][]) {
		for (int position = actualPosition; position > newIndexLocation; position--) {
			this.indexArray[position] = this.indexArray[position - 1];
			for (int columnIndex = 0; columnIndex < this.similarityMatrix.length; columnIndex++) {
				permutedMatrix[columnIndex][position] = this.similarityMatrix[columnIndex][position - 1];
			}
		}
		for (int columnIndex = 0; columnIndex < this.similarityMatrix.length; columnIndex++) {
			permutedMatrix[columnIndex][newIndexLocation] 
					= this.similarityMatrix[columnIndex][actualPosition];
		}
		this.indexArray[newIndexLocation] = actualPosition;
	}
	
	/**
	 * Prints mapping of swapped columns (originalMatrix -> maximizedSwappedMatrix)
	 */
	public void printIndexes() {
		System.out.println();
		for (int i = 0; i < this.similarityMatrix.length; i++) {
			System.out.print(this.indexArray[i]);
			System.out.print(", ");
		}
		System.out.println();
	}
	
	/**
	 * Permutes rows according given mapping
	 * 
	 * @param permutedMatrix - matrix which rows should be permuted
	 */
	public void permuteRowsOnly(double permutedMatrix[][]) {
		Set<Integer> swapped = new HashSet<Integer>(); //to not exchange rows twice
		int swapIndex;
		double help;
		
		for (int permutedIndex = 0; permutedIndex < this.similarityMatrix.length; permutedIndex++) {
			swapIndex = this.indexArray[permutedIndex];
			if (swapped.contains(swapIndex) || swapped.contains(permutedIndex)) {
				continue;
			}
			swapped.add(swapIndex);
			swapped.add(permutedIndex);
			
			for (int columnIndex = 0; columnIndex < this.similarityMatrix.length; columnIndex++) {
				help = permutedMatrix[swapIndex][columnIndex];
				permutedMatrix[swapIndex][columnIndex] = permutedMatrix[permutedIndex][columnIndex];
				permutedMatrix[permutedIndex][columnIndex] = help;
			}
		}
	}

	/**
	 * During optimization permutation process is applied:
	 * 	1. whole matrix is created 
	 *  2. columns are copied according their swap mapping
	 *  3. method to permute rows is finally called
	 * 
	 * @return permuted similarity matrix
	 */
	public double[][] permuteSimilarityMatrix() {
		int swapIndex;
		double permutedMatrix[][] = new double[this.similarityMatrix.length][];
		for (int columnIndex = 0; columnIndex < this.similarityMatrix.length; columnIndex++) {
			permutedMatrix[columnIndex] = new double[this.similarityMatrix.length];
		}
		
		for (int permutedIndex = 0; permutedIndex < this.similarityMatrix.length; permutedIndex++) {
			swapIndex = this.indexArray[permutedIndex];
			
			for (int rowIndex = 0; rowIndex < this.similarityMatrix.length; rowIndex++) {
				permutedMatrix[rowIndex][swapIndex] = this.similarityMatrix[rowIndex][permutedIndex];
			}
		}
		this.permuteRowsOnly(permutedMatrix);
		return permutedMatrix;
	}
	
	/**
	 * All indexes should be initialized for optimized solution to swap matrix parts
	 */
	public void initializeFirstIndexes() {
		for (int rowIndex = 0; rowIndex < this.similarityMatrix.length; rowIndex++) {
			this.indexArray[rowIndex] = rowIndex;
		}
	}

	/**
	 * Evaluates boundary value (AFF affinity matrix = similarity matrix): 
	 * bond(Ax, Ay) = SUM 1 to n [AFF(Az, Ax) * AFF(Az, Ay)]
	 * 
	 * @param columnIndex1 - identifier of first requested column column
	 * @param columnIndex2 - identifier of second requested column column
	 * @return
	 */
	public double evaluateBoundary(int columnIndex1, int columnIndex2) {
		double boundaryValue = 0.0;
		
		for (int rowIndex = 0; rowIndex < this.similarityMatrix.length; rowIndex++) {
			boundaryValue = boundaryValue + 
					this.similarityMatrix[this.indexArray[rowIndex]][this.indexArray[columnIndex1]] * this.similarityMatrix[this.indexArray[rowIndex]][this.indexArray[columnIndex2]]; 
		}
		return boundaryValue;
	}
	
	/**
	 * cont (Ai, Ak, Aj ) = AMnew − AMold = 2bond(Ai, Ak) + 2bond(Ak, Aj ) − 2bond(Ai, Aj )
	 *
	 * @param proposedLeftColumn
	 * @param proposedMiddleColumn
	 * @param proposedRightColumn
	 * @return
	 */
	public double evaluateCont(int proposedLeftColumn, int proposedMiddleColumn, 
			int proposedRightColumn, int actualLength) {
		double leftNew;
		double newRight;
		double leftRight;
		
		if (proposedLeftColumn == -1) {
			leftNew = 0.0;
		} else {
			leftNew = 2.0 * this.evaluateBoundary(proposedLeftColumn, proposedMiddleColumn);
		}
		
		if (proposedRightColumn == actualLength) {
			newRight = 0.0;
		} else {
			newRight = 2.0 * this.evaluateBoundary(proposedMiddleColumn, proposedRightColumn);
		}
		
		if (proposedLeftColumn != -1 && proposedRightColumn != actualLength) {
			leftRight = 2.0 * this.evaluateBoundary(proposedLeftColumn, proposedRightColumn);
		} else {
			leftRight = 0.0;
		}
		return leftNew + newRight - leftRight;
	}

	/**
	 * Only column indices (in optimization) need to be moved to right when new column is added
	 * 
	 * @param actualPosition - actual processed index (number of added columns from affinity matrix)
	 * @param newIndexLocation - new location somewhere below actualPosition index position
	 */
	public void setNewIndexLocation(int actualPosition, int newIndexLocation) {
		for (int position = actualPosition; position > newIndexLocation; position--) {
			this.indexArray[position] = this.indexArray[position - 1];
		}
		this.indexArray[newIndexLocation] = actualPosition;
	}
	
	/**
	 * Only column indices (in optimization) need to be moved to right when new column is added
	 * 
	 * @param actualPosition - actual processed index (number of added columns from affinity matrix)
	 * @param newIndexLocation - new location somewhere below actualPosition index position
	 */
	public void setNewIndexLocationOriginalMapping(int actualPosition, int newIndexLocation) {
		int help;
		for (int position = actualPosition; position > newIndexLocation; position--) {
			help = this.originalIndexArray[position];
			this.originalIndexArray[position] = this.originalIndexArray[position - 1];
			this.originalIndexArray[position - 1] = help;
			
			this.indexArray[position] = this.indexArray[position - 1];
		}
		this.indexArray[newIndexLocation] = actualPosition;
	}
	
	public void setResultMatrix(double[][] resultMatrix) {
		this.resultMatrix = resultMatrix;
	}
	
	public double[][] getResultMatrix() {
		return this.resultMatrix;
	}
	
	public int[] getIndexMapping() {
		return this.indexArray;
	}
	
	public int[] getOriginalIndexMapping() {
		return this.originalIndexArray;
	}
}
