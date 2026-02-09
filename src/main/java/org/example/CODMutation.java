package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class CODMutation {

    public static int applyCOD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        Pattern ifPattern = Pattern.compile("if\\s*\\((.*)\\)");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Matcher ifMatcher = ifPattern.matcher(line);

            if (!ifMatcher.find()) continue;

            String condition = ifMatcher.group(1).trim();

            // شکستن شرط به operandها (سطح بالا)
            List<String> operands = splitCondition(condition);

            // اگر فقط یک operand داشت، COD معنی ندارد
            if (operands.size() < 2) continue;

            for (int j = 0; j < operands.size(); j++) {
                List<String> mutatedOperands = new ArrayList<>();

                for (int k = 0; k < operands.size(); k++) {
                    if (k != j) {
                        mutatedOperands.add(operands.get(k));
                    }
                }

                String newCondition = String.join(" || ", mutatedOperands);

                String mutatedLine = line.replace(condition, newCondition);
                count++;
                MutationUtils.saveMutant(lines, i, mutatedLine, "COD", count);
            }
        }

        System.out.println("COD Operator: " + count + " mutants generated.");
        return count;
    }

    // فقط && و || سطح بالا رو جدا می‌کنه (نه داخل پرانتز)
    private static List<String> splitCondition(String condition) {
        List<String> parts = new ArrayList<>();
        int depth = 0;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < condition.length(); i++) {
            char c = condition.charAt(i);

            if (c == '(') depth++;
            if (c == ')') depth--;

            if (depth == 0 && i + 1 < condition.length()) {
                String twoChars = condition.substring(i, i + 2);
                if (twoChars.equals("&&") || twoChars.equals("||")) {
                    parts.add(current.toString().trim());
                    current.setLength(0);
                    i++; // skip next char
                    continue;
                }
            }
            current.append(c);
        }

        parts.add(current.toString().trim());
        return parts;
    }
}
