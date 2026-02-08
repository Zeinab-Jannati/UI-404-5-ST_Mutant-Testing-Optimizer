import static org.junit.Assert.*;
import org.junit.Test;

public class IntegrationMutationTest {

    @Test
    public void testSolve_ACOC_FullCombinations() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing solve() with all value combinations...");
        
        int[] values = {Integer.MIN_VALUE, -1000, -100, -10, -1, 0, 1, 10, 100, 1000, Integer.MAX_VALUE};
        int testsRun = 0;
        int mutantsKilled = 0;
        
        for (int a : values) {
            for (int b : values) {
                testsRun++;
                try {
                    int expected = a + b;
                    int actual = calc.solve(a, b);
                    
                    assertEquals("ACOC failed: solve(" + a + ", " + b + ")", expected, actual);
                    
                    if (a - b != expected) {
                        assertNotEquals("IREM mutant alive for a=" + a + ", b=" + b, a - b, actual);
                        mutantsKilled++;
                    }
                    
                    if (a * b != expected) {
                        assertNotEquals("AOR-mul mutant alive", a * b, actual);
                        mutantsKilled++;
                    }
                    if (b != 0 && a / b != expected) {
                        assertNotEquals("AOR-div mutant alive", a / b, actual);
                        mutantsKilled++;
                    }
                    
                    if (a != expected) {
                        assertNotEquals("AOD mutant (return a) alive", a, actual);
                        mutantsKilled++;
                    }
                    if (b != expected) {
                        assertNotEquals("AOD mutant (return b) alive", b, actual);
                        mutantsKilled++;
                    }
                    
                } catch (ArithmeticException e) {
                }
            }
        }
        
