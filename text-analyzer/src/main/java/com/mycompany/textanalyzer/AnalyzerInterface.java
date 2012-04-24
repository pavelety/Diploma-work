/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer;

import java.io.IOException;

/**
 *
 * @author pavel
 */
public interface AnalyzerInterface {
    
    void analyze(Boolean useCache, String textFilePath, String encoding);
    
    void fileAnalyze(String path, String encoding) throws IOException;
    
    int getCountRequest();
    
    int getCountSuccess();
    
    long getTimeDictRead();
    
    int getCountCacheHit();
    
    int getCountCacheMiss();
    
}