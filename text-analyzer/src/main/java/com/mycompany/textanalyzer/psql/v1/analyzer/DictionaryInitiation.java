/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer.psql.v1.analyzer;

import com.mycompany.textanalyzer.psql.v1.dictionary
        .DictionaryReader;
import com.mycompany.textanalyzer.psql.v1.dictionary.GrammaReader;
import com.mycompany.textanalyzer.psql.v1.dictionary.PSQLConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Класс, инициирующий копирование словарей в БД словарей
 * @author pavel
 */
public class DictionaryInitiation {
    private final String egramtab = "src/main/resources/dicts/eng/"
            + "egramtab.tab";
    private final String emorphs = "src/main/resources/dicts/eng/"
            + "morphs.mrd";
    private final String rgramtab = "src/main/resources/dicts/rus/"
            + "rgramtab.tab";
    private final String rmorphs = "src/main/resources/dicts/rus/"
            + "morphs.mrd";
    private Connection connectionRus;
    private Connection connectionEng;
    
    protected DictionaryInitiation(){
        PSQLConnection connect = new PSQLConnection();
        try {
            connectionRus = connect.createConnection("rus");
            //initiateRusDictionaries();
            connectionEng = connect.createConnection("eng");
            //initiateEngDictionaries();
//        } catch (IOException ex) {
//            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }    
    }
    
    private void initiateRusDictionaries() throws IOException, 
            SQLException {
        connectionRus.prepareStatement("delete from lemmata")
                .executeUpdate();
        connectionRus.prepareStatement("delete from flexiamodels")
                .executeUpdate();
        connectionRus.prepareStatement("delete from "
                + "flexiamodelsids").executeUpdate();
        connectionRus.prepareStatement("delete from ancodes")
                .executeUpdate();
        new GrammaReader(rgramtab, connectionRus);
        new DictionaryReader(rmorphs, connectionRus);
    }
    
    private void initiateEngDictionaries() throws IOException, 
            SQLException {
        connectionEng.prepareStatement("delete from lemmata")
                .executeUpdate();
        connectionEng.prepareStatement("delete from flexiamodels")
                .executeUpdate();
        connectionEng.prepareStatement("delete from "
                + "flexiamodelsids").executeUpdate();
        connectionEng.prepareStatement("delete from ancodes")
                .executeUpdate();
        new GrammaReader(egramtab, connectionEng);
        new DictionaryReader(emorphs, connectionEng);
    }
    
    public Connection getConnectionRus() {
        return connectionRus;
    }
    
    public Connection getConnectionEng() {
        return connectionEng;
    }
    
    public void closeConnections() {
        try {
            connectionRus.close();
            connectionEng.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}