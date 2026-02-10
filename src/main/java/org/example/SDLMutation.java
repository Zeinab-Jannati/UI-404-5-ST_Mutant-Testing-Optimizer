package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SDLMutation {

    public static int applySDL(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        boolean inMethod = false;
        int braceLevel = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String trimmed = line.trim();

            if (trimmed.isEmpty() || trimmed.startsWith("//") ||
                    trimmed.startsWith("package") || trimmed.startsWith("import") ||
                    trimmed.startsWith("class") || trimmed.startsWith("@")) continue;

            if (trimmed.contains("{")) {
                braceLevel += countChar(trimmed, '{');
                if (!inMethod && line.contains("(") && line.contains(")")) {
                    inMethod = true;
                }
            }
            if (trimmed.contains("}")) {
                braceLevel -= countChar(trimmed, '}');
                if (inMethod && braceLevel == 0) {
                    inMethod = false;
                }
            }

            if (inMethod && (trimmed.endsWith(";") || trimmed.startsWith("return"))) {
                String mutated = line.startsWith("return") ? getSafeReturn(line) : "// deleted statement";
                count++;
                MutationUtils.saveMutant(lines, i, mutated, "SDL", count);
            }
        }

        System.out.println("SDL Operator: " + count + " mutants generated.");
        return count;
    }

    private static int countChar(String s, char c) {
        int count = 0;
        for (char ch : s.toCharArray()) if (ch == c) count++;
        return count;
    }

    private static String getSafeReturn(String originalReturn) {
        if (originalReturn.contains("true") || originalReturn.contains("false")) {
            return "return false;";
        } else if (originalReturn.matches(".*\\d+.*")) {
            return "return 0;";
        } else {
            return "return null;";
        }
    }
}
