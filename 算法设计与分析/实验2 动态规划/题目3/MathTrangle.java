package com.experiment2;

import java.util.Scanner;

public class MathTrangle {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n=  input.nextInt();
        int[][] arr = new int[n+1][n+1];
        for(int i=1;i<=n;i++){
            for(int j = 1;j<=i;j++){
                arr[i][j] = input.nextInt();
            }
        }
        for(int i=n-1;i>=1;i--){
            for(int j=1;j<=i;j++){
                arr[i][j] = Math.max(arr[i+1][j],arr[i+1][j+1])+arr[i][j];
            }
        }
        System.out.println(arr[1][1]);
    }
}
