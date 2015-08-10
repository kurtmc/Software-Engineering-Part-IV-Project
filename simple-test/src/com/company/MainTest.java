package com.company;

import static org.junit.Assert.*;

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
}