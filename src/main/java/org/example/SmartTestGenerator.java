package org.example;

import java.util.*;

public class SmartTestGenerator {

    public static void optimizeTestSuite(Map<String, List<Integer>> mutantKillMap) {
        System.out.println("\n--- شروع فاز بهینه‌سازی هوشمند (AI Optimization) ---");

        Set<Integer> allUniqueMutants = new HashSet<>();
        for (List<Integer> killedList : mutantKillMap.values()) {
            allUniqueMutants.addAll(killedList);
        }

        Set<Integer> coveredByAI = new HashSet<>();
        List<String> optimizedTests = new ArrayList<>();

        while (coveredByAI.size() < allUniqueMutants.size() * 0.9 && !mutantKillMap.isEmpty()) {
            String bestTest = null;
            int maxNewKills = -1;

            for (Map.Entry<String, List<Integer>> entry : mutantKillMap.entrySet()) {
                int newKillsCount = 0;
                for (int mutantId : entry.getValue()) {
                    if (!coveredByAI.contains(mutantId)) newKillsCount++;
                }
                if (newKillsCount > maxNewKills) {
                    maxNewKills = newKillsCount;
                    bestTest = entry.getKey();
                }
            }

            if (bestTest != null && maxNewKills > 0) {
                optimizedTests.add(bestTest);
                coveredByAI.addAll(mutantKillMap.get(bestTest));
            } else break;
        }

        System.out.println("تعداد تست‌های انتخاب شده توسط AI: " + optimizedTests.size());
        System.out.println("تست‌های برگزیده: " + optimizedTests);
        double efficiency = allUniqueMutants.isEmpty() ? 0 : (coveredByAI.size() * 100.0) / allUniqueMutants.size();
        System.out.printf("Mutation Score نهایی با متد AI: %.2f%%\n", efficiency);
    }
}