package Knap.Fractional_Knapsack;

import java.util.ArrayList;
import java.util.Random;

public class Knapsack {
 public ArrayList<double[]> population = new ArrayList<double[]>();
    static public int[] population_fitness;
    static int size;
    private int n;
    private double[] solution = new double[n];
    private int[] weight = new int[n];
    private int[] value = new int[n];
    //private final int MAX_WEIGHT;
    static int MAX_FIT;
    private  final int MAX_WEIGHT;

    public Knapsack(int n, int[] weight, int[] value, int MAX_WEIGHT) {
        this.n = n;
        this.weight = weight;
        this.value = value;
        //this.MAX_WEIGHT = MAX_WEIGHT;
        Knapsack.population_fitness = new int[100];
        Knapsack.MAX_FIT = 0;
        size = 0;
        this.MAX_WEIGHT=MAX_WEIGHT;

    }
    Random r = new Random(); //taking the random class to randomly allocate certain percentage of the given weights

    public double[] random_pop(int length){
        double[] arr = new double[length];
        for (int i = 0; i < arr.length; i++){
            int fraction = r.nextInt(101);
            arr[i] = (double) fraction / 100; //taking upto two decimal places thus multiplication by 100 and division by 100
        }
        return arr;
    }
    public void randomPopulation(int populationsize) {
        for (int i = 0; i < populationsize; i++) {
            double[] chromosome = random_pop(populationsize);
            population.add(chromosome);
       population_fitness[size ++] = fitnesscoff(chromosome);
        }
        for (int i : population_fitness) {
            MAX_FIT = Math.max(i, MAX_FIT);
        }
    }



    public int fitnesscoff(double[] arr) {
        double w = 0, v = 0;
        for (int i = 0; i < arr.length; i++) {
            w += arr[i] * weight[i];
            v += arr[i] * value[i];
        }
        if (w >  MAX_WEIGHT) {
            return 0;
        }
        return (int)v;
    }







//    private int coeff_fitness(double[] chromosome) {
//        //
//    }






    /*  this is another type of mutation function in which swapping of any two bits will take place , edge cases are looked after
     * that's why the if condition with a while loop is there  */
    public double[] mutationSwap(ArrayList<double[]> cross) {
        double[] mutated = cross.get(r.nextInt(2));
        int a = r.nextInt(mutated.length);
        int b = r.nextInt(mutated.length);
        if (a == b) {
            while (a == b) {
                a = r.nextInt(mutated.length);
                b = r.nextInt(mutated.length);
            }
        }
        double temp = mutated[a];
        mutated[a] = mutated[b];
        mutated[b] = temp;
        return mutated;
    }



    static int FIT = 5;
    static int fit1;
    static int fit2;




    public void selection_Fitness(ArrayList<double[]> list) {
        ArrayList<int[]> ans = new ArrayList<>();
        int min = Integer.MIN_VALUE;
        int temp1 = 0;
        int temp2 = 0;

        for ( int i = 0; i < size; i++) {
            if (population_fitness[i] > min) {
                min = population_fitness[i];
                temp1 = i;
            }
        }
        min = Integer.MIN_VALUE;
        for (int  j = 0; j < size; j++) {
            if (population_fitness[j] > min && j!=temp1) {
                min = population_fitness[j];
                temp2 = j;
            }
        }
// corrected the selection function,made a separate loop for fit2 as earlier both fit1 and fit2 were assigned the same value
        fit1 = temp1;
        fit2 = temp2;
    }







    public void crossover_Fitness(ArrayList<double[]> parent) {
        double[] child1 = new double[parent.get(0).length];
        double[] child2 = new double[parent.get(1).length];

        // instead of creating a new object, just make these child1 point at the objects present in the arraylist
        int crossOver_point = r.nextInt(population.get(fit1).length);

        System.arraycopy(population.get(fit1), 0, child1, 0, crossOver_point);
        System.arraycopy(population.get(fit2), crossOver_point, child1, crossOver_point, child1.length - crossOver_point);

        System.arraycopy(population.get(fit2), 0, child2, 0, crossOver_point);
        System.arraycopy(population.get(fit1), crossOver_point, child2, crossOver_point, child2.length - crossOver_point);

        int child1_fitness = fitnesscoff(child1);
        int child2_fitness = fitnesscoff(child2);


        int minn = minsearch(population_fitness);
        if (population_fitness[minn] < child1_fitness) {
            population.add(minn, child1);
            population_fitness[minn] = child1_fitness;
            if (child1_fitness > MAX_FIT) {
                MAX_FIT = child1_fitness;
                fit2 = fit1;
                fit1 = minn;
            }
        }
        minn = minsearch(population_fitness);
        if (population_fitness[minn] < child2_fitness) {
            population.add(minn, child2);
            population_fitness[minn] = child2_fitness;
            if (child2_fitness > MAX_FIT) {
                MAX_FIT = child2_fitness;
                fit2 = fit1;
                fit1 = minn;
            }
        }
    }



    public static int minsearch(int[] arr) {
        if (arr.length == 0) {
            return -1;
        }
        int temp = Integer.MAX_VALUE;
        int i = 0;

        //iterating through the entire the array

        for (int index = 0; index < arr.length; index++) {
            if (arr[index] < temp) {
                temp = arr[index];
                i = index;
            }
        }
        return i;
    }




    void algorithm(int iterations) {
        double []initial = population.get(0);
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
                i=0;
            }
        }
        solution=population.get(0);

        System.out.println("Probable answer:");
        print();
    }

    void print() {
        // modified the print function so that the output is easily readible
        System.out.println("Pick these weights for maximum fitness value: ");
        double f=0;
        for (int i = 0; i < n ; i++) {
            double ans=solution[i]*weight[i];
            f=f+(solution[i]*value[i]);
            if(ans!=0){
                System.out.print(ans+" ");
            }
        }
        System.out.println();
        System.out.println("For these weights,the fitness value = "+f);
    }

}
