package com.mycompany.textanalyzer.dictionarypsql2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GrammaReader {
    private Map<String, Grammem> grammemDictionary = new HashMap<String, Grammem>();

    public GrammaReader(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            line = line.trim();//убирает лишние пробелы по краям
            if (!line.startsWith("//") && line.length() > 0) {
                String[] strings = line.split(" ", 3); //делит линию на 3 части
                Grammem grammem = new Grammem(strings[2]);
                grammemDictionary.put(strings[0], grammem); // карта = анкод + граммема
            }
            line = bufferedReader.readLine();
        }
    }

    public Map<String, Grammem> getGrammemDictionary() {
        return grammemDictionary;
    }
}