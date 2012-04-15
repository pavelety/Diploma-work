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
            + "flexiamodelid) values (?,?)";
    private Connection connection;
    private PreparedStatement psFlexiaModels;

    public DictionaryReader(String fileName, Connection connection) 
            throws SQLException, IOException {
        this.fileName = fileName;
        this.connection = connection;
        psFlexiaModels = connection.prepareStatement(insertIntoFlexiamodels);
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
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++) {
            psFlexiaModelsIds.setInt(1, i);
            psFlexiaModelsIds.executeUpdate();
            for (String line : reader.readLine().split("\\%")) {
                addFlexia(i, line);
            }
        }
    }

    private void addFlexia(int i, String line) throws SQLException{
        String[] fl = line.split("\\*");
        if (fl.length == 2) {
            psFlexiaModels.setInt(1, i);
            psFlexiaModels.setString(2, fl[1]);
            psFlexiaModels.setString(3, fl[0].toLowerCase()); //анкод, суффикс
            try {
                psFlexiaModels.executeUpdate();
            } catch (SQLException ex) {
                //пропуск ошибки
            }
        }
    }

    private void scipBlock(BufferedReader reader) throws IOException {
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++)
            reader.readLine();
    }

    private void readWords(BufferedReader reader) throws IOException, SQLException {
        PreparedStatement psLemmata = connection
                .prepareStatement(insertIntoLemmata);
        int count = Integer.valueOf(reader.readLine()); //число строк (псевдооснов)
        for (int i = 0; i < count; i++) {
            String[] wd = reader.readLine().split(" ");
            String wordBase = wd[0].toLowerCase(); //псевдооснова
            if (wordBase.startsWith("-"))
                continue;
            wordBase = "#".equals(wordBase) ? "" : wordBase; //псевдооснова пуста
            psLemmata.setString(1, wordBase);
            //номер парадигмы (номер строки в первой секции) - набор флексий (окончаний)
            psLemmata.setInt(2, Integer.valueOf(wd[1]));
            try {
                psLemmata.executeUpdate();
            } catch (SQLException ex) {
                //пропуск повторов
            }
        }
    }
}