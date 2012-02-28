package com.mycompany.dictionaryreader.analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavlin
 */
public class WordListReader {
    private String input = "Привет, как дела? Всё-таки! Smashing pumpkins... Good-bye. Йоду мне";
    //   ё/е
    private List wordList = new ArrayList();
    public WordListReader() {
        String word = "";
        for (int i=0; i < input.length(); i++) 
            if (input.codePointAt(i) >= 65 & input.codePointAt(i) <= 90 
                    | input.codePointAt(i) >= 97 & input.codePointAt(i) <= 122 
                    | input.codePointAt(i) >= 1040 & input.codePointAt(i) <= 1103
                    | input.codePointAt(i) == 1105 | input.codePointAt(i) == 1025//ёЁ
                    | input.codePointAt(i) == 45)
                word += input.substring(i, i + 1);
            else if (!word.equals("")) {
                wordList.add(word);
                word = "";
            }
    }
    
    public List getWordList() { 
        return wordList;
    }
}