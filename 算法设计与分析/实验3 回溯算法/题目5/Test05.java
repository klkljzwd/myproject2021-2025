package com.my.algocode;

import java.util.Arrays;
import java.util.Scanner;

public class Test05 {
    private static int minCount = Integer.MAX_VALUE;
    private static int flag=0;
    private static int[] log = new int[21];
    private static int[] minLog = new int[21];
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int m = input.nextInt();
        dfs(0,n,m);
        if(minCount==Integer.MAX_VALUE){
            System.out.println("No Solution!");
        }
        else {
            System.out.println(minCount);
            for(int i=0;i<minLog.length;i++){
                if(minLog[i]==1){
                    System.out.print("f");
                }
                else if(minLog[i]==2){
                    System.out.print("g");
                }
            }
        }

    }
    private static void dfs(int t,int n,int m){
        if(t>20){
            return;
        }
        if(n==m){
            if(minCount>t){
                for(int i=0;i<log.length;i++){
                    minLog[i]=log[i];
                }
            }
            minCount=Math.min(minCount,t);
            return;
        }
        else {
            for(int i=0;i<2;i++){
                if(i==0){
                    log[t]=1;
                    n=f(n);
                    dfs(t+1,n,m);
                    n=n/3;
                    log[t]=0;
                }
                else if(i==1){
                    log[t]=2;
                    if(n%2==1){
                        flag=1;
                    }
                    else {
                        flag=0;
                    }
                    n=g(n);
                    dfs(t+1,n,m);
                    if(flag==1){
                        n=n*2+1;
                    }
                    else {
                        n=n*2;
                    }
                    log[t]=0;
                }
            }
        }
    }
    private static int f(int n){
        return n*3;
    }
    private static int g(int n){
        return n/2;
    }
}
