package com.mycompany.textanalyzer.analyzerpsql3;

import com.mycompany.textanalyzer.dictionarypsql3.DBWriter;
import com.mycompany.textanalyzer.dictionarypsql3.PSQLConnection;
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
            //initiateDictionaries(connectionRus, "rus", rmorphs, 
            //        rgramtab);
            connectionEng = connect.createConnection("eng");
            //initiateDictionaries(connectionEng, "eng", emorphs, 
            //        egramtab);
        //} catch (IOException ex) {
        //    ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }    
    }
    
    private void initiateDictionaries(Connection connection, 
            String lang, String morphs, String gramtab) 
            throws IOException, SQLException {
        cleanTables(connection);
        new DBWriter(connection, lang, morphs, gramtab);
    }
    
    public Connection getConnectionRus() {
        return connectionRus;
    }
    
    public Connection getConnectionEng() {
        return connectionEng;
    }
    
    private void cleanTables(Connection connection) 
            throws SQLException {
        connection.prepareStatement("alter sequence auto_id_bases "
                + "restart with 1").executeUpdate();
        connection.prepareStatement("alter sequence "
                + "auto_id_suffixes restart with 1")
                .executeUpdate();
        connection.prepareStatement("alter sequence "
                + "auto_id_ancodes restart with 1")
                .executeUpdate();
        connection.prepareStatement("alter sequence "
                + "auto_id_flexiamodels restart with 1")
                .executeUpdate();
        connection.prepareStatement("alter sequence "
                + "auto_id_lexems restart with 1")
                .executeUpdate();
        connection.prepareStatement("delete from lexems")
                .executeUpdate();
        connection.prepareStatement("delete from flexiamodels")
                .executeUpdate();
        connection.prepareStatement("delete from paradigmnumber")
                .executeUpdate();
        connection.prepareStatement("delete from ancodes")
                .executeUpdate();        
        connection.prepareStatement("delete from bases")
                .executeUpdate();
        connection.prepareStatement("delete from suffixes")
                .executeUpdate();
        System.out.println("Tables cleaned.");
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