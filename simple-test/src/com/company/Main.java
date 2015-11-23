package com.company;

public class Main {

    /**
     * Compute factorial of n
     * @param n, assume n >= 0
     * @return the factorial of n
     */
    public static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    /**
     * The Fibonacci sequence starts at 0. The sequence starts like this:
     * 0, 1, 1, 2, 3, 5, 8, 13, 21, ...
     * @param n is the index of the number in the fibonacci sequence
     * @return the nth number in the fibonacci sequence
     */
    public static int fibonacci(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * Calculate the minimum integer from a list
     *
     * @param numbers list of integers
     * @return the minimum
     */
    public static int min(int[] numbers) {
        int min = numbers[0];
        for (int number : numbers) {
            if (min > number) {
                min = number;
            }
        }
        return min;
    }

    /**
     * Calculate the maximum integer from a list
     *
     * @param numbers list of integers
     * @return the maximum
     */
    public static int max(int[] numbers) {
        int max = numbers[0];
        for (int number : numbers) {
            if (max < number) {
                max = number;
            }
        }
        return max;
    }

    /**
     * Reverse a list of integers
     * @param list of integers
     * @return the reversed list
     */
    public static int[] reverse(int[] list) {
        int[] reverse = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            reverse[i] = list[list.length - 1 - i];
        }
        return reverse;
    }

    /**
     * Check if a word is a palindrome
     * @param word - string to check
     * @return true or false
     */
    public static boolean isPalindrome(String word) {
        for (int i = 0; i < word.length() / 2; i++) {
            if (word.charAt(i) != word.charAt(word.length() - 1 - i))
                return false;
        }
        return true;
    }

    /**
     * Write a function that combines two lists by alternately taking elements, e.g. [a,b,c], [1,2,3] → [a,1,b,2,c,3]
     * Assume list1 and list2 are of the same length
     * @param list1 - list of integers
     * @param list2 - list of integers
     * @return shuffled list of integers
     */
    public static int[] shuffle(int[] list1, int[] list2) {
        int size = list1.length + list2.length;
        int[] list = new int[size];

        for (int i = 0; i < list1.length; i++) {
            list[i * 2] = list1[i];
            list[i * 2 + 1] = list2[i];
        }

        return list;
    }

    /**
     * Write a function that rotates a list by k elements. For example [1,2,3,4,5,6] rotated by two becomes [3,4,5,6,1,2]
     * @param list of integers
     * @param k - amount to rotate by
     * @return rotated list
     */
    public static int[] rotate(int[] list, int k) {
        int[] new_list = new int[list.length];

        for (int i = 0; i < list.length; i++) {
            new_list[i] = list[(i + k) % list.length];
        }

        return new_list;
    }
}
