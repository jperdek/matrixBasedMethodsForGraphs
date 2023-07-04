package linkSimilarity.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import linkSimilarity.overlappingClusters.ExtendingAlgorithm;


class ExtendedMatrixTest {

	@Test
	void test() {
		double[][] testMatrix = new double[][] {
			{ 45, 0, 45, 0, 1},
			{ 0, 80, 5, 75, 6},
			{ 45, 5, 53, 3, 7},
			{ 0, 75, 3, 78, 8},
			{ 10,11,12, 13, 14}
		};
		
		Set<Integer> chosenNodes = new HashSet<Integer>();
		chosenNodes.add(2);
		chosenNodes.add(4);
		
		double extendedMatrix[][] = ExtendingAlgorithm.extendPartitionedMatrix(0, 2, testMatrix, chosenNodes);
		assertArrayEquals(extendedMatrix, new double[][] {
			{ 45, 0, 45, 1},
			{ 0, 80, 5, 6},
			{ 45, 5, 53, 7},
			{ 10,11,12, 14}
		});
		
		Set<Integer> chosenNodes2 = new HashSet<Integer>();
		chosenNodes2.add(0);
		double extendedMatrix2[][] = ExtendingAlgorithm.extendPartitionedMatrix(2, 5, testMatrix, chosenNodes2);
		//printMatrix(extendedMatrix2);
		/*assertArrayEquals(extendedMatrix2, new double[][] {
			{ 45, 45, 0, 1},
			{ 45, 53, 3, 7},
			{ 0,  3, 78, 8},
			{ 10,12, 13, 14}
		});*/
		
		// exchanged first row to last and after also first column to last
		assertArrayEquals(extendedMatrix2, new double[][] {
			{ 53, 3, 7, 45},
			{ 3, 78, 8, 0},
			{ 12, 13, 14, 10},
			{ 45, 0, 1, 45},
		});
	}

	/**
	 * To print matrix values
	 * 
	 * @param matrix - matrix which should be printed
	 */
	private static void printMatrix(double matrix[][]) {
		System.out.println();
		for (int rowIndex=0; rowIndex < matrix.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < matrix.length; columnIndex++) {
				System.out.print(matrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
}
