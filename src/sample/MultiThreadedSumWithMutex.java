package sample;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadedSumWithMutex {

    static int sum = 0;

    public static void multiThreadedSumWithMutex() throws InterruptedException {
        long startProgram, endProgram;

        int arrSize, numThreads;
        Scanner scanner = new Scanner(System.in);

        int [] data;
        Thread [] threads;

        System.out.println("Пользователь, введи количество элементов массива: ");
        arrSize = scanner.nextInt();
        System.out.println("Пользователь, введи количество потоков: ");
        numThreads = scanner.nextInt();

        data = new int[arrSize];
        threads = new Thread[numThreads];
        Lock mutex = new ReentrantLock();

        System.out.println("Твой массив: ");
        Main.fillingAndDisplayingArray(data, arrSize);

        startProgram = System.nanoTime();

        int numElements = arrSize / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * numElements;
            int end = start + numElements;

            threads[i] = new Thread(new SumThreadsWithMutex(data, start, end, mutex));
            threads[i].start();
        }

        int rem = arrSize % numThreads;
        int sumMainThread = 0;

        for (int i = 0; i < rem; i++) {
            int index = arrSize - 1 - i;
            sumMainThread = sumMainThread + data[index];
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        sum = sum + sumMainThread;

        endProgram = System.nanoTime();

        System.out.println();
        System.out.println("Сумма элементов массива равна: " + sum);

        System.out.println();
        System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }
}
