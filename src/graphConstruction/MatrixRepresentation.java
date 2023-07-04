package graphConstruction;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixRepresentation {

	private String names[];
	private long matrix[][];
	
	MatrixRepresentation(long matrix[][], String names[]) {
		this.matrix = matrix;
		this.names = names;
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
	
	public long[][] getMatrix() { return this.matrix; }
	
	public String[] getMatrixNames() { return this.names; }
	
	public void saveToFile(String pathToFile) {
		FileWriter fw = null;
		BufferedWriter bufferedWriter = null;
		try {
			fw = new FileWriter(pathToFile);
			bufferedWriter = new BufferedWriter(fw);
			
			//bufferedWriter.append("[");
			for (int i = 0; i < matrix.length; i++) {
				//bufferedWriter.append("[");
				for (int j = 0; j < matrix[i].length; j++) {
					bufferedWriter.append(Long.toString(matrix[i][j]));
					if (j != matrix[i].length - 1) {
						bufferedWriter.append(", ");
					}
				}
				//bufferedWriter.append("]");
				bufferedWriter.append("\n");
			}
			//bufferedWriter.append("]\n");
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedWriter != null) { 
				try { bufferedWriter.close(); }
				catch(IOException e) {
					e.printStackTrace();
				};
			}
			if(fw != null) { 
				try {
					fw.close();
				} catch(IOException e) {
				
				};
			}
		}
	}

}
