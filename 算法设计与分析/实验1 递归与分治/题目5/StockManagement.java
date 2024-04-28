package com.my;

import java.lang.reflect.Array;
import java.util.Arrays;

public class StockManagement {
    public static void main(String[] args) {
        Solution1 solution1 = new Solution1();
        int[] arr = {2,3,8,2,4,6};
        System.out.println(Arrays.toString(solution1.inventoryManagement(arr, 2)));
    }

}
class Solution1 {
    public int[] inventoryManagement(int[] stock, int cnt) {
        Arrays.sort(stock);
        return Arrays.copyOfRange(stock,0,cnt);
    }
}