import java.io.*;
import java.nio.file.*;
import java.util.List;

public class COIMutation {
    public static int applyCOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("if (") || line.contains("if(")) {
                count++;
                String mutated = line.replace("if (", "if (!(").replace("if(", "if(!(");
                MutationUtils.saveMutant(lines, i, mutated, "COI", count);
            }
        }
        System.out.println("COI Operator: " + count + " mutants generated.");
        return count;
    }
}