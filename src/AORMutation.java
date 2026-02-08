import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class AORMutation {

    private static final String[] OPS = {"+", "-", "*", "/"};

    public static int applyAOR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.matches(".*return\\s+\\w+\\s*[+\\-*/]\\s*\\w+\\s*;.*")) {
                for (String op : OPS) {
                    if (line.contains(" " + op + " ")) {
                        for (String rep : OPS) {
                            if (!op.equals(rep)) {
                                String mutated = line.replace(
                                        " " + op + " ",
                                        " " + rep + " "
                                );
                                count++;
                                MutationUtils.saveMutant(lines, i, mutated, "AOR", count);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("AOR Operator: " + count + " mutants generated.");
        return count;
    }
}
