package graphSimilarities;


public class MaximizedHungarianMethodWrapper {

	private double[][] rebuildMatrix;
	private HungarianAlgorithm hungarianAlgorithm;
	
	
	public MaximizedHungarianMethodWrapper(double[][] costMatrix) {
		this.rebuildMatrix = this.rebuildMatrixWithReversedValues(costMatrix);
		//printMatrix(this.rebuildMatrix);
		this.hungarianAlgorithm = new HungarianAlgorithm(this.rebuildMatrix);
	}

	private double findMaximumFromMatrix(double[][] costMatrix) {
		double maximum = 0.0;
		for(int rowIndex = 0; rowIndex < costMatrix.length; rowIndex++) {
			for (int colIndex = 0; colIndex < costMatrix[0].length; colIndex++) {
				if (costMatrix[rowIndex][colIndex] > maximum) {
					maximum = costMatrix[rowIndex][colIndex];
				}
			}
		}
		return maximum;
	}
	
	private double[][] rebuildMatrixWithReversedValues(double[][] costMatrix) {
		double maximum = this.findMaximumFromMatrix(costMatrix);
		double[][] rebuildMatrix = new double[costMatrix.length][];
		
		for(int rowIndex = 0; rowIndex < costMatrix.length; rowIndex++) {
			rebuildMatrix[rowIndex] = new double[costMatrix[0].length];
			for (int colIndex = 0; colIndex < costMatrix[0].length; colIndex++) {

				rebuildMatrix[rowIndex][colIndex] = maximum - costMatrix[rowIndex][colIndex];
			}
		}
		return rebuildMatrix;
	}
	
	/**
	 * To print matrix values
	 * 
	 * @param matrix - matrix which should be printed
	 */
	private static void printMatrix(double matrix[][]) {
		System.out.println();
		for (int rowIndex=0; rowIndex < matrix.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < matrix[0].length; columnIndex++) {
				System.out.print(matrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}

	public int[] execute() {
		return this.hungarianAlgorithm.execute();
	}
}
