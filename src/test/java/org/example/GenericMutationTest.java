package org.example;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
public class GenericMutationTest {
    @Test
    public void runDynamicTests() throws Exception {
        Class<?> clazz = Class.forName("org.example.Calculator");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        for (Method m : clazz.getDeclaredMethods()) {
            if (java.lang.reflect.Modifier.isPublic(m.getModifiers())) {
                try {
                    Object[] testInputs = {5, 0, -5, 100};
                    for(Object input : testInputs) {
                        Object[] args = new Object[m.getParameterCount()];
                        for(int i = 0; i < args.length; i++) args[i] = input;
                        m.invoke(instance, args);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Killed");
                }
            }
        }
    }
}