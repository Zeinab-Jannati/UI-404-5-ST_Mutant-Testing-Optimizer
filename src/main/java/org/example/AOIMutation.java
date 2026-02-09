package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.regex.*;
import java.util.ArrayList; // اضافه شده برای کپی ایمن لیست

public class AOIMutation {

    public static int applyAOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int count = 0;
        boolean insideNumericMethod = false;

        // تشخیص متدهای عددی و منطقی
        Pattern numericMethod = Pattern.compile("(public|private|protected)?\\s*int\\s+\\w+\\s*\\(");
        Pattern booleanMethod = Pattern.compile("(public|private|protected)?\\s*boolean\\s+\\w+\\s*\\(");

        // الگوی شناسایی بازگشت متغیرها
        Pattern unaryPattern = Pattern.compile("(return|=)\\s*(\\(\\s*([+-])\\s*([a-zA-Z_$][a-zA-Z\\d_$]*)\\s*\\)|([+-])?([a-zA-Z_$][a-zA-Z\\d_$]*))\\s*;");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (numericMethod.matcher(line).find()) {
                insideNumericMethod = true;
                continue;
            }
            if (booleanMethod.matcher(line).find()) {
                insideNumericMethod = false;
                continue;
            }

            // فقط در متدهای عددی تغییر ایجاد کن تا خطای boolean نگیریم
            if (!insideNumericMethod) continue;

            Matcher m = unaryPattern.matcher(line);
            if (!m.find()) continue;

            String mutated = null;

            // مدیریت حالت‌های پرانتزدار (+a) یا (-a)
            if (m.group(3) != null) {
                String sign = m.group(3);
                String var = m.group(4);
                if (sign.equals("+")) {
                    mutated = line.replaceFirst("\\(\\s*\\+\\s*" + var + "\\s*\\)", "(-" + var + ")");
                } else {
                    mutated = line.replaceFirst("\\(\\s*-\\s*" + var + "\\s*\\)", var);
                }
            }
            // مدیریت حالت‌های بدون پرانتز a یا -a
            else {
                String sign = m.group(5);
                String var = m.group(6);
                if (sign == null) {
                    mutated = line.replaceFirst("(return|=)\\s*" + var, "$1 -" + var);
                } else if (sign.equals("-")) {
                    mutated = line.replaceFirst("(return|=)\\s*-" + var, "$1 " + var);
                }
            }

            if (mutated != null) {
                count++;
                // --- بخش اصلاح شده برای اطمینان از وجود پکیج ---
                saveMutantWithPackage(lines, i, mutated, "AOI", count);
            }
        }

        System.out.println("AOI Operator: " + count + " smart mutants generated.");
        return count;
    }

    // متد کمکی برای اطمینان از اینکه فایل خروجی حتما پکیج دارد
    private static void saveMutantWithPackage(List<String> originalLines, int mutatedLineIndex, String mutatedContent, String opName, int count) throws IOException {
        List<String> mutantContent = new ArrayList<>(originalLines);
        mutantContent.set(mutatedLineIndex, mutatedContent);

        // چک کن اگر خط اول پکیج نیست، اضافه کن (اگر Calculator.java خودش پکیج داشته باشد، اینجا تکرار نمی‌شود)
        if (!mutantContent.isEmpty() && !mutantContent.get(0).trim().startsWith("package")) {
            mutantContent.add(0, "package org.example;");
        }

        // ایجاد پوشه mutants اگر وجود ندارد
        Path mutantFolder = Paths.get("mutants");
        if (!Files.exists(mutantFolder)) Files.createDirectories(mutantFolder);

        String fileName = "mutant_" + opName + "_" + count + ".java";
        Files.write(mutantFolder.resolve(fileName), mutantContent);
    }
}