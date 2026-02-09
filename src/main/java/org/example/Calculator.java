package org.example;

public class Calculator {
    // متد برای تست AOI, AOR, AOD
    public int solve(int a, int b) {
        return -a; // این باید جمع باشد نه فقط a!
    }

    // متد برای تست COR, COI, COD به روش ACOC
    public boolean checkLogic(boolean A, boolean B) {
        if (A && B) {
            return true;
        }
        return false;
    }

    // متد جدید برای تست Integration Mutation
    public int calculateSum(int x, int y) {
        return 0;
    }
}
