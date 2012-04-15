package com.mycompany.textanalyzer.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GrammaReader {
    private Map<String, String> inversIndex = new HashMap<String, String>();

    public GrammaReader(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            line = line.trim();//убирает лишние пробелы по краям
            if (!line.startsWith("//") && line.length() > 0) {
                String[] strings = line.split(" ", 2); //делит линию на 2 части
                inversIndex.put(strings[0], strings[1]); // карта = анкод + оставшаяся строка
            }
            line = bufferedReader.readLine();
        }
    }

    public Map<String, String> getGrammInversIndex() {
        return inversIndex;
    }
}