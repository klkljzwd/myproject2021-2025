package com.my;

import java.net.Socket;

public class BestNiceString {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.longestNiceSubstring("aAadfbvdsbvs"));
    }
}
class Solution {

    public String longestNiceSubstring(String s) {
        if(s.length()<2){
            return "";
        }
        for(int i=0;i<s.length();i++){

            char c = s.charAt(i);


            if((c <= 'Z' && !s.contains(String.valueOf((char)((int)c + 32)))) || (c >= 'a' && !s.contains(String.valueOf((char)((int)c - 32))))) {

                String sl = longestNiceSubstring(s.substring(0,i));

                String sr = longestNiceSubstring(s.substring(i+1));

                if(sl.length()>=sr.length()){

                    return sl;

                }

                else{

                    return sr;

                }

            }

        }

        return s;

    }

}

