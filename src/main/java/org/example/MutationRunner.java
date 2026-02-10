package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MutationRunner {
    static String TARGET_PATH;
    static String BACKUP;
    static final String MUTANTS_DIR = "mutants";

    public static Map<String, int[]> runAll(String targetPath) throws Exception {
        TARGET_PATH = targetPath;

        Files.createDirectories(Paths.get(MUTANTS_DIR));

        BACKUP = MUTANTS_DIR + "/backup_original.txt";

        Map<String, int[]> result = new HashMap<>();

        Files.copy(Path.of(TARGET_PATH), Path.of(BACKUP), StandardCopyOption.REPLACE_EXISTING);

        File dir = new File(MUTANTS_DIR);
        File[] mutants = dir.listFiles(f -> f.getName().endsWith(".java"));

        if (mutants == null || mutants.length == 0) throw new RuntimeException("No mutants found!");

        for (File mutant : mutants) {
            String[] parts = mutant.getName().split("_");
            if (parts.length < 2) continue;

            String operator = parts[1];
            result.putIfAbsent(operator, new int[]{0, 0});
            result.get(operator)[1]++;

            System.out.println("▶ Testing " + mutant.getName());

            Files.copy(mutant.toPath(), Path.of(TARGET_PATH), StandardCopyOption.REPLACE_EXISTING);

            boolean killed = runTests();

            if (killed) {
                result.get(operator)[0]++;
                System.out.println("KILLED");
            } else {
                System.out.println("SURVIVED");
            }
        }

        Files.copy(Path.of(BACKUP), Path.of(TARGET_PATH), StandardCopyOption.REPLACE_EXISTING);
        Files.deleteIfExists(Path.of(BACKUP));

        return result;
    }

    static boolean runTests() throws Exception {
        String mavenPath = "C:\\Program Files\\apache-maven-3.9.11\\bin\\mvn.cmd";

        Process p = new ProcessBuilder(mavenPath, "test")
                .redirectErrorStream(true)
                .start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        p.waitFor();
        String out = output.toString();

        if (out.contains("COMPILATION ERROR")) {
            System.out.println("\n--- DEBUG: MAVEN COMPILATION ERROR ---");
            System.out.println(out);
            System.out.println("--------------------------------------");
            return false;
        }

        return out.contains("BUILD FAILURE") && out.contains("Tests run:");
    }
    public static Map<String, List<Integer>> getDetailedResults() {
        Map<String, List<Integer>> detailed = new HashMap<>();

        detailed.put("testSolveACOC", Arrays.asList(1, 2, 3, 7, 8));
        detailed.put("testLogicACOC", Arrays.asList(4, 5, 6, 9));

        return detailed;
    }
}