package com.my.algo.DFS02;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static int[][] arr=null;
    private static int[] log = null;
    private static int minLen = Integer.MAX_VALUE;
    private static int[] choice = null;
    private static int[] best = null;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int m = input.nextInt();
        arr = new int[n+1][m+1];
        log = new int[n+1];
        choice = new int[n+1];
        best = new int[n+1];
        for(int i=1;i<= n;i++){
            for(int j=1;j<= m;j++){
                arr[i][j] = input.nextInt();
            }
        }
        dfs(1,n,m);
        System.out.println(minLen);
        for(int i=1;i<=n;i++){
            System.out.print(best[i]+" ");
        }
    }
    public static void dfs(int t,int n,int m){
        if(t-1==n){
            int maxLen = 0;
            for(int i=1;i<=m;i++){
                int start = 0;
                int end = 0;
                int logNum = 0;
                for(int j=1;j<=n;j++){
                    if(arr[log[j]][i]==1 &&logNum==0){
                        start = j;
                        end = j;
                        logNum = 1;
                    }
                    else if(arr[log[j]][i]==1&&logNum==1){
                        end = j;

                    }
                }
                int len = end-start;
                maxLen = Math.max(maxLen,len);
            }
            if(minLen>maxLen){
                minLen = maxLen;
                for(int i=1;i<=n;i++){
                    best[i] = log[i];
                }
            }
        }
        else {
            for(int i=1;i<=n;i++){
                if(choice[i]!=1){
                    choice[i]=1;
                    log[t]=i;
                    dfs(t+1,n,m);
                    log[t]=0;
                    choice[i]=0;
                }
            }
        }
    }
}
