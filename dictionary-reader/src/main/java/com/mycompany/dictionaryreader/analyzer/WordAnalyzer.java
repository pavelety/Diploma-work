package com.mycompany.dictionaryreader.analyzer;

import com.mycompany.dictionaryreader.dictionary.DictionaryReader;
import com.mycompany.dictionaryreader.dictionary.FlexiaModel;
import com.mycompany.dictionaryreader.dictionary.GrammaReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pavlin
 */
public class WordAnalyzer {
    private static GrammaReader engGrammaInfo;
    private static DictionaryReader engDictionaryReader;
    private static GrammaReader rusGrammaInfo;
    private static DictionaryReader rusDictionaryReader;
    
    public static void main(String[] args) throws IOException {
        engGrammaInfo = new GrammaReader("src/main/resources/dicts/eng/egramtab.tab");
        engDictionaryReader = new DictionaryReader("src/main/resources/dicts/eng/morphs.mrd");
        engDictionaryReader.proccess();
        rusGrammaInfo = new GrammaReader("src/main/resources/dicts/rus/rgramtab.tab");
        rusDictionaryReader = new DictionaryReader("src/main/resources/dicts/rus/morphs.mrd");
        rusDictionaryReader.proccess();
        WordListReader wordListReader = new WordListReader();
        Iterator listIterator = wordListReader.getWordList().listIterator();
        String word;
        while (listIterator.hasNext()) {
           word = listIterator.next().toString();
           System.out.println(word + ": " + analyze(word));
        }
    }
    
    private static String analyze(String word) {
        String result;
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) <= 122) {
            result = "eng" + searchWord(engDictionaryReader, engGrammaInfo, word);
        } else {
            result = "rus" + searchWord(rusDictionaryReader, rusGrammaInfo, word);
        }
        return result;
    }
    
    private static String searchWord(DictionaryReader dictionaryReader, GrammaReader grammaInfo, String word) {
        String base;
        String result = null;
        List<FlexiaModel> formsOfWord = new ArrayList();
        for (int i = word.length(); i >= 0; i--){
            base = word.toLowerCase().substring(0, i);
            if (dictionaryReader.getWords().containsKey(base)) {
                Iterator<FlexiaModel> wordForms = dictionaryReader.getWords().get(base).getWordsForms().listIterator();
                while (wordForms.hasNext()) {
                    FlexiaModel flexiaModel = wordForms.next();
                    if (flexiaModel.create(base).equalsIgnoreCase(word.toLowerCase()))
                        formsOfWord.add(flexiaModel);
                }
                if (!formsOfWord.isEmpty()){
                    result = " " + base;
                    break;
                }
            }
        }
        Iterator<FlexiaModel> it = formsOfWord.listIterator();
        while (it.hasNext()){
            result += ", " + grammaInfo.getGrammInversIndex().get(it.next().getCode());
        }
        return result;
    }
}