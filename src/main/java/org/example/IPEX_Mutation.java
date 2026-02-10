package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class IPEX_Mutation {

    public static int applyIPEX(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        Pattern callPattern = Pattern.compile("\\b(\\w+)\\s*\\(\\s*([^,()]+)\\s*,\\s*([^,()]+)\\s*\\)");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Matcher m = callPattern.matcher(line);

            while (m.find()) {
                String methodName = m.group(1);
                String arg1 = m.group(2);
                String arg2 = m.group(3);

                String mutated = line.substring(0, m.start(1))
                        + methodName + "(" + arg2 + ", " + arg1 + ")"
                        + line.substring(m.end(3) + 1);

                mutantCount++;
                saveMutant(lines, i, mutated, "IPEX", mutantCount);
            }
        }

        System.out.println("IPEX Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }

    private static void saveMutant(List<String> originalLines, int mutatedLineIndex,
                                   String mutatedContent, String opName, int count) throws IOException {
        List<String> mutantContent = new ArrayList<>(originalLines);
        mutantContent.set(mutatedLineIndex, mutatedContent);

        if (!mutantContent.isEmpty() && !mutantContent.get(0).trim().startsWith("package")) {
            mutantContent.add(0, "package org.example;");
        }

        Path mutantFolder = Paths.get("mutants");
        if (!Files.exists(mutantFolder)) Files.createDirectories(mutantFolder);

        String fileName = "mutant_" + opName + "_" + count + ".java";
        Files.write(mutantFolder.resolve(fileName), mutantContent);
    }
}
