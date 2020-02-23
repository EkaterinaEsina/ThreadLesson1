package sample;

public class MatrixMultiplicationThreads implements Runnable {

    private int [][] firstMatrix, secondMatrix, resultMatrix;
    private int start, end;

    public MatrixMultiplicationThreads(int [][] firstMatrix, int [][] secondMatrix, int [][] resultMatrix, int start, int end) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {

        for (int index = start; index < end; index++) {
            int sum = 0;
            int i = index / secondMatrix[0].length;
            int j = index % secondMatrix[0].length;
            for (int k = 0; k < secondMatrix.length; k++) {
                sum += firstMatrix[i][k] * secondMatrix[k][j];
            }
            resultMatrix[i][j] = sum;
        }

    }
}