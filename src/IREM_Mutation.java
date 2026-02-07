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
            }
        }
        System.out.println("IREM Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}