package sample;

public class SquareThreads implements Runnable {

    private int [] data;
    private int start, end;

    public SquareThreads(int [] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            data[i] = data[i] * data[i];
        }
    }
}