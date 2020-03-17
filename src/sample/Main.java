package sample;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static sample.MultiThreadedSum.multiThreadedSum;
import static sample.MultiThreadedSumWithMutex.multiThreadedSumWithMutex;

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
    private Condition noProductsCondition;
    private int counter;

    public Producer(Queue<Product> products, Lock productsLock, Condition noProductsCondition) {
        this.products = products;
        this.productsLock = productsLock;
        this.noProductsCondition = noProductsCondition;
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
                noProductsCondition.signal();
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
    private Condition noProductsCondition;

    public Consumer(Queue<Product> products, Lock productsLock, Condition noProductsCondition) {
        this.products = products;
        this.productsLock = productsLock;
        this.noProductsCondition = noProductsCondition;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100;) {
            productsLock.lock();
            try {
                Product product = products.poll();
                while (product == null) {
                    System.out.println("Поток № " + Thread.currentThread().getId() + ": продуктов нет, жду");
                    try {
                        noProductsCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    product = products.poll();
                }
                System.out.println("Поток № " + Thread.currentThread().getId() + ": приобрел продукт " + product.name);
                i++;
            } finally {
                productsLock.unlock();
            }
        }
        System.out.println("Поток № " + Thread.currentThread().getId() + ": приобрел все необходимое");
    }
}

//=================================================================ПРОИЗВОДИТЕЛЬ ПОТРЕБИТЕЛЬ ФИБОНАЧЧИ=========================================================================

//КЛАСС ПРОДУКТА ДЛЯ ЗАДАЧИ С ЧИСЛОМ ФИБОНАЧЧИ
class ProductFibonacci {
    public int id;
    public String name;

