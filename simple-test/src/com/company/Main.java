package com.company;

public class Main {

    /**
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
        
        return 0;
    }

    public static int min(int[] numbers) {
        return 0;
    }

    public static int max(int[] list) {
        return 0;
    }

    public static int[] reverse(int[] list) {
        return null;
    }

    public static boolean isPalindrome(String word) {
        return false;
    }

    /** Write a function that combines two lists by alternatingly taking elements, e.g. [a,b,c], [1,2,3] → [a,1,b,2,c,3]
     *
     */
    public static int[] shuffle(int[] list) {
        return null;
    }

    /** Write a function that rotates a list by k elements. For example [1,2,3,4,5,6] rotated by two becomes [3,4,5,6,1,2]
     *
     */
    public static int[] rotate(int[] list) {
        return null;
    }
}
