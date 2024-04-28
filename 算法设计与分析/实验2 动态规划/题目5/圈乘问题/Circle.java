package com.experiment2;

import java.util.Scanner;

public class Circle {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int x = input.nextInt();
        int k = input.nextInt();
        while (x!=0&&k!=0){
            int n = lenOfX(x);
            int m = 81*Math.max(2,n)+9;
            if(k>m){
                return;
            }
            int[][] arr = new int[m+2][4];
            for(int i=0;i< arr.length-1;i++){
                init(arr[i],i);
            }
            init(arr[arr.length-1],x);
            arr[arr.length-1][0]=0;
            boolean flag = true;
            while (flag){
                flag = false;
                for(int i=1;i<=m+1;i++){
                    if(arr[i][0]<Integer.MAX_VALUE){
                        for(int j=1;j<=m+1;j++){
                            if(arr[j][0]<Integer.MAX_VALUE){
                                int tend = arr[i][1]*arr[j][3]+arr[j][2];
                                if (arr[tend][0]>arr[i][0]+arr[j][0]+1){//与原来的圈乘生产该数的次数对比找最小
                                    flag = true;//若有变化则更新x的寻值
                                    arr[tend][0] = arr[i][0] + arr[j][0] + 1; //r[i][0]为得到x的圈乘次数,r[j][0]位得到y的圈乘次数
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(arr[k][0]);
            x = input.nextInt();
            k = input.nextInt();
        }
    }
    private static int lenOfX(int x){
        String str = String.valueOf(x);
        return str.length();
    }
    private static void init(int[] arr,int x){
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int num = 0;
        int tend = 0;
        while (x>0){
            tend = x % 10;
            x /= 10;
            num += tend;
            if (tend > max){
                max = tend;
            }
            if (tend < min){
                min = tend;
            }
        }
        arr[0]=Integer.MAX_VALUE;
        arr[1]=num;
        arr[2]=min;
        arr[3]=max;
    }
}
