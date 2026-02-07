import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IMCD_Mutation {
    public static int applyIMCD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("solve(int a, int b)")) {
                mutantCount++;
                String mutated = line.replace("solve(int a, int b)", "0");                MutationUtils.saveMutant(lines, i, mutated, "IMCD", mutantCount);
            }
        }
        System.out.println("IMCD Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}