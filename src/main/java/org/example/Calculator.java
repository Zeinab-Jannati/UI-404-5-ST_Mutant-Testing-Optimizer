package org.example;

public class Calculator {

    public int solve(int a, int b) {
        if (a > 0) {
            return a + b;
        }
        return a - -b;
    }

}
