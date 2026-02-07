import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IUOI_Mutation {
    public static int applyIUOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;
        String[] unaryOps = {"++", "--", "-", "+"};

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("solve(int a, int b)")) {
                for (String op : unaryOps) {
                    mutantCount++;
                    String mutated = line.replace("solve(int a, int b)", "solve(" + op + "a, int b)");                    MutationUtils.saveMutant(lines, i, mutated, "IUOI", mutantCount);
                }
            }
        }
        System.out.println("IUOI Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}