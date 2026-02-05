import java.io.*;
import java.nio.file.*;
import java.util.List;

public class AOIMutation {
    public static int applyAOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.matches(".*(return|=)\\s*\\w+.*") && !line.contains("==")) {
                count++;
                String mutated = line.replaceAll("(=|return)\\s*", "$1 -");
                MutationUtils.saveMutant(lines, i, mutated, "AOI", count);
            }
        }
        System.out.println("AOI Operator: " + count + " mutants generated.");
        return count;
    }
}