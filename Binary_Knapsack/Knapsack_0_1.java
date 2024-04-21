package Knap.Binary_Knapsack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Knapsack_0_1 {
    static public ArrayList<int[]> population = new ArrayList<>();
    static public int[] population_fitness;

    static int MAX_fit;
    static int size;
    private int n; // number of weights
    private int[] solution=new int[n];
    private int[] weight = new int[n];
    // this array will have the weight values
    private int[] value = new int[n];
    // this will store the values
    private final int MAX_WEIGHT;


    //constructor
    public Knapsack_0_1(int n, int[] weight, int[] value, int MAX) {
        this.n = n;
        this.weight = weight;
        this.value = value;
        this.MAX_WEIGHT = MAX;
        Knapsack_0_1.population_fitness = new int[100];
        Knapsack_0_1.MAX_fit = 0;
        size = 0;
    }

//    static double CROSS_OVER_RATE;
//    static double MUTATION_RATE;

    Random r = new Random();

    // use to do selection of different weights ( 0 for not taking 1 for taking)
    // Random class is being used to get random values so that random selection takes place and this function returns
    // a specific array having selected and not selected weights

    public int[] random_pop(int length) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt(2);
        }
        return arr;
    }


    // this creates an arraylist having populationSize number of different population
    public void randomPopulation(int populationSize) {
        for (int i = 0; i < populationSize; i++) {
            int []temp = random_pop(populationSize);
            population.add(temp);
            population_fitness[size++] = fitnesscoff(temp);
        }
        for (int i : population_fitness) {
            MAX_fit = Math.max(i, MAX_fit);
        }
    }


    public HashMap<int[], Integer> populationFitness(ArrayList<int[]> parents) {
        HashMap<int[], Integer> ans = new HashMap<>();

        for (int[] i : parents) {
            ans.put(i, fitnesscoff(i));
        }
        return ans;
    }


    // it returns the integer value of the fitnesscoff, it gives 0 when the weight increases than max_weight otherwise
    // it returns the sum of values as the fitness cofficient
    public int fitnesscoff(int[] arr) {
        int w = 0, v = 0;
        for (int i = 0; i < arr.length; i++) {
            w += arr[i] * weight[i];
            v += arr[i] * value[i];
        }
        if (w > MAX_WEIGHT) {
            return 0;
        }
        return v;
    }


    // though this function is not yet used anywhere it gives an array which have the fitness cofficients of all the
    // population stored in a list
    public int[] fitnessArray(ArrayList<int[]> list) {
        int arr[] = new int[list.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = fitnesscoff(list.get(i));
        }
        return arr;
    }

    // let us suppose the minimum value of fitness function has to be 5
    static int FIT = 5;
    static int fit1;
    static int fit2;


    public void selection_Fitness(ArrayList<int[]> list) {
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


    //this is being used for random selection of two chromosomes
    public ArrayList<int[]> selection(ArrayList<int[]> list) {
        ArrayList<int[]> ans = new ArrayList<>();
        int random = r.nextInt(list.size());
        int count = 0;
        while (count < 2) {
            if (fitnesscoff(list.get(random)) > FIT) {
                ans.add(list.get(random));
                count++;
            } else {
                random = r.nextInt(list.size());
            }
        }

        return ans;
    }
    // this function will do 2 random selection of population from the list and the selection
    // will only be done if the fitness value of that population is greater than the FIT (which has been initialised)


    // cross-over
    /* this function is going to do cross over by mid-partition  */

    public void crossover_Fitness(ArrayList<int[]> parent) {
        int[] child1 = new int[parent.get(0).length];
        int[] child2 = new int[parent.get(1).length];

        // instead of creating a new object, just make these child1 point at the objects present in the arraylist
        int crossOver_point = r.nextInt(population.get(fit1).length);

        System.arraycopy(population.get(fit1), 0, child1, 0, crossOver_point);
        System.arraycopy(population.get(fit2), crossOver_point, child1, crossOver_point, child1.length - crossOver_point);

        System.arraycopy(population.get(fit2), 0, child2, 0, crossOver_point);
        System.arraycopy(population.get(fit1), crossOver_point, child2, crossOver_point, child2.length - crossOver_point);

        int child1_fitness = fitnesscoff(child1);
        int child2_fitness = fitnesscoff(child2);


        int minn = search(population_fitness);
        if (population_fitness[minn] < child1_fitness) {
            population.add(minn, child1);
            population_fitness[minn] = child1_fitness;
            if (child1_fitness > MAX_fit) {
                MAX_fit = child1_fitness;
                fit2 = fit1;
                fit1 = minn;
            }
        }
        minn = search(population_fitness);
        if (population_fitness[minn] < child2_fitness) {
            population.add(minn, child2);
            population_fitness[minn] = child2_fitness;
            if (child2_fitness > MAX_fit) {
                MAX_fit = child2_fitness;
                fit2 = fit1;
                fit1 = minn;
            }
        }
    }


    //mutation

    /* This type of mutation function will generate a random number based upon the length of the array and then
     * that many bits are flipped in that specific array resulting into a mutated array       */

    public int[] mutationRandom(ArrayList<int[]> cross) {
        int[] mutated = cross.get(r.nextInt(2));

        int bit = r.nextInt(mutated.length);
        int count = 0;
        do {
            int flip = r.nextInt(mutated.length);
            mutated[flip] = mutated[flip] ^ 1;
            count++;
        } while (count < bit);
        return mutated;
    }

    /*  this is another type of mutation function in which swapping of any two bits will take place , edge cases are looked after
     * that's why the if condition with a while loop is there  */
    public int[] mutationSwap(ArrayList<int[]> cross) {
        int[] mutated = cross.get(r.nextInt(2));
        int a = r.nextInt(mutated.length);
        int b = r.nextInt(mutated.length);
        if (a == b) {
            while (a == b) {
                a = r.nextInt(mutated.length);
                b = r.nextInt(mutated.length);
            }
        }
        int temp = mutated[a];
        mutated[a] = mutated[b];
        mutated[b] = temp;
        return mutated;
    }


    public static int search(int[] arr) {
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
        int []initial = population.get(0);
        int count = 0;
        for (int i = 0; i < iterations; i++) {
            selection_Fitness(population);
            crossover_Fitness(population);
            int[] temp = population.get(0);
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
        int f=0;
        for (int i = 0; i < n ; i++) {
            int ans=solution[i]*weight[i];
            f=f+(solution[i]*value[i]);
            if(ans!=0){
                System.out.print(ans+" ");
            }
        }
        System.out.println();
        System.out.println("For these weights,the fitness value = "+f);
    }
}

