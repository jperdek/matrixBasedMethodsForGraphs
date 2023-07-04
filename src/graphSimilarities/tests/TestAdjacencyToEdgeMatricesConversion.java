package graphSimilarities.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import graphSimilarities.SourceAndTerminusEdgeMatrices;


class TestAdjacencyToEdgeMatricesConversion {

	@Test
	void test() {
		double matrix[][] = new double[][] {
			//1  2, 3 
			{ 0, 1, 0}, //1 
			{ 0, 0, 1}, //2
			{ 0, 0, 0}, //3
		};
	
		SourceAndTerminusEdgeMatrices sourceTerminusMatrix1 = new SourceAndTerminusEdgeMatrices(matrix);
		double sourceEdgeMatrix[][] = sourceTerminusMatrix1.getSourceEdgeMatrix();
		//printSimilarityMatrix(sourceEdgeMatrix);
		assertArrayEquals(sourceEdgeMatrix, new double[][] {
			{1, 0, 0},
			{0, 1, 0}
		});
		
		double[][] terminusEdgeMatrix = sourceTerminusMatrix1.getTerminusEdgeMatrix();
		//printSimilarityMatrix(terminusEdgeMatrix);
		assertArrayEquals(terminusEdgeMatrix, new double[][] {
			{0, 1, 0},
			{0, 0, 1}
		});
	}

	@Test
	void test2() {
		double matrix[][] = new double[][] {
			//1  2, 3, 4, 5, 6
			{ 0, 1, 0, 0, 0, 0}, //1 
			{ 0, 0, 0, 1, 1, 0}, //2
			{ 0, 0, 0, 1, 0, 0}, //3
			{ 0, 0, 0, 0, 1, 0}, //4
			{ 0, 0, 0, 0, 0, 1}, //5
			{ 0, 0, 0, 0, 0, 0}  //6
		};
		SourceAndTerminusEdgeMatrices sourceTerminusMatrix1 = new SourceAndTerminusEdgeMatrices(matrix);
		double sourceEdgeMatrix[][] = sourceTerminusMatrix1.getSourceEdgeMatrix();
		//printSimilarityMatrix(sourceEdgeMatrix);
		assertArrayEquals(sourceEdgeMatrix, new double[][] {
			{1, 0, 0, 0, 0, 0}, //1-2
			{0, 1, 0, 0, 0, 0}, //2-4
			{0, 1, 0, 0, 0, 0}, //2-5
			{0, 0, 1, 0, 0, 0}, //3-4
			{0, 0, 0, 1, 0, 0}, //4-5
			{0, 0, 0, 0, 1, 0}	//5-6
		});
		
		double[][] terminusEdgeMatrix = sourceTerminusMatrix1.getTerminusEdgeMatrix();
		//printSimilarityMatrix(terminusEdgeMatrix);
		assertArrayEquals(terminusEdgeMatrix, new double[][] {
			{0, 1, 0, 0, 0, 0}, //1-2
			{0, 0, 0, 1, 0, 0}, //2-4
			{0, 0, 0, 0, 1, 0}, //2-5
			{0, 0, 0, 1, 0, 0}, //3-4
			{0, 0, 0, 0, 1, 0}, //4-5
			{0, 0, 0, 0, 0, 1}  //5-6
		});
	}
	
	public void printSimilarityMatrix(double[][] similarityMatrix) {
		for (int rowIndex = 0; rowIndex < similarityMatrix.length; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < similarityMatrix[0].length; columnIndex++) {
				System.out.print(similarityMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
}
