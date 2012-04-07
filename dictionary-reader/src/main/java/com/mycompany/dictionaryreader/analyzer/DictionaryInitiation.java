/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dictionaryreader.analyzer;

import com.mycompany.dictionaryreader.dictionary.DictionaryReader;
import com.mycompany.dictionaryreader.dictionary.GrammaReader;
import java.io.IOException;

/**
 *
 * @author pavel
 */
public class DictionaryInitiation {
    private GrammaReader engGrammaInfo;
    private DictionaryReader engDictionaryReader;
    private GrammaReader rusGrammaInfo;
    private DictionaryReader rusDictionaryReader;
    private final String egramtab = "src/main/resources/dicts/eng/egramtab.tab";
    private final String emorphs = "src/main/resources/dicts/eng/morphs.mrd";
    private final String rgramtab = "src/main/resources/dicts/rus/rgramtab.tab";
    private final String rmorphs = "src/main/resources/dicts/rus/morphs.mrd";
    
    protected DictionaryInitiation() {
        initiateRusDictionaries();
        initiateEngDictionaries();
    }
    
    private void initiateRusDictionaries() {
        try {
            rusGrammaInfo = new GrammaReader(rgramtab);
            rusDictionaryReader = new DictionaryReader(rmorphs);
            rusDictionaryReader.proccess();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void initiateEngDictionaries() {
        try {
            engGrammaInfo = new GrammaReader(egramtab);
            engDictionaryReader = new DictionaryReader(emorphs);
            engDictionaryReader.proccess();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public DictionaryReader getDictionary(String lang) {
        if (lang.equalsIgnoreCase("rus")) 
            return rusDictionaryReader;
        else if (lang.equalsIgnoreCase("eng")) 
            return engDictionaryReader;
        else return null;
    }
    
    public GrammaReader getGramma(String lang) {
        if (lang.equalsIgnoreCase("rus")) 
            return rusGrammaInfo;
        else if (lang.equalsIgnoreCase("eng")) 
            return engGrammaInfo;
        else return null;
    }
}