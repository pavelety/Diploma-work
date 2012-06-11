package com.mycompany.textanalyzer.dictionarypsql3;

import com.mycompany.textanalyzer.dictionary.FlexiaModel;
import com.mycompany.textanalyzer.dictionary.WordCard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
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
    private final String insertIntoParadigmNumber = "insert into paradigmnumber"
            + " (id) values (?)";
    private final String insertIntoAncodesRus = "insert into "
            + "ancodes (ancode, partofspeechid, "
            + "genderid, animacyid, countid, caseid, aspectid, "
            + "typeofverbid, typeofvoiceid, tenseid, imperativemood"
            + ", typeofpronounid, unchanging, shortadid, "
            + "comparativeadjective, typeofnameid, "
            + "locativeororganizationid, qualitativeadjective, "
            + "interrogativerelativeadverbid, noplural, typo, "
            + "jargonarchaicprofessionalismid, abbreviation, "
            + "impersonalverb) values (?, ?, ?, ?, ?, ?, ?, ?, ?"
            + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String insertIntoAncodesEng = "insert into "
            + "ancodes (ancode, partofspeechid, "
            + "genderid, countId, caseId, PNFormId, AdDegreeId, "
            + "tenseId, typeOfPersonId , PNTypeId, commonName, "
            + "geographical, properName , plsgId , name, "
            + "organization) values (?, ?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?, ?)";
    private final String insertIntoFlexiaModels = "insert into "
            + "flexiamodels (paradigmid, ancodeid, suffixid) values"
            + " (?, ?, ?)";
    private final String insertIntoLexems = "insert into "
            + "lexems (basestrid, paradigmid) values (?, ?)";
    private Map<String, WordCard> dictionary; 
    private List<List<FlexiaModel>> flexiaModels;
    private Map<String, Grammem> grammaDictionary;
    private Map<String, Integer> suffixes;
    private Map<String, Integer> ancodes = new HashMap<String, Integer>();
    private String lang;
    
    public DBWriter(Connection connection, String lang, 
            String morphs, String gramtab) {
            DictionaryReader dict = new DictionaryReader(morphs);
            System.out.println("Dictionary (words) loaded.");
            dictionary = dict.getMainDictionary();
            flexiaModels = dict.getWordsFlexias();
            suffixes = dict.getSuffixes();
            grammaDictionary = new GrammaReader(lang, gramtab)
                    .getGrammemDictionary();
            System.out.println("Dictionary (grammems) loaded.");
            this.connection = connection;
            this.lang = lang;
            writeSuffixes();
            System.out.println("Suffixes are ready.");
            writeAncodes();
            System.out.println("Ancodes are ready.");
            writeFlexiaModelsAndParadigmNumber();
            System.out.println("FlexiaModels and ParadigmNumber are ready.");
            writeBasesAndLexems();
            System.out.println("Lexems are ready.");
    }
    
    private void writeSuffixes() {
        try {
            PreparedStatement psSuffixes = 
                    connection.prepareStatement(insertIntoSuffixes);
            int suffixId = 0;
            try {
                for (Entry<String, Integer> suffix : suffixes.entrySet()){
                    suffixId++;
                    suffix.setValue(suffixId);
                    psSuffixes.setString(1, suffix.getKey());
                    psSuffixes.addBatch();
                }
                psSuffixes.executeBatch();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException().toString());
            } finally {
                psSuffixes.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        }
    }
    
    private void writeAncodes() {
        try {
            PreparedStatement psAncodes;
            if (lang.equalsIgnoreCase("rus"))
                psAncodes = connection.prepareStatement(insertIntoAncodesRus);
            else
                psAncodes = connection.prepareStatement(insertIntoAncodesEng);
            String ancode;
            int ancodeId = 0;
            try {
                for (Entry<String, Grammem> grammemEntry : grammaDictionary.entrySet()) {
                    ancodeId++;
                    ancode = grammemEntry.getKey();
                    ancodes.put(ancode, ancodeId);
                    psAncodes.setString(1, ancode);
                    grammemEntry.getValue().setAll(psAncodes).addBatch();
                }
                psAncodes.executeBatch();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException().toString());
            } finally {
                psAncodes.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        }
    }
    
    private void writeFlexiaModelsAndParadigmNumber() {
        try {
            PreparedStatement psFlexiaModels = 
                    connection.prepareStatement(insertIntoFlexiaModels);
            PreparedStatement psParadigmNumber = 
                    connection.prepareStatement(insertIntoParadigmNumber);
            Iterator<List<FlexiaModel>> fm = flexiaModels.listIterator();
            int paradigmId = 0;
            FlexiaModel flexia = null;
            try {
                while (fm.hasNext()) {
                    psParadigmNumber.setInt(1, paradigmId);
                    psParadigmNumber.addBatch();
                    Iterator<FlexiaModel> fmm = fm.next().listIterator();
                    while (fmm.hasNext()) {
                        flexia = fmm.next();
                        psFlexiaModels.setInt(1, paradigmId);
                        psFlexiaModels.setInt(2, ancodes.get(flexia.getCode()));
                        psFlexiaModels.setInt(3, suffixes.get(flexia.getSuffix()));
                        psFlexiaModels.addBatch();
                    }
                    if (paradigmId % 500 == 0) {
                        psParadigmNumber.executeBatch();
                        psFlexiaModels.executeBatch();
                    }
                    paradigmId++;
                }
                psParadigmNumber.executeBatch();
                psFlexiaModels.executeBatch();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException().toString());
            } finally {
                psParadigmNumber.close();
                psFlexiaModels.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        }
    }
    
    private void writeBasesAndLexems() {
        try {
            PreparedStatement psLexems = 
                    connection.prepareStatement(insertIntoLexems);
            PreparedStatement psBases = 
                    connection.prepareStatement(insertIntoBases);
            int wordBaseId = 0;
            try {
                for (Map.Entry<String, WordCard> wordWithCard 
                            : dictionary.entrySet()) {
                    String wordBase = wordWithCard.getKey();
                    wordBaseId++;
                    psBases.setString(1, wordBase);
                    psBases.addBatch();
                    psLexems.setInt(1, wordBaseId);
                    psLexems.setInt(2, wordWithCard.getValue().getParadigmId());
                    psLexems.addBatch();
                    if (wordBaseId % 6000 == 0) {
                        psBases.executeBatch();
                        psLexems.executeBatch();
                    }    
                }
                psBases.executeBatch();
                psLexems.executeBatch();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getNextException().toString());
            } finally {
                psBases.close();
                psLexems.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getNextException().toString());
        }
    }
}