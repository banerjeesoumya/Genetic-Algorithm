package Knap.Fractional_Knapsack;

import java.util.ArrayList;
import java.util.Random;

public class Knapsack2 {
    // ArrayList to store population
    public ArrayList<double[]> population = new ArrayList<double[]>();
    // Array to store fitness of population
    public static int[] population_fitness;
    // Size of population
    public static int size;
    // Number of items
    private int n;
    // Solution array
    private double[] solution;
    // Array to store weights of items
    private int[] weight;
    // Array to store values of items
    private int[] value;
    // Maximum weight that the knapsack can hold
    private int MAX_WEIGHT;
    // Maximum fitness value
    public static int MAX_FIT;

    // Random generator
    Random r = new Random();

    // Constructor
    public Knapsack2(int n, int[] weight, int[] value, int MAX_WEIGHT) {
        this.n = n;
        this.weight = weight;
        this.value = value;
        this.MAX_WEIGHT = MAX_WEIGHT;
        Knapsack.population_fitness = new int[100];
        Knapsack.MAX_FIT = Integer.MIN_VALUE;
        size = 0;
        this.solution = new double[n]; // Initializing solution array with the correct size
    }


    public double[] random_pop(int length){
        double[] arr = new double[length];
        for (int i = 0; i < arr.length; i++){
            int fraction = r.nextInt(101);
            arr[i] = (double) fraction / 100; //taking upto two decimal places thus multiplication by 100 and division by 100
        }
        return arr;
    }

    // Method to generate random population
    public void randomPopulation(int populationSize) {
        for (int i = 0; i < populationSize; i++) {
            double[] chromosome = random_pop(n); // Generate a random chromosome
            population.add(chromosome);
            population_fitness[size++] = fitnesscoff(chromosome); // Calculate fitness and store it
        }
        // Find the maximum fitness value in the population
        for (int i : population_fitness) {
            MAX_FIT = Math.max(i, MAX_FIT);
        }
    }

    // Method to calculate fitness of a chromosome
    public int fitnesscoff(double[] arr) {
        double w = 0, v = 0;
        for (int i = 0; i < arr.length; i++) {
            w += arr[i] * weight[i];
            v += arr[i] * value[i];
        }
        // If the weight exceeds the maximum, return 0 fitness
        if ((int)w > MAX_WEIGHT) {
            return 0;
        }
        // Otherwise return the calculated fitness value
        return (int)v;
    }

    // Method to perform mutation by swapping two elements in a chromosome
    public double[] mutationSwap(ArrayList<double[]> cross) {
        double[] mutated = cross.get(r.nextInt(2));
        int a = r.nextInt(mutated.length);
        int b = r.nextInt(mutated.length);
        // Ensure that a and b are different
        if (a == b) {
            while (a == b) {
                a = r.nextInt(mutated.length);
                b = r.nextInt(mutated.length);
            }
        }
        // Swap elements at positions a and b
        double temp = mutated[a];
        mutated[a] = mutated[b];
        mutated[b] = temp;
        return mutated;
    }

    // Method to select parents based on fitness
    public void selection_Fitness(ArrayList<double[]> list) {
        int minFit = Integer.MIN_VALUE;
        int temp1 = 0;
        int temp2 = 0;

        // Find the first parent with the highest fitness
        for (int i = 0; i < size; i++) {
            if (population_fitness[i] > minFit) {
                minFit = population_fitness[i];
                temp1 = i;
            }
        }
        // Find the second parent with the second highest fitness, different from the first one
        minFit = Integer.MIN_VALUE;
        for (int j = 0; j < size; j++) {
            if (population_fitness[j] > minFit && j != temp1) {
                minFit = population_fitness[j];
                temp2 = j;
            }
        }
        // Set the indices of the selected parents
        fit1 = temp1;
        fit2 = temp2;
    }

    // Method to perform crossover between selected parents
    public void crossover_Fitness(ArrayList<double[]> parent) {
        double[] child1 = new double[parent.get(0).length];
        double[] child2 = new double[parent.get(1).length];
        // Choose a crossover point randomly
        int crossOver_point = r.nextInt(population.get(fit1).length);

        // Create child1 by combining parts of the two parents
        System.arraycopy(population.get(fit1), 0, child1, 0, crossOver_point);
        System.arraycopy(population.get(fit2), crossOver_point, child1, crossOver_point, child1.length - crossOver_point);

        // Create child2 by combining parts of the two parents
        System.arraycopy(population.get(fit2), 0, child2, 0, crossOver_point);
        System.arraycopy(population.get(fit1), crossOver_point, child2, crossOver_point, child2.length - crossOver_point);

        // Calculate fitness values of the children
        int child1_fitness = fitnesscoff(child1);
        int child2_fitness = fitnesscoff(child2);

        // Find the position to insert the child with the higher fitness value
        int minIndex = minsearch(population_fitness);
        if (population_fitness[minIndex] < child1_fitness) {
            population.add(minIndex, child1);
            population_fitness[minIndex] = child1_fitness;
            if (child1_fitness > MAX_FIT) {
                MAX_FIT = child1_fitness;
                fit2 = fit1;
                fit1 = minIndex;
            }
        }
        minIndex = minsearch(population_fitness);
        if (population_fitness[minIndex] < child2_fitness) {
            population.add(minIndex, child2);
            population_fitness[minIndex] = child2_fitness;
            if (child2_fitness > MAX_FIT) {
                MAX_FIT = child2_fitness;
                fit2 = fit1;
                fit1 = minIndex;
            }
        }
    }

    // Indices of selected parents
    public static int fit1;
    public static int fit2;

    // Main algorithm to evolve the population
    public void algorithm(int iterations) {
        double[] initial = population.get(0);
        int count = 0;
        for (int i = 0; i < iterations; i++) {
            selection_Fitness(population);
            crossover_Fitness(population);
            double[] temp = population.get(0);
            if (temp == initial) {
                count++;
            }
            if (temp != initial) {
                initial = temp;
            }
            if (count == iterations / 2) {
                mutationSwap(population);
                i = 0;
            }
        }
        solution = population.get(0);

        // Print the result
        System.out.println("Probable answer:");
        print();
    }

    // Method to print the solution
    public void print() {
        System.out.println("Pick these weights for maximum fitness value: ");
        double fitnessValue = 0;
        for (int i = 0; i < n; i++) {
            double ans = solution[i] * weight[i];
            fitnessValue += solution[i] * value[i];
            if (ans != 0) {
                System.out.print(ans + " ");
            }
        }
        System.out.println();
        System.out.println("For these weights, the fitness value = " + fitnessValue);
    }

    // Method to find the index of the minimum value in an array
    public static int minsearch(int[] arr) {
        if (arr.length == 0) {
            return -1;
        }
        int min = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                index = i;
            }
        }
        return index;
    }
}
