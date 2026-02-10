package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LORMutation {

    public static int applyLOR(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {

            String originalLine = lines.get(i);

            if (originalLine.trim().startsWith("//")) {
                continue;
            }

            if (originalLine.contains("&&") || originalLine.contains("||")) {

                for (int idx = 0; idx < originalLine.length() - 1; idx++) {

                    String sub = originalLine.substring(idx, idx + 2);

                    if (sub.equals("&&") || sub.equals("||")) {

                        String replacement = sub.equals("&&") ? "||" : "&&";

                        String mutatedLine =
                                originalLine.substring(0, idx)
                                        + replacement
                                        + originalLine.substring(idx + 2);

                        count++;
                        MutationUtils.saveMutant(lines, i, mutatedLine, "LOR", count);
                    }
                }
            }
        }

        System.out.println("LOR Operator: " + count + " mutants generated.");
        return count;
    }
}
