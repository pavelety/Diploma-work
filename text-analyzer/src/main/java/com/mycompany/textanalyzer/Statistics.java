/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer;

/**
 *
 * @author pavel
 */
public class Statistics {
    private int countObjDictRequest = 0;
    private int countObjDictSuccess = 0;
    private long timeDictRead;
    private int countCacheHit = 0;
    private int countCacheMiss = 0;
    
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
}