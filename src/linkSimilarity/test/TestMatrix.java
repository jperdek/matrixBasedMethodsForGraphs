package linkSimilarity.test;

/**
 * Ajdacency matrix should not contain INFINITE VALUES - INFINITE NUMBER OF CONNECTIONS!!!
 * -minimum weight will be 0
 * 
 * @author perde
 *
 */
public class TestMatrix {
	private long[][] adjacencySymmetricMatrix = 
		{ 	{ 0, 1, 2, 0 },	 //3
			{ 1, 0, 3, 1 },	 //5
			{ 2, 3, 0, 2 },	 //7
			{ 0, 1, 2, 0 }	 //3
			//3  5  7  3
		};
	
	private long[][] adjacencyAsymmetricMatrix = 
		{ 	{ 0, 1, 2, 2 },  //5
			{ 1, 0, 3, 1 },  //5
			{ 0, 1, 0, 3 },  //4
			{ 0, 1, 2, 0 }   //3
			//1  3  7  6 	 //MAX 5, MIN 3
			//MAX 7, MIN 1
		};
	
	public long[][] getAdjacencySymmetricMatrix() { return this.adjacencySymmetricMatrix; }
	
	public long[][] getAdjacencyAsymmetricMatrix() { return this.adjacencyAsymmetricMatrix; }
}			
