package analysisAPI.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestMatrixMerge {

	@Test
	void test() {
		String matrix1Names[] = new String[] {"A1", "B1", "C1", "D1", "E1"};
		double matrix1[][] = new double[][] {
			{1.1, 2.1, 3.1, 4.1, 5.1},
			{6.1, 7.1, 8.1, 9.1, 10.1},
			{11.1, 12.1, 13.1, 14.1, 15.1},
			{16.1, 17.1, 18.1, 19.1, 20.1},
			{21.1, 22.1, 23.1, 24.1, 25.1}
		};
		
		String matrix2Names[] = new String[] {"A2", "B2", "C2", "D2"};
		double matrix2[][] = new double[][] {
			{1.2, 2.2, 3.2, 4.2},
			{5.2, 6.2, 7.2, 8.2},
			{9.2, 10.2, 11.2, 12.2},
			{13.2, 14.2, 15.2, 16.2}
		};

		int mapping[] = new int[]{-1, 2, 1, -1};
		MatricesMerger matrixMerger = new MatricesMerger();
		double matrix[][] = matrixMerger.mergeMatricesAccordingMappingFromSecondOneUnion(matrix2, matrix1, mapping);
		assertArrayEquals(matrix, new double[][] {
			{ 1.1,  2.1,  3.1,  4.1,  5.1, 0.0, 0.0},
			{ 6.1,  7.1,  8.1,  9.1, 10.1, 9.2, 12.2},
			{11.1, 12.1, 13.1, 14.1, 15.1, 5.2,  8.2},
			{16.1, 17.1, 18.1, 19.1, 20.1, 0.0, 0.0},
			{21.1, 22.1, 23.1, 24.1, 25.1, 0.0, 0.0},
			{ 0.0,  3.2,  2.2,  0.0,  0.0, 1.2, 4.2},
			{ 0.0, 15.2, 14.2,  0.0,  0.0, 13.2, 16.2},
		});
		String mergedNames[] = matrixMerger.transferNamesAccordingMapping(mapping, matrix2Names, matrix1Names);
		assertArrayEquals(new String[] {"A1", "B1 | C2", "C1 | B2", "D1", "E1", "A2", "D2"}, mergedNames);
	}
	
	@Test
	void test2() {
		String matrix1Names[] = new String[] {"A1", "B1", "C1", "D1"};
		double matrix1[][] = new double[][] {
			{1.1, 2.1, 3.1, 4.1},
			{5.1, 6.1, 7.1, 8.1},
			{9.1, 10.1, 11.1, 12.1},
			{13.1, 14.1, 15.1, 16.1}
		};
		
		String matrix2Names[] = new String[] {"A2", "B2", "C2"};
		double matrix2[][] = new double[][] {
			{1.2, 2.2, 3.2},
			{5.2, 6.2, 7.2},
			{9.2, 10.2, 11.2}
		};
		int mapping[] = new int[]{-1, 1, 2};
		MatricesMerger matrixMerger = new MatricesMerger();
		double matrix[][] = matrixMerger.mergeMatricesAccordingMappingFromSecondOneUnion(matrix2, matrix1, mapping);
		// printMatrix(matrix);
		assertArrayEquals(matrix, new double[][] {
			{ 1.1,  2.1,  3.1,  4.1, 0.0},
			{ 5.1,  6.1,  7.1,  8.1, 5.2}, // 1->1
			{ 9.1, 10.1, 11.1, 12.1, 9.2}, //2->2
			{13.1, 14.1, 15.1, 16.1, 0.0},
			{ 0.0,  2.2,  3.2,  0.0, 1.2} //0->0
			//    1->1  2->2		
		});
		String mergedNames[] = matrixMerger.transferNamesAccordingMapping(mapping, matrix2Names, matrix1Names);
		assertArrayEquals(new String[] {"A1", "B1 | B2", "C1 | C2", "D1", "A2"}, mergedNames);
	}
	
	@Test
	void test3() {
		String matrix1Names[] = new String[] {"A1", "B1", "C1", "D1"};
		double matrix1[][] = new double[][] {
			{1.1, 2.1, 3.1, 4.1},
			{5.1, 6.1, 7.1, 8.1},
			{9.1, 10.1, 11.1, 12.1},
			{13.1, 14.1, 15.1, 16.1}
		};
		
		String matrix2Names[] = new String[] {"A2", "B2", "C2"};
		double matrix2[][] = new double[][] {
			{1.2, 2.2, 3.2},
			{5.2, 6.2, 7.2},
			{9.2, 10.2, 11.2}
		};
		int mapping[] = new int[]{-1, 2, 1};
		MatricesMerger matrixMerger = new MatricesMerger();
		double matrix[][] = matrixMerger.mergeMatricesAccordingMappingFromSecondOneUnion(matrix2, matrix1, mapping);

		assertArrayEquals(matrix, new double[][] {
			{ 1.1,  2.1,  3.1,  4.1, 0.0},
			{ 5.1,  6.1,  7.1,  8.1, 9.2}, //2->1
			{ 9.1, 10.1, 11.1, 12.1, 5.2}, //1->2
			{13.1, 14.1, 15.1, 16.1, 0.0},
			{ 0.0,  3.2,  2.2,  0.0, 1.2} //0
			//    2->1  1->2
		});
		
		String mergedNames[] = matrixMerger.transferNamesAccordingMapping(mapping, matrix2Names, matrix1Names);
		assertArrayEquals(new String[] {"A1", "B1 | C2", "C1 | B2", "D1", "A2"}, mergedNames);
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

}
