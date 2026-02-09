public class Calculator {
    // متد برای تست AOI, AOR, AOD
//    public int solve(int a, int b) {
//        return a + b; // این باید جمع باشد نه فقط a!
//    }

    // متد برای تست AOI, AOR, AOD و ROR
    public int solve(int a, int b) {
        // اضافه کردن شرط برای فعال شدن ROR (مانند > یا <)
        if (a > b) {
            return a + b;
        }
        return a - b;
    }

    // متد برای تست SOR (Shift Operator Replacement)
    public int shiftBits(int a) {
        // عملگر شیفت برای اینکه SOR بتواند آن را به >> یا موارد دیگر تغییر دهد
        return a << 2;
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
        return solve(x, y);
    }
}