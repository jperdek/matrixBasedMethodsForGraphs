package linkSimilarity.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import linkSimilarity.AdjacencyToWeight;
import linkSimilarity.CorrelationDegreeMatrix;
import linkSimilarity.CorrelationToSimilarityMatrix;
import linkSimilarity.clusteringAlgorithms.MatrixOverlappingPartition;
import linkSimilarity.clusteringAlgorithms.MatrixPartition;
import linkSimilarity.clusteringAlgorithms.NodeHierarchicCluster;


class INandOUTlinksTest {

	@Test
	void test() {
		TestMatrix testMatrix = new TestMatrix();
		long[][] symmetricMatrix = testMatrix.getAdjacencySymmetricMatrix();
		AdjacencyToWeight adjacencyToWeightSymmetric = new AdjacencyToWeight(symmetricMatrix);
		//System.out.println(java.util.Arrays.toString(adjacencyToWeightSymmetric.getIncomingValues()));
		
		assertArrayEquals(adjacencyToWeightSymmetric.getIncomingValues(), new double[] {3, 5, 7, 3});
		assertEquals(adjacencyToWeightSymmetric.getNormalizedSumIN(), (3.0 / 4.0 + 5.0 / 4.0 + 7.0 / 4.0 + 3.0 / 4.0));
		
		assertArrayEquals(adjacencyToWeightSymmetric.getOutcomingValues(), new double[] {3, 5, 7, 3});
		assertEquals(adjacencyToWeightSymmetric.getNormalizedSumOUT(), (3.0 / 4.0 + 5.0 / 4.0 + 7.0 / 4.0 + 3.0 / 4.0));
	
		double nIN =adjacencyToWeightSymmetric.getNormalizedSumIN();
		double nOUT =adjacencyToWeightSymmetric.getNormalizedSumOUT();
		assertArrayEquals(adjacencyToWeightSymmetric.getWeights(), new double[] {
				1 + Math.max((3 - nIN + 0.0) / (7 - 3 + 0.0), (3 - nOUT + 0.0) / (7 - 3 + 0.0)), 
				1 + Math.max((5 - nIN + 0.0) / (7 - 3 + 0.0), (5 - nOUT + 0.0) / (7 - 3 + 0.0)),
				1 + Math.max((7 - nIN + 0.0) / (7 - 3 + 0.0), (7 - nOUT + 0.0) / (7 - 3 + 0.0)),
				1 + Math.max((3 - nIN + 0.0) / (7 - 3 + 0.0), (3 - nOUT + 0.0) / (7 - 3 + 0.0))
		});
		
		double[][] weigthSymmetricMatrix = adjacencyToWeightSymmetric.convertToWeightMatrix(symmetricMatrix);
		assertArrayEquals(weigthSymmetricMatrix, new double[][]{
			{ 1.0, 1.125, 1.625, 0.625 },
			{ 1.125, 1.0, 1.625, 1.125},
			{ 1.625, 1.625, 1.0, 1.625 },
			{ 0.625, 1.125, 1.625, 1.0 }
		});
		
		System.out.println("WEIGHT MATRIX");
		adjacencyToWeightSymmetric.printWeightMatrix(weigthSymmetricMatrix);
		System.out.println();
		//CorrelationDegreeMatrix.floydWarshall(weigthSymmetricMatrix);
		
		int matrixSize = weigthSymmetricMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];

	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weigthSymmetricMatrix, symmetricMatrix, CorrelationDegreeMatrix.CORRELATION_FACTOR, shortestPathMatrix, distance, shortestWeightsPathMatrix);
		
		
		assertArrayEquals(shortestWeightsPathMatrix, new double[][]{
			{ 1.0, 1.125, 1.625, 1.125 * 1.125 }, //A -> B -> D = 1.125 * 1.125 = 1.265625
			{ 1.125, 1.0, 1.625, 1.125},
			{ 1.625, 1.625, 1.0, 1.625 },
			{ 1.125 * 1.125, 1.125, 1.625, 1.0 }
		});
		
		
		double[][] correlationMatrix = CorrelationDegreeMatrix.constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, CorrelationDegreeMatrix.CORRELATION_FACTOR);
		
		CorrelationToSimilarityMatrix correlationToSimilarityMatrix = new CorrelationToSimilarityMatrix(correlationMatrix);
		double[][] similarityMatrix = correlationToSimilarityMatrix.constructSimilarityMatrix();
		System.out.println("SIMILARITY MATRIX");
		correlationToSimilarityMatrix.printSimilarityMatrix(similarityMatrix);
		
		MatrixPartition matrixPartition = new MatrixPartition();
		NodeHierarchicCluster nodeHierarchicCluster = new NodeHierarchicCluster();
		System.out.println("CLUSTERING WITHOUT DUPLICITIES");
		int nodesMapping[] = new int[] { 0,1,2,3};
		matrixPartition.hierarchicalMatrixPartitioning(nodeHierarchicCluster, similarityMatrix, 2, 0, true, nodesMapping);
		nodeHierarchicCluster.printHierarchy(0);
		
		System.out.println("CLUSTERING WITH DUPLICITIES");
		int nodesMapping2[] = new int[] { 0,1,2,3};
		NodeHierarchicCluster nodeHierarchicCluster2 = new NodeHierarchicCluster();
		MatrixOverlappingPartition matrixOverlappingPartition2 = new MatrixOverlappingPartition();
		matrixOverlappingPartition2.hierarchicalMatrixPartitioningWithOverlapping(nodeHierarchicCluster2, similarityMatrix, 2, 0, true, nodesMapping2, 5, 0);
		nodeHierarchicCluster2.printHierarchy(0);
		
		System.out.println("CLUSTERING WITH DUPLICITIES");
		int nodesMapping3[] = new int[] { 0,1,2,3};
		NodeHierarchicCluster nodeHierarchicCluster3 = new NodeHierarchicCluster();
		MatrixOverlappingPartition matrixOverlappingPartition3 = new MatrixOverlappingPartition();
		matrixOverlappingPartition3.hierarchicalMatrixPartitioningWithOverlapping(nodeHierarchicCluster3, similarityMatrix, 4, 0, true, nodesMapping3, 5, 0);
		nodeHierarchicCluster3.printHierarchy(0);
	}
	
	@Test
	void test2() {
		TestMatrix testMatrix = new TestMatrix();
		long[][] asymmetricMatrix = testMatrix.getAdjacencyAsymmetricMatrix();
		AdjacencyToWeight adjacencyToWeightAsymmetric = new AdjacencyToWeight(asymmetricMatrix);
		//System.out.println(java.util.Arrays.toString(adjacencyToWeightAsymmetric.getIncomingValues()));
		//System.out.println(java.util.Arrays.toString(adjacencyToWeightAsymmetric.getOutcomingValues()));
		
		
		assertArrayEquals(adjacencyToWeightAsymmetric.getIncomingValues(), new double[] {1, 3, 7, 6});
		assertEquals(adjacencyToWeightAsymmetric.getNormalizedSumIN(), (1.0 / 4.0 + 3.0 / 4.0 + 7.0 / 4.0 + 6.0 / 4.0));
		
		assertArrayEquals(adjacencyToWeightAsymmetric.getOutcomingValues(), new double[] {5, 5, 4, 3});
		assertEquals(adjacencyToWeightAsymmetric.getNormalizedSumOUT(), (5.0 / 4.0 + 5.0 / 4.0 + 4.0 / 4.0 + 3.0 / 4.0));
	
		double nIN =adjacencyToWeightAsymmetric.getNormalizedSumIN();
		double nOUT =adjacencyToWeightAsymmetric.getNormalizedSumOUT();
		assertArrayEquals(adjacencyToWeightAsymmetric.getWeights(), new double[] {
				1 + Math.max((1 - nIN + 0.0) / (7 - 1 + 0.0), (5 - nOUT + 0.0) / (5 - 3 + 0.0)), 
				1 + Math.max((3 - nIN + 0.0) / (7 - 1 + 0.0), (5 - nOUT + 0.0) / (5 - 3 + 0.0)),
				1 + Math.max((7 - nIN + 0.0) / (7 - 1 + 0.0), (4 - nOUT + 0.0) / (5 - 3 + 0.0)),
				1 + Math.max((6 - nIN + 0.0) / (7 - 1 + 0.0), (3 - nOUT + 0.0) / (5 - 3 + 0.0))
		});
	}
	
	

}
