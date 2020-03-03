package sample;

import java.util.concurrent.locks.Lock;

public class SumThreadsWithMutex implements Runnable {

    private int[] data;
    private int start, end;
    private Lock mutex;

    public SumThreadsWithMutex(int[] data, int start, int end, Lock mutex) {
        this.data = data;
        this.start = start;
        this.end = end;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        int sum = 0;

        for (int i = start; i < end; i++) {
            sum = sum + data[i];
        }

        mutex.lock();

        try {
            MultiThreadedSumWithMutex.sum = MultiThreadedSumWithMutex.sum + sum;

        } finally {
            mutex.unlock();
        }
    }
}

