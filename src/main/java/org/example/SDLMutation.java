package org.example;

import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;

public class SDLMutation {

    public static int applySDL(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.startsWith("return") || line.endsWith(";")) {

                count++;
                String mutated = "// deleted statement";
                MutationUtils.saveMutant(lines, i, mutated, "SDL", count);
            }
        }

        System.out.println("SDL Operator: " + count + " mutants generated.");
        return count;
    }
}
