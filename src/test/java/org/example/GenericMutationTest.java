package org.example;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
public class GenericMutationTest {
    @Test
    public void runKillTests() throws Exception {
        Calculator calc = new Calculator();
        Method[] methods = calc.getClass().getDeclaredMethods();
        
        for (Method m : methods) {
            if (m.getName().equals("solve")) {
                // تست اختصاصی برای متد solve
                assertEquals("Killed by solve pos", 15, (int)m.invoke(calc, 5, 10));
                assertEquals("Killed by solve neg", 15, (int)m.invoke(calc, -5, 10));
            }
            if (m.getName().equals("checkLogic")) {
                // تست اختصاصی برای متد منطقی
                assertTrue("Killed logic TT", (boolean)m.invoke(calc, true, true));
                assertFalse("Killed logic FT", (boolean)m.invoke(calc, false, true));
            }
        }
    }
}