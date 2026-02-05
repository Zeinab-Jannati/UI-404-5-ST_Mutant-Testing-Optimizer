import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CORMutation {
    public static int applyCOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("&&") || line.contains("||")) {
                count++;
                String mutated = line.contains("&&") ? line.replace("&&", "||") : line.replace("||", "&&");
                MutationUtils.saveMutant(lines, i, mutated, "COR", count);
            }
        }
        System.out.println("COR Operator: " + count + " mutants generated.");
        return count;
    }
}