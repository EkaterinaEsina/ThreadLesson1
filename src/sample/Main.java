package sample;

import java.util.Random;
import java.util.Scanner;

import static sample.MultiThreadedSum.multiThreadedSum;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        //ОДНОПОТОЧНОЕ УМНОЖЕНИЕ МАТРИЦ
        //singleThreadedMatrixMultiplication();

        //МНОГОПОТОЧНОЕ УМНОЖЕНИЕ МАТРИЦ
        int
                firstMatrixHeight, firstMatrixWidth,
                secondMatrixHeight, secondMatrixWidth,
                numThreads,
                resultMatrixHeight, resultMatrixWidth, resultMatrixNumberOfElements,
                numberOfElementsToThread;
        long startProgram, endProgram;

        int [][] firstMatrix, secondMatrix, resultMatrix;
        Thread [] threads;
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Пользователь, введи высоту первой матрицы: ");
        firstMatrixHeight = resultMatrixHeight = scanner.nextInt();
        System.out.println("Пользователь, введи ширину первой матрицы, она же высота второй: ");
        firstMatrixWidth = secondMatrixHeight = scanner.nextInt();
        System.out.println("Пользователь, введи ширину второй матрицы: ");
        secondMatrixWidth = resultMatrixWidth = scanner.nextInt();
        System.out.println("Пользователь, введи количество потоков: ");
        numThreads = scanner.nextInt();

        firstMatrix = new int[firstMatrixHeight][firstMatrixWidth];
        secondMatrix = new int[firstMatrixWidth][secondMatrixWidth];
        threads = new Thread[numThreads];

        System.out.println("Твоя первая матрица: ");
        for (int i = 0; i < firstMatrixHeight; i++) {
            for (int j = 0; j < firstMatrixWidth; j++) {
                firstMatrix[i][j] = random.nextInt(10) + 1;
                System.out.print(" " + firstMatrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Твоя вторая матрица: ");
        for (int i = 0; i < secondMatrixHeight; i++) {
            for (int j = 0; j < secondMatrixWidth; j++) {
                secondMatrix[i][j] = random.nextInt(10) + 1;
                System.out.print(" " + secondMatrix[i][j] + " ");
            }
            System.out.println();
        }

        startProgram = System.nanoTime();

        resultMatrix = new int [resultMatrixHeight][resultMatrixWidth]; //объявление конечной матрицы

        resultMatrixNumberOfElements = resultMatrixHeight * resultMatrixWidth; //количество элементов конечной матрицы

        numberOfElementsToThread = resultMatrixNumberOfElements / numThreads; //сколько элементов передаем в каждый поток

        for (int i = 0; i < numThreads; i++) {
            int start = i * numberOfElementsToThread;
            int end = start + numberOfElementsToThread;

            threads[i] = new Thread(new MatrixMultiplicationThreads(firstMatrix, secondMatrix, resultMatrix, start, end));
            threads[i].start();
        }

        int numberOfElementsToMainThread = resultMatrixNumberOfElements % numThreads; //остатки элементов конечной матрицы, кот. считает главный поток
        int start = resultMatrixNumberOfElements - numberOfElementsToMainThread;
        MatrixMultiplicationThreads multiThread = new MatrixMultiplicationThreads(firstMatrix, secondMatrix, resultMatrix, start, resultMatrixNumberOfElements);
        multiThread.run();

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        endProgram = System.nanoTime();

        System.out.println("Конечная матрица: ");
        for (int i = 0; i < resultMatrixHeight; i++) {
            for (int j = 0; j < resultMatrixWidth; j++) {
                System.out.print(" " + resultMatrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Время выполнения программы: " + (endProgram - startProgram));

        //МУЛЬТИПОТОЧНОЕ СУММИРОВАНИЕ ЭЛЕМЕНТОВ МАССИВА
        //MultiThreadedSum multiThreadedSum = new MultiThreadedSum();
        //multiThreadedSum();

        //ОДНОПОТОЧНОЕ ВОЗВЕДЕНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
        //singleThreadedCalculation();


        //МНОГОПОТОЧНОЕ ВОЗВЕДЕНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
        //long startProgram, endProgram;

        //int arrSize, numThreads;
        //Scanner scanner = new Scanner(System.in);

        //Random random = new Random();

        //int [] data;

        //System.out.println("Пользователь, введи количество элементов массива: ");
        //arrSize = scanner.nextInt();
        //System.out.println("Пользователь, введи количество потоков: ");
        //numThreads = scanner.nextInt();

        //data = new int[arrSize];

        //System.out.println("До: ");
        //for (int i = 0; i < arrSize; i++) {
        //data[i] = random.nextInt(100) + 1;
        //System.out.println(i + ". " + data[i]);
        //}

        //startProgram = System.nanoTime();

        //ВЫЗОВ ВЫНЕСЕННОЙ ФУНКЦИИ МНОГОПОТОЧНОГО ВОЗВЕДЕНИЯ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
        //multithreadedCalculation(arrSize, numThreads, data);

        //endProgram = System.nanoTime();

        //System.out.println();
        //System.out.println("После: ");

        //for (int i = 0; i < arrSize; i++) {
        //System.out.println(i + ". " + data[i]);
        //}

        //System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }

    //ОДНОПОТОЧНОЕ УМНОЖЕНИЕ МАТРИЦ
    public static void singleThreadedMatrixMultiplication() {
        int
                firstMatrixHeight, firstMatrixWidth,
                secondMatrixHeight, secondMatrixWidth,
                resultMatrixHeight, resultMatrixWidth, resultMatrixNumberOfElements;
        long startProgram, endProgram;

        int [][] firstMatrix, secondMatrix, resultMatrix;
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Пользователь, введи высоту первой матрицы: ");
        firstMatrixHeight = resultMatrixHeight = scanner.nextInt();
        System.out.println("Пользователь, введи ширину первой матрицы, она же высота второй: ");
        firstMatrixWidth = secondMatrixHeight = scanner.nextInt();
        System.out.println("Пользователь, введи ширину второй матрицы: ");
        secondMatrixWidth = resultMatrixWidth = scanner.nextInt();

        firstMatrix = new int[firstMatrixHeight][firstMatrixWidth];
        secondMatrix = new int[firstMatrixWidth][secondMatrixWidth];

        System.out.println("Твоя первая матрица: ");
        for (int i = 0; i < firstMatrixHeight; i++) {
            for (int j = 0; j < firstMatrixWidth; j++) {
                firstMatrix[i][j] = random.nextInt(10) + 1;
                System.out.print(" " + firstMatrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Твоя вторая матрица: ");
        for (int i = 0; i < secondMatrixHeight; i++) {
            for (int j = 0; j < secondMatrixWidth; j++) {
                secondMatrix[i][j] = random.nextInt(10) + 1;
                System.out.print(" " + secondMatrix[i][j] + " ");
            }
            System.out.println();
        }

        startProgram = System.nanoTime();

        resultMatrix = new int [resultMatrixHeight][resultMatrixWidth]; //объявление конечной матрицы
        resultMatrixNumberOfElements = resultMatrixHeight * resultMatrixWidth; //количество элементов конечной матрицы

        for (int index = 0; index < resultMatrixNumberOfElements; index++) {
            int sum = 0;
            int i = index / secondMatrix[0].length;
            int j = index % secondMatrix[0].length;
            for (int k = 0; k < secondMatrix.length; k++) {
                sum += firstMatrix[i][k] * secondMatrix[k][j];
            }
            resultMatrix[i][j] = sum;
        }

        endProgram = System.nanoTime();

        System.out.println("Конечная матрица: ");
        for (int i = 0; i < resultMatrixHeight; i++) {
            for (int j = 0; j < resultMatrixWidth; j++) {
                System.out.print(" " + resultMatrix[i][j] + " ");
            }
            System.out.println();
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