import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IREM_Mutation {
    public static int applyIREM(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.contains("return a + b")) {
                mutantCount++;
                String mutated = line.replace("return a + b", "return a - b");
                MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);

                mutantCount++;
                String mutated2 = line.replace("return a + b", "return a * b");
                MutationUtils.saveMutant(lines, i, mutated2, "IREM", mutantCount);

                mutantCount++;
                String mutated3 = line.replace("return a + b", "return b + a");
                MutationUtils.saveMutant(lines, i, mutated3, "IREM", mutantCount);
            }

            if (line.contains("return true")) {
                if (line.trim().equals("return true;")) {
                    mutantCount++;
                    String mutated = line.replace("return true;", "return false;");
                    MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
                }
            }

            if (line.contains("return solve(")) {
                mutantCount++;
                String mutated = line.replaceAll("return solve\\(.*\\);", "return 0;");
                MutationUtils.saveMutant(lines, i, mutated, "IREM", mutantCount);
            }
        }
        System.out.println("IREM Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}