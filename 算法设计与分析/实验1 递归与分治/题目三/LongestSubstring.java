package com.my;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstring {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.longestSubstring("aaavvvdads",3));
    }

}

class Solution{
    public int longestSubstring(String s, int k) {
        return dfs(s,k);
    }

    public static int dfs(String s,int k){
        Map<Character,Integer> map = new HashMap<>();
        for(int i=0;i<s.length();i++){
            char a = s.charAt(i);
            if(!map.keySet().contains(a)){
                map.put(a,1);
            }
            else{
                map.put(a,map.get(a)+1);
            }
        }
        for(int i=0;i<s.length();i++){
            if(map.get(s.charAt(i))<k){
                int maxLength = 0;
                for(String str:s.split(String.valueOf(s.charAt(i)))){
                    maxLength = Math.max(maxLength,dfs(str,k));
                }
                return maxLength;
            }
        }
        return s.length();
    }
}