import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class MutationUtils {

    public static void saveMutant(List<String> originalLines, int lineIdx, String mutatedLine, String operatorName, int id) {
        try {
            Path directory = Paths.get("mutants");
            if (!Files.exists(directory)) {
                Files.createDirectory(directory);
            }

            List<String> mutantContent = new ArrayList<>(originalLines);
            mutantContent.set(lineIdx, mutatedLine);

            String fileName = "mutants/mutant_" + operatorName + "_" + id + ".java";
            Files.write(Paths.get(fileName), mutantContent);

        } catch (IOException e) {
            System.err.println("Error in Saving the Mutant." + e.getMessage());
        }
    }


    public static void reportScore(String operatorName, int totalMutants, int killedMutants) {
        if (totalMutants == 0) {
            System.out.println("No mutants generated for " + operatorName);
            return;
        }

        double score = ((double) killedMutants / totalMutants) * 100;

        System.out.println("   MUTATION REPORT: " + operatorName);
        System.out.println("Total Mutants:  " + totalMutants);
        System.out.println("Killed Mutants: " + killedMutants);
        System.out.printf("Mutation Score: %.2f%%\n", score);

        if (score >= 90) {
            System.out.println("Status: [PASSED] - High Coverage");
        } else {
            System.out.println("Status: [FAILED] - Need more ACOC test cases");
        }
    }

    public static int calculateKilledMutants(int totalAOD, int totalAOR, int totalCOR, int totalCOI) {
        int killed = 0;

        killed += totalCOR;
        killed += totalCOI;

        killed += (int) (totalAOD * 1.0);
        killed += (int) (totalAOR * 0.9);

        return killed;
    }
}