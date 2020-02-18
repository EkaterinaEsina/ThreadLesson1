package sample;

import java.util.Random;
import java.util.Scanner;

import static sample.MultiThreadedSum.multiThreadedSum;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        //МУЛЬТИПОТОЧНОЕ СУММИРОВАНИЕ ЭЛЕМЕНТОВ МАССИВА
        //MultiThreadedSum multiThreadedSum = new MultiThreadedSum();
        //multiThreadedSum();

        //ОДНОПОТОЧНОЕ ВОЗВЕДЕНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
        //singleThreadedCalculation();


        //МНОГОПОТОЧНОЕ ВОЗВЕДЕНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
        long startProgram, endProgram;

        int arrSize, numThreads;
        Scanner scanner = new Scanner(System.in);

        Random random = new Random();

        int [] data;

        System.out.println("Пользователь, введи количество элементов массива: ");
        arrSize = scanner.nextInt();
        System.out.println("Пользователь, введи количество потоков: ");
        numThreads = scanner.nextInt();

        data = new int[arrSize];

        System.out.println("До: ");
        for (int i = 0; i < arrSize; i++) {
            data[i] = random.nextInt(100) + 1;
            System.out.println(i + ". " + data[i]);
        }

        startProgram = System.nanoTime();

        //ВЫЗОВ ВЫНЕСЕННОЙ ФУНКЦИИ МНОГОПОТОЧНОГО ВОЗВЕДЕНИЯ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
        multithreadedCalculation(arrSize, numThreads, data);

        endProgram = System.nanoTime();

        System.out.println();
        System.out.println("После: ");

        for (int i = 0; i < arrSize; i++) {
            System.out.println(i + ". " + data[i]);
        }

        System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }

    //ВЫНЕСЕНИЕ МНОГОПОТОЧНОГО ВОЗВЕДЕНИЯ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ В ОТДЕЛЬНУЮ ФУНКЦИЮ
    public static void multithreadedCalculation(int arrSize, int numThreads, int [] data) throws InterruptedException {

        Thread [] threads;
        threads = new Thread[numThreads];

        int numElements = arrSize / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * numElements;
            int end = start + numElements;

            threads[i] = new Thread(new SquareThreads(data, start, end));
            threads[i].start();
        }

        int rem = arrSize % numThreads;

        for (int i = 0; i < rem; i++) {
            int index = arrSize - 1 - i;
            data[index] = data[index] * data[index];
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }
    }

    //РЕАЛИЗАЦИЯ ОДНОПОТОЧНОГО ВОЗВЕДЕНИЯ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
    public static void singleThreadedCalculation() {
        long startProgram, endProgram;

        int arrSize;
        Scanner scanner = new Scanner(System.in);

        Random random = new Random();

        int [] data;

        System.out.println("Пользователь, введи количество элементов массива: ");
        arrSize = scanner.nextInt();

        data = new int[arrSize];

        System.out.println("До: ");
        for (int i = 0; i < arrSize; i++) {
            data[i] = random.nextInt(100) + 1;
            System.out.println(i+1 + ". " + data[i]);
        }

        startProgram = System.nanoTime();

        for (int i = 0; i < arrSize; i++) {
            data[i] = data[i] * data[i];
        }

        endProgram = System.nanoTime();


        System.out.println("Полсе: ");
        for (int i = 0; i < arrSize; i++) {
            System.out.println(i+1 + ". " + data[i]);
        }

        System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }

}