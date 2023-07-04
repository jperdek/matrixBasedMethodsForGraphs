package graphConstruction;

public class HyperlinkMatrix {

	private String names[];
	private double matrix[][];
	private long connectionsCount[];
	private int size;

	public HyperlinkMatrix(int size, String names[]) {
		this.size = size;
		this.names = names;
		this.matrix = new double[size][size];
		this.connectionsCount = new long[size];
		
		this.nullsCount();
		this.nullsMatrix();
	}
	
	private void nullsCount() {
		for (int i = 0; i < this.size; i++) {
			this.connectionsCount[i] = 0;
		}
	}
	
	private void nullsMatrix() {
		for (int j = 0; j < this.size; j++) {
			for (int i = 0; i < this.size; i++) { //row iteration
				this.matrix[j][i] = 0.0;
			}
		}
	}
	
	public void observeConnectionCount(long[][] adjacencyMatrix) {
		for (int j = 0; j < this.size; j++) {
			for (int i = 0; i < this.size; i++) { //row iteration
				this.connectionsCount[j] += adjacencyMatrix[j][i];
			}
		}
	}
	
	public void divideByNumberLinksPointingFromPage(long[][] adjacencyMatrix) {
		for (int j = 0; j < this.size; j++) {
			if (this.connectionsCount[j] == 0) { continue; }
			for (int i = 0; i < this.size; i++) { //row iteration
				this.matrix[j][i] = (adjacencyMatrix[j][i] + 0.0) / this.connectionsCount[j];
			}
		}
	}

	public void printMatrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println("");
		}
	}

	public static HyperlinkMatrix convertAdjacencyMatrixToHyperlink(MatrixRepresentation adjacencyMatrix) {
		long adjacency[][] = adjacencyMatrix.getMatrix();
		String matrixNames[] = adjacencyMatrix.getMatrixNames();
		int matrixSize = adjacency.length;
		
		HyperlinkMatrix hyperlinkMatrix = new HyperlinkMatrix(matrixSize, matrixNames);
		hyperlinkMatrix.observeConnectionCount(adjacency);
		hyperlinkMatrix.divideByNumberLinksPointingFromPage(adjacency);
		hyperlinkMatrix.printMatrix();
		
		return hyperlinkMatrix;
	}
	
	public int getSize() { return this.size; }
	
	public double[][] getMatrix() { return this.matrix; }
}
