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
            String trimmed = line.trim();

            if (trimmed.startsWith("if") && line.contains("(") && line.contains(")")) {

                int firstParen = line.indexOf("(");
                int lastParen = line.lastIndexOf(")");

                if (firstParen != -1 && lastParen != -1 && firstParen < lastParen) {
                    String condition = line.substring(firstParen + 1, lastParen);


                    String mutatedLine = line.substring(0, firstParen + 1) +
                            "!(" + condition + ")" +
                            line.substring(lastParen);

                    count++;
                    MutationUtils.saveMutant(lines, i, mutatedLine, "COI", count);
                }
            }
        }

        System.out.println("COI Operator: " + count + " mutants generated.");
        return count;
    }
}