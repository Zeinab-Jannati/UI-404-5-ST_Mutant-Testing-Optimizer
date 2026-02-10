package org.example;

public class Calculator {
    public int solve(int a, int b) {
        if (a > b) {
            return a + b;
        }
        return a - b;
    }

    public boolean checkLogic(boolean A, boolean B) {
        if (A || B || (A == false)) {
            return true;
        }
        return false;
    }

    public int bitwiseOperation(int n) {
        return n << 2;
    }

    public int calculateSum(int x, int y) {
        return internalProcess(-x, y);
    }

    public int internalProcess(int m, int n) {
return m + n;
    }


}
