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
    private int countCacheMiss;
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
    
    public void increaseCountCacheMiss() { 
        countCacheMiss++; 
    }
    
    public int getCountRequest() {
        return countObjDictRequest;
    }

    public int getCountSuccess() {
        return countObjDictSuccess;
    }

    public long getTimeDictRead() {
        return timeDictRead;
    }
    
    public int getCountCacheHit() { 
        return countCacheHit; 
    }
    
    public int getCountCacheMiss() { 
        return countCacheMiss; 
    }
    
    public void end() {
        timeEnd = System.currentTimeMillis();
    }
    
    public void print(boolean useCache) {
        Long timeRead = getTimeDictRead() - timeStart;
        Long time = timeEnd - timeStart;
        System.out.println("Время чтения словаря: " 
                + String.valueOf((timeRead - timeRead % 1000) 
                / 1000) + "c " + String.valueOf(timeRead % 1000) 
                + "мс");
        System.out.println("Общее время: " + String.valueOf((time 
                - time % 1000) / 1000) + "c " + String.valueOf(time
                % 1000) + "мс");
        System.out.println("Запросов к словарю: " 
                + String.valueOf(getCountRequest()) 
                + ". Успешно: " 
                + String.valueOf(getCountSuccess()));
        if (useCache) {
            System.out.println("Cache hit: " 
                    + String.valueOf(getCountCacheHit() + "."
                    + " Cache miss: " 
                    + String.valueOf(getCountCacheMiss())));
            System.out.println("Запросов в секунду: " 
                    + String.valueOf((getCountRequest() 
                    + getCountCacheHit()) * 1000 / (timeEnd 
                    - getTimeDictRead())));
        } else
            System.out.println("Запросов к словарю в секунду: " 
                    + String.valueOf(getCountRequest() * 1000
                    / (timeEnd - getTimeDictRead())));
    }
}