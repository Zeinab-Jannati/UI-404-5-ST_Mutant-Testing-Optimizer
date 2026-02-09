package org.example;

import java.util.*;

public class TestExecutor {

    // حالا این متد نمرات واقعی را از خروجی مرحله تست می‌گیرد
    public static void showTraditionalReport(Map<String, int[]> realResults) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("           TRADITIONAL MUTATION TEST REPORT (REAL DATA)");
        System.out.println("=".repeat(70));

        System.out.println(" Operator    Mutants Generated   Killed/Total   Mutation Score   Status");
        System.out.println(" --------    ----------------   ------------   --------------   -------");

        String[] ops = {"AOD", "AOR", "AOI", "COR", "COI", "COD", "LOD", "LOI", "LOR", "ROR", "SDL", "SOR"};
        int totalMutants = 0;
        int totalKilled = 0;

        for (String op : ops) {
            int[] data = realResults.getOrDefault(op, new int[]{0, 0});
            int killed = data[0];
            int total = data[1];

            totalMutants += total;
            totalKilled += killed;

            double score = total == 0 ? 0.0 : (killed * 100.0 / total);
            String status = score >= 80 ? "EXCELLENT" : score >= 50 ? "GOOD" : "NEEDS WORK";

            System.out.println(String.format(" %-10s %15d %10d/%-6d %13.2f%%   %s",
                    op, total, killed, total, score, status));
        }

        double finalScore = totalMutants == 0 ? 0 : (totalKilled * 100.0 / totalMutants);
        System.out.println("=".repeat(70));
        System.out.println(String.format(" TOTAL: %d/%d mutants killed (%.2f%%)",
                totalKilled, totalMutants, finalScore));
    }

    // متد مشابه برای Integration
    public static void showIntegrationReport(Map<String, int[]> realResults) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("           INTEGRATION MUTATION TEST REPORT (REAL DATA)");
        System.out.println("=".repeat(70));

        String[] ops = {"IPVR", "IUOI", "IPEX", "IMCD", "IREM"};
        int totalKilled = 0;
        int totalMutants = 0;

        for (String op : ops) {
            int[] data = realResults.getOrDefault(op, new int[]{0, 0});
            totalKilled += data[0];
            totalMutants += data[1];
            double score = data[1] == 0 ? 0 : (data[0] * 100.0 / data[1]);

            System.out.println(String.format(" %-10s %15d %10d/%-6d %13.2f%%",
                    op, data[1], data[0], data[1], score));
        }
        System.out.println("=".repeat(70));
    }
}