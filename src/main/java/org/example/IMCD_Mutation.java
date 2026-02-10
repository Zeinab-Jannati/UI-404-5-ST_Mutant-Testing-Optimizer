package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IMCD_Mutation {

    public static int applyIMCD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String trimmedLine = line.trim();

            // =====================
            // Case 1: return solve(...)
            // =====================
            if (trimmedLine.matches("return\\s+solve\\s*\\(.*\\)\\s*;")) {
                mutantCount++;
                String mutated0 = line.replaceAll("return\\s+solve\\s*\\(.*\\)\\s*;", "return 0;");
                if (!mutated0.equals(line)) {
                    MutationUtils.saveMutant(lines, i, mutated0, "IMCD", mutantCount);
                }

                mutantCount++;
                String mutated1 = line.replaceAll("return\\s+solve\\s*\\(.*\\)\\s*;", "return 1;");
                if (!mutated1.equals(line)) {
                    MutationUtils.saveMutant(lines, i, mutated1, "IMCD", mutantCount);
                }
            }

            // =====================
            // Case 2: solve(...) used as argument or statement
            // =====================
            if (trimmedLine.contains("solve(") && !trimmedLine.startsWith("return")) {
                // Extract first argument
                mutantCount++;
                String mutatedA = line.replaceAll("solve\\s*\\(\\s*(\\w+)\\s*,\\s*(\\w+)\\s*\\)", "$1");
                if (!mutatedA.equals(line)) {
                    MutationUtils.saveMutant(lines, i, mutatedA, "IMCD", mutantCount);
                }

                // Extract second argument
                mutantCount++;
                String mutatedB = line.replaceAll("solve\\s*\\(\\s*(\\w+)\\s*,\\s*(\\w+)\\s*\\)", "$2");
                if (!mutatedB.equals(line)) {
                    MutationUtils.saveMutant(lines, i, mutatedB, "IMCD", mutantCount);
                }
            }
        }

        System.out.println("IMCD Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}
