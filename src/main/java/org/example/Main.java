package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== PROFESSIONAL GENERIC MUTATION TESTING TOOL ===");

        System.out.print("\nEnter the full path of the Java file to test: ");
        String userInputPath = scanner.nextLine().trim();
        File sourceFile = new File(userInputPath);
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

        try {
            System.out.println("\n GENERATING MUTANTS FOR " + fileName + "...");

            switch (input) {
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

            System.out.println("\n PHASE 2: EXECUTION (SMART TEST ENGINE)");
            Map<String, int[]> realResults = MutationRunner.runAll(targetPath);

            TestExecutor.showTraditionalReport(realResults);
            TestExecutor.showIntegrationReport(realResults);

            double traditionalScore = calculateRealScore(realResults, new String[]{
                    "AOD", "AOR", "AOI", "COR", "COI", "COD", "LOD", "LOI", "LOR", "ROR", "SDL", "SOR"
            });
            double integrationScore = calculateRealScore(realResults, new String[]{
                    "IPVR", "IUOI", "IPEX", "IMCD", "IREM"
            });

            double finalScore = (traditionalScore * 0.4) + (integrationScore * 0.6);
            System.out.println("\nFINAL SCORE FOR " + fileName + ": " + String.format("%.2f", finalScore) + "%");

            System.out.println("\n PHASE 3: AI TEST SUITE MINIMIZATION");
            SmartTestGenerator.optimizeTestSuite(MutationRunner.getDetailedResults());

        } catch (Exception e) {
            System.out.println("\nAN ERROR OCCURRED: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void createGenericAutoTest(String className) throws IOException {
        Path testPath = Paths.get("src/test/java/org/example/GenericMutationTest.java");
        Files.createDirectories(testPath.getParent());

        StringBuilder sb = new StringBuilder();
        sb.append("package org.example;\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import static org.junit.Assert.*;\n");
        sb.append("public class GenericMutationTest {\n");

        sb.append("    @Test\n");
        sb.append("    public void testSolveACOC() {\n");
        sb.append("        Calculator calc = new Calculator();\n");
        sb.append("        assertEquals(15, calc.solve(10, 5));\n");
        sb.append("        assertEquals(-5, calc.solve(5, 10));\n");
        sb.append("    }\n");

        sb.append("    @Test\n");
        sb.append("    public void testLogicACOC() {\n");
        sb.append("        Calculator calc = new Calculator();\n");
        sb.append("        assertTrue(calc.checkLogic(true, true));\n");
        sb.append("        assertFalse(calc.checkLogic(true, false));\n");
        sb.append("        assertTrue(calc.checkLogic(false, true));\n");
        sb.append("        assertTrue(calc.checkLogic(false, false));\n");
        sb.append("    }\n");

        sb.append("}");
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