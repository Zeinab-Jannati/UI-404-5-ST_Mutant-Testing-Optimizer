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

            // تعویض AND با OR (با رعایت فواصل برای امنیت بیشتر)
            if (line.contains("&&")) {
                String mutated = line.replace("&&", "||");
                count++;
                MutationUtils.saveMutant(lines, i, mutated, "COR", count);
            }

            // تعویض OR با AND
            if (line.contains("||")) {
                String mutated = line.replace("||", "&&");
                count++;
                MutationUtils.saveMutant(lines, i, mutated, "COR", count);
            }
        }
        System.out.println("COR Operator: " + count + " mutants generated.");
        return count;
    }
}