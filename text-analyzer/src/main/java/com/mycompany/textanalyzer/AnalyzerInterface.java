package com.mycompany.textanalyzer;

/**
 * Интерфейс, позволяющий вызывать метод анализа текстового файла 
 * @author pavel
 */
public interface AnalyzerInterface {
    
    void analyze(Boolean useCache, String textFilePath, 
            String encoding);

}