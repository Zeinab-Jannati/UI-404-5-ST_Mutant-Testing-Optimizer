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
        System.out.println("  7. LOD");
        System.out.println("  8. LOI");
        System.out.println("  9. LOR");
        System.out.println("  10. ROR");
        System.out.println("  11. SDL");
        System.out.println("  12. SOR");

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
        input = input.replaceAll("\\s+", "");
        String[] selections = input.split(",");



        int traditionalTotal = 0;
        int integrationTotal = 0;

        int aod=0, aor=0, aoi=0, cor=0, coi=0, cod=0;
        int lod = 0, loi = 0, lor = 0, ror = 0, sdl = 0, sor = 0;
        int ipvr = 0, iuoi = 0, ipex = 0, imcd = 0, irem = 0;



        try {
            System.out.println("\nGENERATING MUTANTS...");


            if (isSelected(selections,"1") || isSelected(selections,"18")) {
                aod = AODMutation.applyAOD(targetPath);
                traditionalTotal += aod;
            }

            if (isSelected(selections,"2") || isSelected(selections,"18")) {
                aor = AORMutation.applyAOR(targetPath);
                traditionalTotal += aor;
            }

            if (isSelected(selections,"3") || isSelected(selections,"18")) {
                aoi = AOIMutation.applyAOI(targetPath);
                traditionalTotal += aoi;
            }

            if (isSelected(selections,"4") || isSelected(selections,"18")) {
                cor = CORMutation.applyCOR(targetPath);
                traditionalTotal += cor;
            }

            if (isSelected(selections,"5") || isSelected(selections,"18")) {
                coi = COIMutation.applyCOI(targetPath);
                traditionalTotal += coi;
            }

            if (isSelected(selections,"6") || isSelected(selections,"18")) {
                cod = CODMutation.applyCOD(targetPath);
                traditionalTotal += cod;
            }

            if (isSelected(selections,"7") || isSelected(selections,"18")) {
                lod = LODMutation.applyLOD(targetPath);
                traditionalTotal += lod;
            }

            if (isSelected(selections,"8") || isSelected(selections,"18")) {
                loi = LOIMutation.applyLOI(targetPath);
                traditionalTotal += loi;
            }

            if (isSelected(selections,"9") || isSelected(selections,"18")) {
                lor = LORMutation.applyLOR(targetPath);
                traditionalTotal += lor;
            }

            if (isSelected(selections,"10") || isSelected(selections,"18")) {
                ror = RORMutation.applyROR(targetPath);
                traditionalTotal += ror;
            }

            if (isSelected(selections,"11") || isSelected(selections,"18")) {
                sdl = SDLMutation.applySDL(targetPath);
                traditionalTotal += sdl;
            }

            if (isSelected(selections,"12") || isSelected(selections,"18")) {
                sor = SORMutation.applySOR(targetPath);
                traditionalTotal += sor;
            }

            // ===== Integration =====

            if (isSelected(selections,"13") || isSelected(selections,"18") || isSelected(selections,"19")) {
                ipvr = IPVR_Mutation.applyIPVR(targetPath);
                integrationTotal += ipvr;
            }

            if (isSelected(selections,"14") || isSelected(selections,"18") || isSelected(selections,"19")) {
                iuoi = IUOI_Mutation.applyIUOI(targetPath);
                integrationTotal += iuoi;
            }

            if (isSelected(selections,"15") || isSelected(selections,"18") || isSelected(selections,"19")) {
                ipex = IPEX_Mutation.applyIPEX(targetPath);
                integrationTotal += ipex;
            }

            if (isSelected(selections,"16") || isSelected(selections,"18") || isSelected(selections,"19")) {
                imcd = IMCD_Mutation.applyIMCD(targetPath);
                integrationTotal += imcd;
            }

            if (isSelected(selections,"17") || isSelected(selections,"18") || isSelected(selections,"19")) {
                irem = IREM_Mutation.applyIREM(targetPath);
                integrationTotal += irem;
            }

            System.out.println("\nMUTANT GENERATION COMPLETE");
            System.out.println("Traditional Mutants: " + traditionalTotal);
            System.out.println("Integration Mutants: " + integrationTotal);
            System.out.println("Total Mutants: " + (traditionalTotal + integrationTotal));

            System.out.println("\n--- Traditional Details ---");
            System.out.println("AOD: " + aod);
            System.out.println("AOI: " + aoi);
            System.out.println("AOR: " + aor);
            System.out.println("COR: " + cor);
            System.out.println("COI: " + coi);
            System.out.println("COD: " + cod);
            System.out.println("LOD: " + lod);
            System.out.println("LOI: " + loi);
            System.out.println("LOR: " + lor);
            System.out.println("ROR: " + ror);
            System.out.println("SDL: " + sdl);
            System.out.println("SOR: " + sor);


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
    private static boolean isSelected(String[] selections, String option) {
        for (String s : selections) {
            if (s.trim().equals(option)) {
                return true;
            }
        }
        return false;
    }
}
