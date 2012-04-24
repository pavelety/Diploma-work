/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer;

/**
 *
 * @author pavel
 */
public class ChoiceRunner {
    private final static Boolean useCache = true;
    private final static Boolean usePSQL = true;
    private final static String textFilePath = "texts/Форд Генри - Моя жизнь, "
            + "мои достижения - 1924.txt";
    private final static String encoding = "cp1251";
    
    public static void main(String[] args) {
        Long timeStart = System.currentTimeMillis();
        AnalyzerInterface ma; 
        if (usePSQL)
            ma = new com.mycompany.textanalyzer.analyzerpsql.Analyzer();
        else
            ma = new com.mycompany.textanalyzer.analyzer.Analyzer();
        ma.analyze(useCache, textFilePath, encoding);
        Long timeEnd = System.currentTimeMillis();
        Long timeRead = ma.getTimeDictRead() - timeStart;
        Long time = timeEnd - timeStart;
        System.out.println("Время чтения словаря: " 
                + String.valueOf((timeRead - timeRead % 1000) / 1000) + "c " 
                + String.valueOf(timeRead % 1000) + "мс");
        System.out.println("Общее время: " 
                + String.valueOf((time - time % 1000) / 1000) + "c " 
                + String.valueOf(time % 1000) + "мс");
        System.out.println("Запросов к словарю: " 
                + String.valueOf(ma.getCountRequest()) 
                + ". Успешно: " + String.valueOf(ma.getCountSuccess()));
        if (useCache) {
            System.out.println("Cache hit: " 
                    + String.valueOf(ma.getCountCacheHit() + ". Cache miss: " 
                    + String.valueOf(ma.getCountCacheMiss())));
            System.out.println("Запросов в секунду: " 
                    + String.valueOf((ma.getCountRequest() 
                    + ma.getCountCacheHit()) * 1000 / (timeEnd 
                    - ma.getTimeDictRead())));
        } else
            System.out.println("Запросов к словарю в секунду: " 
                    + String.valueOf(ma.getCountRequest() * 1000 / (timeEnd 
                    - ma.getTimeDictRead())));
    }
}