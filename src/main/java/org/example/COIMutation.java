package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class COIMutation {
    public static int applyCOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // فقط روی خطوطی که شامل شرط هستند (if یا while) کار کن
            // Regex: پیدا می‌کند اگر خط با if شروع شود و پرانتز داشته باشد
            if (line.trim().startsWith("if") && line.contains("(") && line.contains(")")) {
                // پیدا کردن محتوای داخل اولین و آخرین پرانتز شرط
                int firstParen = line.indexOf("(");
                int lastParen = line.lastIndexOf(")");

                String prefix = line.substring(0, firstParen + 1);
                String condition = line.substring(firstParen + 1, lastParen);
                String suffix = line.substring(lastParen);

                // نقیض کردن کل شرط: if (condition) -> if (!(condition))
                String mutated = prefix + "!(" + condition + ")" + suffix;

                count++;
                MutationUtils.saveMutant(lines, i, mutated, "COI", count);
            }
        }
        System.out.println("COI Operator: " + count + " mutants generated.");
        return count;
    }
}