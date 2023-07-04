package graphSimilarities.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import graphSimilarities.HungarianAlgorithm;
import graphSimilarities.GraphsVertexEdgeSimilarities;

class TestGraphVertexEdgeSimilarities {

	/**
	 * TEST ACCORDING Graph similarity scoring and matching, Laura A. Zager∗, George C. Verghese
	 * - should pass with only round errors
	 */
	@Test
	void test() {
		double matrix1[][] = new double[][] {
			//1  2, 3 
			{ 0, 1, 0}, //1 
			{ 0, 0, 1}, //2
			{ 0, 0, 0}, //3
		};
		double matrix2[][] = new double[][] {
			//1  2, 3, 4, 5, 6
			{ 0, 1, 0, 0, 0, 0}, //1 
			{ 0, 0, 0, 1, 1, 0}, //2
			{ 0, 0, 0, 1, 0, 0}, //3
			{ 0, 0, 0, 0, 1, 0}, //4
			{ 0, 0, 0, 0, 0, 1}, //5
			{ 0, 0, 0, 0, 0, 0}  //6
		};
		
		GraphsVertexEdgeSimilarities graphsVertexEdgeSimilarities = new GraphsVertexEdgeSimilarities(matrix1, matrix2);
		graphsVertexEdgeSimilarities.computeSimilarityMatrices();
		double edgeMatrix[][] = graphsVertexEdgeSimilarities.getConvergedMatrixX();
		this.roundMatrix(edgeMatrix);
		assertArrayEquals(edgeMatrix, new double[][] {
			{0.265, 0},
			{0.426, 0.297},
			{0.320, 0.389},
			{0.336, 0.115},
			{0.202, 0.445},
			{0	  , 0.202},
		});
		//printSimilarityMatrix(edgeMatrix);
		
		//System.out.println("----------------------->");
		double vertexMatrix[][] = graphsVertexEdgeSimilarities.getConvergedMatrixY();
		//printSimilarityMatrix(vertexMatrix);
		this.roundMatrix(vertexMatrix);
		//printSimilarityMatrix(vertexMatrix);
		assertArrayEquals(vertexMatrix, new double[][] {
			{0.124, 0, 	   0},
			{0.348, 0.444, 0}, 		//0.445 -> 0.444
			{0.157, 0.054, 0},
			{0.094, 0.564, 0.192}, 	//0.563 -> 0.564; 0.193 -> 0.192
			{0	  , 0.338, 0.389},  //0.340 -> 0.389
			{0	  , 0	 , 0.094}
		});
	}


	/**
	 * TEST ACCORDING PRSENTATION FOR DOI. 10.1137/S0036144502415960
	 * - should pass with only round errors
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
		
		GraphsVertexEdgeSimilarities graphsVertexEdgeSimilarities = new GraphsVertexEdgeSimilarities(matrix1, matrix2);
		graphsVertexEdgeSimilarities.computeSimilarityMatrices();
		
		double vertexMatrix[][] = graphsVertexEdgeSimilarities.getConvergedMatrixY();
		//System.out.println("-------------------->");
		//printSimilarityMatrix(vertexMatrix);
		roundMatrix(vertexMatrix);
		
		//System.out.println("-------------------->");
		//printSimilarityMatrix(vertexMatrix);
		assertArrayEquals(vertexMatrix, new double[][] {
			{0.324, 0.054, 0 	},
			{0.177, 0.587, 0.017}, //0.018 -> 0.017
			{0.017, 0.587, 0.177}, //0.018 -> 0.017
			{0.127, 0.010, 0.127},
			{0	  , 0.054, 0.324}
		});
		HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(vertexMatrix);
		int results[] = hungarianAlgorithm.execute();
		for (int index = 0; index < results.length; index++) {
			System.out.println(results[index] + 1);
		}
	}

	private void roundMatrix(double[][] similarityMatrix) {
		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < similarityMatrix[0].length; columnIndex++) {
				similarityMatrix[rowIndex][columnIndex] = Math.round(similarityMatrix[rowIndex][columnIndex] * 1000.0) / 1000.0;
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
