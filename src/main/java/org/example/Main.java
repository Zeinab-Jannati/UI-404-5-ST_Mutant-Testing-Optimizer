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

        // 1. دریافت مسیر فایل
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
            System.out.println("✅ Successfully loaded " + fileName + " and generated Smart Test Suite.");

        } catch (IOException e) {
            System.out.println("ERROR during file setup: " + e.getMessage());
            return;
        }

        // 2. انتخاب اپراتورها
        System.out.println("\nPHASE 1: MUTANT GENERATION");
        System.out.println("Traditional: 1.AOD, 2.AOR, 3.AOI, 4.COR, 5.COI, 6.COD, 7.ROR");
        System.out.print("\nYour choice (e.g., 18 for ALL): ");

        String input = scanner.nextLine().trim();

        try {
            System.out.println("\n🛠 GENERATING MUTANTS FOR " + fileName + "...");

            // Traditional Mutations
            if (input.contains("1") || input.contains("18")) AODMutation.applyAOD(targetPath);
            if (input.contains("2") || input.contains("18")) AORMutation.applyAOR(targetPath);
            if (input.contains("3") || input.contains("18")) AOIMutation.applyAOI(targetPath);
            if (input.contains("4") || input.contains("18")) CORMutation.applyCOR(targetPath);
            if (input.contains("5") || input.contains("18")) COIMutation.applyCOI(targetPath);
            if (input.contains("6") || input.contains("18")) CODMutation.applyCOD(targetPath);
            // Integration Mutations
            if (input.contains("13") || input.contains("18")) IPVR_Mutation.applyIPVR(targetPath);
            if (input.contains("14") || input.contains("18")) IUOI_Mutation.applyIUOI(targetPath);
            if (input.contains("15") || input.contains("18")) IPEX_Mutation.applyIPEX(targetPath);
            if (input.contains("16") || input.contains("18")) IMCD_Mutation.applyIMCD(targetPath);
            if (input.contains("17") || input.contains("18")) IREM_Mutation.applyIREM(targetPath);

            System.out.println("\nMUTANT GENERATION COMPLETE");

            // 3. اجرای Mutant ها
            System.out.println("\n🚀 PHASE 2: EXECUTION (SMART TEST ENGINE)");
            Map<String, int[]> realResults = MutationRunner.runAll(targetPath);

            TestExecutor.showTraditionalReport(realResults);
            TestExecutor.showIntegrationReport(realResults);

            double traditionalScore = calculateRealScore(realResults, new String[]{
                    "AOD", "AOR", "AOI", "COR", "COI", "COD", "ROR"
            });
            double integrationScore = calculateRealScore(realResults, new String[]{
                    "IPVR", "IUOI", "IPEX", "IMCD", "IREM"
            });

            double finalScore = (traditionalScore * 0.4) + (integrationScore * 0.6);
            System.out.println("\nFINAL SCORE FOR " + fileName + ": " + String.format("%.2f", finalScore) + "%");

        } catch (Exception e) {
            System.out.println("\nAN ERROR OCCURRED: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // ایجاد تست داینامیک عمومی برای Mutant ها
    private static void createGenericAutoTest(String className) throws IOException {
        // --- اضافه شده: ایجاد مسیر پوشه‌ها قبل از ساخت فایل ---
        Path testPath = Paths.get("src/test/java/org/example/GenericMutationTest.java");
        Files.createDirectories(testPath.getParent());
        // --------------------------------------------------

        String testContent = "package org.example;\n" +
                "import org.junit.Test;\n" +
                "import static org.junit.Assert.*;\n" +
                "import java.lang.reflect.Method;\n" +
                "public class GenericMutationTest {\n" +
                "    @Test\n" +
                "    public void runDynamicTests() throws Exception {\n" +
                "        Class<?> clazz = Class.forName(\"org.example." + className + "\");\n" +
                "        Object instance = clazz.getDeclaredConstructor().newInstance();\n" +
                "        for (Method m : clazz.getDeclaredMethods()) {\n" +
                "            if (java.lang.reflect.Modifier.isPublic(m.getModifiers())) {\n" +
                "                try {\n" +
                "                    Object[] testInputs = {5, 0, -5, 100};\n" +
                "                    for(Object input : testInputs) {\n" +
                "                        Object[] args = new Object[m.getParameterCount()];\n" +
                "                        for(int i = 0; i < args.length; i++) args[i] = input;\n" +
                "                        m.invoke(instance, args);\n" +
                "                    }\n" +
                "                } catch (Exception e) {\n" +
                "                    throw new RuntimeException(\"Killed\");\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        // اصلاح شده: استفاده از متغیر testPath که در بالا تعریف کردیم
        Files.write(testPath, testContent.getBytes());
    }

    // محاسبه درصد واقعی Mutation
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
}
