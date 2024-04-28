package com.experiment2;

import java.util.Arrays;
import java.util.Scanner;

public class Stone {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();//石子堆数
        int[] a = new int[202];//记录每堆石子的质量
        int[] s = new int[202];//记录前缀和
        int[][] dpmin = new int[202][202];//从l到r合并成一堆的最小代价
        int[][] dpmax = new int[202][202];
        for(int i =0;i<dpmin.length;i++){
            for(int j=0;j<dpmin[i].length;j++){
                dpmin[i][j]=Integer.MAX_VALUE;
            }
        }
        for(int i =0;i<dpmax.length;i++){
            for(int j=0;j<dpmax[i].length;j++){
                dpmax[i][j]=Integer.MIN_VALUE;
            }
        }
        for(int i=1;i<=n;i++){
            a[i]=input.nextInt();
            a[i+n]=a[i];  //复制一遍数组
        }
        for(int i=1;i<=2*n;i++){
            s[i]=s[i-1]+a[i]; //前缀和
            dpmax[i][i]=0;
            dpmin[i][i]=0;
        }
        //状态计算
        for(int len=2;len<=n;len++){  //区间长度
            for(int l=1;l+len-1<=n*2;l++){  //枚举区间起点
                int r = l+len-1; //区间终点
                for(int k=l;k<r;k++){ //从起点到终点枚举分割点
                    dpmin[l][r]=Math.min(dpmin[l][r],dpmin[l][k]+dpmin[k+1][r]+s[r]-s[l-1]);
                    dpmax[l][r]=Math.max(dpmax[l][r],dpmax[l][k]+dpmax[k+1][r]+s[r]-s[l-1]);
                }
            }
        }
        int minv = Integer.MAX_VALUE;
        int maxv = Integer.MIN_VALUE;
        for(int i=1;i<=n;i++){
            minv=Math.min(minv,dpmin[i][i+n-1]);//dp[1][n],dp[2][n+1],...'
            maxv=Math.max(maxv,dpmax[i][i+n-1]);
        }
        System.out.println(minv);
        System.out.println(maxv);
    }
}
