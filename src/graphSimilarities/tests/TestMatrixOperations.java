package graphSimilarities.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import graphSimilarities.GraphsVertexSimilarities;

class TestMatrixOperations {

	@Test
	void test() {
		double matrix1[][] = new double[][] {
			{1, 2},
			{3, 4},
		};
		double matrix2[][] = new double[][] {
			{5, 6},
			{7, 8},
		};
		
		double matrix23[][] = new double[][] {
			{5, 6, 1},
			{7, 8, 9},
		};
		
		double result4[][] = GraphsVertexSimilarities.transposeMatrix(matrix1);
		assertArrayEquals(result4, new double[][] {
			{1, 3},
			{2, 4}
		});
		
		double result23[][] = GraphsVertexSimilarities.transposeMatrix(matrix23);
		assertArrayEquals(result23, new double[][] {
			{5, 7},
			{6, 8},
			{1, 9}
		});
		
		double result1[][] = GraphsVertexSimilarities.multiplyMatrixWithTransposedMatrixNewMatrix(matrix1, matrix2);
		assertArrayEquals(result1, new double[][] {
			{1 * 5 + 2 * 6, 1 * 7 + 2 * 8},
			{3 * 5 + 4 * 6, 3 * 7 + 4 * 8}
		});
		
		double result2[][] = GraphsVertexSimilarities.multiplyMatrixWithMatrixNewMatrix(matrix1, matrix2);
		assertArrayEquals(result2, new double[][] {
			{1 * 5 + 2 * 7, 1 * 6 + 2 * 8},
			{3 * 5 + 4 * 7, 3 * 6 + 4 * 8}
		});
		
		double result3[][] = GraphsVertexSimilarities.multiplyMatrixTransposedWithMatrixNewMatrix(matrix2, matrix1);
		assertArrayEquals(result3, new double[][] {
			{5 * 1 + 7 * 3, 5 * 2 + 7 * 4},
			{6 * 1 + 8 * 3, 6 * 2 + 8 * 4}
		});
		
		double frobeniusNorm1 = GraphsVertexSimilarities.calculateFrobeniusNorm(matrix1);
		assertEquals(frobeniusNorm1, Math.sqrt(1*1 + 2*2 + 3*3 + 4*4));
		
		double frobeniusNorm2 = GraphsVertexSimilarities.calculateFrobeniusNorm(matrix2);
		assertEquals(frobeniusNorm2, Math.sqrt(5*5 + 6*6 + 7*7 + 8*8));
		
		
		GraphsVertexSimilarities.divideMatrix(matrix1, 5);
		assertArrayEquals(matrix1, new double[][] {
			{1.0 / 5, 2.0 / 5},
			{3.0 / 5, 4.0 / 5}
		});
	}

}
