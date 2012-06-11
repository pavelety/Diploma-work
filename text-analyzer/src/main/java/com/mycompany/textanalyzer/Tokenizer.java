package com.mycompany.textanalyzer;

/**
 * Класс, который слово за словом считывает лексемы из файла.
 * @author pavel
 */
public class Tokenizer {
    
    private int i = 0;
    private String chars;
    private FileReader text;
    private String line;
    
    public Tokenizer(String textFilePath, String encoding) {
        text = new FileReader(textFilePath, encoding);
        line = text.getLine();
    }
    
    public String getWord() {
        String word;
        while (line != null) {
            chars = "";
            while (i < line.length()) {
                word = tokenize(line);
                i++;
                if (word != null)
                    return word;
            }
            line = text.getLine();
            i = 0;
        }
        return null;
    }
    
    private String tokenize(String line) {
        if (line.codePointAt(i) >= 65 & line.codePointAt(i) <= 90 
                | line.codePointAt(i) >= 97 
                & line.codePointAt(i) <= 122
                | line.codePointAt(i) >= 1040 
                & line.codePointAt(i) <= 1103 
                | line.codePointAt(i) == 1105 
                | line.codePointAt(i) == 1025 
                | line.codePointAt(i) == 45)
            chars += line.substring(i, i + 1);
        else if (!chars.equalsIgnoreCase("")) {
            String buf = chars;
            chars = "";
            return buf;
        }
        if (i + 1 == line.length() && !chars.equalsIgnoreCase(""))
            return chars;
        return null;
    }
}