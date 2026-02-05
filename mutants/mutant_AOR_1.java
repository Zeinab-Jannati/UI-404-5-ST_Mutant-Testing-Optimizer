public class Calculator {

    ++ متد برای تست AOI, AOR, AOD
    public int solve(int a, int b) {
        return a + b; // هدف اصلی جهش‌ها
    }

    // متد برای تست COR, COI, COD به روش ACOC
    // شرط: (A && B)
    public boolean checkLogic(boolean A, boolean B) {
        if (A && B) {
            return true;
        }
        return false;
    }
}
