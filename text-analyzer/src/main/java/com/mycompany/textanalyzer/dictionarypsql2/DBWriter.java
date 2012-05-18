/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer.dictionarypsql2;

import com.mycompany.textanalyzer.dictionary.FlexiaModel;
import com.mycompany.textanalyzer.dictionary.WordCard;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pavel
 */
public class DBWriter {
    private Connection connection;
    private final String insertIntoBases = "insert into bases (baseStr) values "
            + "(?)";
    private final String insertIntoSuffixes = "insert into suffixes (suffix) "
            + "values (?)";
    private final String insertIntoGrammemInfo = "insert into grammemInfo "
            + "(basestrid, suffixid, partofspeechid, genderid, animacyid, "
            + "countid, caseid, aspectid, typeofverbid, typeofvoiceid, tenseid,"
            + " imperativemood, typeofpronounid, unchanging, shortadid, "
            + "comparativeadjective, typeofnameid, locativeororganizationid, "
            + "qualitativeadjective, interrogativerelativeadverbid, noplural, "
            + "typo, jargonarchaicprofessionalismid, abbreviation, "
            + "impersonalverb) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private Map<String, WordCard> dictionary; 
    private Map<String, Grammem> grammaDictionary;
    private List<List<FlexiaModel>> wordsFlexias;
    private Map<Integer, String> suffixes = new HashMap<Integer, String>();
    
    public DBWriter(String morphs, String gramtab, Connection connection) {
        try {
            DictionaryReader dict = new DictionaryReader(morphs);
            dictionary = dict.getMainDictionary();
            wordsFlexias = dict.getWordsFlexias();
            grammaDictionary = new GrammaReader(gramtab).getGrammemDictionary();
            this.connection = connection;
            writeSuffixes();
            writeBases();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void writeSuffixes() throws IOException, SQLException {
        PreparedStatement psSuffixes = 
                connection.prepareStatement(insertIntoSuffixes);
        Iterator flexiaListIterator = wordsFlexias.iterator();
        List<FlexiaModel> flexiaList;
        while (flexiaListIterator.hasNext()) {
             flexiaList = (List<FlexiaModel>) flexiaListIterator.next();
             Iterator flexiaIterator = flexiaList.iterator();
             while (flexiaIterator.hasNext()) {
                FlexiaModel flexia = (FlexiaModel) flexiaIterator.next();
                String suffix = flexia.getSuffix();
                int id = 1;
                if (!suffixes.containsValue(suffix)) {  
                    try {
                        psSuffixes.setString(1, suffix);
                        psSuffixes.addBatch();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println(ex.getNextException().toString());
                    }
                    suffixes.put(id, suffix);
                    id++;
                }
             }
        }
        try {
            psSuffixes.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        } finally {
            psSuffixes.close();
        }
    }

    private void writeBases() throws IOException, SQLException {
        PreparedStatement psBases = 
                connection.prepareStatement(insertIntoBases);
        PreparedStatement psGrammemInfo = 
                connection.prepareStatement(insertIntoGrammemInfo);
        int id = 1;
        for (Map.Entry<String, WordCard> wordWithCard : dictionary.entrySet()) {
            String wordBase = wordWithCard.getKey();
            List<FlexiaModel> wordForms = 
                    wordWithCard.getValue().getWordsForms();
            id++;
            Iterator formsIterator = wordForms.listIterator();
            try {
                psBases.setString(1, wordBase);
                psBases.addBatch();
                while (formsIterator.hasNext()) {
                    FlexiaModel form = (FlexiaModel) formsIterator.next();
                    psGrammemInfo.setString(1, wordBase);
                    psGrammemInfo.setString(2, form.getSuffix());
                    grammaDictionary.get(form.getCode()).setAll(psGrammemInfo);
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException().toString());
            }
            if (id % 6000 == 0)
                try {
                    psBases.executeBatch();
                    psGrammemInfo.executeBatch();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getNextException().toString());
                }
        }
        try {
            psBases.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        } finally {
            psBases.close();
        }
    }
}
