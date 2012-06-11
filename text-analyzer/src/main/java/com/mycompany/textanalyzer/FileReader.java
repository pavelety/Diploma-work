package com.mycompany.textanalyzer;

import java.io.*;

/**
 * Класс, считавающий построчно файл
 * @author pavel
 */
public class FileReader {
    private BufferedReader bufferedReader;
    
    public FileReader(String textFilePath, String encoding){
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(textFilePath), encoding));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
    
    public String getLine() {
        try {
            if (bufferedReader.ready())
                return bufferedReader.readLine();
            else
                return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}