package linkSimilarity.clusteringAlgorithms;

import java.util.Iterator;

import linkSimilarity.affinityOperations.BondEnergyAlgorithm;
import linkSimilarity.affinityOperations.BondMatrixPermutation;
import linkSimilarity.overlappingClusters.ExtendingAlgorithm;
import linkSimilarity.overlappingClusters.NewClusteredMatrixes;


/**
 * Launching point of matrix overlapping algorithm
 * 
 * @author Jakub Perdek
 *
 */
public class MatrixOverlappingPartition {
	

	/**
	 * Recursively divides matrix to clusters
	 * NOTE: 
	 * IN ExtendingAlgorithm.clusterOverlappingClusters: N can be 0 in the most cases - no duplicates are produced
	 * IN ExtendingAlgorithm.clusterOverlappingClusters: threshold can be too high in the most casess
	 * PROBLEMS: infinite recursion due to lower preferredNumber, or high N or threshold variables
	 * 
	 * @param processedCluster - parent cluster
	 * @param similarityMatrix - similarity matrix which should be divided
	 * @param preferredNumber - maximal number of cluster members
	 * @param previousPointD - previous chosen division point D (0 for the first iteration)
	 * @param previousTopLeft - if top left matrix part was processed in previous iteration
	 */
	public void hierarchicalMatrixPartitioningWithOverlapping(NodeHierarchicCluster processedCluster, 
			double[][] similarityMatrix, int preferredNumber, int previousPointD, boolean previousTopLeft, 
			int[] previousNodesMappingOriginal, int maxDepth, int depth) {
		if (depth > maxDepth) {
			return;
		}
		// similarityMatrix.length as length of m representing R (highest-ranked pages)
		BondMatrixPermutation bondMatrixPermutation =  BondEnergyAlgorithm.evaluateBondPermutationOptimizedMapping(
				similarityMatrix, previousNodesMappingOriginal);
		double permutedSimilarityMatrix[][] = bondMatrixPermutation.getResultMatrix();
		bondMatrixPermutation.getIndexMapping(); //outputs mapping but only for the one independent iteration
		int nodesMappingOriginal[] = bondMatrixPermutation.getOriginalIndexMapping();
		int index;
		int chosenDPoint = MatrixPartition.getMiddlePointLocation(permutedSimilarityMatrix); 
		NewClusteredMatrixes newClusteredMatrixes = ExtendingAlgorithm.clusterOverlappingClusters(permutedSimilarityMatrix, chosenDPoint);
		double similarityMatrix11[][] = newClusteredMatrixes.getUpperLeftMatrix();
		double similarityMatrix22[][] = newClusteredMatrixes.getBottomRightMatrix();
		int cardinalitySM11 = similarityMatrix11.length;
		int cardinalitySM22 = similarityMatrix22.length;
		
		processedCluster.setRelativePointD(chosenDPoint);
		if (previousTopLeft) {
			processedCluster.setPointD(chosenDPoint);
		} else {
			processedCluster.setPointD(chosenDPoint + previousPointD);
		}
		
		NodeHierarchicCluster topCluster = processedCluster.createTopCluster();
		if (cardinalitySM11 <= preferredNumber) {
			MatrixPartition.insertPreviousDocumentIndexesIteration(topCluster, nodesMappingOriginal, chosenDPoint, true);
		} else {
			MatrixPartition.insertPreviousDocumentIndexesIteration(topCluster, nodesMappingOriginal, chosenDPoint, true);
			int newNodesMapping11[] = new int[similarityMatrix11.length];
			
			index = 0;
			for (; index <= chosenDPoint; index++) {
				newNodesMapping11[index] = nodesMappingOriginal[index];
			}
			Iterator<Integer> chosenNodes = newClusteredMatrixes.getUpperLeftExtensionIndexes().iterator();
			while (chosenNodes.hasNext()) {
				newNodesMapping11[index] = nodesMappingOriginal[chosenNodes.next()];
				index++;
			}
			this.hierarchicalMatrixPartitioningWithOverlapping(topCluster, similarityMatrix11, preferredNumber, 
					chosenDPoint, true, newNodesMapping11, maxDepth, depth + 1);
		}
		
		NodeHierarchicCluster bottomCluster = processedCluster.createBottomCluster();
		if (cardinalitySM22 <= preferredNumber) {
			MatrixPartition.insertPreviousDocumentIndexesIteration(bottomCluster, nodesMappingOriginal, chosenDPoint, false);
		} else {
			MatrixPartition.insertPreviousDocumentIndexesIteration(bottomCluster, nodesMappingOriginal, chosenDPoint, false);
			Iterator<Integer> chosenNodes = newClusteredMatrixes.getBottomRightExtensionIndexes().iterator();
			int newNodesMapping22[] = new int[similarityMatrix22.length];
			index = 0;
			while (chosenNodes.hasNext()) {
				//indexes from chosenNodes are indexes of original matrix, thus their position should be extracted according nodesMappingOriginal
				newNodesMapping22[index] = nodesMappingOriginal[chosenNodes.next()];
				index++;
			}
			for (int chosenPointIndex = chosenDPoint + 1; chosenPointIndex < permutedSimilarityMatrix.length; chosenPointIndex++) {
				newNodesMapping22[index + chosenPointIndex - 1 - chosenDPoint] = nodesMappingOriginal[chosenPointIndex];
			}
			this.hierarchicalMatrixPartitioningWithOverlapping(bottomCluster, similarityMatrix22, preferredNumber, 
					chosenDPoint, false, newNodesMapping22, maxDepth, depth + 1);
		}
	}
}
