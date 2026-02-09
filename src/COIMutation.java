import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class COIMutation {

    public static int applyCOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // if (condition)
            if (line.matches(".*if\\s*\\(.*\\).*")) {
                String mutated = line.replaceAll(
                        "if\\s*\\((.*)\\)",
                        "if (!($1))"
                );
                count++;
                MutationUtils.saveMutant(lines, i, mutated, "COI", count);
            }
        }

        System.out.println("COI Operator: " + count + " mutants generated.");
        return count;
    }
}
