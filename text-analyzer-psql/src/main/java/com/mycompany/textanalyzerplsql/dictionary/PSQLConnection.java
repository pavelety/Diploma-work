/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzerplsql.dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author pavel
 */
public class PSQLConnection {
    private final String urlPSQL = "jdbc:postgresql:";
    private final String dictionaryDBRus = "dictionaryrus";
    private final String dictionaryDBEng = "dictionaryeng";
    private final String username = "pavel";
    private final String password = "123";
    private Connection connectionRus = null;
    private Connection connectionEng = null;

    public PSQLConnection() {
        try {
             Class.forName("org.postgresql.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection createConnection(String lang) throws SQLException {
        if (lang.equalsIgnoreCase("rus")) {
            connectionRus = DriverManager.getConnection(urlPSQL 
                    + dictionaryDBRus, username, password);
            return connectionRus;
        }
        else if (lang.equalsIgnoreCase("eng")) {
            connectionEng = DriverManager.getConnection(urlPSQL 
                    + dictionaryDBEng, username, password);
            return connectionEng;
        }    
        return null;
    }
}