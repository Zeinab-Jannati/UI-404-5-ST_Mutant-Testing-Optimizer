package org.example;

public class Calculator {

    // 1. برای تست AOI, AOR, AOD, ROR (Traditional)
    // تغییر: استفاده از هر دو پارامتر و یک عملگر ریاضی
    public int solve(int a, int b) {
        if (a > 0) { // ROR اینجا میوتنت می‌سازد
            return a + b; // AOR, AOI, AOD اینجا عمل می‌کنند
        }
        return a - b;
    }

    // 2. برای تست COR, COI, COD به روش ACOC
    // تغییر: استفاده از ترکیب شرط‌ها برای تست اپراتورهای منطقی
    public boolean checkLogic(boolean A, boolean B) {
        // COR (تغییر && به ||)، COI (نقیض کردن)، COD (حذف یکی از شرط‌ها)
        if (A && B || (A == false)) {
            return true;
        }
        return false;
    }

    // 3. برای تست Integration Mutation (IPVR, IUOI, IPEX, IMCD, IREM)
    // تغییر: فراخوانی یک متد دیگر با پارامترها برای تست تعامل بین متدها
    public int calculateSum(int x, int y) {
        // IPVR: جایجایی x با y در فراخوانی متد
        // IUOI: تبدیل x به -x قبل از ارسال
        // IREM: حذف کامل فراخوانی متد
        return internalProcess(x, y);
    }

    // متد کمکی برای ایجاد وابستگی و تست Integration
    public int internalProcess(int m, int n) {
        return m * n;
    }
}