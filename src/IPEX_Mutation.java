import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IPEX_Mutation {
    public static int applyIPEX(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("solve(a, b)")) {
                mutantCount++;
                String mutated = line.replace("solve(a, b)", "solve(b, a)");
                MutationUtils.saveMutant(lines, i, mutated, "IPEX", mutantCount);
            }
        }
        System.out.println("IPEX Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}