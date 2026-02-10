package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.regex.*;
import java.util.ArrayList;

public class AOIMutation {

    public static int applyAOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        boolean insideNumericMethod = false;

        Pattern numericMethod = Pattern.compile("(public|private|protected)?\\s*(int|double|float|long)\\s+\\w+\\s*\\(");
        Pattern booleanMethod = Pattern.compile("(public|private|protected)?\\s*boolean\\s+\\w+\\s*\\(");

        Pattern varPattern = Pattern.compile("\\b([a-zA-Z_$][a-zA-Z\\d_$]*)\\b");

        String keywords = "|public|private|protected|static|final|return|if|else|int|boolean|double|float|long|class|package|import|";

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (numericMethod.matcher(line).find()) { insideNumericMethod = true; continue; }
            if (booleanMethod.matcher(line).find()) { insideNumericMethod = false; continue; }

            if (!insideNumericMethod || line.trim().startsWith("package") || line.trim().startsWith("import")) continue;

            Matcher m = varPattern.matcher(line);
            while (m.find()) {
                String varName = m.group(1);

                if (keywords.contains("|" + varName + "|")) continue;

                if (m.start() > 0 && line.charAt(m.start() - 1) == '-') continue;

                String mutated = line.substring(0, m.start()) + "-" + varName + line.substring(m.end());

                count++;
                saveMutantWithPackage(lines, i, mutated, "AOI", count);
            }
        }
        System.out.println("AOI Operator: " + count + " smart mutants generated.");
        return count;
    }

    private static void saveMutantWithPackage(List<String> originalLines, int mutatedLineIndex, String mutatedContent, String opName, int count) throws IOException {
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