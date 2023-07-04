package graphSimilarities.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import graphSimilarities.HungarianAlgorithm;
import graphSimilarities.GraphsVertexSimilarities;
import graphSimilarities.MaximizedHungarianMethodWrapper;


class TestGraphSimilarities {
	
	@Test
	void test2() {
		double matrix1[][] = new double[][] {
			{0, 0, 1, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 1, 0, 1, 0},
			{0, 0, 0, 0, 1},
			{0, 0, 1, 0, 0}
		};
		double matrix2[][] = new double[][] {
			{0, 1, 1, 0, 0},
			{0, 0, 1, 0, 1},
			{0, 0, 0, 1, 1},
			{0, 1, 0, 0, 0},
			{0, 0, 0, 0, 0},
		};
		
		GraphsVertexSimilarities graphsVertexSimilarities = new GraphsVertexSimilarities(matrix1, matrix2);
		double mergedSimilarityMatrix[][] = graphsVertexSimilarities.computeSimilarityMatrices();
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		int results[] = hungarianAlgorithm.execute();
		assertEquals(results[2], 2);
	}

	@Test
	//indexing from vertices of the second one to first one!!!
	void test3() {
		double matrix1[][] = new double[][] {
			{0, 0, 0, 0, 0},
			{1, 0, 1, 0, 0},
			{1, 0, 0, 1, 1},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0}
		};
		double matrix2[][] = new double[][] {
			{0, 1, 0, 0, 1},
			{0, 0, 1, 1, 1},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0}
		};
		
		GraphsVertexSimilarities graphsVertexSimilarities = new GraphsVertexSimilarities(matrix1, matrix2);
		double mergedSimilarityMatrix[][] = graphsVertexSimilarities.computeSimilarityMatrices();

		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		int results[] = hungarianAlgorithm.execute();
		assertEquals(results[0], 1);
		assertEquals(results[1], 2);
		assertEquals(results[4], 0);
	}
	
	@Test
	void test4() {
		double matrix1[][] = new double[][] {
			{0, 1, 0},
			{0, 0, 1},
			{0, 1, 0}
		};
		double matrix2[][] = new double[][] {
			{0, 0, 1},
			{1, 0, 0},
			{1, 0, 0}
		};
		
		GraphsVertexSimilarities graphsVertexSimilarities = new GraphsVertexSimilarities(matrix1, matrix2);
		double mergedSimilarityMatrix[][] = graphsVertexSimilarities.computeSimilarityMatrices();
		//printSimilarityMatrix(mergedSimilarityMatrix);
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		int results[] = hungarianAlgorithm.execute();
		assertArrayEquals(results, new int[] {1, 0, 2});
	}
	
	/**
	 * TEST ACCORDING DOI. 10.1137/S0036144502415960 (one value is different)
	 */
	@Test
	void test5() {
		double matrix1[][] = new double[][] {
			//1  2, 3 , 4
			{ 0, 1, 1, 0}, //1 
			{ 1, 0, 1, 0}, //2
			{ 0, 1, 0, 0}, //3
			{ 1, 0, 1, 0}  //4
		};
		double matrix2[][] = new double[][] {
			//1  2, 3, 4, 5, 6
			{ 0, 0, 1, 1, 0, 0}, //1 
			{ 0, 0, 0, 1, 0, 1}, //2
			{ 1, 0, 0, 0, 1, 1}, //3
			{ 0, 0, 0, 0, 0, 0}, //4
			{ 0, 0, 0, 0, 0, 0}, //5
			{ 1, 0, 1, 1, 0, 0}  //6
		};
		
		GraphsVertexSimilarities graphsVertexSimilarities = new GraphsVertexSimilarities(matrix1, matrix2);
		double mergedSimilarityMatrix[][] = graphsVertexSimilarities.computeSimilarityMatrices();
		roundMatrix(mergedSimilarityMatrix);
		//printSimilarityMatrix(mergedSimilarityMatrix);
		assertArrayEquals(mergedSimilarityMatrix, new double[][] {
			{0.2636, 0.2786, 0.2723, 0.1289 },
			{0.1286, 0.1268, 0.0624, 0.1268 }, //0.1286 -> 0.1268
			{0.2904, 0.3115, 0.2825, 0.1667 },
			{0.1540, 0.1701, 0.2462, 0 		},
			{0.0634, 0.0759, 0.1018, 0		},
			{0.3038, 0.3011, 0.2532, 0.1999 }
		});
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		int results[] = hungarianAlgorithm.execute();
		for (int index = 0; index < results.length; index++) {
			System.out.println(results[index] + 1);
		}
	}
	
	
	/**
	 * TEST ACCORDING DOI. 10.1137/S0036144502415960
	 */
	@Test
	void test6() {
		double matrix1[][] = new double[][] {
			//1  2, 3 
			{ 0, 1, 0}, //1 
			{ 0, 0, 1}, //2
			{ 0, 0, 0}, //3
		};
		double matrix2[][] = new double[][] {
			//1  2, 3, 4, 5
			{ 0, 1, 1, 0, 0}, //1 
			{ 0, 0, 1, 0, 1}, //2
			{ 0, 0, 0, 1, 1}, //3
			{ 0, 1, 0, 0, 0}, //4
			{ 0, 0, 0, 0, 0}, //5
		};
		
		GraphsVertexSimilarities graphsVertexSimilarities = new GraphsVertexSimilarities(matrix1, matrix2);
		double mergedSimilarityMatrix[][] = graphsVertexSimilarities.computeSimilarityMatrices();
		roundMatrix(mergedSimilarityMatrix);
		assertArrayEquals(mergedSimilarityMatrix, new double[][] {
			{0.4433, 0.1043, 0 		},
			{0.2801, 0.3956, 0.0858 }, //0.1286 -> 0.1268
			{0.0858, 0.3956, 0.2801 },
			{0.2216, 0.0489, 0.2216 },
			{0	   , 0.1043, 0.4433	}
		});
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		int results[] = hungarianAlgorithm.execute();
		for (int index = 0; index < results.length; index++) {
			System.out.println(results[index] + 1);
		}
	}
	
	@Test
	//https://www.wisdomjobs.com/e-university/quantitative-techniques-for-management-tutorial-297/hungarian-method-for-solving-assignment-problem-9898.html
	void testHungarian() {
		double matrix[][] = new double[][] {
			{7, 11, 3, 6},
			{0, 11, 13, 13},
			{23, 0, 0, 0},
			{9, 12, 11, 0}
		};
		//GETTING MINIMAL OUTCOME - OPTIMALIZATION
		HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(matrix);
		int results[] = hungarianAlgorithm.execute();
		assertArrayEquals(results, new int[] {2, 0, 1, 3});
		
		//GETTING MAXIMAL OUTCOME
		MaximizedHungarianMethodWrapper hungarianAlgorithm2 = new MaximizedHungarianMethodWrapper(matrix);
		int results2[] = hungarianAlgorithm2.execute();
		assertArrayEquals(results2, new int[] {1, 3, 0, 2});
	}
	
	private void roundMatrix(double[][] similarityMatrix) {
		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < similarityMatrix[0].length; columnIndex++) {
				similarityMatrix[rowIndex][columnIndex] = Math.round(similarityMatrix[rowIndex][columnIndex] * 10000.0) / 10000.0;
			}
		}
	}
	
	private void printSimilarityMatrix(double[][] similarityMatrix) {
		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < similarityMatrix[0].length; columnIndex++) {
				System.out.print(similarityMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
}
