package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SORMutation {

    public static int applySOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.trim().startsWith("//")) continue;

            String trimmed = line.trim();

            if (trimmed.startsWith("return ")) {
                String mutated = generateReturnMutation(trimmed);
                if (mutated != null) {
                    count++;
                    MutationUtils.saveMutant(lines, i, mutated, "SOR", count);
                }
            } else if (trimmed.contains("=") && !trimmed.startsWith("if") && !trimmed.startsWith("while")) {
                String mutated = generateAssignmentMutation(trimmed);
                if (mutated != null) {
                    count++;
                    MutationUtils.saveMutant(lines, i, mutated, "SOR", count);
                }
            }
        }

        System.out.println("SOR Operator: " + count + " mutants generated.");
        return count;
    }

    private static String generateReturnMutation(String line) {
        if (line.contains("true")) return line.replace("true", "false");
        if (line.contains("false")) return line.replace("false", "true");
        if (line.matches(".*return\\s+\\d+.*")) return line.replaceAll("\\d+", "0");
        if (line.contains("+")) return line.replace("+", "-");
        if (line.contains("-")) return line.replace("-", "+");
        if (line.contains("*")) return line.replace("*", "+");
        if (line.contains("/")) return line.replace("/", "*");
        return null;
    }

    private static String generateAssignmentMutation(String line) {
        if (line.contains("+")) return line.replace("+", "-");
        if (line.contains("-")) return line.replace("-", "+");
        if (line.contains("*")) return line.replace("*", "+");
        if (line.contains("/")) return line.replace("/", "*");
        return null;
    }
}
