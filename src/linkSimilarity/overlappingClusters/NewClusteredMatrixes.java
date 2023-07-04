package linkSimilarity.overlappingClusters;

import java.util.Set;

/**
 * Extended clustered matrixes
 * 
 * @author Jakub Perdek
 *
 */
public class NewClusteredMatrixes {

	private double[][] upperLeftMatrix;
	private double[][] bottomRightMatrix;
	private Set<Integer> upperLeftExtensionIndexes;
	private Set<Integer> bottomRightExtensionIndexes;
	
	/**
	 * Stores newly created matrixes
	 * 
	 * @param upperLeftMatrix
	 * @param bottomRightMatrix
	 */
	public NewClusteredMatrixes(double[][] upperLeftMatrix, 
			double[][] bottomRightMatrix) {
		this.upperLeftMatrix = upperLeftMatrix;
		this.bottomRightMatrix = bottomRightMatrix;
	}
	
	public double[][] getUpperLeftMatrix() {
		return this.upperLeftMatrix;
	}
	
	public double[][] getBottomRightMatrix() {
		return this.bottomRightMatrix;
	}
	
	public void setUpperLeftExtensionIndexes(Set<Integer> upperLeftExtensionIndexes) {
		this.upperLeftExtensionIndexes = upperLeftExtensionIndexes;
	}
	
	public void setBottomRightExtensionIndexes(Set<Integer> bottomRightExtensionIndexes) {
		this.bottomRightExtensionIndexes = bottomRightExtensionIndexes;
	}
	
	public Set<Integer> getUpperLeftExtensionIndexes() { return this.upperLeftExtensionIndexes; }
	
	public Set<Integer> getBottomRightExtensionIndexes() { return this.bottomRightExtensionIndexes; }
}
