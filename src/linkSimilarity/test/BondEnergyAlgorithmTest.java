package linkSimilarity.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import linkSimilarity.affinityOperations.BondEnergyAlgorithm;


class BondEnergyAlgorithmTest {

	@Test
	void test() {
		double[][] testMatrix = new double[][] {
			{ 45, 0, 45, 0},
			{ 0, 80, 5, 75},
			{ 45, 5, 53, 3},
			{ 0, 75, 3, 78}
		};
		
		BondEnergyAlgorithm bondEnergyAlgorithm = new BondEnergyAlgorithm();

		double resultMatrix1[][] = bondEnergyAlgorithm.evaluateBondPermutation(testMatrix);
		double resultMatrix2[][] = bondEnergyAlgorithm.evaluateBondPermutationOptimized(testMatrix);
		assertArrayEquals(resultMatrix1, new double[][] {
			{45.0, 45.0,  0.0,  0.0},
			{45.0, 53.0,  5.0,  3.0},
			{ 0.0,  5.0, 80.0, 75.0},
			{ 0.0,  3.0, 75.0, 78.0}
		});
		assertArrayEquals(resultMatrix2, new double[][] {
			{45.0, 45.0,  0.0,  0.0},
			{45.0, 53.0,  5.0,  3.0},
			{ 0.0,  5.0, 80.0, 75.0},
			{ 0.0,  3.0, 75.0, 78.0}
		});
	}

}
