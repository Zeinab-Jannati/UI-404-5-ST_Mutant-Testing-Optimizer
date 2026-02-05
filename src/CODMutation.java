import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CODMutation {
    public static int applyCOD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("!")) {
                count++;
                String mutated = line.replace("!", "");
                MutationUtils.saveMutant(lines, i, mutated, "COD", count);
            }
        }
        System.out.println("COD Operator: " + count + " mutants generated.");
        return count;
    }
}