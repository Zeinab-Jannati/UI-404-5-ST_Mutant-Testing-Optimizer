package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class IPVR_Mutation {

    public static int applyIPVR(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        Pattern methodPattern = Pattern.compile("(public|private|protected)?\\s+\\w+\\s+(\\w+)\\s*\\(([^)]*,[^)]*)\\)");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Matcher matcher = methodPattern.matcher(line);

            if (matcher.find()) {
                String methodName = matcher.group(2);
                String params = matcher.group(3).trim();

                String[] paramArr = params.split("\\s*,\\s*");
                if (paramArr.length > 1) {
                    String swappedParams = paramArr[1] + ", " + paramArr[0];
                    String mutatedLine = line.replace("(" + params + ")", "(" + swappedParams + ")");
                    mutantCount++;
                    saveMutant(lines, i, mutatedLine, "IPVR", mutantCount);
                }
            }
        }

        System.out.println("IPVR Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }

    private static void saveMutant(List<String> originalLines, int mutatedLineIndex, String mutatedContent,
                                   String opName, int count) throws IOException {
        List<String> mutantContent = new ArrayList<>(originalLines);
        mutantContent.set(mutatedLineIndex, mutatedContent);

        Path mutantFolder = Paths.get("mutants");
        if (!Files.exists(mutantFolder)) Files.createDirectories(mutantFolder);

        String fileName = "mutant_" + opName + "_" + count + ".java";
        Files.write(mutantFolder.resolve(fileName), mutantContent);
    }
}
