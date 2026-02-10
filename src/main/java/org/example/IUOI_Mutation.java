package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class IUOI_Mutation {

    // اپراتورهای تک‌تایی که میخوایم اضافه کنیم
    private static final String[] UNARY_OPERATORS = { "+", "-", "!" };

    public static int applyIUOI(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int mutantCount = 0;

        // تشخیص متدها (عدد یا بولی)
        Pattern methodPattern = Pattern.compile("(public|private|protected)?\\s*(int|double|float|long|boolean)\\s+\\w+\\s*\\(");
        Pattern varPattern = Pattern.compile("\\b([a-zA-Z_$][a-zA-Z\\d_$]*)\\b");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // اگر خط داخل یک متد عددی یا بولی هست
            if (methodPattern.matcher(line).find()) {
                // بررسی بقیه خطوط متد
                for (int j = i + 1; j < lines.size(); j++) {
                    String innerLine = lines.get(j);
                    if (innerLine.contains("{") || innerLine.contains("}")) continue; // نادیده گرفتن براکت‌ها

                    Matcher m = varPattern.matcher(innerLine);
                    while (m.find()) {
                        String varName = m.group(1);

                        // جلوگیری از تغییر کلمات کلیدی
                        String keywords = "|public|private|protected|static|final|return|if|else|int|boolean|double|float|long|class|package|import|";
                        if (keywords.contains("|" + varName + "|")) continue;

                        // اعمال اپراتور تک‌تایی
                        for (String op : UNARY_OPERATORS) {
                            String mutated = innerLine.substring(0, m.start()) + op + varName + innerLine.substring(m.end());
                            mutantCount++;
                            saveMutant(lines, j, mutated, "IUOI", mutantCount);
                        }
                    }

                    // اگر به انتهای متد رسیدیم
                    if (innerLine.contains("}")) break;
                }
            }
        }

        System.out.println("IUOI Operator: " + mutantCount + " mutants generated.");
        return mutantCount;
    }

    private static void saveMutant(List<String> originalLines, int lineIndex, String mutatedLine, String opName, int count) throws IOException {
        List<String> mutantContent = new ArrayList<>(originalLines);
        mutantContent.set(lineIndex, mutatedLine);

        Path mutantFolder = Paths.get("mutants"); // همون فولدر
        if (!Files.exists(mutantFolder)) Files.createDirectories(mutantFolder);

        String fileName = "mutant_" + opName + "_" + count + ".java";
        Files.write(mutantFolder.resolve(fileName), mutantContent);
    }
}
