import java.util.*;

public class MajorNum{
    public static void main(String[] args){
        Solution solution = new Solution();
        System.out.println(solution.majorityElement(new int[]{2,2,1,1,1,2,2}));
    }
}
class Solution {
    public int majorityElement(int[] nums) {
        return findMajor(nums,0,nums.length-1);
    }
    public static int findMajor(int[] nums,int left,int right){
        if(left==right){
            return nums[left];
        }
        int mid = (left+right)/2;
        int leftResult = findMajor(nums,left,mid);
        int rightResult = findMajor(nums,mid+1,right);

        if(leftResult==rightResult){
            return leftResult;
        }
        int leftCount = count(nums,left,right,leftResult);
        int rightCount = count(nums,left,right,rightResult);
        return leftCount>rightCount?leftResult:rightResult;

    }
    public static int count(int[] nums,int left,int right,int num){
        int count=0;
        for(int i=left;i<nums.length;i++){
            if(nums[i]==num){
                count++;
            }
        }
        return count;
    }
}