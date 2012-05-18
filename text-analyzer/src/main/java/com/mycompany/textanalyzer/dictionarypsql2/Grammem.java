/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer.dictionarypsql2;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author pavel
 */
public class Grammem {
    private int partOfSpeechId; 
    private int genderId; 
    private String animacy = null;
    private String count = null;
    private int caseId;
    private String aspect = null;
    private String typeOfVerb = null;
    private String typeOfVoice = null;
    private int tenseId;
    private String imperativeMood = null;
    private int typeOfPronounId;
    private String unchanging = null;
    private String shortAd = null;
    private String comparativeAdjective = null;
    private int typeOfNameId;
    private String locativeOrOrganization = null;
    private String qualitativeAdjective = null;
    private String interrogativeRelativeAdverb = null;
    private String noPlural = null;
    private String typo = null;
    private int jargonArchaicProfessionalismId;
    private String abbreviation = null;
    private String impersonalVerb = null;
    
    public Grammem(String line) {
        String[] strings = line.split(" ", 2);
        switch (strings[0].toUpperCase()) {
            case "C": 
                partOfSpeechId = 1;
                break;
            case "П": 
                partOfSpeechId = 2;
                break;
            case "МС": 
                partOfSpeechId = 3;
                break;
            case "Г": 
                partOfSpeechId = 4;
                break;
            case "ПРИЧАСТИЕ": 
                partOfSpeechId = 5;
                break;
            case "ДЕЕПРИЧАСТИЕ": 
                partOfSpeechId = 6;
                break;
            case "ИНФИНИТИВ": 
                partOfSpeechId = 7;
                break;
            case "МС-ПРЕДК": 
                partOfSpeechId = 8;
                break;
            case "МС-П": 
                partOfSpeechId = 9;
                break;
            case "ЧИСЛ": 
                partOfSpeechId = 10;
                break;
            case "ЧИСЛ-П": 
                partOfSpeechId = 11;
                break;
            case "Н": 
                partOfSpeechId = 12;
                break;
            case "ПРЕДК": 
                partOfSpeechId = 13;
                break;
            case "ПРЕДЛ": 
                partOfSpeechId = 14;
                break;
            case "СОЮЗ": 
                partOfSpeechId = 15;
                break;
            case "МЕЖД": 
                partOfSpeechId = 16;
                break;
            case "ЧАСТ": 
                partOfSpeechId = 17;
                break;
            case "ВВОДН": 
                partOfSpeechId = 18;
                break;
            case "КР_ПРИЛ": 
                partOfSpeechId = 19;
                break;
            case "КР_ПРИЧАСТИЕ": 
                partOfSpeechId = 20;
                break;
        }
        if (line.indexOf(" ") > -1) {
            String[] grammems = line.split(",");
            for (String grammem : grammems) {
                switch (grammem) {
                    case "мр":
                        genderId = 1;
                        break;
                    case "жр": 
                        genderId = 2;
                        break;
                    case "ср": 
                        genderId = 3;
                        break;
                    case "од":
                        animacy = "true";
                        break;
                    case "но":
                        animacy = "false";
                        break;
                    case "ед":
                        count = "true";
                        break;
                    case "мн":
                        count = "false";
                        break;
                    case "им":
                        caseId = 1;
                        break;
                    case "рд":
                        caseId = 2;
                        break;
                    case "дт":
                        caseId = 3;
                        break;
                    case "вн":
                        caseId = 4;
                        break;
                    case "тв":
                        caseId = 5;
                        break;
                    case "пр":
                        caseId = 6;
                        break;
                    case "зв":
                        caseId = 7;
                        break;
                    case "2":
                        caseId = 8;
                        break;    
                    case "св":
                        aspect = "true";
                        break;
                    case "нс":
                        aspect = "false";
                        break;
                    case "пе":
                        typeOfVerb = "true";
                        break;
                    case "нп":
                        typeOfVerb = "false";
                        break;
                    case "дст":
                        typeOfVoice = "true";
                        break;
                    case "стр":
                        typeOfVoice = "false";
                        break;
                    case "нст":
                        tenseId = 1;
                        break;
                    case "прш":
                        tenseId = 2;
                        break;
                    case "буд":
                        tenseId = 3;
                        break;
                    case "пвл":
                        imperativeMood = "true";
                        break;
                    case "1л":
                        typeOfPronounId = 1;
                        break;
                    case "2л":
                        typeOfPronounId = 2;
                        break;
                    case "3л":
                        typeOfPronounId = 3;
                        break;
                    case "0":
                        unchanging = "true";
                        break;
                    case "кр":
                        shortAd = "true";
                        break;
                    case "сравн":
                        comparativeAdjective = "true";
                        break;
                    case "имя":
                        typeOfNameId = 1;
                        break;
                    case "фам":
                        typeOfNameId = 2;
                        break;
                    case "отч":
                        typeOfNameId = 3;
                        break;
                    case "лок":
                        locativeOrOrganization = "true";
                        break;
                    case "орг":
                        locativeOrOrganization = "false";
                        break;
                    case "кач":
                        qualitativeAdjective = "true";
                        break;
                    case "вопр":
                        interrogativeRelativeAdverb = "true";
                        break;
                    case "относ":
                        interrogativeRelativeAdverb = "false";
                        break;
                    case "дфст":
                        noPlural = "true";
                        break;
                    case "опч":
                        typo = "true";
                        break;
                    case "жарг":
                        jargonArchaicProfessionalismId = 1;
                        break;
                    case "арх":
                        jargonArchaicProfessionalismId = 2;
                        break;
                    case "проф":
                        jargonArchaicProfessionalismId = 3;
                        break;
                    case "аббр":
                        abbreviation = "true";
                        break;
                    case "безл":
                        impersonalVerb = "true";
                        break;
                }
            }
        }
    }
    
