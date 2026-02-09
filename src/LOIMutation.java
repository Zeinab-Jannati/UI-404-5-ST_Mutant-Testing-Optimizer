
import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;

public class LOIMutation {

    public static int applyLOI(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // رد کردن کامنت
            if (line.startsWith("//")) {
                continue;
            }

            if (line.contains("&&") || line.contains("||")) {

                String operator = line.contains("&&") ? "&&" : "||";

                int start = line.indexOf("(");
                int end = line.indexOf(")");

                if (start != -1 && end != -1) {

                    String condition = line.substring(start + 1, end);
                    String[] parts = condition.split("\\Q" + operator + "\\E");

                    if (parts.length == 2) {

                        String left = parts[0].trim();
                        String right = parts[1].trim();

                        // Mutant 1 → negate left
                        count++;
                        String mutated1 = line.substring(0, start + 1)
                                + "!" + left + " " + operator + " " + right
                                + line.substring(end);
                        MutationUtils.saveMutant(lines, i, mutated1, "LOI", count);

                        // Mutant 2 → negate right
                        count++;
                        String mutated2 = line.substring(0, start + 1)
                                + left + " " + operator + " !" + right
                                + line.substring(end);
                        MutationUtils.saveMutant(lines, i, mutated2, "LOI", count);
                    }
                }
            }
        }

        System.out.println("LOI Operator: " + count + " mutants generated.");
        return count;
    }
}

