package linkSimilarity;

public class CorrelationDegreeMatrix {
	public static final double CORRELATION_FACTOR = 0.5;
	private double correlationFactor = 0.5;
	
	public CorrelationDegreeMatrix() {
		this(0.5);
	}
	
	public CorrelationDegreeMatrix(double correlationFactor) {
		this.correlationFactor = correlationFactor;
	}
	
	public static double[][] floydWarshall(double weightMatrix[][]) {
		int matrixSize = weightMatrix.length;
	    double shortestPathMatrix[][] = new double[matrixSize][matrixSize];
	    int i, j, k;

	    for (i = 0; i < matrixSize; i++) {
	      for (j = 0; j < matrixSize; j++) {
	    	  shortestPathMatrix[i][j] = weightMatrix[i][j];
	      }
	    }
	    
	    for (k = 0; k < matrixSize; k++) {
	      for (i = 0; i < matrixSize; i++) {
	        for (j = 0; j < matrixSize; j++) {
	          if (shortestPathMatrix[i][k] + shortestPathMatrix[k][j] < shortestPathMatrix[i][j])
	        	  shortestPathMatrix[i][j] = shortestPathMatrix[i][k] + shortestPathMatrix[k][j];
	        }
	      }
	    }

	    //printDoubleMatrix(shortestPathMatrix);
	    return shortestPathMatrix;
	}
	
	public static double[][] createCorrelationDegreeMatrix(
			double weightMatrix[][], long adjacencyMatrix[][], double correlationFactor) {
		int matrixSize = weightMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];

	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weightMatrix, adjacencyMatrix, correlationFactor, shortestPathMatrix, distance, shortestWeightsPathMatrix);

	    //printDoubleMatrix(shortestWeightsPathMatrix);
	    //System.out.println();
	    //printDistanceMatrix(distance);
	    return constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, correlationFactor);
	}

	public static void printDoubleMatrix(double[][] shortestMathMatrix) {
		int matrixSize = shortestMathMatrix.length;
		for (int rowIndex = 0; rowIndex < matrixSize; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < matrixSize; columnIndex++) {
				System.out.print(shortestMathMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
	
	public static void printDistanceMatrix(int[][] distanceMatrix) {
		int matrixSize = distanceMatrix.length;
		for (int rowIndex = 0; rowIndex < matrixSize; rowIndex++) {
			System.out.println();
			for (int columnIndex = 0; columnIndex < matrixSize; columnIndex++) {
				System.out.print(distanceMatrix[rowIndex][columnIndex]);
				System.out.print(' ');
			}
		}
	}
	
	public static double[][] constructCorrelationDegreeMatrix(
			double[][] shortestWeightPathMatrix, int[][] distanceMatrix, double correlationFactor) {
		int matrixSize = distanceMatrix.length;
		double correlationMatrix[][] = new double[matrixSize][matrixSize];
		
		for (int rowIndex = 0; rowIndex < matrixSize; rowIndex++) {
			for (int columnIndex = 0; columnIndex < matrixSize; columnIndex++) {
				if (shortestWeightPathMatrix[rowIndex][columnIndex] < Long.MAX_VALUE) {
					correlationMatrix[rowIndex][columnIndex] = 
						shortestWeightPathMatrix[rowIndex][columnIndex] * Math.pow(correlationFactor, distanceMatrix[rowIndex][columnIndex]);
				}
			}
		}
		
		//printDoubleMatrix(correlationMatrix);
		return correlationMatrix;
	}
	
	public static void findMinimumWeightsAndDistance(
			double weightMatrix[][], long adjacencyMatrix[][], 
			double correlationFactor, 
			long shortestPathMatrix[][], int distance[][], 
			double shortestWeightsPathMatrix[][]) {
		int matrixSize = weightMatrix.length;
	    int i, j, k;

	    for (i = 0; i < matrixSize; i++) {
	      for (j = 0; j < matrixSize; j++) {
	    	  shortestWeightsPathMatrix[i][j] = weightMatrix[i][j]; //copy
	    	  if (adjacencyMatrix[i][j] != 0) {
	    		  shortestPathMatrix[i][j] = adjacencyMatrix[i][j]; // assign original value
	    		  if (i == j) {
	    			  distance[i][j] = 0; // the same point - 0 length
	    		  } else {
	    			  distance[i][j] = 1; // connection - value 1
	    		  }
	    	  } else {
	    		  if (i == j) {
	    			  // SAME POINT/NODE - zero distance
	    			  distance[i][j] = 0;
	    			  shortestPathMatrix[i][j] = 0;
	    		  } else {
	    		  // NO CONNECTION
	    			  shortestPathMatrix[i][j] = Long.MAX_VALUE; // path value must be overwritten to MAX
	    			  distance[i][j] = Integer.MAX_VALUE; // path length must be overwritten to MAX
	    		  }
	    	  }
	      }
	    }
	  
	   
	    for (k = 0; k < matrixSize; k++) {
	      for (i = 0; i < matrixSize; i++) {
	        for (j = 0; j < matrixSize; j++) {
	          if (shortestPathMatrix[i][k] < Long.MAX_VALUE && 
	        		  shortestPathMatrix[k][j] < Long.MAX_VALUE && 
	        		  shortestPathMatrix[i][k] + shortestPathMatrix[k][j] < shortestPathMatrix[i][j]) {
	        	 shortestPathMatrix[i][j] = shortestPathMatrix[i][k] + shortestPathMatrix[k][j];
	          	 shortestWeightsPathMatrix[i][j] = shortestWeightsPathMatrix[i][k] * shortestWeightsPathMatrix[k][j];
	          	 distance[i][j] = distance[i][k] + distance[k][j];
	          }
	         }
	      }
	    }
	}
}
