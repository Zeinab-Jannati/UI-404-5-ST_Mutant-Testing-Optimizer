package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class AORMutation {
    private static final String[] OPS = {"+", "-", "*", "/"};

    public static int applyAOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.startsWith("//") || line.isEmpty()) continue;

            // --- ۱. تغییر Binary Operators (مثل a + b) ---
            for (String op : OPS) {
                int idx = line.indexOf(" " + op + " ");
                if (idx == -1) continue;

                String left = line.substring(0, idx).trim();
                String right = line.substring(idx + 3).trim(); // 3 = فاصله+عملگر+فاصله

                // فقط Binary operator بین دو مقدار را تغییر بده، جلوی right اگر Unary بود تغییر نده
                String rightClean = right;
                if (right.startsWith("-") || right.startsWith("+")) {
                    rightClean = right; // نگه داشتن Unary بدون تغییر
                }

                for (String repOp : OPS) {
                    if (!op.equals(repOp)) {
                        // جایگزینی عملگر بدون تغییر Unary جلوی right
                        String mutated = left + " " + repOp + " " + rightClean;
                        count++;
                        MutationUtils.saveMutant(lines, i, mutated, "AOR_Binary", count);
                    }
                }
            }

            // --- ۲. تغییر Unary Operators (مثل -a یا +b) ---
            // فقط زمانی اعمال شود که جلوی متغیر یک Unary مستقل باشد
            String unaryRegex = "(?<!\\S)([+\\-])([a-zA-Z_$][a-zA-Z\\d_$]*)";
            java.util.regex.Pattern uPattern = java.util.regex.Pattern.compile(unaryRegex);
            java.util.regex.Matcher uMatcher = uPattern.matcher(line);

            while (uMatcher.find()) {
                String currentSign = uMatcher.group(1);
                String varName = uMatcher.group(2);

                String newSign = currentSign.equals("-") ? "+" : "-";

                // جایگزینی علامت بدون دستکاری بقیه خط
                String mutated = line.substring(0, uMatcher.start(1)) + newSign + line.substring(uMatcher.start(2));
                count++;
                MutationUtils.saveMutant(lines, i, mutated, "AOR_Unary", count);
            }
        }

        System.out.println("AOR Operator: " + count + " smart mutants generated.");
        return count;
    }
}
