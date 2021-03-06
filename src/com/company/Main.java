package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static Double alpha = 0.00001;
    public static int nb_iterations = 1500000;

    public static Double[][] readSimpleDataset(String path) {
        try {

            int nbLine = 0;
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                myReader.nextLine();
                nbLine++;
            }
            Double[][] result = new Double[2][nbLine];
            myReader.close();
            myReader = new Scanner(myObj);
            int index = 0;
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String values[] = data.split(";");
                for(int i = 0; i< values.length; i++) {
                    result[i][index] = Double.parseDouble(values[i]);
                }
                index++;
                System.out.println(data);
            }
            myReader.close();
            return result;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }
    public static Double[][] buildCoefs(Double data[]) {
        Double coefs[][] = new Double[2][data.length];
        for(int i =0; i < data.length; i++) {
            coefs[0][i] = 1.0;
            coefs[1][i] = data[i];
        }
        return coefs;
    }
    public static Double[][] buildCoefsNDegree(Double data[], int degree) {
        Double coefs[][] = new Double[degree + 1][data.length];
        for(int i =0; i < data.length; i++) {
            for(int d = 0; d <= degree; d ++) coefs[d][i] = Math.pow(data[i], d) * (data[i] < 0 ? -1 : 1);
        }
        return coefs;
    }

    public static Double calculCost(Double coefs[][], Double y[], Double theta[]) {
        Double total = 0.0;
        for(int i =0; i < y.length; i ++) {
            Double calculI = 0.0;
            for(int j = 0; j< theta.length; j++) {
                calculI += coefs[j][i] * theta[j];
            }
            calculI -= y[i];
            total += calculI * calculI;
        }
        return total;
    }

    public static Double[] gradient(Double X[][], Double y[], Double theta[], Double coefs[]) {
        Double[] error = new Double[y.length];
        Double errorTheta[] = new Double[theta.length];
        int size = y.length;
//        Double thetaCoef = alpha/size;
        Arrays.fill(errorTheta, 0.0);
        for(int i =0; i < size; i ++) {
            error[i] = 0.0;
            for(int j = 0; j< theta.length; j++) {
                error[i] += X[j][i] * theta[j];
            }
            error[i] -= y[i];
        }
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < theta.length; j++) errorTheta[j] += error[i] * X[j][i];
        }
        Double[] newTheta = new Double[theta.length];
        for(int j = 0; j < theta.length; j++) {
            errorTheta[j] *= coefs[j] / size;
            newTheta[j] = theta[j] - errorTheta[j];
        }

        return newTheta;
    }
    public static Double[] findMinCoef(Double X[][], Double y[], Double theta[]) {
        Double min = 0.0000000000000000000000000000000000000000000001;
        Double mins[] = new Double[theta.length];
        Double finalMins[] = new Double[theta.length];
        for(int i =0; i<theta.length; i++) {
            Arrays.fill(mins, min);
            Double thetaCopy[] = new Double[theta.length];
            Arrays.fill(thetaCopy, 0.0);
            Double initCost = calculCost(X, y, thetaCopy);
            mins[i] = .0001;
            Double newCost, newCost2;
            do {
                Arrays.fill(thetaCopy, 0.0);
                thetaCopy = gradient(X, y, thetaCopy, mins);
                newCost = calculCost(X, y, thetaCopy);

                thetaCopy = gradient(X, y, thetaCopy, mins);
                newCost2 = calculCost(X, y, thetaCopy);
                mins[i] *= 0.5;
            } while(newCost > initCost || newCost2 > initCost);
            finalMins[i] = mins[i];
        }
        return finalMins;
    }
    public static void main(String[] args) {
	// write your code here

        Double data[][] = readSimpleDataset("dataset");
//        Double[][] coefs = buildCoefs(data[0]);
        Double[][] X = buildCoefsNDegree(data[0], 2);
        Double theta[] = new Double[]{0.0, 0.0, 0.0};
        Double minCoefs[] = findMinCoef(X, data[1], theta);
        for(int i = 0; i < nb_iterations; i++) {
            theta = gradient(X, data[1], theta, minCoefs);
//            Double cost = calculCost(X, data[1], theta);
//            System.out.println("Cost : "  + cost);
        }
        System.out.println(theta[2] + "x^2 + " + theta[1] + " x + " + theta[0]);

    }
}
