package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTests {

    @Test
    public void testCheckLogic_ACOC_AllBooleanCombinations() {

        Calculator calc = new Calculator();

        boolean[] values = {true, false};

        for (boolean A : values) {
            for (boolean B : values) {

                boolean expected = (!A) || B;
                boolean actual = calc.checkLogic(B, A);

                assertEquals(
                        "ACOC failed for A=" + A + ", B=" + B,
                        expected,
                        actual
                );
            }
        }
    }


    @Test
    public void testIntegration_ACOC_WithArithmeticChain() {

        Calculator calc = new Calculator();

        boolean[] values = {true, false};

        for (boolean A : values) {
            for (boolean B : values) {

                boolean logicResult = calc.checkLogic(B, A);

                int base = logicResult ? 20 : 10;

                int diff = calc.solve(5, base);              // base - 5
                int shifted = calc.bitwiseOperation(diff);   // << 2
                int multiplied = calc.internalProcess(shifted, 2);
                int finalResult = calc.calculateSum(multiplied, 8);

                boolean expectedLogic = (!A) || B;
                int expectedBase = expectedLogic ? 20 : 10;
                int expectedDiff = expectedBase - 5;
                int expectedShift = expectedDiff << 2;
                int expectedMult = expectedShift * 2;
                int expectedFinal = expectedMult - 8;

                assertEquals(expectedFinal, finalResult);
            }
        }
    }

    @Test
    public void testSolve_AllValueCombinations() {

        Calculator calc = new Calculator();

        int[] values = {-10, -1, 0, 1, 10};

        for (int b : values) {
            for (int a : values) {

                int expected = a - b;
                int actual = calc.solve(b, a);

                assertEquals(
                        "solve failed for a=" + a + ", b=" + b,
                        expected,
                        actual
                );
            }
        }
    }


    @Test
    public void testBoundaryValues() {

        Calculator calc = new Calculator();

        assertEquals(Integer.MAX_VALUE, calc.solve(0, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, calc.solve(0, Integer.MIN_VALUE));

        assertEquals(0, calc.calculateSum(5, 5));
        assertEquals(0, calc.internalProcess(0, 100));
        assertEquals(0, calc.bitwiseOperation(0));
    }


    @Test
    public void testArithmeticIntegrationCombinations() {

        Calculator calc = new Calculator();

        int[] values = {-5, 0, 5};

        for (int x : values) {
            for (int y : values) {

                int diff = calc.solve(y, x);  // x - y
                int sumStyle = calc.calculateSum(x, y); // x - y
                int mult = calc.internalProcess(x, y);
                int shift = calc.bitwiseOperation(x);

                assertEquals(x - y, diff);
                assertEquals(x - y, sumStyle);
                assertEquals(x * y, mult);
                assertEquals(x << 2, shift);
            }
        }
    }
}
