package com.mycompany.textanalyzer;

/**
 * Класс статистики, с помощью которого ведутся подсчёты запросов к
 * БД, к кэшу, общего времения чтения.
 * @author pavel
 */
public class Statistics {
    private int countObjDictRequest;
    private int countObjDictSuccess;
    private long timeDictRead;
    private int countCacheHit;
    private int countWords;
    private Long timeStart;
    private Long timeEnd;
    
    public Statistics() {
        timeStart = System.currentTimeMillis();
    }
    
    public void increaseCountRequest() {
        countObjDictRequest++;
    }

    public void increaseCountSuccess() {
        countObjDictSuccess++;
    }
    
    public void setTimeDictRead(long timeDictRead) {
        this.timeDictRead = timeDictRead;
    }
    
    public void increaseCountCacheHit() { 
        countCacheHit++; 
    }
    
    public void increaseCountWords() { 
        countWords++; 
    }
    
    public void end() {
        timeEnd = System.currentTimeMillis();
    }
    
    public void print(boolean useCache) {
        Long timeRead = timeDictRead - timeStart;
        Long timeAnalyze = timeEnd - timeDictRead;
        System.out.println("Время чтения словаря: " 
                + String.valueOf((timeRead - timeRead % 1000) 
                / 1000) + "c " + String.valueOf(timeRead % 1000) 
                + "мс\nВремя анализа файла: " 
                + String.valueOf((timeAnalyze - timeAnalyze % 1000)
                / 1000) + "c " + String.valueOf(timeAnalyze % 1000) 
                + "мс\nСлов прочитано: " 
                + String.valueOf(countWords) 
                + ". Из них распознано: " 
                + String.valueOf(countObjDictSuccess 
                + countCacheHit) + "\nУникальных известных "
                + "словоформ: "
                + String.valueOf(countObjDictSuccess));
        if (useCache)
            System.out.println("Cache hit: " 
                    + String.valueOf(countCacheHit) + ". Cache miss"
                    + ": " + String.valueOf(countObjDictRequest) 
                    + "\nСкорость анализа: " 
                    + String.valueOf(countWords * 1000 
                    / timeAnalyze) + " слов в секунду.");
        else
            System.out.println("Скорость анализа: " 
                    + String.valueOf(countWords * 1000
                    / timeAnalyze) + " слов в секунду.");
    }
}