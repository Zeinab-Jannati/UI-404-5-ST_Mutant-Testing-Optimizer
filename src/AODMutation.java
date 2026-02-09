import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class AODMutation {

    public static int applyAOD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // return a + b;
            if (line.matches(".*return\\s+\\w+\\s*[+\\-*/]\\s*\\w+\\s*;.*")) {

                // return a;
                String m1 = line.replaceAll(
                        "return\\s+(\\w+)\\s*[+\\-*/]\\s*(\\w+)\\s*;",
                        "return $1;"
                );
                count++;
                MutationUtils.saveMutant(lines, i, m1, "AOD", count);

                // return b;
                String m2 = line.replaceAll(
                        "return\\s+(\\w+)\\s*[+\\-*/]\\s*(\\w+)\\s*;",
                        "return $2;"
                );
                count++;
                MutationUtils.saveMutant(lines, i, m2, "AOD", count);
            }
        }

        System.out.println("AOD Operator: " + count + " mutants generated.");
        return count;
    }
}
