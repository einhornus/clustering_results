package com.alisovenko.geeks4geeks;

import java.util.Arrays;

/**
 * @author alisovenko 06.02.15
 */
public class FindKSubsets {
    /**
     * @param arr the initial array to find k subsets
     * @param current the current k subset that we are constructing
     * @param idx the index in k subset we are on
     * @param k the size of subset
     * @param start the left border of "window"
     */
    private static void findAll(char[] arr, char[] current, int idx, int k, int start) {
        if (idx == k) {
            System.out.println(Arrays.toString(current));
            return;
        }

        for (int i = start; i < arr.length && i < arr.length - (k - idx - 1); i++ ) {
            current[idx] = arr[i];
            findAll(arr, current, idx + 1, k, i + 1);
        }
    }
    private static void findKSubsets(char[] arr, int k) {
        findAll(arr, new char[k], 0, k, 0);
    }

    public static void main(String[] args) {
        findKSubsets("abcdef".toCharArray(), 2);
    }
}

--------------------

public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        Arrays.sort(nums);
        HashSet<ArrayList<Integer>> set = new HashSet<ArrayList<Integer>>();
        for (int i =0; i < nums.length - 2; i++) {
            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {
                int total = nums[i] + nums[j] + nums[k];
                if (total == 0) {
                    ArrayList<Integer> oneResult = new ArrayList<Integer>();
                    oneResult.add(nums[i]);
                    oneResult.add(nums[j]);
                    oneResult.add(nums[k]);
                    if (set.add(oneResult)) {
                        result.add(oneResult);
                    }
                    j++;
                    k--;
                } else if (total < 0) {
                    j++;
                } else {
                    k--;
                }
            }
        }
        return result;
    }

--------------------

package com.leetcode.array.leetcode;

//https://leetcode.com/problems/first-missing-positive/description/
public class FirstMissingPositive {

    public int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0 || nums[i] > nums.length) nums[i] = nums.length + 1;
        }
        for (int i = 0; i < nums.length; i++) {
            int abs = Math.abs(nums[i]);
            if (abs <= nums.length) {
                nums[abs - 1] = -Math.abs(nums[abs - 1]);
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= 0) return i + 1;
        }
        return nums.length + 1;
    }

    public static void main(String[] args) {
        int arr[] = {-1};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        if (result == -1) {
            System.out.println(arr.length);
        } else {
            System.out.println(result);
        }
    }
}

--------------------

