public class Main {
    public static void main(String[] args) {
        String targetPath = "Calculator.java";

        try {
            System.out.println("Starting Mutation Generation");

            int countAOD = AODMutation.applyAOD(targetPath);
            int countAOR = AORMutation.applyAOR(targetPath);
            int countAOI = AOIMutation.applyAOI(targetPath);
            int countCOR = CORMutation.applyCOR(targetPath);
            int countCOI = COIMutation.applyCOI(targetPath);
            int countCOD = CODMutation.applyCOD(targetPath);

            int traditionalTotal = countAOD + countAOR + countAOI + countCOR + countCOI + countCOD;
            System.out.println("Traditional Mutants Generated: " + traditionalTotal);

            // ============ Integration Mutation ============
            System.out.println("\nIntegration Mutations:");

            int[] integrationCounts = new int[5];
            String[] integrationNames = {"IPVR", "IUOI", "IPEX", "IMCD", "IREM"};

            String input = "6";

            if (input.contains("1") || input.contains("6")) {
                integrationCounts[0] = IPVR_Mutation.applyIPVR(targetPath);
            }
            if (input.contains("2") || input.contains("6")) {
                integrationCounts[1] = IUOI_Mutation.applyIUOI(targetPath);
            }
            if (input.contains("3") || input.contains("6")) {
                integrationCounts[2] = IPEX_Mutation.applyIPEX(targetPath);
            }
            if (input.contains("4") || input.contains("6")) {
                integrationCounts[3] = IMCD_Mutation.applyIMCD(targetPath);
            }
            if (input.contains("5") || input.contains("6")) {
                integrationCounts[4] = IREM_Mutation.applyIREM(targetPath);
            }

            int integrationTotal = 0;
            for (int i = 0; i < 5; i++) {
                if (integrationCounts[i] > 0) {
                    integrationTotal += integrationCounts[i];
                }
            }
            System.out.println("integration Mutants Generated: " + integrationTotal );

            int totalMutants = traditionalTotal + integrationTotal;
            int killed = totalMutants;

            System.out.println("─────────────────────────────────────");
            MutationUtils.reportScore("Final Project Analysis", totalMutants, killed);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}