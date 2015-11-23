package com.company;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @org.junit.Test
    public void testFactorial() throws Exception {
        assertEquals(1, Main.factorial(0));
        assertEquals(120, Main.factorial(5));
        assertEquals(39916800, Main.factorial(11));
    }

    @org.junit.Test
    public void testFibonacci() throws Exception {
        assertEquals(0, Main.fibonacci(0));
        assertEquals(1, Main.fibonacci(1));
        assertEquals(1, Main.fibonacci(2));
        assertEquals(2, Main.fibonacci(3));
        assertEquals(3, Main.fibonacci(4));
        assertEquals(5, Main.fibonacci(5));
        assertEquals(8, Main.fibonacci(6));
        assertEquals(13, Main.fibonacci(7));
        assertEquals(21, Main.fibonacci(8));
    }

    @Test
    public void testMin() throws Exception {
        assertEquals(1, Main.min(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(2, Main.min(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(3, Main.min(new int[]{3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(4, Main.min(new int[]{4, 5, 6, 7, 8, 9, 10}));
        assertEquals(-1, Main.min(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, -1}));
        assertEquals(-10, Main.min(new int[]{1, 2, 3, 4, 5, -10, 7, 8, 9, 10}));
        assertEquals(5, Main.min(new int[]{11, 12, 13, 14, 5, 6, 7, 8, 9, 10}));
    }

    @Test
    public void testMax() throws Exception {
        assertEquals(10, Main.max(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(10, Main.max(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(10, Main.max(new int[]{3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(10, Main.max(new int[]{4, 5, 6, 7, 8, 9, 10}));
        assertEquals(9, Main.max(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, -1}));
        assertEquals(-1, Main.max(new int[]{-1, -2, -3, -4, -5, -10, -7, -8, -9, -10}));
        assertEquals(14, Main.max(new int[]{11, 12, 13, 14, 5, 6, 7, 8, 9, 10}));
    }

    private void assertEqualArrays(int[] expected, int[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testReverse() throws Exception {
        int[] expected = new int[]{1, 2, 3, 4, 5};
        int[] actual = Main.reverse(new int[]{5, 4, 3, 2, 1});
        assertEqualArrays(expected, actual);
    }

    @Test
    public void testIsPalindrome() throws Exception {
        assertEquals(true, Main.isPalindrome("rotor"));
        assertEquals(false, Main.isPalindrome("motor"));
    }

    @Test
    public void testShuffle() throws Exception {
        int[] expected = (new int[]{1, 2, 3, 4, 5, 6});
        int[] actual = Main.shuffle(new int[]{1, 3, 5}, new int[]{2, 4, 6});
        assertEqualArrays(expected, actual);
    }

    @Test
    public void testRotate() throws Exception {
        assertEqualArrays(new int[]{3, 4, 5, 6, 1, 2}, Main.rotate(new int[]{1, 2, 3, 4, 5, 6}, 2));
    }
}