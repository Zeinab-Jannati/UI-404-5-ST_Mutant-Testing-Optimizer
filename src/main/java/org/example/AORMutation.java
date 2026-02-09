package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class AORMutation {
    private static final String[] OPS = {"+", "-", "*", "/"};

    public static int applyAOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        // ۱. تعریف الگوهای Regex برای شناسایی هوشمند
        // الگوی Unary: پیدا کردن علامت (+ یا -) که قبل از یک نام متغیر (بدون در نظر گرفتن نام) قرار دارد
        String unaryRegex = "([+\\-])\\s*([a-zA-Z_$][a-zA-Z\\d_$]*)";

        // الگوی Binary: پیدا کردن عملگرهای اصلی بین دو متغیر یا عدد
        String binaryRegex = "([a-zA-Z\\d_$]+)\\s*([+\\-*/])\\s*([a-zA-Z\\d_$]+)";

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().startsWith("//") || line.trim().isEmpty()) continue; // نادیده گرفتن کامنت و خط خالی

            // --- بخش اول: مدیریت Unary Operators (مثل -a یا +c) ---
            java.util.regex.Pattern uPattern = java.util.regex.Pattern.compile(unaryRegex);
            java.util.regex.Matcher uMatcher = uPattern.matcher(line);

            if (uMatcher.find()) {
                String currentSign = uMatcher.group(1);
                String varName = uMatcher.group(2);

                // معکوس کردن علامت: اگر مثبت بود منفی، اگر منفی بود مثبت
                String newSign = currentSign.equals("-") ? "+" : "-";
                String mutated = line.replaceFirst("\\" + currentSign + "\\s*" + varName, newSign + varName);

                count++;
                MutationUtils.saveMutant(lines, i, mutated, "AOR_Unary", count);
            }

            // --- بخش دوم: مدیریت Binary Operators (مثل a + b) ---
            java.util.regex.Pattern bPattern = java.util.regex.Pattern.compile(binaryRegex);
            java.util.regex.Matcher bMatcher = bPattern.matcher(line);

            if (bMatcher.find()) {
                String leftPart = bMatcher.group(1);
                String currentOp = bMatcher.group(2);
                String rightPart = bMatcher.group(3);

                String[] allOps = {"+", "-", "*", "/"};
                for (String repOp : allOps) {
                    if (!currentOp.equals(repOp)) {
                        // جایگزینی فقط عملگر وسط با حفظ طرفین
                        String mutated = line.replaceFirst(
                                java.util.regex.Pattern.quote(leftPart) + "\\s*\\" + currentOp + "\\s*" + java.util.regex.Pattern.quote(rightPart),
                                leftPart + " " + repOp + " " + rightPart
                        );

                        count++;
                        MutationUtils.saveMutant(lines, i, mutated, "AOR_Binary", count);
                    }
                }
            }
        }
        System.out.println("AOR Operator: " + count + " smart mutants generated.");
        return count;
    }
}