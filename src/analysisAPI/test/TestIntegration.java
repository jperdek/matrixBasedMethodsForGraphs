package analysisAPI.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import analysisAPI.GraphsSimilarityEvaluator;
import graphSimilarities.GraphsVertexSimilarities;
import graphSimilarities.MaximizedHungarianMethodWrapper;
import webSimilarity.clusteringAlgorithms.NodeHierarchicCluster;

class TestIntegration {

	//@Test
	void test() {
		String matrix1Names[] = new String[] {"A1", "B1", "C1", "D1", "E1"};
		long matrix1[][] = new long[][] {
			{0, 0, 0, 0, 0},
			{1, 0, 1, 0, 0},
			{1, 0, 0, 1, 1},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0}
		};
		
		String matrix2Names[] = new String[] {"A2", "B2", "C2", "D2", "E2"};
		long matrix2[][] = new long[][] {
			{0, 1, 0, 0, 1},
			{0, 0, 1, 1, 1},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0}
		};
		GraphsSimilarityEvaluator graphSimilarityEvaluator = new GraphsSimilarityEvaluator();
		NodeHierarchicCluster clusteredResult = graphSimilarityEvaluator.process2Graphs(matrix1, matrix2, matrix1Names, matrix2Names);
		//clusteredResult.printHierarchyWithNames(0);
	}

	/**
	 * TEST ACCORDING DOI. 10.1137/S0036144502415960 (one value is different)
	 */
	//@Test
	void test2() {
		String matrix1Names[] = new String[] {"A1", "B1", "C1", "D1"};
		long matrix1[][] = new long[][] {
			//1  2, 3 , 4
			{ 0, 1, 1, 0}, //1 
			{ 1, 0, 1, 0}, //2
			{ 0, 1, 0, 0}, //3
			{ 1, 0, 1, 0}  //4
		};
		
		String matrix2Names[] = new String[] {"A2", "B2", "C2", "D2", "E2", "F2"};
		long matrix2[][] = new long[][] {
			//1  2, 3, 4, 5, 6
			{ 0, 0, 1, 1, 0, 0}, //1 
			{ 0, 0, 0, 1, 0, 1}, //2
			{ 1, 0, 0, 0, 1, 1}, //3
			{ 0, 0, 0, 0, 0, 0}, //4
			{ 0, 0, 0, 0, 0, 0}, //5
			{ 1, 0, 1, 1, 0, 0}  //6
		};
		
		GraphsSimilarityEvaluator graphSimilarityEvaluator = new GraphsSimilarityEvaluator();
		NodeHierarchicCluster clusteredResult1 = graphSimilarityEvaluator.process2Graphs(matrix2, matrix1, matrix2Names, matrix1Names);
		NodeHierarchicCluster clusteredResult2 = graphSimilarityEvaluator.process2Graphs(matrix1, matrix2, matrix1Names, matrix2Names);
		clusteredResult1.printHierarchyWithNames(0);
		assertEquals(clusteredResult1.printHierarchyWithNamesToString(0), clusteredResult2.printHierarchyWithNamesToString(0));
	}
	
	/**
	 * TEST ACCORDING DOI. 10.1137/S0036144502415960 (one value is different)
	 */
	@Test
	void test3() {
		String matrix1Names[] = new String[] {"A1", "B1", "C1", "D1"};
		long matrix1[][] = new long[][] {
			//1  2, 3 , 4
			{ 0, 1, 1, 0}, //1 
			{ 1, 0, 1, 0}, //2
			{ 0, 1, 0, 0}, //3
			{ 1, 0, 1, 0}  //4
		};
		
		String matrix2Names[] = new String[] {"A2", "B2", "C2", "D2", "E2", "F2"};
		long matrix2[][] = new long[][] {
			//1  2, 3, 4, 5, 6
			{ 0, 0, 1, 1, 0, 0}, //1 
			{ 0, 0, 0, 1, 0, 1}, //2
			{ 1, 0, 0, 0, 1, 1}, //3
			{ 0, 0, 0, 0, 0, 0}, //4
			{ 0, 0, 0, 0, 0, 0}, //5
			{ 1, 0, 1, 1, 0, 0}  //6
		};
		
		GraphsSimilarityEvaluator graphSimilarityEvaluator = new GraphsSimilarityEvaluator();
		NodeHierarchicCluster clusteredResult1 = graphSimilarityEvaluator.process2GraphsExtended(matrix2, matrix1, matrix2Names, matrix1Names);
		NodeHierarchicCluster clusteredResult2 = graphSimilarityEvaluator.process2GraphsExtended(matrix1, matrix2, matrix1Names, matrix2Names);
		clusteredResult1.printHierarchyWithNames(0);
		assertEquals(clusteredResult1.printHierarchyWithNamesToString(0), clusteredResult2.printHierarchyWithNamesToString(0));
	}
	
	
}
