package com.mycompany.textanalyzer.psql.v2.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/**
 * Класс, считывающий словарь из файла gramtab.tab
 * @author pavel
 */
public class GrammaReader {
    private Map<String, Grammem> grammemDictionary 
            = new HashMap<String, Grammem>();

    public GrammaReader(String lang, String fileName) 
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName),
                "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            line = line.trim();
            if (!line.startsWith("//") && line.length() > 0) {
                String[] strings = line.split(" ", 3);
                Grammem grammem = new Grammem(lang, strings[2]);
                grammemDictionary.put(strings[0], grammem);
            }
            line = bufferedReader.readLine();
        }
    }

    public Map<String, Grammem> getGrammemDictionary() {
        return grammemDictionary;
    }
}