import java.io.*;
import java.nio.file.*;
import java.util.List;

public class AORMutation {
    public static int applyAOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        String[] ops = {"+", "-", "*", "/"};

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (String op : ops) {
                if (line.contains(op) && !line.contains("==")) {
                    for (String replacement : ops) {
                        if (!op.equals(replacement)) {
                            count++;
                            String mutated = line.replace(op, replacement);
                            MutationUtils.saveMutant(lines, i, mutated, "AOR", count);
                        }
                    }
                }
            }
        }
        System.out.println("AOR Operator: " + count + " mutants generated.");
        return count;
    }
}