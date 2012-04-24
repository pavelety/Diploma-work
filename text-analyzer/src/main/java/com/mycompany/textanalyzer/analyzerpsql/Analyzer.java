/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer.analyzerpsql;

import com.mycompany.textanalyzer.AnalyzerInterface;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author pavel
 */
public class Analyzer implements AnalyzerInterface {
    private final String selectLemmata = "select flexiamodelid from lemmata "
            + "where basestr=?";
    private final String selectFlexia = "select ancode, flexiastr from "
            + "flexiamodels where flexiamodelid=?";
    private final String selectAncodes = "select partofspeech, grammems from "
            + "ancodes where ancode=?";
    private int countObjDictRequest;
    private int countObjDictSuccess;
    private long timeDictRead;
    private DictionaryInitiation dictionaries;
    private Boolean useCache;
    private TreeMap<String, String> rusCache;
    private TreeMap<String, String> engCache;
    private int countCacheHit;
    private int countCacheMiss;
    private PreparedStatement psSelectLemmataEng;
    private PreparedStatement psSelectFlexiaEng;
    private PreparedStatement psSelectAncodesEng;
    private PreparedStatement psSelectLemmataRus;
    private PreparedStatement psSelectFlexiaRus;
    private PreparedStatement psSelectAncodesRus;
    
    public void analyze(Boolean useCache, String textFilePath, String encoding) {
        //this.useCache = useCache;
        dictionaries = new DictionaryInitiation();
        timeDictRead = System.currentTimeMillis();
        this.useCache = useCache;
        if (useCache) { 
            rusCache = new TreeMap<String, String>(); 
            engCache = new TreeMap<String, String>(); 
        }
        try {
            psSelectLemmataEng = dictionaries.getConnectionEng()
                    .prepareStatement(selectLemmata);
            psSelectFlexiaEng = dictionaries.getConnectionEng()
                    .prepareStatement(selectFlexia);
            psSelectAncodesEng = dictionaries.getConnectionEng()
                    .prepareStatement(selectAncodes);
            psSelectLemmataRus = dictionaries.getConnectionRus()
                    .prepareStatement(selectLemmata);
            psSelectFlexiaRus = dictionaries.getConnectionRus()
                    .prepareStatement(selectFlexia);
            psSelectAncodesRus = dictionaries.getConnectionRus()
                    .prepareStatement(selectAncodes);
            fileAnalyze(textFilePath, encoding);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            dictionaries.closeConnections();
        }
    }

    public void fileAnalyze(String path, String encoding) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), encoding));
        while (bufferedReader.ready()) {
            stringAnalyze(bufferedReader.readLine());
        }
    }

    private void stringAnalyze(String input) {
        String word = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.codePointAt(i) >= 65 & input.codePointAt(i) <= 90
                    | input.codePointAt(i) >= 97 & input.codePointAt(i) <= 122
                    | input.codePointAt(i) >= 1040
                    & input.codePointAt(i) <= 1103
                    | input.codePointAt(i) == 1105
                    | input.codePointAt(i) == 1025 | input.codePointAt(i) == 45) {
                word += input.substring(i, i + 1);
            } else if (!word.equalsIgnoreCase("")) {
                if (useCache) 
                    analyzeInCache(word);
                else 
                    analyze(word);
                word = "";
            }
            if (i + 1 == input.length() && !word.equalsIgnoreCase("")) {
                if (useCache) 
                    analyzeInCache(word);
                else 
                    analyze(word);
            }
        }
    }

    private void analyze(String word) {
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) <= 122) {
            searchWord(psSelectLemmataEng, psSelectFlexiaEng, 
                    psSelectAncodesEng, word);
        } else {
            searchWord(psSelectLemmataRus, psSelectFlexiaRus, 
                    psSelectAncodesRus, word);
        }
    }

    private void analyzeInCache(String word) {
        String s;
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) <= 122)
            if (searchInCache(engCache, word) == null) {
                countCacheMiss++;
                s = searchWord(psSelectLemmataEng, psSelectFlexiaEng, 
                    psSelectAncodesEng, word);
                engCache.put(word, s==null?"":s);
            } else
                countCacheHit++;
        else
            if (searchInCache(rusCache, word) == null) {
                countCacheMiss++;
                s = searchWord(psSelectLemmataRus, psSelectFlexiaRus, 
                    psSelectAncodesRus, word);
                rusCache.put(word, s==null?"":s);  
            } else
                countCacheHit++;
    }
    
    private String searchInCache(Map<String, String> map, String word) {
        return map.get(word);
    }
    
    private String searchWord(PreparedStatement psSelectLemmata, 
            PreparedStatement psSelectFlexia, PreparedStatement psSelectAncodes,
            String word) {
        String base;
        String result = "";
        countObjDictRequest++;
        try {
            for (int i = word.length(); i >= 0; i--) {
                base = word.toLowerCase().substring(0, i);
                psSelectLemmata.setString(1, base);
                ResultSet rsSelectLemmata = psSelectLemmata.executeQuery();
                while (rsSelectLemmata.next()) {
                    psSelectFlexia.setInt(1, 
                            rsSelectLemmata.getInt("flexiamodelid"));
                    ResultSet rsSelectFlexia = psSelectFlexia.executeQuery();
                    while (rsSelectFlexia.next()) {
                        if (word.equalsIgnoreCase(base 
                                + rsSelectFlexia.getString("flexiastr"))) {
                            psSelectAncodes.setString(1, 
                                    rsSelectFlexia.getString("ancode"));
                            ResultSet rsSelectAncodes = psSelectAncodes
                                    .executeQuery();
                            rsSelectAncodes.next();
                            result += rsSelectAncodes.getString("partofspeech") 
                                    + " " 
                                    + rsSelectAncodes.getString("grammems") 
                                    + "; ";
                        }
                    }
                }
                if (!result.isEmpty()) {
                    countObjDictSuccess++;
                    break;
                }    
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int getCountRequest() {
        return countObjDictRequest;
    }

    public int getCountSuccess() {
        return countObjDictSuccess;
    }

    public long getTimeDictRead() {
        return timeDictRead;
    }
    
    public int getCountCacheHit() { 
        return countCacheHit; 
    }
    
    public int getCountCacheMiss() { 
        return countCacheMiss; 
    }
}