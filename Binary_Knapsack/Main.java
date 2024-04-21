package Knap.Binary_Knapsack;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int wno, W;
        System.out.println("Enter the maximum weight of the Knapsack");
        W = sc.nextInt();
        System.out.print("Enter the no:of weights :- ");
        wno = sc.nextInt();
        int[] weight = new int[wno];
        int[] value = new int[wno];
        System.out.println("Enter the weights of the Knapsack ");
        for (int i = 0; i < weight.length; i ++) {
            System.out.print('\t'+" Enter weight "+i+" :");
            weight[i] = sc.nextInt();
        }
        System.out.println("Enter the values of each weight of Knapsack ");
        for (int i = 0; i < value.length; i++) {
            System.out.print('\t'+" Enter the value for "+weight[i]+":");
            value[i] = sc.nextInt();
        }
        Knapsack_0_1 ob = new Knapsack_0_1(wno, weight, value, W);
//        ob.randomPopulation(wno);
//        ob.selection(ob.randomPopulation(wno));
//        ob.crossover(ob.selection(ob.randomPopulation(wno)));
//        ob.mutationRandom(ob.crossover(ob.selection(ob.randomPopulation(wno))));
        System.out.println("Enter the number of iterations for which you want to run the algorithm:");
        int n=sc.nextInt();
        ob.randomPopulation(wno); //Populates the ArrayList 'population' with size defined to be the number of weights
        ob.algorithm(n);
        sc.close();
    }
}

