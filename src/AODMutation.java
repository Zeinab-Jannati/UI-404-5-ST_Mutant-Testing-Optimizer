import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class AODMutation {
    private static final String[] OPERATORS = {"\\+", "-", "\\*", "/"};

    public static int applyAOD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (String op : OPERATORS) {
                if (line.matches(".*\\w\\s*" + op + "\\s*\\w.*")) {
                    count++;
                    String mutatedLine = line.replaceAll("(\\w)\\s*" + op + "\\s*\\w", "$1");

                    saveMutant(lines, i, mutatedLine, count);
                }
            }
        }
        System.out.println("AOD Operator: Generated " + count + " mutants.");
        return count;
    }

    private static void saveMutant(List<String> originalLines, int lineIdx, String mutatedLine, int id) throws IOException {
        List<String> mutantContent = new ArrayList<>(originalLines);
        mutantContent.set(lineIdx, mutatedLine);
        Files.write(Paths.get("mutant_AOD_" + id + ".java"), mutantContent);
    }
}