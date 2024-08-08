package com.zipking.cloud.springbootmybatis.util.calculate;

public class TwoNumSum {

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] a = twoSum(nums,target);
        for(int i : a){
            System.out.println(i);
        }
    }

    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for(int i = 0;i<nums.length;i++){
            int m = nums[i];
            for(int j = 0;i<nums.length;j++){
                int n = nums[j];
                if(m + n == target){
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }
}
