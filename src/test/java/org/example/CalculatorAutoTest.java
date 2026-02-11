package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorAutoTest {

    Calculator target = new Calculator();

    @Test
    public void test_solve_Dynamic() {
        int[] bv = {0, 1, -1, 100};
        for(int x : bv) { for(int y : bv) { try { target.solve(x, y); } catch(Exception e){} } }
    }

    @Test
    public void test_checkLogic_Dynamic() {
        boolean[] v = {true, false};
        for(boolean a : v) { for(boolean b : v) { target.checkLogic(a, b); } }
    }

    @Test
    public void test_bitwiseOperation_Dynamic() {
        int[] bv = {0, 1, -1, 100};
        for(int x : bv) { try { target.bitwiseOperation(x); } catch(Exception e){} }
    }

    @Test
    public void test_calculateSum_Dynamic() {
        int[] bv = {0, 1, -1, 100};
        for(int x : bv) { for(int y : bv) { try { target.calculateSum(x, y); } catch(Exception e){} } }
    }

    @Test
    public void test_internalProcess_Dynamic() {
        int[] bv = {0, 1, -1, 100};
        for(int x : bv) { for(int y : bv) { try { target.internalProcess(x, y); } catch(Exception e){} } }
    }

}
