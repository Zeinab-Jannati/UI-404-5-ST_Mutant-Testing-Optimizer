package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== PROFESSIONAL GENERIC MUTATION TESTING TOOL ===");

        System.out.print("\nWhich file to test? : ");
        System.out.println("\n1.p1\n2.p2\n3.p3");
        String userInputPath = scanner.nextLine().trim();

        if (userInputPath == "p1")
        {
            File sourceFile = new File("src/main/java/org/example/Calculator.java");
            if (!sourceFile.exists()) {
                System.out.println("ERROR: File not found!");
                return;
            }
        }

        File sourceFile = new File("src/main/java/org/example/Calculator.java");
        if (!sourceFile.exists()) {
            System.out.println("ERROR: File not found!");
            return;
        }

        String fileName = sourceFile.getName();
        String className = fileName.replace(".java", "");
        String targetPath = "src/main/java/org/example/" + fileName;

        try {
            Path destPath = Paths.get(targetPath);
            Files.createDirectories(destPath.getParent());
            Files.copy(sourceFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            createGenericAutoTest(className);
            System.out.println("Successfully loaded " + fileName + " and generated Smart Test Suite.");

        } catch (IOException e) {
            System.out.println("ERROR during file setup: " + e.getMessage());
            return;
        }

        System.out.println("\nPHASE 1: MUTANT GENERATION");
        System.out.println("Traditional: 1.AOD, 2.AOR, 3.AOI, 4.COR, 5.COI, 6.COD, 7.LOD, 8.LOI, 9.LOR, 10.ROR, 11.SDL, 12.SOR");
        System.out.println("Traditional: 13.IMCD, 14.IPEX, 15.IPVR, 16.IREM, 17.IUOI");
        System.out.print("\nYour choice (e.g., 18 for ALL): ");

        String input = scanner.nextLine().trim();
        String[] choices = input.split("[,\\s]+");

        try {
            System.out.println("\n GENERATING MUTANTS FOR " + fileName + "...");

            for (String choice: choices)
            {
                switch (choice) {
                    case "1" -> AODMutation.applyAOD(targetPath);
                    case "2" -> AORMutation.applyAOR(targetPath);
                    case "3" -> AOIMutation.applyAOI(targetPath);
                    case "4" -> CORMutation.applyCOR(targetPath);
                    case "5" -> COIMutation.applyCOI(targetPath);
                    case "6" -> CODMutation.applyCOD(targetPath);
                    case "7" -> LODMutation.applyLOD(targetPath);
                    case "8" -> LOIMutation.applyLOI(targetPath);
                    case "9" -> LORMutation.applyLOR(targetPath);
                    case "10" -> RORMutation.applyROR(targetPath);
                    case "11" -> SDLMutation.applySDL(targetPath);
                    case "12" -> SORMutation.applySOR(targetPath);
                    case "13" -> IMCD_Mutation.applyIMCD(targetPath);
                    case "14" -> IPEX_Mutation.applyIPEX(targetPath);
                    case "15" -> IPVR_Mutation.applyIPVR(targetPath);
                    case "16" -> IREM_Mutation.applyIREM(targetPath);
                    case "17" -> IUOI_Mutation.applyIUOI(targetPath);
                    case "18" -> {
                        AODMutation.applyAOD(targetPath);
                        AORMutation.applyAOR(targetPath);
                        AOIMutation.applyAOI(targetPath);
                        CORMutation.applyCOR(targetPath);
                        COIMutation.applyCOI(targetPath);
                        CODMutation.applyCOD(targetPath);
                        LODMutation.applyLOD(targetPath);
                        LOIMutation.applyLOI(targetPath);
                        LORMutation.applyLOR(targetPath);
                        RORMutation.applyROR(targetPath);
                        SDLMutation.applySDL(targetPath);
                        SORMutation.applySOR(targetPath);
                        IMCD_Mutation.applyIMCD(targetPath);
                        IPEX_Mutation.applyIPEX(targetPath);
                        IPVR_Mutation.applyIPVR(targetPath);
                        IREM_Mutation.applyIREM(targetPath);
                        IUOI_Mutation.applyIUOI(targetPath);
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }


            System.out.println("\n PHASE 2: EXECUTION (SMART TEST ENGINE)");
            Map<String, int[]> realResults = MutationRunner.runAll(targetPath);

            TestExecutor.showTraditionalReport(realResults);
            TestExecutor.showIntegrationReport(realResults);

            String[] tradOps = {"AOD", "AOR", "AOI", "COR", "COI", "COD", "LOD", "LOI", "LOR", "ROR", "SDL", "SOR"};
            String[] integOps = {"IPVR", "IUOI", "IPEX", "IMCD", "IREM"};

            double traditionalScore = calculateRealScore(realResults, tradOps);
            double integrationScore = calculateRealScore(realResults, integOps);

            // تشخیص اینکه آیا از هر دو بخش میوتنت داریم یا خیر
            boolean hasTrad = hasAnyMutants(realResults, tradOps);
            boolean hasInteg = hasAnyMutants(realResults, integOps);

            double finalScore;
            if (hasTrad && hasInteg) {
                // فرمول ترکیبی با وزن 40 و 60
                finalScore = (traditionalScore * 0.4) + (integrationScore * 0.6);
                System.out.println("\n--- FINAL COMBINED SCORE (40% Traditional / 60% Integration) ---");
            } else if (hasInteg) {
                // فقط اینتگریشن انتخاب شده
                finalScore = integrationScore;
                System.out.println("\n--- FINAL INTEGRATION SCORE (100%) ---");
            } else {
                // فقط سنتی انتخاب شده
                finalScore = traditionalScore;
                System.out.println("\n--- FINAL TRADITIONAL SCORE (100%) ---");
            }

            System.out.println("\nFINAL SCORE FOR " + fileName + ": " + String.format("%.2f", finalScore) + "%");

            System.out.println("\n PHASE 3: AI TEST SUITE MINIMIZATION");
            SmartTestGenerator.optimizeTestSuite(MutationRunner.getDetailedResults());

        } catch (Exception e) {
            System.out.println("\nAN ERROR OCCURRED: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    private static boolean hasAnyMutants(Map<String, int[]> results, String[] ops) {
        for (String op : ops) {
            if (results.containsKey(op) && results.get(op)[1] > 0) {
                return true;
            }
        }
        return false;
    }

    private static void createGenericAutoTest(String className) throws IOException {
        Path sourcePath = Paths.get("src/main/java/org/example/" + className + ".java");
        String content = Files.readString(sourcePath);

        Path testPath = Paths.get("src/test/java/org/example/" + className + "AutoTest.java");
        Files.createDirectories(testPath.getParent());

        StringBuilder sb = new StringBuilder();
        sb.append("package org.example;\n\nimport org.junit.Test;\nimport static org.junit.Assert.*;\n\n");
        sb.append("public class " + className + "AutoTest {\n\n");
        sb.append("    " + className + " target = new " + className + "();\n\n");

        // Regex برای استخراج متد، نوع بازگشتی و پارامترها
        Pattern pattern = Pattern.compile("public\\s+(int|boolean)\\s+(\\w+)\\s*\\(([^)]*)\\)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String returnType = matcher.group(1);
            String methodName = matcher.group(2);
            String params = matcher.group(3).trim();
            int paramCount = params.isEmpty() ? 0 : params.split(",").length;

            sb.append("    @Test\n    public void test_" + methodName + "_Dynamic() {\n");

            if (returnType.equals("boolean")) {
                sb.append("        boolean[] v = {true, false};\n");
                if (paramCount == 1) {
                    sb.append("        for(boolean a : v) { target." + methodName + "(a); }\n");
                } else if (paramCount == 2) {
                    sb.append("        for(boolean a : v) { for(boolean b : v) { target." + methodName + "(a, b); } }\n");
                }
            } else if (returnType.equals("int")) {
                sb.append("        int[] bv = {0, 1, -1, 100};\n");
                if (paramCount == 1) {
                    sb.append("        for(int x : bv) { try { target." + methodName + "(x); } catch(Exception e){} }\n");
                } else if (paramCount == 2) {
                    sb.append("        for(int x : bv) { for(int y : bv) { try { target." + methodName + "(x, y); } catch(Exception e){} } }\n");
                }
            }
            sb.append("    }\n\n");
        }
        sb.append("}\n");
        Files.write(testPath, sb.toString().getBytes());
    }
    private static double calculateRealScore(Map<String, int[]> results, String[] ops) {
        int killed = 0;
        int totalExecuted = 0;
        for (String op : ops) {
            int[] data = results.getOrDefault(op, new int[]{0, 0});
            killed += data[0];
            totalExecuted += data[1];
        }
        return totalExecuted == 0 ? 0.0 : (killed * 100.0 / totalExecuted);
    }

    public boolean complexCondition(int a, int b) {
        if (a > 0 && (b < 5 || b == 100)) {
            return true;
        }
        return false;
    }
    public boolean simpleCondition(int a) {
        if (a > 0) {
            return true;
        }
        return false;
    }
}