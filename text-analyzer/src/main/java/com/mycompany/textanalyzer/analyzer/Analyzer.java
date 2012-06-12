package com.mycompany.textanalyzer.analyzer;

import com.mycompany.textanalyzer.AnalyzerInterface;
import com.mycompany.textanalyzer.Statistics;
import com.mycompany.textanalyzer.Tokenizer;
import com.mycompany.textanalyzer.dictionary.DictionaryReader;
import com.mycompany.textanalyzer.dictionary.FlexiaModel;
import com.mycompany.textanalyzer.dictionary.GrammaReader;
import com.mycompany.textanalyzer.dictionary.WordCard;
import java.util.*;
/**
 * Класс анализатора, который использует словарь, хранимый в объектах
 * классов (в оперативной памяти).
 * (35000 слов в секунду)
 * @author pavel
 */
public class Analyzer implements AnalyzerInterface {
    private DictionaryInitiation dictionaries;
    private Map<String, String> rusCache;
    private Map<String, String> engCache;
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
        Tokenizer token = new Tokenizer(textFilePath, encoding);
        String word = token.getWord();
        while (word != null) {
            analyzeWord(word, useCache);
            word = token.getWord();
        }
    }
    
    protected void analyzeWord(String word, boolean useCache) {
        stats.increaseCountWords();
        if (useCache)
            analyzeInCache(word);
        else 
            analyze(word);
    }
    
    private void analyze(String word) {
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 
                & word.codePointAt(0) <= 122)
            searchWord(dictionaries.getDictionary("eng"), 
                    dictionaries.getGramma("eng"), word);
        else
            searchWord(dictionaries.getDictionary("rus"), 
                    dictionaries.getGramma("rus"), word);
    }
    
    private void analyzeInCache(String word) {
        String s;
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 
                & word.codePointAt(0) <= 122)
            if (searchInCache(engCache, word) == null) {
                s = searchWord(dictionaries.getDictionary("eng"), 
                        dictionaries.getGramma("eng"), word);
                engCache.put(word, s==null?"":s);
            } else
                stats.increaseCountCacheHit();
        else
            if (searchInCache(rusCache, word) == null) {
                s = searchWord(dictionaries.getDictionary("rus"), 
                        dictionaries.getGramma("rus"), word);
                rusCache.put(word, s==null?"":s);  
            } else
                stats.increaseCountCacheHit();
    }
    
    private String searchInCache(Map<String, String> map, 
            String word) {
        return map.get(word);
    }
    
    public String searchWord(DictionaryReader dictionaryReader, 
            GrammaReader grammaInfo, String word) {
        String base;
        String result = null;
        stats.increaseCountRequest();
        List<FlexiaModel> formsOfWord = new ArrayList();
        for (int i = word.length(); i >= 0; i--) {
            base = word.toLowerCase().substring(0, i);
            WordCard wc = dictionaryReader.getWords().get(base);
            if (wc != null) {
                Iterator<FlexiaModel> wordForms 
                        = wc.getWordsForms().listIterator();
                while (wordForms.hasNext()) {
                    FlexiaModel flexiaModel = wordForms.next();
                    if (flexiaModel.create(base).equalsIgnoreCase(
                            word.toLowerCase()))
                        formsOfWord.add(flexiaModel);
                }
                if (!formsOfWord.isEmpty()){
                    result = base;
                    stats.increaseCountSuccess();
                    break;
                }
            }
        }
        Iterator<FlexiaModel> formsIterator 
                = formsOfWord.listIterator();
        while (formsIterator.hasNext()){
            result += ", " + grammaInfo.getGrammInversIndex().get(
                    formsIterator.next().getCode());
        }
        return result;
    }
}