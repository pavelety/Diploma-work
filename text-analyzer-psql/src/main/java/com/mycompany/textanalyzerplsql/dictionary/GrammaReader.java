package com.mycompany.textanalyzerplsql.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GrammaReader {
    private final String insertAncodes = "insert into ancodes values (?, ?, ?)";
    
    public GrammaReader(String fileName, Connection connect) 
            throws IOException, SQLException {
        PreparedStatement psAncodes = connect.prepareStatement(insertAncodes);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            line = line.trim();//убирает лишние пробелы по краям
            if (!line.startsWith("//") && line.length() > 0) {
                String[] strings = line.split(" ", 4); //делит линию на 4 части
                psAncodes.setString(1, strings[0]);
                psAncodes.setString(2, strings[2]);
                psAncodes.setString(3, (strings.length == 3 ? ""
                        : strings[3]));
                // карта = анкод + часть речи + оставшаяся строка
                psAncodes.executeUpdate();
            }
            line = bufferedReader.readLine();
        }
    }
}