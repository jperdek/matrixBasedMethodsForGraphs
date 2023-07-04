package linkSimilarity.affinityOperations;


/**
 * Implementation of energy bond algorithm
 * 
 * @author Jakub Perdek
 *
 */
public class BondEnergyAlgorithm {

	/**
	 * Energy Bond Algorithm
	 */
	public BondEnergyAlgorithm() {		
	}
	
	/**
	 * Slower version of Bond Energy algorithm
	 * 
	 * @param similarityMatrix - similarity or affinity attribute matrix (AA) which should be permuted
	 * @return permuted similarity matrix by maximizing affinity value
	 */
	public static double[][] evaluateBondPermutation(double similarityMatrix[][]) {
		BondMatrixPermutation bondMatrixPermutation = new BondMatrixPermutation(similarityMatrix);
		double maximumCont = Double.NEGATIVE_INFINITY;
		double permutedMatrix[][];
		double cont;
		int newIndexLocation;
		
		permutedMatrix = bondMatrixPermutation.initializeFirstIndexesAndPermutedMatrix();
		for (int index = 2; index < similarityMatrix.length; index++) {
			maximumCont = Double.NEGATIVE_INFINITY;
			newIndexLocation = index + 1;

			for (int i = 0; i <= index; i++) {
				cont = bondMatrixPermutation.evaluateCont(i - 1, index, i, index, permutedMatrix);
				if (cont > maximumCont) {
					maximumCont = cont;
					newIndexLocation = i;
				}
			}
			
			bondMatrixPermutation.setNewIndexLocationWithMatrix(index, newIndexLocation, permutedMatrix);
			// bondMatrixPermutation.printIndexes();
		}
		
		bondMatrixPermutation.permuteRowsOnly(permutedMatrix);
		return permutedMatrix;
	}
	
	/**
	 * Optimized version of Bond Energy algorithm
	 * 
	 * @param similarityMatrix - similarity or affinity attribute matrix (AA) which should be permuted
	 * @return permuted similarity matrix by maximizing affinity value
	 */
	public static double[][] evaluateBondPermutationOptimized(double similarityMatrix[][]) {
		BondMatrixPermutation bondMatrixPermutation = new BondMatrixPermutation(similarityMatrix);
		double maximumCont = Double.NEGATIVE_INFINITY;
		double permutedMatrix[][];
		double cont;
		int newIndexLocation;
		
		bondMatrixPermutation.initializeFirstIndexes();
		for (int index = 2; index < similarityMatrix.length; index++) {
			maximumCont = Double.NEGATIVE_INFINITY;
			newIndexLocation = index + 1;

			for (int i = 0; i <= index; i++) {
				cont = bondMatrixPermutation.evaluateCont(i - 1, index, i, index);
				if (cont > maximumCont) {
					maximumCont = cont;
					newIndexLocation = i;
				}
			}
			
			bondMatrixPermutation.setNewIndexLocation(index, newIndexLocation);
			//bondMatrixPermutation.printIndexes();
		}
		
		permutedMatrix = bondMatrixPermutation.permuteSimilarityMatrix();
		return permutedMatrix;
	}

	/**
	 * Slower version of Bond Energy algorithm
	 * 
	 * @param similarityMatrix - similarity or affinity attribute matrix (AA) which should be permuted
	 * @return permuted similarity matrix by maximizing affinity value
	 */
	public static BondMatrixPermutation evaluateBondPermutationMapping(double similarityMatrix[][]) {
		BondMatrixPermutation bondMatrixPermutation = new BondMatrixPermutation(similarityMatrix);
		double maximumCont = Double.NEGATIVE_INFINITY;
		double permutedMatrix[][];
		double cont;
		int newIndexLocation;
		
		permutedMatrix = bondMatrixPermutation.initializeFirstIndexesAndPermutedMatrix();
		for (int index = 2; index < similarityMatrix.length; index++) {
			maximumCont = Double.NEGATIVE_INFINITY;
			newIndexLocation = index + 1;

			for (int i = 0; i <= index; i++) {
				cont = bondMatrixPermutation.evaluateCont(i - 1, index, i, index, permutedMatrix);
				if (cont > maximumCont) {
					maximumCont = cont;
					newIndexLocation = i;
				}
			}
			
			bondMatrixPermutation.setNewIndexLocationWithMatrix(index, newIndexLocation, permutedMatrix);
			// bondMatrixPermutation.printIndexes();
		}
		
		bondMatrixPermutation.permuteRowsOnly(permutedMatrix);
		bondMatrixPermutation.setResultMatrix(permutedMatrix);
		return bondMatrixPermutation;
	}

	/**
	 * Optimized version of Bond Energy algorithm 
	 * 
	 * @param similarityMatrix - similarity or affinity attribute matrix (AA) which should be permuted
	 * @return permuted similarity matrix by maximizing affinity value
	 */
	public static BondMatrixPermutation evaluateBondPermutationOptimizedMapping(double similarityMatrix[][]) {
		BondMatrixPermutation bondMatrixPermutation = new BondMatrixPermutation(similarityMatrix);
		double maximumCont = Double.NEGATIVE_INFINITY;
		double permutedMatrix[][];
		double cont;
		int newIndexLocation;
		
		bondMatrixPermutation.initializeFirstIndexes();
		for (int index = 2; index < similarityMatrix.length; index++) {
			maximumCont = Double.NEGATIVE_INFINITY;
			newIndexLocation = index + 1;

			for (int i = 0; i <= index; i++) {
				cont = bondMatrixPermutation.evaluateCont(i - 1, index, i, index);
				if (cont > maximumCont) {
					maximumCont = cont;
					newIndexLocation = i;
				}
			}
			
			bondMatrixPermutation.setNewIndexLocation(index, newIndexLocation);
			//bondMatrixPermutation.printIndexes();
		}
		
		permutedMatrix = bondMatrixPermutation.permuteSimilarityMatrix();
		bondMatrixPermutation.setResultMatrix(permutedMatrix);
		return bondMatrixPermutation;
	}

	/**
	 * Optimized version of Bond Energy algorithm with applying mapping from previous steps
	 * 
	 * @param similarityMatrix - similarity or affinity attribute matrix (AA) which should be permuted
	 * @return permuted similarity matrix by maximizing affinity value
	 */
	public static BondMatrixPermutation evaluateBondPermutationOptimizedMapping(double similarityMatrix[][], int indexMapping[]) {
		BondMatrixPermutation bondMatrixPermutation = new BondMatrixPermutation(similarityMatrix, indexMapping);
		double maximumCont = Double.NEGATIVE_INFINITY;
		double permutedMatrix[][];
		double cont;
		int newIndexLocation;
		
		bondMatrixPermutation.initializeFirstIndexes();
		for (int index = 2; index < similarityMatrix.length; index++) {
			maximumCont = Double.NEGATIVE_INFINITY;
			newIndexLocation = index + 1;

			for (int i = 0; i <= index; i++) {
				cont = bondMatrixPermutation.evaluateCont(i - 1, index, i, index);
				if (cont > maximumCont) {
					maximumCont = cont;
					newIndexLocation = i;
				}
			}
			
			bondMatrixPermutation.setNewIndexLocationOriginalMapping(index, newIndexLocation);
			//bondMatrixPermutation.printIndexes();
		}
		
		permutedMatrix = bondMatrixPermutation.permuteSimilarityMatrix();
		bondMatrixPermutation.setResultMatrix(permutedMatrix);
		return bondMatrixPermutation;
	}
	

	/**
	 * To print matrix values
	 * 
	 * @param matrix - matrix which should be printed
	 */
	private void printMatrix(double matrix[][]) {
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
