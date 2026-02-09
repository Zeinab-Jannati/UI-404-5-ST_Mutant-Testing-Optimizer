package org.example;

public class Calculator {
    public int solve(int a, int b) {
        if (true) {
            return a + b;
        }
        return a - b;
    }

    public boolean checkLogic(boolean A, boolean B) {
        if (A && B || (A == false)) {
            return true;
        }
        return false;
    }

    public int calculateSum(int x, int y) {
        return internalProcess(x, y);
    }


    public int internalProcess(int m, int n) {
        return m * n;
    }

    public int solvee(int a, int b) {
        if (!(a > 0)) {
            return a + b;
        }
        return a - b;
    }
}
