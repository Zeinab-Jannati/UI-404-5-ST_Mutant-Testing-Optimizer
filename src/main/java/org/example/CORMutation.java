package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class CORMutation {

    public static int applyCOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String trimmed = line.trim();

            if (trimmed.startsWith("//") || (!trimmed.contains("if") && !trimmed.contains("while") && !trimmed.contains("for")))
                continue;

            int firstParen = line.indexOf('(');
            int lastParen = line.lastIndexOf(')');

            if (firstParen == -1 || lastParen == -1 || lastParen <= firstParen) continue;

            String prefix = line.substring(0, firstParen + 1);
            String condition = line.substring(firstParen + 1, lastParen);
            String suffix = line.substring(lastParen);

            for (int j = 0; j < condition.length() - 1; j++) {
                String op = condition.substring(j, j + 2);
                if (op.equals("&&") || op.equals("||")) {
                    String mutatedCondition;
                    if (op.equals("&&")) {
                        mutatedCondition = condition.substring(0, j) + "||" + condition.substring(j + 2);
                        count++;
                        MutationUtils.saveMutant(lines, i, prefix + mutatedCondition + suffix, "COR_AND", count);
                    } else {
                        mutatedCondition = condition.substring(0, j) + "&&" + condition.substring(j + 2);
                        count++;
                        MutationUtils.saveMutant(lines, i, prefix + mutatedCondition + suffix, "COR_OR", count);
                    }
                }
            }
        }

        System.out.println("Advanced COR: " + count + " mutants generated.");
        return count;
    }
}
