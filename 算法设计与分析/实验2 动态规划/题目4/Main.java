package com.experiment2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int[][] m = new int[n+1][n+1];
        for(int i=1;i<=n-1;i++){
            for(int j=i+1;j<=n;j++){
                m[i][j] = input.nextInt();
            }
        }
        int[] dp = new int[n+1];
        dp[1]=0;
        for(int i=2;i<=n;i++){
            dp[i]=m[1][i];
        }
        for(int i=3;i<=n;i++){
            for(int j =1;j<=i-1;j++){
                dp[i] = Math.min(dp[i],dp[j]+m[j][i]);
            }
        }
        System.out.println(dp[n]);
    }
}
