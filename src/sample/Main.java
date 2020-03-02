package sample;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//КЛАСС ПРОДУКТА
class Product {
    public int id;
    public String name;

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

//КЛАСС ПРОИЗВОДИТЕЛЯ
class Producer implements Runnable {
    private static final String[] productTypes = new String[]{"хлеб", "молоко", "сыр"};

    private Queue<Product> products;
    private Lock productsLock;
    private int counter;

    public Producer(Queue<Product> products, Lock productsLock) {
        this.products = products;
        this.productsLock = productsLock;
        this.counter = 0;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            productsLock.lock();
            try {
                Product product = new Product(++counter, productTypes[Main.random.nextInt(3)]);
                System.out.println("Продукт " + product.name + " произведен");
                products.add(product);
            } finally {
                productsLock.unlock();
            }

            try {
                Thread.sleep(Main.random.nextInt(50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Закончил производство");
    }
}

//КЛАСС ПОТРЕБИТЕЛЯ
class Consumer implements Runnable {
    private Queue<Product> products;
    private Lock productsLock;

    public Consumer(Queue<Product> products, Lock productsLock) {
        this.products = products;
        this.productsLock = productsLock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100;) {
            productsLock.lock();
            try {
                Product product = products.poll();
                if (product == null) {
                    System.out.println(Thread.currentThread().getId() + ": продуктов нет, жду");
                } else {
                    System.out.println(Thread.currentThread().getId() + ": приобрел продукт " + product.name);
                    i++;
                }
            } finally {
                productsLock.unlock();
            }
        }
        System.out.println(Thread.currentThread().getId() + ": приобрел все необходимое");
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    private static final int numConsumers = 10;

    public static void main(String[] args) throws InterruptedException {

    Queue<Product> products = new LinkedList<>();
    Lock productsLock = new ReentrantLock();

    Thread producerThread = new Thread(new Producer(products, productsLock));
    producerThread.start();

    Thread [] consumerThreads = new Thread[numConsumers];
    for (int i = 0; i < numConsumers; i++) {
        consumerThreads[i] = new Thread(new Consumer(products, productsLock));
        consumerThreads[i].start();
    }

    for (int i = 0; i < numConsumers; i++) {
        consumerThreads[i].join();
    }

    producerThread.join();

        //ОДНОПОТОЧНОЕ УМНОЖЕНИЕ МАТРИЦ
//        singleThreadedMatrixMultiplication();

        //МНОГОПОТОЧНОЕ УМНОЖЕНИЕ МАТРИЦ
//        int
//                firstMatrixHeight, firstMatrixWidth,
//                secondMatrixHeight, secondMatrixWidth,
//                numThreads,
//                resultMatrixHeight, resultMatrixWidth, resultMatrixNumberOfElements,
//                numberOfElementsToThread;
//        long startProgram, endProgram;
//
//        int [][] firstMatrix, secondMatrix, resultMatrix;
//        Thread [] threads;
//
//        System.out.println("Пользователь, введи высоту первой матрицы: ");
//        firstMatrixHeight = resultMatrixHeight = scanner.nextInt();
//        System.out.println("Пользователь, введи ширину первой матрицы, она же высота второй: ");
//        firstMatrixWidth = secondMatrixHeight = scanner.nextInt();
//        System.out.println("Пользователь, введи ширину второй матрицы: ");
//        secondMatrixWidth = resultMatrixWidth = scanner.nextInt();
//        System.out.println("Пользователь, введи количество потоков: ");
//        numThreads = scanner.nextInt();
//
//        firstMatrix = new int[firstMatrixHeight][firstMatrixWidth];
//        secondMatrix = new int[firstMatrixWidth][secondMatrixWidth];
//        threads = new Thread[numThreads];
//
//        System.out.println("Твоя первая матрица: ");
//        randomMatrixFilling(firstMatrixHeight, firstMatrixWidth, firstMatrix);
//
//        System.out.println("Твоя вторая матрица: ");
//        randomMatrixFilling(secondMatrixHeight, secondMatrixWidth, secondMatrix);
//
//        resultMatrix = new int [resultMatrixHeight][resultMatrixWidth]; //объявление конечной матрицы
//        resultMatrixNumberOfElements = resultMatrixHeight * resultMatrixWidth; //количество элементов конечной матрицы
//        numberOfElementsToThread = resultMatrixNumberOfElements / numThreads; //сколько элементов передаем в каждый поток
//
//        startProgram = System.nanoTime();
//
//
//        for (int i = 0; i < numThreads; i++) {
//            int start = i * numberOfElementsToThread;
//            int end = start + numberOfElementsToThread;
//
//            threads[i] = new Thread(new MatrixMultiplicationThreads(firstMatrix, secondMatrix, resultMatrix, start, end));
//            threads[i].start();
//        }
//
//        int numberOfElementsToMainThread = resultMatrixNumberOfElements % numThreads; //остатки элементов конечной матрицы, кот. считает главный поток
//        int start = resultMatrixNumberOfElements - numberOfElementsToMainThread;
//        MatrixMultiplicationThreads multiThread = new MatrixMultiplicationThreads(firstMatrix, secondMatrix, resultMatrix, start, resultMatrixNumberOfElements);
//        multiThread.run();
//
//        for (int i = 0; i < numThreads; i++) {
//            threads[i].join();
//        }
//
//        endProgram = System.nanoTime();
//
//        System.out.println("Конечная матрица: ");
//        for (int i = 0; i < resultMatrixHeight; i++) {
//            for (int j = 0; j < resultMatrixWidth; j++) {
//                System.out.print(" " + resultMatrix[i][j] + " ");
//            }
//            System.out.println();
//        }
//
//        System.out.println("Время выполнения программы: " + (endProgram - startProgram));

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

        //multithreadedCalculation(arrSize, numThreads, data); //вызов вынесенной функции многопоточного возведения элементов массива в квадрат

        //endProgram = System.nanoTime();

        //System.out.println();
        //System.out.println("После: ");

        //for (int i = 0; i < arrSize; i++) {
        //System.out.println(i + ". " + data[i]);
        //}

        //System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }


    //ЗАПОЛНЕНИЕ МАТРИЦЫ РАНДОМНЫМИ ЧИСЛАМИ
    public static void randomMatrixFilling(int matrixHeight, int matrixWidth, int [][] matrix) {
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                matrix[i][j] = random.nextInt(10) + 1;
                System.out.print(" " + matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    //ВЫЧИСЛЕНИЕ ПРОИЗВЕДЕНИЯ МАТРИЦ
    public static void calculationOfTheResultingMatrix(int ind, int resultMatrixNumberOfElements, int [][] firstMatrix, int [][] secondMatrix, int [][] resultMatrix) {
        for (int index = ind; index < resultMatrixNumberOfElements; index++) {
            int sum = 0;
            int i = index / secondMatrix[0].length;
            int j = index % secondMatrix[0].length;
            for (int k = 0; k < secondMatrix.length; k++) {
                sum += firstMatrix[i][k] * secondMatrix[k][j];
            }
            resultMatrix[i][j] = sum;
        }
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
        randomMatrixFilling(firstMatrixHeight, firstMatrixWidth, firstMatrix);

        System.out.println("Твоя вторая матрица: ");
        randomMatrixFilling(secondMatrixHeight, secondMatrixWidth, secondMatrix);

        resultMatrix = new int [resultMatrixHeight][resultMatrixWidth]; //объявление конечной матрицы
        resultMatrixNumberOfElements = resultMatrixHeight * resultMatrixWidth; //количество элементов конечной матрицы

        startProgram = System.nanoTime();

        calculationOfTheResultingMatrix(0, resultMatrixNumberOfElements, firstMatrix, secondMatrix, resultMatrix);

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