    public PreparedStatement setAll(PreparedStatement grammemInfo){
        try {
            setGrammem(grammemInfo, 3, partOfSpeechId);
            setGrammem(grammemInfo, 4, genderId);
            setGrammem(grammemInfo, 5, animacy);
            setGrammem(grammemInfo, 6, count);
            setGrammem(grammemInfo, 7, caseId);
            setGrammem(grammemInfo, 8, aspect);
            setGrammem(grammemInfo, 9, typeOfVerb);
            setGrammem(grammemInfo, 10, typeOfVoice);
            setGrammem(grammemInfo, 11, tenseId);
            setGrammem(grammemInfo, 12, imperativeMood);
            setGrammem(grammemInfo, 13, typeOfPronounId);
            setGrammem(grammemInfo, 14, unchanging);
            setGrammem(grammemInfo, 15, shortAd);
            setGrammem(grammemInfo, 16, comparativeAdjective);
            setGrammem(grammemInfo, 17, typeOfNameId);
            setGrammem(grammemInfo, 18, locativeOrOrganization);
            setGrammem(grammemInfo, 19, qualitativeAdjective);
            setGrammem(grammemInfo, 20, interrogativeRelativeAdverb);
            setGrammem(grammemInfo, 21, noPlural);
            setGrammem(grammemInfo, 22, typo);
            setGrammem(grammemInfo, 23, jargonArchaicProfessionalismId);
            setGrammem(grammemInfo, 24, abbreviation);
            setGrammem(grammemInfo, 25, impersonalVerb);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return grammemInfo;
        }
    }
    
    private PreparedStatement setGrammem(PreparedStatement grammemInfo, 
        int index, int param) throws SQLException {
        if (param > 0)
            grammemInfo.setInt(index, param);
        else
            grammemInfo.setNull(index, Types.INTEGER);
        return grammemInfo;
    }
    
    private PreparedStatement setGrammem(PreparedStatement grammemInfo, 
        int index, String param) throws SQLException {
        if (param != null)
            grammemInfo.setString(index, param);
        else
            grammemInfo.setNull(index, Types.VARCHAR);
        return grammemInfo;
    }
}
