/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzerplsql.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class contain logic how read dictonary and produce word with it all
 * forms.
 */
public class DictionaryReader {

    private String fileName;
    private String fileEncoding = "UTF-8";
    private String insertIntoFlexiamodels = "insert into flexiamodels "
            + "(flexiamodelid, ancode, flexiastr) values (?, ?, ?)";
    private String insertIntoFlexiamodelsids = "insert into flexiamodelsids "
            + "values (?)";
    private String insertIntoLemmata = "insert into lemmata (basestr, "
            + "flexiamodelid) values (?, ?)";
    private Connection connection;
    //private PreparedStatement psFlexiaModels;

    public DictionaryReader(String fileName, Connection connection) 
            throws SQLException, IOException {
        this.fileName = fileName;
        this.connection = connection;
        
        process();
    }

    public void process() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName),
                    fileEncoding));
            readFlexias(bufferedReader);//считывание окончаний
            scipBlock(bufferedReader);//пропуск набора ударений
            scipBlock(bufferedReader); //пропуск изменений
            scipBlock(bufferedReader);//чтение основ
            readWords(bufferedReader);//чтение основ
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void readFlexias(BufferedReader reader) throws IOException, 
            SQLException {
        PreparedStatement psFlexiaModelsIds = connection
                .prepareStatement(insertIntoFlexiamodelsids);
        PreparedStatement psFlexiaModels = connection
                .prepareStatement(insertIntoFlexiamodels);
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++) {
            psFlexiaModelsIds.setInt(1, i);
            psFlexiaModelsIds.addBatch();
            String backAncode = null;
            String backSuffix = null;
            for (String line : reader.readLine().split("\\%")) {
                String[] fl = line.split("\\*");
                if (fl.length == 2) {
                    String ancode = fl[1];
                    String suffix = fl[0].toLowerCase();
                    if (!ancode.equalsIgnoreCase(backAncode) 
                            && !suffix.equalsIgnoreCase(backSuffix)) {
                        psFlexiaModels.setInt(1, i);
                        psFlexiaModels.setString(2, ancode);
                        psFlexiaModels.setString(3, suffix);
                        psFlexiaModels.addBatch();
                        backAncode = ancode;
                        backSuffix = suffix;
                    }
                }     
            }
        }
        try {
            psFlexiaModelsIds.executeBatch();
            psFlexiaModels.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        } finally {
            psFlexiaModelsIds.close();
            psFlexiaModels.close();
        }
    }

    private void scipBlock(BufferedReader reader) throws IOException {
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++)
            reader.readLine();
    }

    private void readWords(BufferedReader reader) throws IOException, 
            SQLException {
        PreparedStatement psLemmata = connection
                .prepareStatement(insertIntoLemmata);
        int count = Integer.valueOf(reader.readLine()); //число строк (псевдооснов)
        String backBase = null;
        int backId = -1;
        for (int i = 0; i < count; i++) {
            String[] wd = reader.readLine().split(" ");
            String wordBase = wd[0].toLowerCase(); //псевдооснова
            int id = Integer.valueOf(wd[1]);
            if (wordBase.startsWith("-") || wordBase.equalsIgnoreCase(backBase) 
                    && backId == id)
                continue;
            wordBase = "#".equals(wordBase) ? "" : wordBase; //псевдооснова пуста
            psLemmata.setString(1, wordBase);
            //номер парадигмы (номер строки в первой секции) - набор флексий (окончаний)
            psLemmata.setInt(2, id);
            psLemmata.addBatch();
            backBase = wordBase;
            backId = id;
            if (i % 6000 == 0)
                try {
                    psLemmata.executeBatch();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getNextException().toString());
                }
        }
        try {
            psLemmata.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        } finally {
            psLemmata.close();
        }
    }
}