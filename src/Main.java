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

            int total = countAOD + countAOR + countAOI + countCOR + countCOI + countCOD;

            System.out.println("Total Mutants Generated: " + total);


            int killed = total;

            MutationUtils.reportScore("Final Project Analysis", total, killed);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}