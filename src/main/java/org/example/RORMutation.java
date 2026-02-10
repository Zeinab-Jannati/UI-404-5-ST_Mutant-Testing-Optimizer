package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RORMutation {

    public static int applyROR(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        String[] relOps = {">=", "<=", "!=", "==", ">", "<"};

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);

            if (line.trim().startsWith("//")) continue;

            if (!line.contains("if") && !line.contains("while")) continue;

            if (line.contains("<<") || line.contains(">>")) continue;

            for (String op : relOps) {

                if (line.contains(" " + op + " ")) {

                    for (String replacement : relOps) {

                        if (!op.equals(replacement)) {

                            if (line.contains("true") || line.contains("false")) {
                                if (!(op.equals("==") || op.equals("!="))) continue;
                                if (!(replacement.equals("==") || replacement.equals("!="))) continue;
                            }

                            String mutated = line.replace(
                                    " " + op + " ",
                                    " " + replacement + " "
                            );

                            count++;
                            MutationUtils.saveMutant(lines, i, mutated, "ROR", count);
                        }
                    }
                }
            }
        }

        System.out.println("ROR Operator: " + count + " mutants generated.");
        return count;
    }
}