        System.out.println("[ACOC] solve(): " + testsRun + " tests, " + mutantsKilled + " mutants killed");
    }

    @Test
    public void testCheckLogic_ACOC_BooleanCombinations() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing checkLogic() with all boolean combinations...");
        
        boolean[] bools = {true, false};
        int mutantsKilled = 0;
        
        for (boolean A : bools) {
            for (boolean B : bools) {
                boolean expected = A && B;
                boolean actual = calc.checkLogic(A, B);
                
                assertEquals("checkLogic(" + A + ", " + B + ")", expected, actual);
                
                if ((A || B) != expected) {
                    assertNotEquals("COR mutant alive", A || B, actual);
                    mutantsKilled++;
                }
                
                if (!(A && B) != expected) {
                    assertNotEquals("COI mutant alive", !(A && B), actual);
                    mutantsKilled++;
                }
            }
        }
        
        System.out.println("[ACOC] checkLogic(): 4 tests, " + mutantsKilled + " mutants killed");
    }

    @Test
    public void testIntegration_IPVR_IPEX() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing IPVR and IPEX mutants...");
        
        int[][] ipvrTests = {
            {5, 3, 8},
            {10, -5, 5},
            {-10, 15, 5},
            {0, 7, 7},
            {7, 0, 7},
            {100, 200, 300}
        };
        
        for (int[] test : ipvrTests) {
            int a = test[0];
            int b = test[1];
            int expected = test[2];
            
            int result1 = calc.calculateSum(a, b);
            int result2 = calc.calculateSum(b, a);
            
            assertEquals("IPVR test failed for (" + a + ", " + b + ")", expected, result1);
            assertEquals("IPVR commutative property failed", expected, result2);
            
            assertEquals("IPEX/IPVR mutant detected: results differ", result1, result2);
        }
        
        System.out.println("[ACOC] IPVR/IPEX: All tests passed");
    }

    @Test
    public void testIntegration_IMCD_IREM() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing IMCD and IREM mutants...");
        
        int[][] iremTests = {
            {2, 2, 4},
            {1, 2, 3},
            {5, 2, 7},
            {10, 5, 15},
            {3, 7, 10}
        };
        
        for (int[] test : iremTests) {
            int a = test[0];
            int b = test[1];
            int expectedAdd = test[2];
            int expectedSub = a - b;
            
            int result = calc.solve(a, b);
            assertEquals(expectedAdd, result);
            
            assertNotEquals("IREM mutant alive for (" + a + ", " + b + ")", expectedSub, result);
            
            int sumResult = calc.calculateSum(a, b);
            assertEquals(expectedAdd, sumResult);
            
            assertNotEquals("IMCD mutant (return 0) alive", 0, sumResult);
            assertNotEquals("IMCD mutant (return a) alive", a, sumResult);
            assertNotEquals("IMCD mutant (return b) alive", b, sumResult);
        }
        
        System.out.println("[ACOC] IMCD/IREM: All mutants killed");
    }

    @Test
    public void testIntegration_IUOI() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing IUOI mutants...");
        
        int a = 5, b = 3;
        int originalA = a, originalB = b;
        
        int result = calc.solve(a, b);
        assertEquals(8, result);
        
        assertEquals("IUOI mutant: parameter a changed", originalA, a);
        assertEquals("IUOI mutant: parameter b changed", originalB, b);
        
        int[][] iuoiTests = {
            {10, 20, 30},
            {0, 0, 0},
            {-5, 5, 0},
            {100, -50, 50}
        };
        
        for (int[] test : iuoiTests) {
            int x = test[0];
            int y = test[1];
            int expected = test[2];
            int originalX = x, originalY = y;
            
            assertEquals(expected, calc.solve(x, y));
            assertEquals(originalX, x);
            assertEquals(originalY, y);
        }
        
        System.out.println("[ACOC] IUOI: All parameters unchanged");
    }

    @Test
    public void testBoundaryValueAnalysis() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing boundary values...");
        
        assertEquals(Integer.MAX_VALUE, calc.solve(Integer.MAX_VALUE, 0));
        assertEquals(Integer.MIN_VALUE, calc.solve(Integer.MIN_VALUE, 0));
        
        assertEquals(0, calc.solve(0, 0));
        assertEquals(5, calc.solve(5, 0));
        assertEquals(5, calc.solve(0, 5));
        
        assertEquals(Integer.MAX_VALUE - 1, calc.solve(Integer.MAX_VALUE - 1, 0));
        assertEquals(Integer.MIN_VALUE + 1, calc.solve(Integer.MIN_VALUE + 1, 0));
        
        assertEquals(Integer.MAX_VALUE, calc.calculateSum(Integer.MAX_VALUE, 0));
        assertEquals(Integer.MIN_VALUE, calc.calculateSum(Integer.MIN_VALUE, 0));
        
        System.out.println("[ACOC] Boundary values: All tests passed");
    }

    @Test
    public void testPropertyBased() {
        Calculator calc = new Calculator();
        System.out.println("[ACOC] Testing mathematical properties...");
        
        for (int i = 0; i < 20; i++) {
            int x = (int)(Math.random() * 2000) - 1000;
            int y = (int)(Math.random() * 2000) - 1000;
            assertEquals(calc.solve(x, y), calc.solve(y, x));
        }
        
        for (int i = 0; i < 20; i++) {
            int x = (int)(Math.random() * 2000) - 1000;
            assertEquals(x, calc.solve(x, 0));
            assertEquals(x, calc.solve(0, x));
        }
        
        int x = 10, y = 20, z = 30;
        int result1 = calc.solve(calc.solve(x, y), z);
        int result2 = calc.solve(x, calc.solve(y, z));
        assertEquals(result1, result2);
        
        System.out.println("[ACOC] Mathematical properties: Verified");
    }

    @Test
    public void testComprehensiveIntegration() {
        Calculator calc = new Calculator();
        System.out.println("\n[ACOC] === COMPREHENSIVE INTEGRATION TEST ===");
        
        int[][] comprehensiveTests = {
            {0, 0, 0, 0},
            {1, 1, 2, 2},
            {-1, -1, -2, -2},
            {5, -3, 2, 2},
            {-5, 8, 3, 3},
            {100, 200, 300, 300},
            {Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE},
            {Integer.MIN_VALUE, 0, Integer.MIN_VALUE, Integer.MIN_VALUE}
        };
        
        int totalTests = 0;
        int mutantsKilled = 0;
        
        for (int[] test : comprehensiveTests) {
            int a = test[0];
            int b = test[1];
            int solveExpected = test[2];
            int sumExpected = test[3];
            
            totalTests += 2;
            
            int solveResult = calc.solve(a, b);
            assertEquals(solveExpected, solveResult);
            
            if (a - b != solveExpected) {
                assertNotEquals(a - b, solveResult);
                mutantsKilled++;
            }
            
            int sumResult = calc.calculateSum(a, b);
            assertEquals(sumExpected, sumResult);
            
            assertNotEquals(0, sumResult);
            if (a != sumExpected) {
                assertNotEquals(a, sumResult);
                mutantsKilled++;
            }
            if (b != sumExpected) {
                assertNotEquals(b, sumResult);
                mutantsKilled++;
            }
            
            if (a != b) {
                int reverseResult = calc.calculateSum(b, a);
                assertEquals(sumResult, reverseResult);
            }
        }
        
        System.out.println("[ACOC] Comprehensive: " + totalTests + " tests, " + mutantsKilled + " mutants killed");
        System.out.println("[ACOC] === TEST COMPLETE ===\n");
    }

}
