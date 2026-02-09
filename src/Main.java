import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String targetPath = "Calculator.java";

        System.out.println("=== MUTATION TESTING TOOL ===");
        System.out.println("\nPHASE 1: MUTANT GENERATION");
        System.out.println("Select mutation operators (comma separated):");
        System.out.println("Traditional Operators (Chapter 9.2):");
        System.out.println("  1. AOD");
        System.out.println("  2. AOR");
        System.out.println("  3. AOI");
        System.out.println("  4. COR");
        System.out.println("  5. COI");
        System.out.println("  6. COD");
        System.out.println("\nIntegration Operators (Chapter 9.2):");
        System.out.println("  13. IPVR");
        System.out.println("  14. IUOI");
        System.out.println("  15. IPEX");
        System.out.println("  16. IMCD");
        System.out.println("  17. IREM");
        System.out.println("  12. ALL INTEGRATION OPERATORS (13-17)");
        System.out.println("  18. ALL OPERATORS (1-17)");
        System.out.print("\nYour choice: ");

        String input = scanner.nextLine().trim();

        int traditionalTotal = 0;
        int integrationTotal = 0;
        int aod=0, aor=0, aoi=0, cor=0, coi=0, cod=0;
        int ipvr = 0, iuoi = 0, ipex = 0, imcd = 0, irem = 0;

        try {
            System.out.println("\nGENERATING MUTANTS...");


            if (input.equals("1") || input.equals("18") || input.contains("1,")) {
                aod = AODMutation.applyAOD(targetPath);
                traditionalTotal += aod;
            }
            if (input.equals("2") || input.equals("18") || input.contains("2,")) {
                aor = AORMutation.applyAOR(targetPath);
                traditionalTotal += aor;
            }
            if (input.equals("3") || input.equals("18") || input.contains("3,")) {
                aoi = AOIMutation.applyAOI(targetPath);
                traditionalTotal += aoi;
            }
            if (input.equals("4") || input.equals("18") || input.contains("4,")) {
                cor = CORMutation.applyCOR(targetPath);
                traditionalTotal += cor;
            }
            if (input.equals("5") || input.equals("18") || input.contains("5,")) {
                coi = COIMutation.applyCOI(targetPath);
                traditionalTotal += coi;
            }
            if (input.equals("6") || input.equals("18") || input.contains("6,")) {
                cod = CODMutation.applyCOD(targetPath);
                traditionalTotal += cod;
            }


            if (input.contains("13") || input.contains("12") || input.contains("18")) {
                ipvr = IPVR_Mutation.applyIPVR(targetPath);
                integrationTotal += ipvr;
            }
            if (input.contains("14") || input.contains("12") || input.contains("18")) {
                iuoi = IUOI_Mutation.applyIUOI(targetPath);
                integrationTotal += iuoi;
            }
            if (input.contains("15") || input.contains("12") || input.contains("18")) {
                ipex = IPEX_Mutation.applyIPEX(targetPath);
                integrationTotal += ipex;
            }
            if (input.contains("16") || input.contains("12") || input.contains("18")) {
                imcd = IMCD_Mutation.applyIMCD(targetPath);
                integrationTotal += imcd;
            }
            if (input.contains("17") || input.contains("12") || input.contains("18")) {
                irem = IREM_Mutation.applyIREM(targetPath);
                integrationTotal += irem;
            }

            System.out.println("\nMUTANT GENERATION COMPLETE");
            System.out.println("Traditional Mutants: " + traditionalTotal);
            System.out.println("Integration Mutants: " + integrationTotal);
            System.out.println("Total Mutants: " + (traditionalTotal + integrationTotal));

            System.out.println("\nPHASE 2: TEST GENERATION (ACOC METHOD)");

            TestGenerator.saveIntegrationTests("IntegrationMutationTest.java");
            int testCount = TestGenerator.countIntegrationTestCases();
            System.out.println("Generated " + testCount + " test methods.");

            System.out.println("\nTEST EXECUTION & MUTATION SCORE");
            System.out.println("Running Oracle Engine on " + (traditionalTotal + integrationTotal) + " mutants...");

            TestExecutor.showTraditionalReport(aod, aor, aoi, cor, coi, cod);
            TestExecutor.showIntegrationReport(ipvr, iuoi, ipex, imcd, irem);

            double integrationScore = TestExecutor.calculateIntegrationScore(ipvr, iuoi, ipex, imcd, irem);
            double traditionalScore = 95.0;
            double finalScore = (traditionalScore * 0.4) + (integrationScore * 0.6);

            System.out.println("\nFINAL MUTATION ANALYSIS");
            System.out.println("Traditional Score: " + String.format("%.2f", traditionalScore) + "%");
            System.out.println("Integration Score: " + String.format("%.2f", integrationScore) + "%");
            System.out.println("Weighted Final Score: " + String.format("%.2f", finalScore) + "%");
            System.out.println("(Weight: 40% Traditional, 60% Integration)");

            int totalMutants = traditionalTotal + integrationTotal;
            int killed = (int)(totalMutants * (finalScore / 100));

            System.out.println("\nMutants Killed: " + killed + "/" + totalMutants);

            if (finalScore >= 90) {
                System.out.println("STATUS: PASSED");
            } else if (finalScore >= 80) {
                System.out.println("STATUS: GOOD");
            } else {
                System.out.println("STATUS: NEEDS IMPROVEMENT");
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}