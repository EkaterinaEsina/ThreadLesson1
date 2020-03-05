package sample;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class RacerThread implements Runnable {
    private Lock mutex;
    private Lock startLock;
    private Condition startCondition;

    public RacerThread(Lock mutex, Lock startLock, Condition startCondition) {
        this.mutex = mutex;
        this.startLock = startLock;
        this.startCondition = startCondition;
    }

    @Override
    public void run() {
        startLock.lock();
        try {
            System.out.println(Thread.currentThread().getId() + " ожидает");
            startCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            startLock.unlock();
        }

        while (true) {
            mutex.lock();
            try {
                Main.counter--;
                if (Main.counter < 0) break;
                if (Main.counter == 0) {
                    System.out.println(Thread.currentThread().getId() + " Ура, я победил!");
                }
            } finally {
                mutex.unlock();
            }

            try {
                Thread.sleep(Main.random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}