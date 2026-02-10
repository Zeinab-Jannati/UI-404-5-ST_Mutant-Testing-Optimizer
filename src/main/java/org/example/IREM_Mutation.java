package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class IREM_Mutation {

    public static int applyIREM(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        Pattern numericMethod = Pattern.compile("(public|private|protected)?\\s*(int|double|float|long)\\s+\\w+\\s*\\(([^)]*)\\)");
        Pattern booleanMethod = Pattern.compile("(public|private|protected)?\\s*boolean\\s+\\w+\\s*\\(([^)]*)\\)");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.equals("return true;")) {
                mutantCount++;
                String mutated = "return false;";
                MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
            } else if (line.equals("return false;")) {
                mutantCount++;
                String mutated = "return true;";
                MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
            }

            if (line.startsWith("return ")) {
                if (line.matches("return .*;")) {
                    String expr = line.substring(7, line.length() - 1).trim();

                    if (expr.contains("+")) {
                        mutantCount++;
                        String mutated = "return " + expr.replace("+", "-") + ";";
                        MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                    }
                    if (expr.contains("-")) {
                        mutantCount++;
                        String mutated = "return " + expr.replace("-", "+") + ";";
                        MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                    }
                    if (expr.contains("*")) {
                        mutantCount++;
                        String mutated = "return " + expr.replace("*", "/") + ";";
                        MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                    }
                    if (expr.contains("/")) {
                        mutantCount++;
                        String mutated = "return " + expr.replace("/", "*") + ";";
                        MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                    }
                    Pattern paramPattern = Pattern.compile("\\b([a-zA-Z_$][a-zA-Z\\d_$]*)\\b");
                    Matcher m = paramPattern.matcher(expr);
                    List<String> params = new ArrayList<>();
                    while (m.find()) params.add(m.group(1));
                    if (params.size() >= 2) {
                        mutantCount++;
                        String swapped = expr;
                        swapped = swapped.replaceFirst(params.get(0), "TMP_PARAM_SWAP");
                        swapped = swapped.replaceFirst(params.get(1), params.get(0));
                        swapped = swapped.replace("TMP_PARAM_SWAP", params.get(1));
                        String mutated = "return " + swapped + ";";
                        MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                    }
                    mutantCount++;
                    String mutated = "return 0;";
                    MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                }
            }
        }

        System.out.println("IREM Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }

}
