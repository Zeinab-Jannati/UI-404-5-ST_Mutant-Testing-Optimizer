import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IMCD_Mutation {
    public static int applyIMCD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.contains("return solve(")) {
                mutantCount++;
                String mutated = line.replaceAll("return solve\\(.*\\);", "return 0;");
                MutationUtils.saveMutant(lines, i, mutated, "IMCD", mutantCount);

                mutantCount++;
                String mutated2 = line.replaceAll("return solve\\(.*\\);", "return 1;");
                MutationUtils.saveMutant(lines, i, mutated2, "IMCD", mutantCount);
            }

            if (line.contains("solve(") && !line.contains("return solve(")) {
                mutantCount++;
                String mutated = line.replaceAll("solve\\(\\s*(\\w+)\\s*,\\s*(\\w+)\\s*\\)", "$1");
                if (!mutated.equals(line)) {
                    MutationUtils.saveMutant(lines, i, mutated, "IMCD", mutantCount);
                }

                mutantCount++;
                String mutated2 = line.replaceAll("solve\\(\\s*(\\w+)\\s*,\\s*(\\w+)\\s*\\)", "$2");
                if (!mutated2.equals(line)) {
                    MutationUtils.saveMutant(lines, i, mutated2, "IMCD", mutantCount);
                }
            }
        }
        System.out.println("IMCD Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}