/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer.analyzer;

import com.mycompany.textanalyzer.AnalyzerInterface;
import com.mycompany.textanalyzer.Statistics;
import com.mycompany.textanalyzer.dictionary.DictionaryReader;
import com.mycompany.textanalyzer.dictionary.FlexiaModel;
import com.mycompany.textanalyzer.dictionary.GrammaReader;
import com.mycompany.textanalyzer.dictionary.WordCard;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
/**
 *
 * @author pavel
 */
public class Analyzer implements AnalyzerInterface {
    private Boolean useCache;
    private DictionaryInitiation dictionaries;
    private TreeMap<String, String> rusCache;
    private TreeMap<String, String> engCache;
    private Statistics stats;
    
    public Analyzer(Statistics stats) {
        this.stats = stats;
    }
    
    public void analyze(Boolean useCache, String textFilePath, String encoding) {
        this.useCache = useCache;
        dictionaries = new DictionaryInitiation();
        stats.setTimeDictRead(System.currentTimeMillis());
        if (useCache) {
            rusCache = new TreeMap<String, String>();
            engCache = new TreeMap<String, String>();
        }
        try {
            fileAnalyze(textFilePath, encoding);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void fileAnalyze(String textFilePath, String encoding) 
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(textFilePath), 
                encoding));
        while (bufferedReader.ready()) {
            stringAnalyze(bufferedReader.readLine());
        } 
    }
    
    private void stringAnalyze(String input) {
        String word = "";
        for (int i=0; i < input.length(); i++) {
            if (input.codePointAt(i) >= 65 & input.codePointAt(i) <= 90 
                    | input.codePointAt(i) >= 97 & input.codePointAt(i) <= 122 
                    | input.codePointAt(i) >= 1040 
                    & input.codePointAt(i) <= 1103 
                    | input.codePointAt(i) == 1105 
                    | input.codePointAt(i) == 1025 | input.codePointAt(i) == 45)
                word += input.substring(i, i + 1);
            else if (!word.equalsIgnoreCase("")) {
                if (useCache) 
                    analyzeInCache(word);
                else 
                    analyze(word);
                word = "";
            }
            if (i + 1 == input.length() && !word.equalsIgnoreCase(""))
                if (useCache) 
                    analyzeInCache(word);
                else 
                    analyze(word);
        }
    }
    
    private void analyze(String word) {
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) <= 122)
            searchWord(dictionaries.getDictionary("eng"), 
                    dictionaries.getGramma("eng"), word);
        else
            searchWord(dictionaries.getDictionary("rus"), 
                    dictionaries.getGramma("rus"), word);
    }
    
    private void analyzeInCache(String word) {
        String s;
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) <= 122)
            if (searchInCache(engCache, word) == null) {
                stats.increaseCountCacheMiss();
                s = searchWord(dictionaries.getDictionary("eng"), 
                        dictionaries.getGramma("eng"), word);
                engCache.put(word, s==null?"":s);
            } else
                stats.increaseCountCacheHit();
        else
            if (searchInCache(rusCache, word) == null) {
                stats.increaseCountCacheMiss();
                s = searchWord(dictionaries.getDictionary("rus"), 
                        dictionaries.getGramma("rus"), word);
                rusCache.put(word, s==null?"":s);  
            } else
                stats.increaseCountCacheHit();
    }
    
    private String searchInCache(Map<String, String> map, String word) {
        return map.get(word);
    }
    
    public String searchWord(DictionaryReader dictionaryReader, 
            GrammaReader grammaInfo, String word) {
        String base;
        String result = null;
        stats.increaseCountRequest();
        //word.startsWith(base);!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Сделать поиск с помощью этого!
        List<FlexiaModel> formsOfWord = new ArrayList();
        for (int i = word.length(); i >= 0; i--) {
            base = word.toLowerCase().substring(0, i);
            WordCard wc = dictionaryReader.getWords().get(base);
            if (wc != null) {
                Iterator<FlexiaModel> wordForms = wc.getWordsForms().listIterator();
                while (wordForms.hasNext()) {
                    FlexiaModel flexiaModel = wordForms.next();
                    if (flexiaModel.create(base).equalsIgnoreCase(word
                            .toLowerCase()))
                        formsOfWord.add(flexiaModel);
                }
                if (!formsOfWord.isEmpty()){
                    result = base;
                    stats.increaseCountSuccess();
                    break;
                }
            }
        }
        Iterator<FlexiaModel> formsIterator = formsOfWord.listIterator();
        while (formsIterator.hasNext()){
            result += ", " + grammaInfo.getGrammInversIndex().get(formsIterator
                    .next().getCode());
        }
        return result;
    }
}