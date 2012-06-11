package com.mycompany.textanalyzer;

/**
 * Главный класс запуска анализаторов. Опции задаются булевыми 
 * полями useCache (использовать кэш) и usePSQL (использовать 
 * PostgreSQL). Запускает анализатор и выводит информацию после 
 * анализа.
 * @author pavel
 */
public class ChoiceRunner {
    private final static Boolean useCache = true;
    private final static Boolean usePSQL = true;
    private final static int version = 2; /*enter from 1 to 4*/
    private final static String textFilePath = "texts/20111225";
    private final static String encoding = "UTF-8";
    
    public static void main(String[] args) {
        Long timeStart = System.currentTimeMillis();
        AnalyzerInterface ai = null;
        Statistics stats = new Statistics();
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
        Long timeEnd = System.currentTimeMillis();
        Long timeRead = stats.getTimeDictRead() - timeStart;
        Long time = timeEnd - timeStart;
        System.out.println("Время чтения словаря: " 
                + String.valueOf((timeRead - timeRead % 1000) 
                / 1000) + "c " + String.valueOf(timeRead % 1000) 
                + "мс");
        System.out.println("Общее время: " 
                + String.valueOf((time - time % 1000) / 1000) + "c " 
                + String.valueOf(time % 1000) + "мс");
        System.out.println("Запросов к словарю: " 
                + String.valueOf(stats.getCountRequest()) 
                + ". Успешно: " 
                + String.valueOf(stats.getCountSuccess()));
        if (useCache) {
            System.out.println("Cache hit: " 
                    + String.valueOf(stats.getCountCacheHit() + "."
                    + " Cache miss: " 
                    + String.valueOf(stats.getCountCacheMiss())));
            System.out.println("Запросов в секунду: " 
                    + String.valueOf((stats.getCountRequest() 
                    + stats.getCountCacheHit()) * 1000 / (timeEnd 
                    - stats.getTimeDictRead())));
        } else
            System.out.println("Запросов к словарю в секунду: " 
                    + String.valueOf(stats.getCountRequest() * 1000
                    / (timeEnd - stats.getTimeDictRead())));
    }
}