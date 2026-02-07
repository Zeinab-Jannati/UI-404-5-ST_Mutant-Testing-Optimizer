import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IPVR_Mutation {
    public static int applyIPVR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // پیدا کردن فراخوانی متدها مثل solve(a, b)
            if (line.contains("solve(")) {  // تغییر این خط
                mutantCount++;
                String mutated = line.replace("solve(a, b)", "solve(b, a)");
                MutationUtils.saveMutant(lines, i, mutated, "IPVR", mutantCount);
            }
        }
        System.out.println("IPVR Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }
}