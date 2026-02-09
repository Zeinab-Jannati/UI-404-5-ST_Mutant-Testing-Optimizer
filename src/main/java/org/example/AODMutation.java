package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class AODMutation {
    public static int applyAOD(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // پیدا کردن الگوهایی مثل a + b در هر جای کد
            if (line.matches(".*\\w+\\s*[+\\-*/]\\s*\\w+.*")) {
                // نگه داشتن فقط متغیر اول
                String m1 = line.replaceAll("(\\w+)\\s*[+\\-*/]\\s*\\w+", "$1");
                count++;
                MutationUtils.saveMutant(lines, i, m1, "AOD", count);

                // نگه داشتن فقط متغیر دوم
                String m2 = line.replaceAll("\\w+\\s*[+\\-*/]\\s*(\\w+)", "$1");
                count++;
                MutationUtils.saveMutant(lines, i, m2, "AOD", count);
            }
        }
        return count;
    }
}