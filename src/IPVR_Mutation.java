import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPVR_Mutation {
    public static int applyIPVR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.contains("solve(")) {
                if (line.contains("solve(a, b)")) {
                    mutantCount++;
                    String mutated = line.replace("solve(a, b)", "solve(b, a)");
                    MutationUtils.saveMutant(lines, i, mutated, "IPVR", mutantCount);
                }
                if (line.contains("solve(x, y)")) {
                    mutantCount++;
                    String mutated = line.replace("solve(x, y)", "solve(y, x)");
                    MutationUtils.saveMutant(lines, i, mutated, "IPVR", mutantCount);
                }
                Pattern pattern = Pattern.compile("solve\\(\\s*(\\w+)\\s*,\\s*(\\w+)\\s*\\)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find() && !line.contains("solve(a, b)") && !line.contains("solve(x, y)")) {
                    mutantCount++;
                    String param1 = matcher.group(1);
                    String param2 = matcher.group(2);
                    String mutated = line.replace("solve(" + param1 + ", " + param2 + ")",
                            "solve(" + param2 + ", " + param1 + ")");
                    MutationUtils.saveMutant(lines, i, mutated, "IPVR", mutantCount);
                }
            }

            if (line.contains("calculateSum(")) {
                Pattern pattern = Pattern.compile("calculateSum\\(\\s*(\\w+)\\s*,\\s*(\\w+)\\s*\\)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    mutantCount++;
                    String param1 = matcher.group(1);
                    String param2 = matcher.group(2);
                    String mutated = line.replace("calculateSum(" + param1 + ", " + param2 + ")",
                            "calculateSum(" + param2 + ", " + param1 + ")");
                    MutationUtils.saveMutant(lines, i, mutated, "IPVR", mutantCount);
                }
            }
        }
        System.out.println("IPVR Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}