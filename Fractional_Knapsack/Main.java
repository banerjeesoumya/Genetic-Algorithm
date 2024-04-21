package Knap.Fractional_Knapsack;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int weight_no, W;
        System.out.println("Enter the maximum weight of the Knapsack");
        W = sc.nextInt();
        System.out.println("Enter the number of weights :- ");
        weight_no = sc.nextInt();
        int[] weight = new int[weight_no];
        int[] value = new int[weight_no];
        System.out.println("Enter the each weight of the Knapsack ");
        for (int i = 0; i < weight.length; i ++) {
            System.out.print('\t'+" Enter weight "+i+" :");
            weight[i] = sc.nextInt();
        }
        System.out.println("Enter the values of each weight of Knapsack ");
        for (int i = 0; i < value.length; i++) {
            System.out.print('\t'+" Enter the value for "+weight[i]+":");
            value[i] = sc.nextInt();
        }
        Knapsack ob = new Knapsack(weight_no, weight, value, W);
        System.out.println("Enter the number of iterations");
        int n = sc.nextInt();
        ob.randomPopulation(weight_no);
        ob.algorithm(n);
    }
}
