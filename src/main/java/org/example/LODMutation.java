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
            String line = lines.get(i).trim();

            // ⛔ کامنت‌ها رو رد کن
            if (line.startsWith("//")) {
                continue;
            }

            if (line.contains("&&") || line.contains("||")) {

                String operator = line.contains("&&") ? "&&" : "||";

                int start = line.indexOf("(");
                int end = line.indexOf(")");

                if (start != -1 && end != -1) {

                    String condition = line.substring(start + 1, end);
                    String[] parts = condition.split("\\Q" + operator + "\\E");

                    if (parts.length == 2) {

                        // Mutant 1
                        count++;
                        String mutated1 = line.substring(0, start + 1)
                                + parts[0].trim()
                                + line.substring(end);
                        MutationUtils.saveMutant(lines, i, mutated1, "LOD", count);

                        // Mutant 2
                        count++;
                        String mutated2 = line.substring(0, start + 1)
                                + parts[1].trim()
                                + line.substring(end);
                        MutationUtils.saveMutant(lines, i, mutated2, "LOD", count);
                    }
                }
            }
        }

        System.out.println("LOD Operator: " + count + " mutants generated.");
        return count;
    }
}
