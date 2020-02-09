package sample;

public class SumThreads implements Runnable {

    private int [] data, sumFromThreads;
    private int start, end, index;

    public SumThreads(int [] data, int start, int end, int [] sumFromThreads, int i) {
        this.data = data;
        this.sumFromThreads = sumFromThreads;
        this.start = start;
        this.end = end;
        this.index = i;
    }

    @Override
    public void run() {
        int sum = 0;

        for (int i = start; i < end; i++) {
            sum = sum + data[i];
        }

        sumFromThreads[index] = sum;
    }
}