    public ProductFibonacci(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

//КЛАСС ПРОИЗВОДИТЕЛЯ ДЛЯ ЗАДАЧИ С ЧИСЛОМ ФИБОНАЧЧИ
class ProducerFibonacci implements Runnable {

    private Queue <Integer> numbers;
    private Lock lock;
    private File file;
    private Condition condition;

    public ProducerFibonacci(Queue <Integer> numbers, Lock lock, File file, Condition condition) {
        this.numbers = numbers;
        this.lock = lock;
        this.file = file;
        this.condition = condition;
    }

    @Override
    public void run() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);

            Scanner scan = new Scanner(fileReader);

            try {
                lock.lock();
                int i = 1;
                while (scan.hasNextLine()) {
                    numbers.add(Integer.parseInt(scan.nextLine()));
                    i++;
                    condition.signal();
                }
                Main.theEnd = true;
                System.out.println(numbers);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

//КЛАСС ПОТРЕБИТЕЛЯ ДЛЯ ЗАДАЧИ С ЧИСЛОМ ФИБОНАЧЧИ
class ConsumerFibonacci implements Runnable {

    private Queue <Integer> numbers;
    private Lock lock;
    private File file;
    Condition condition;

    public ConsumerFibonacci(Queue <Integer> numbers, Lock lock, File file, Condition condition) {
        this.numbers = numbers;
        this.lock = lock;
        this.file = file;
        this.condition = condition;
    }

    private static int numFib(int fib){
        int a = 0;
        int b = 1;
        for (int i = 2; i <= fib; i++) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

    @Override
    public void run() {

        try {
            lock.lock();
            Integer number = numbers.poll();
            while (Main.theEnd) {
                while (number == null) {
                    System.out.println(Thread.currentThread().getId() + " : чисел нет");
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    number = numbers.poll();

                }

                    Integer fibonacciNumber = numFib(number);
                    System.out.println("Число Фибоначчи № " + number + " равно " + fibonacciNumber);
                    number = numbers.poll();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}

public class Main {
    static int counter;

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static boolean theEnd;

    private static final int numConsumers = 10;

    public static void main(String[] args) throws Exception {

        theEnd = false;
        producerConsumerFibonacci();

    }

    //============================================================ПРОИЗВОДИТЕЛЬ ПОТРЕБИТЕЛЬ ФИБОНАЧЧИ======================================================================

    public static void producerConsumerFibonacci() throws Exception {

        int consumers;

        System.out.println("Пользователь, введи количество потребителей: ");
        consumers = scanner.nextInt();

        Queue <Integer> numbers = new LinkedList<>();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        File dir = new File("C:/Users/eesina/Desktop/FibonacciNumber");
        File file = new File(dir, "FibonacciNumber.txt");

        if (file.createNewFile()) {
            FileWriter fileWriter = new FileWriter(file, false);

            while (file.length()/1024 < 101) {
                for(int i = 1; i < 10000; i++) {
                    fileWriter.write(random.nextInt(100) + "\n");
                }
            }
            fileWriter.close();
        }

        Thread producerThread = new Thread(new ProducerFibonacci(numbers, lock, file, condition));
        producerThread.start();


        Thread [] consumerThreads = new Thread[consumers];
        for (int i = 0; i < consumers; i++) {
            consumerThreads[i] = new Thread(new ConsumerFibonacci(numbers, lock, file, condition));
            consumerThreads[i].start();
        }

        for (int i = 0; i < consumers; i++) {
            consumerThreads[i].join();
        }

        producerThread.join();
    }

    //==============================================================ПРОИЗВОДИТЕЛЬ ПОТРЕБИТЕЛЬ=============================================================================

    public static void producerConsumer() throws InterruptedException {
        Queue<Product> products = new LinkedList<>();
        Lock productsLock = new ReentrantLock();
        Condition noProductsCondition = productsLock.newCondition();

        Thread producerThread = new Thread(new Producer(products, productsLock, noProductsCondition));
        producerThread.start();

        Thread [] consumerThreads = new Thread[numConsumers];
        for (int i = 0; i < numConsumers; i++) {
            consumerThreads[i] = new Thread(new Consumer(products, productsLock, noProductsCondition));
            consumerThreads[i].start();
        }

        for (int i = 0; i < numConsumers; i++) {
            consumerThreads[i].join();
        }

        producerThread.join();
    }

    //=====================================================================ГОНКА ПОТОКОВ==================================================================================

    public static void raceOfThreads() throws InterruptedException {
        int numThreads;

        System.out.println("Пользователь, введи значение счетчика: ");
        counter = scanner.nextInt();
        System.out.println("Пользователь, введи количество потоков: ");
        numThreads = scanner.nextInt();

        Lock mutex = new ReentrantLock();

        Lock startLock = new ReentrantLock();
        Condition startCondition = startLock.newCondition();

        Thread [] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new RacerThread(mutex, startLock, startCondition));
            System.out.println(threads[i].getId() + " участвует в соревновании");
            threads[i].start();
        }

        System.out.println("На старт!");
        Thread.sleep(1000);
        System.out.println("Внимание!");
        Thread.sleep(1000);

        startLock.lock();
        System.out.println("Марш!!!");
        Thread.sleep(1000);
        startCondition.signalAll();
        startLock.unlock();

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        System.out.println("Завершено!");
    }

    //============================================================СУММИРОВАНИЕ ЭЛЕМЕНТОВ МАССИВА==========================================================================

    //ЗАПОЛНЕНИЕ МАССИВА СЛУЧАЙНЫМИ ЧИСЛАМИ И ВЫВОД МАССИВА В КОНСОЛЬ
    public static void fillingAndDisplayingArray(int [] data, int arrSize) {
        for (int i = 0; i < arrSize; i++) {
            data[i] = random.nextInt(100) + 1;
            if (i != arrSize - 1) {
                System.out.print(data[i] + ", ");
            } else {
                System.out.print(data[i]);
                System.out.println("   ");
            }
        }
    }

    //МУЛЬТИПОТОЧНОЕ СУММИРОВАНИЕ ЭЛЕМЕНТОВ МАССИВА
    public static void multiThreadedSumOfArrayElements() throws InterruptedException {
        multiThreadedSum();
    }

    //МУЛЬТИПОТОЧНОЕ СУММИРОВАНИЕ ЭЛЕМЕНТОВ МАССИВА С ИСПОЛЬЗОВПАНИЕМ МЬЮТЕКСА
    public static void multiThreadedSumOfArrayElementsWithMutex() throws InterruptedException {
        multiThreadedSumWithMutex();
    }

    //ОДНОПОТОЧНОЕ СУММИРОВАНИЕ ЭЛЕМЕНТОВ МАССИВА
    public static void singleThreadedSumOfArray() {
        long startProgram, endProgram;

        int arrSize;
        int sum = 0;
        Scanner scanner = new Scanner(System.in);

        int [] data;

        System.out.println("Пользователь, введи количество элементов массива: ");
        arrSize = scanner.nextInt();

        data = new int[arrSize];

        System.out.println("Твой массив: ");
        fillingAndDisplayingArray(data, arrSize);

        startProgram = System.nanoTime();

        for (int i = 0; i < arrSize; i++) {
            sum = sum + data[i];
        }

        endProgram = System.nanoTime();

        System.out.println();
        System.out.println("Сумма: " + sum);

        System.out.println();
        System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }

    //===========================================================================МАТРИЦЫ==================================================================================

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

    //МНОГОПОТОЧНОЕ УМНОЖЕНИЕ МАТРИЦ
    public static void multiThreadedMatrixMultiplication() throws InterruptedException {
        int
                firstMatrixHeight, firstMatrixWidth,
                secondMatrixHeight, secondMatrixWidth,
                numThreads,
                resultMatrixHeight, resultMatrixWidth, resultMatrixNumberOfElements,
                numberOfElementsToThread;
        long startProgram, endProgram;

        int [][] firstMatrix, secondMatrix, resultMatrix;
        Thread [] threads;

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
        randomMatrixFilling(firstMatrixHeight, firstMatrixWidth, firstMatrix);

        System.out.println("Твоя вторая матрица: ");
        randomMatrixFilling(secondMatrixHeight, secondMatrixWidth, secondMatrix);

        resultMatrix = new int [resultMatrixHeight][resultMatrixWidth]; //объявление конечной матрицы
        resultMatrixNumberOfElements = resultMatrixHeight * resultMatrixWidth; //количество элементов конечной матрицы
        numberOfElementsToThread = resultMatrixNumberOfElements / numThreads; //сколько элементов передаем в каждый поток

        startProgram = System.nanoTime();


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
    }

    //========================================================ВОЗВЕЗЕДНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ======================================================================

    //МНОГОПОТОЧНОЕ ВОЗВЕДЕНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
    public static void multiThreadedCalculation() throws InterruptedException {
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

        multithreadedCalculationFunction(arrSize, numThreads, data); //вызов вынесенной функции многопоточного возведения элементов массива в квадрат

        endProgram = System.nanoTime();

        System.out.println();
        System.out.println("После: ");

        for (int i = 0; i < arrSize; i++) {
            System.out.println(i + ". " + data[i]);
        }

        System.out.println("Время выполнения программы: " + (endProgram - startProgram));
    }

    //ВЫНЕСЕНИЕ МНОГОПОТОЧНОГО ВОЗВЕДЕНИЯ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ В ОТДЕЛЬНУЮ ФУНКЦИЮ
    public static void multithreadedCalculationFunction(int arrSize, int numThreads, int [] data) throws InterruptedException {

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

    //ОДНОПОТОЧНОЕ ВОЗВЕДЕНИЕ ЭЛЕМЕНТОВ МАССИВА В КВАДРАТ
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