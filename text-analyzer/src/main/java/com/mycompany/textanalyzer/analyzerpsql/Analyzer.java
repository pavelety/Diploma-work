package com.mycompany.textanalyzer.analyzerpsql;

import com.mycompany.textanalyzer.AnalyzerInterface;
import com.mycompany.textanalyzer.Statistics;
import com.mycompany.textanalyzer.Tokenizer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс анализатора: делает морфологический анализ слова, используя
 * БД (1700 слов в секунду)
 * @author pavel
 */
public class Analyzer implements AnalyzerInterface {
    private final String selectLemmata = "select flexiamodelid from"
            + " lemmata where basestr=?";
    private final String selectFlexia = "select ancode, flexiastr "
            + "from flexiamodels where flexiamodelid=?";
    private final String selectAncodes = "select partofspeech, "
            + "grammems from ancodes where ancode=?";
    private DictionaryInitiation dictionaries;
    private Map<String, String> rusCache;
    private Map<String, String> engCache;
    private PreparedStatement psSelectLemmataEng;
    private PreparedStatement psSelectFlexiaEng;
    private PreparedStatement psSelectAncodesEng;
    private PreparedStatement psSelectLemmataRus;
    private PreparedStatement psSelectFlexiaRus;
    private PreparedStatement psSelectAncodesRus;
    private Statistics stats;
    
    public Analyzer(Statistics stats) {
        this.stats = stats;
    }
    
    public void analyze(Boolean useCache, String textFilePath, 
            String encoding) {
        dictionaries = new DictionaryInitiation();
        stats.setTimeDictRead(System.currentTimeMillis());
        if (useCache) { 
            rusCache = new HashMap<String, String>(); 
            engCache = new HashMap<String, String>(); 
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
            Tokenizer token = new Tokenizer(textFilePath, encoding);
            String word = token.getWord();
            while (word != null) {
            //for (int i = 1; i < 50; i++) {
            //    System.out.println(i);
                analyzeWord(word, useCache);
                word = token.getWord();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dictionaries.closeConnections();
        }
    }

    private void analyzeWord(String word, boolean useCache) {
        if (useCache)
            analyzeInCache(word);
        else 
            analyze(word);
    }
    
    private void analyze(String word) {
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) 
                <= 122) {
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
                | word.codePointAt(0) >= 97 & word.codePointAt(0)
                <= 122)
            if (searchInCache(engCache, word) == null) {
                stats.increaseCountCacheMiss();
                s = searchWord(psSelectLemmataEng, 
                        psSelectFlexiaEng, psSelectAncodesEng, 
                        word);
                engCache.put(word, s == null ? "" : s);
            } else
                stats.increaseCountCacheHit();
        else
            if (searchInCache(rusCache, word) == null) {
                stats.increaseCountCacheMiss();
                s = searchWord(psSelectLemmataRus, 
                        psSelectFlexiaRus, psSelectAncodesRus,
                        word);
                rusCache.put(word, s == null ? "" : s);  
            } else
                stats.increaseCountCacheHit();
    }
    
    private String searchInCache(Map<String, String> map, 
            String word) {
        return map.get(word);
    }
    
    private String searchWord(PreparedStatement psSelectLemmata, 
            PreparedStatement psSelectFlexia, 
            PreparedStatement psSelectAncodes, String word) {
        String base;
        String result = "";
        stats.increaseCountRequest();
        try {
            for (int i = word.length(); i >= 0; i--) {
                base = word.toLowerCase().substring(0, i);
                psSelectLemmata.setString(1, base);
                ResultSet rsSelectLemmata = psSelectLemmata
                        .executeQuery();
                while (rsSelectLemmata.next()) {
                    psSelectFlexia.setInt(1, rsSelectLemmata
                            .getInt("flexiamodelid"));
                    ResultSet rsSelectFlexia = psSelectFlexia
                            .executeQuery();
                    while (rsSelectFlexia.next()) {
                        if (word.equalsIgnoreCase(base 
                                + rsSelectFlexia
                                .getString("flexiastr"))) {
                            psSelectAncodes.setString(1, 
                                    rsSelectFlexia
                                    .getString("ancode"));
                            ResultSet rsSelectAncodes 
                                    = psSelectAncodes
                                    .executeQuery();
                            rsSelectAncodes.next();
                            result += rsSelectAncodes
                                    .getString("partofspeech") + " " 
                                    + rsSelectAncodes
                                    .getString("grammems") + "; ";
                        }
                    }
                }
                if (!result.isEmpty()) {
                    stats.increaseCountSuccess();
                    break;
                }    
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}