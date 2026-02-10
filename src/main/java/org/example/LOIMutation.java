package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LOIMutation {

    public static int applyLOI(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String originalLine = lines.get(i);
            String line = originalLine.trim();

            // رد کردن کامنت‌ها
            if (line.startsWith("//")) continue;

            // فقط if / while
            if (!line.startsWith("if") && !line.startsWith("while")) continue;

            // شرط ساده باشد (نباید && یا || داشته باشد)
            if (line.contains("&&") || line.contains("||")) continue;

            int start = line.indexOf("(");
            int end = line.lastIndexOf(")");

            if (start == -1 || end == -1 || end <= start) continue;

            String prefix = originalLine.substring(0, originalLine.indexOf("(") + 1);
            String condition = originalLine.substring(
                    originalLine.indexOf("(") + 1,
                    originalLine.lastIndexOf(")")
            );
            String suffix = originalLine.substring(originalLine.lastIndexOf(")"));

            // Mutant 1: insert &&
            String mutant1 = prefix + condition + " && true" + suffix;
            count++;
            MutationUtils.saveMutant(lines, i, mutant1, "LOI_AND", count);

            // Mutant 2: insert ||
            String mutant2 = prefix + condition + " || false" + suffix;
            count++;
            MutationUtils.saveMutant(lines, i, mutant2, "LOI_OR", count);
        }

        System.out.println("LOI Operator: " + count + " mutants generated.");
        return count;
    }
}
