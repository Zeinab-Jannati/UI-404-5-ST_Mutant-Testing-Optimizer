import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class AOIMutation {

    public static int applyAOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // return a;
            if (line.matches(".*return\\s+\\w+\\s*;.*")) {
                String mutated = line.replaceAll(
                        "return\\s+(\\w+)\\s*;",
                        "return -$1;"
                );
                count++;
                MutationUtils.saveMutant(lines, i, mutated, "AOI", count);
            }
        }

        System.out.println("AOI Operator: " + count + " mutants generated.");
        return count;
    }
}
