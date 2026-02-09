package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IPEX_Mutation {
    public static int applyIPEX(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.contains("solve(int a, int b)")) {
                mutantCount++;
                String mutated = line.replace("solve(int a, int b)", "solve(int b, int a)");
                MutationUtils.saveMutant(lines, i, mutated, "IPEX", mutantCount);
            }

            if (line.contains("calculateSum(int x, int y)")) {
                mutantCount++;
                String mutated = line.replace("calculateSum(int x, int y)", "calculateSum(int y, int x)");
                MutationUtils.saveMutant(lines, i, mutated, "IPEX", mutantCount);
            }

            if (line.contains("checkLogic(boolean A, boolean B)")) {
                mutantCount++;
                String mutated = line.replace("checkLogic(boolean A, boolean B)",
                        "checkLogic(boolean B, boolean A)");
                MutationUtils.saveMutant(lines, i, mutated, "IPEX", mutantCount);
            }
        }
        System.out.println("IPEX Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}