package linkSimilarity;
//https://www.geeksforgeeks.org/java-program-to-find-the-determinant-of-a-matrix/


public class MatrixDeterminant {
 
    // Function to get determinant of matrix
    public static double determinantOfMatrix(double matrix[][], int n)
    {
    	double num1, num2, det = 1;
    	int index;
    	double total = 1;
 
        double[] temp = new double[n + 1];
 

        for (int i = 0; i < n; i++) {
            index = i;
 
            while (index < n && matrix[index][i] == 0) {
                index++;
            }
            
            if (index == n) {
                continue;
            }
            
            
            if (index != i) {
                for (int j = 0; j < n; j++) {
                    swap(matrix, index, j, i, j);
                }

                det = (int)(det * Math.pow(-1, index - i));
            }

            for (int j = 0; j < n; j++) {
                temp[j] = matrix[i][j];
            }

            for (int j = i + 1; j < n; j++) {
                num1 = temp[i];
                num2 = matrix[j][i];
 

                for (int k = 0; k < n; k++) {
                    matrix[j][k] = (num1 * matrix[j][k]) - (num2 * temp[k]);
                }
                total = total * num1;
            }
        }
 
        for (int i = 0; i < n; i++) {
            det = det * matrix[i][i];
        }
        
        if (total == 0.0) {
        	total = 1.0;
        }
        return (det / total);
    }
 
    static double[][] swap(double[][] matrix, int i1, int j1, int i2, int j2)
    {
        double temp = matrix[i1][j1];
        matrix[i1][j1] = matrix[i2][j2];
        matrix[i2][j2] = temp;
        return matrix;
    }
 
    // Driver code
    public static void main(String[] args)
    {
        double matrix[][] = { { 1, 0, 2, -1 },
                        { 3, 0, 0, 5 },
                        { 2, 1, 4, -3 },
                        { 1, 0, 5, 0 } };

        System.out.println("Determinants :" + determinantOfMatrix(matrix, matrix.length));
    }
}
