package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LODMutation {

    public static int applyLOD(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            String trimmed = line.trim();

            if (trimmed.startsWith("//")) continue;

            if (!trimmed.startsWith("if") && !trimmed.startsWith("while")) continue;

            int open = line.indexOf('(');
            int close = line.lastIndexOf(')');

            if (open == -1 || close == -1 || close <= open) continue;

            String prefix = line.substring(0, open + 1);
            String condition = line.substring(open + 1, close);
            String suffix = line.substring(close);

            String operator = null;

            if (condition.contains("||")) operator = "\\|\\|";
            else if (condition.contains("&&")) operator = "&&";
            else continue;

            String[] operands = condition.split("\\s*" + operator + "\\s*");

            for (String operand : operands) {
                String mutatedLine = prefix + operand.trim() + suffix;
                count++;
                MutationUtils.saveMutant(lines, i, mutatedLine, "LOD", count);
            }
        }

        System.out.println("LOD Operator: " + count + " mutants generated.");
        return count;
    }
}
