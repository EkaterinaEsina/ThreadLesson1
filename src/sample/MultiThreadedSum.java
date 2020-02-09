package sample;

import java.util.Random;
import java.util.Scanner;

public class MultiThreadedSum {
    public static void multiThreadedSum() throws InterruptedException {
        long startProgram, endProgram;

        int arrSize, numThreads;
        Scanner scanner = new Scanner(System.in);

        Random random = new Random();

        int [] data;
        Thread [] threads;

        System.out.println("Пользователь, введи количество элементов массива: ");
        arrSize = scanner.nextInt();
        System.out.println("Пользователь, введи количество потоков: ");
        numThreads = scanner.nextInt();

        data = new int[arrSize];
        threads = new Thread[numThreads];

        System.out.println("Твой массив: ");
        for (int i = 0; i < arrSize; i++) {
            data[i] = random.nextInt(100) + 1;
            System.out.print(data[i] + ", ");
        }

        startProgram = System.nanoTime();

        int numElements = arrSize / numThreads;
        int [] sumFromThreads;
        sumFromThreads = new int[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int start = i * numElements;
            int end = start + numElements;

            sumFromThreads[i] = 0;

            threads[i] = new Thread(new SumThreads(data, start, end, sumFromThreads, i));
            threads[i].start();
        }

        int rem = arrSize % numThreads;
        int sumsum = 0;
        int sumMainThread = 0;

        for (int i = 0; i < rem; i++) {
            int index = arrSize - 1 - i;
            sumMainThread = sumMainThread + data[index];
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        for (int i = 0; i < numThreads; i++) {
            sumsum = sumsum + sumFromThreads[i];
        }

        sumsum = sumsum + sumMainThread;

        System.out.println();
        System.out.println("Сумма: " + sumsum);

        endProgram = System.nanoTime();
        System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }
}
