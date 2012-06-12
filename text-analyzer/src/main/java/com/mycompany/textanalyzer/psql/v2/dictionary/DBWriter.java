package com.mycompany.textanalyzer.psql.v2.dictionary;

import com.mycompany.textanalyzer.dictionary.FlexiaModel;
import com.mycompany.textanalyzer.dictionary.WordCard;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Класс, который записывает словарь в БД
 * @author pavel
 */
public class DBWriter {
    private Connection connection;
    private final String insertIntoBases = "insert into bases "
            + "(baseStr) values (?)";
    private final String insertIntoSuffixes = "insert into suffixes"
            + " (suffix) values (?)";
    private final String insertIntoRusGrammemInfo = "insert into "
            + "grammemInfo (basestrid, suffixid, partofspeechid, "
            + "genderid, animacyid, countid, caseid, aspectid, "
            + "typeofverbid, typeofvoiceid, tenseid, imperativemood"
            + ", typeofpronounid, unchanging, shortadid, "
            + "comparativeadjective, typeofnameid, "
            + "locativeororganizationid, qualitativeadjective, "
            + "interrogativerelativeadverbid, noplural, typo, "
            + "jargonarchaicprofessionalismid, abbreviation, "
            + "impersonalverb) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
            + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String insertIntoEngGrammemInfo = "insert into "
            + "grammemInfo (basestrid, suffixid, partofspeechid, "
            + "genderid, countId, caseId, PNFormId, AdDegreeId, "
            + "tenseId, typeOfPersonId , PNTypeId, commonName, "
            + "geographical, properName , plsgId , name, "
            + "organization) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?, ?)";
    
    private Map<String, WordCard> dictionary; 
    private Map<String, Grammem> grammaDictionary;
    private Map<String, Integer> suffixes;
    private String lang;
    
    public DBWriter(Connection connection, String lang, 
            String morphs, String gramtab) {
        try {
            DictionaryReader dict = new DictionaryReader(morphs);
            System.out.println("Dictionary (words) loaded.");
            dictionary = dict.getMainDictionary();
            suffixes = dict.getSuffixes();
            grammaDictionary = new GrammaReader(lang, gramtab)
                    .getGrammemDictionary();
            System.out.println("Dictionary (grammems) loaded.");
            this.connection = connection;
            this.lang = lang;
            writeSuffixes();
            System.out.println("Suffixes ready.");
            writeBasesAndGrammemInfo();
            System.out.println("GrammemInfo ready.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void writeSuffixes() throws IOException, SQLException {
        PreparedStatement psSuffixes = 
                connection.prepareStatement(insertIntoSuffixes);
        int suffixId = 0;
        for (Entry<String, Integer> suffix : suffixes.entrySet()){
            try {
                suffixId++;
                suffix.setValue(suffixId);
                psSuffixes.setString(1, suffix.getKey());
                psSuffixes.addBatch();
             } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException()
                        .toString());
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
    
    private void writeBasesAndGrammemInfo() throws IOException,
            SQLException {
        PreparedStatement psBases = 
                connection.prepareStatement(insertIntoBases);
        PreparedStatement psGrammemInfo;
        if (lang.equalsIgnoreCase("rus"))
            psGrammemInfo = connection.prepareStatement(
                    insertIntoRusGrammemInfo);
        else
            psGrammemInfo = connection.prepareStatement(
                    insertIntoEngGrammemInfo);
        int wordBaseId = 0;
        for (Map.Entry<String, WordCard> wordWithCard 
                : dictionary.entrySet()) {
            String wordBase = wordWithCard.getKey();
            List<FlexiaModel> wordForms = 
                    wordWithCard.getValue().getWordsForms();
            wordBaseId++;
            Iterator formsIterator = wordForms.listIterator();
            try {
                psBases.setString(1, wordBase);
                psBases.addBatch();
                while (formsIterator.hasNext()) {
                    FlexiaModel form = (FlexiaModel) 
                            formsIterator.next();
                    psGrammemInfo.setInt(1, wordBaseId);
                    psGrammemInfo.setInt(2, 
                            suffixes.get(form.getSuffix()));
                    grammaDictionary.get(form.getCode())
                            .setAll(psGrammemInfo).addBatch();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException()
                        .toString());
            }
            if (wordBaseId % 1000 == 0)
                try {
                    psBases.executeBatch();
                    psGrammemInfo.executeBatch();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getNextException()
                            .toString());
                }
        }
        try {
            psBases.executeBatch();
            psGrammemInfo.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        } finally {
            psBases.close();
        }
    }
}