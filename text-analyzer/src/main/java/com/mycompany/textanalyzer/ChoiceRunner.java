package com.mycompany.textanalyzer;

/**
 * Главный класс запуска анализаторов. Опции задаются булевыми 
 * полями useCache (использовать кэш) и usePSQL (использовать 
 * PostgreSQL). Вариант программы c PSQL выбирается изменением 
 * значения поля version от 1 до 4. Запускает анализатор и выводит
 * информацию после анализа.
 * @author pavel
 */
public class ChoiceRunner {
    private final static Boolean useCache = true;
    private final static Boolean usePSQL = false;
    private final static int version = 2; /*enter from 1 to 4*/
    private final static String textFilePath = "texts/20111225";
    private final static String encoding = "UTF-8";
    
    public static void main(String[] args) {
        Statistics stats = new Statistics();
        AnalyzerInterface ai = null;
        if (usePSQL)
            switch (version) {
                case 1:
                    ai = new com.mycompany.textanalyzer.analyzerpsql
                            .Analyzer(stats);   
                    break;
                case 2:
                    ai = new com.mycompany.textanalyzer.analyzerpsql2
                            .Analyzer(stats);   
                    break;
                case 3:
                    ai = new com.mycompany.textanalyzer.analyzerpsql3
                            .Analyzer(stats);   
                    break;
                case 4:
                    ai = new com.mycompany.textanalyzer.analyzerpsql4
                            .Analyzer(stats);   
                    break;    
            }
        else
            ai = new com.mycompany.textanalyzer.analyzer
                    .Analyzer(stats);
        ai.analyze(useCache, textFilePath, encoding);
        stats.end();
        stats.print(useCache);
    }
}