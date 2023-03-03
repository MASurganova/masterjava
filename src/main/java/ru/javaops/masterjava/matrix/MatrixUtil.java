package ru.javaops.masterjava.matrix;

import ru.javaops.masterjava.service.MailService;

import java.util.*;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        Set<Callable<Void>> tasks = new HashSet<>();
        for (int i = 0; i < matrixSize; i++) {
            int[] columnB = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                columnB[j]= matrixB[j][i];
            }
            int col = i;
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() {
                    for (int row = 0; row < matrixSize; row++) {
                        int sum = 0;
                        int[] rowA = matrixA[row];
                        for (int k = 0; k < matrixSize; k++) {
                            sum += rowA[k] * columnB[k];
                        }
                        matrixC[row][col] = sum;
                    }
                    return null;
                }
            });
        }
        executor.invokeAll(tasks);
        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int col = 0; col < matrixSize; col++) {
            int[] columnB = new int[matrixSize];
            for (int i = 0; i < matrixSize; i++) {
                columnB[i]= matrixB[i][col];
            }
            for (int row = 0; row < matrixSize; row++) {
                int sum = 0;
                int[] rowA = matrixA[row];
                for (int k = 0; k < matrixSize; k++) {
                    sum += rowA[k] * columnB[k];
                }
                matrixC[row][col] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
