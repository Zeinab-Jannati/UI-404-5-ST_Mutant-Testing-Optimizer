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
            String line = lines.get(i).trim();

            // رد کردن کامنت
            if (line.startsWith("//")) {
                continue;
            }

            if (line.contains("&&") || line.contains("||")) {

                String mutated;

                if (line.contains("&&")) {
                    mutated = line.replace("&&", "||");
                } else {
                    mutated = line.replace("||", "&&");
                }

                count++;
                MutationUtils.saveMutant(lines, i, mutated, "LOR", count);
            }
        }

        System.out.println("LOR Operator: " + count + " mutants generated.");
        return count;
    }
}


