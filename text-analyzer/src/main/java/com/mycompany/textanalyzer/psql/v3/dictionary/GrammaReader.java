package com.mycompany.textanalyzer.psql.v3.dictionary;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
/**
 * Класс, считывающий словарь из файла gramtab.tab
 * @author pavel
 */
public class GrammaReader {
    private Map<String, Grammem> grammemDictionary 
            = new HashMap<String, Grammem>();

    public GrammaReader(String lang, String fileName) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(fileName), "UTF-8"));
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
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Map<String, Grammem> getGrammemDictionary() {
        return grammemDictionary;
    }
}