package org.example;
import org.junit.Test;
import static org.junit.Assert.*;
public class GenericMutationTest {
    @Test
    public void testSolveACOC() {
        Calculator calc = new Calculator();
        assertEquals(15, calc.solve(10, 5));
        assertEquals(-5, calc.solve(5, 10));
    }
    @Test
    public void testLogicACOC() {
        Calculator calc = new Calculator();
        assertTrue(calc.checkLogic(true, true));
        assertFalse(calc.checkLogic(true, false));
        assertTrue(calc.checkLogic(false, true));
        assertTrue(calc.checkLogic(false, false));
    }
}