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

        Main.calculationOfTheResultingMatrix(start, end, firstMatrix, secondMatrix, resultMatrix);

    }
}