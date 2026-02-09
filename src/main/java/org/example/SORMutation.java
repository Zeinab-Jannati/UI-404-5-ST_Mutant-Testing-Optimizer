package org.example;

import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;

public class SORMutation {

    public static int applySOR(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        String[] operators = {"<<", ">>", ">>>"};

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.startsWith("//"))
                continue;

            for (String op : operators) {

                if (line.contains(op)) {

                    for (String replacement : operators) {

                        if (!op.equals(replacement)) {

                            count++;
                            String mutated = line.replace(op, replacement);
                            MutationUtils.saveMutant(lines, i, mutated, "SOR", count);
                        }
                    }
                }
            }
        }

        System.out.println("SOR Operator: " + count + " mutants generated.");
        return count;
    }
}
