package com.mycompany.textanalyzer.psql.v2.dictionary;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Класс граммемы, который преобразует каждую строку, хранящуюся в
 * gramtab.tab, в формат индексов для БД.
 * @author pavel
 */
public class Grammem {
    private int partOfSpeechId; 
    private int genderId; 
    private int animacy;
    private int count;
    private int caseId;
    private int aspect;
    private int typeOfVerb;
    private int typeOfVoice;
    private int tenseId;
    private int imperativeMood;
    private int typeOfPronounId;
    private int unchanging;
    private int shortAd;
    private int comparativeAdjective;
    private int typeOfNameId;
    private int locativeOrOrganization;
    private int qualitativeAdjective;
    private int interrogativeRelativeAdverb;
    private int noPlural;
    private int typo;
    private int jargonArchaicProfessionalismId;
    private int abbreviation;
    private int impersonalVerb;
    private int pnFormId;
    private int adDegreeId;
    private int typeOfPersonId;
    private int countId;
    private int pnTypeId;
    private int commonName;
    private int geographical;
    private int properName;
    private int plsgId;
    private int name;
    private int organization;
    private String lang;
    
    public Grammem(String lang, String line) {
        this.lang = lang;
        String[] strings = line.split(" ", 2);
        if (lang.equalsIgnoreCase("rus"))
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
        else
            switch (strings[0].toUpperCase()) {
                case "ADJ": 
                    partOfSpeechId = 1;
                    break;
                case "ADV": 
                    partOfSpeechId = 2;
                    break;
                case "VERB": 
                    partOfSpeechId = 3;
                    break;
                case "VBE": 
                    partOfSpeechId = 4;
                    break;
                case "MOD": 
                    partOfSpeechId = 5;
                    break;
                case "NUMERAL": 
                    partOfSpeechId = 6;
                    break;
                case "ORDNUM": 
                    partOfSpeechId = 7;
                    break;
                case "CONJ": 
                    partOfSpeechId = 8;
                    break;
                case "INT": 
                    partOfSpeechId = 9;
                    break;
                case "PREP": 
                    partOfSpeechId = 10;
                    break;
                case "PART": 
                    partOfSpeechId = 11;
                    break;
                case "ART": 
                    partOfSpeechId = 12;
                    break;
                case "NOUN": 
                    partOfSpeechId = 13;
                    break;
                case "PN": 
                    partOfSpeechId = 14;
                    break;
                case "PRON": 
                    partOfSpeechId = 15;
                    break;
                case "PN_ADJ": 
                    partOfSpeechId = 16;
                    break;
                case "POSS": 
                    partOfSpeechId = 17;
                    break;
            }
        if (line.indexOf(" ") > -1) {
            String[] grammems = line.split(",");
            if (lang.equalsIgnoreCase("rus"))
                for (String grammem : grammems)
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
                            animacy = 1;
                            break;
                        case "но":
                            animacy = 2;
                            break;
                        case "ед":
                            count = 1;
                            break;
                        case "мн":
                            count = 2;
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
                            aspect = 1;
                            break;
                        case "нс":
                            aspect = 2;
                            break;
                        case "пе":
                            typeOfVerb = 1;
                            break;
                        case "нп":
                            typeOfVerb = 2;
                            break;
                        case "дст":
                            typeOfVoice = 1;
                            break;
                        case "стр":
                            typeOfVoice = 2;
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
                            imperativeMood = 1;
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
                            unchanging = 1;
                            break;
                        case "кр":
                            shortAd = 1;
                            break;
                        case "сравн":
                            comparativeAdjective = 1;
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
                            locativeOrOrganization = 1;
                            break;
                        case "орг":
                            locativeOrOrganization = 2;
                            break;
                        case "кач":
                            qualitativeAdjective = 1;
                            break;
                        case "вопр":
                            interrogativeRelativeAdverb = 1;
                            break;
                        case "относ":
                            interrogativeRelativeAdverb = 2;
                            break;
                        case "дфст":
                            noPlural = 1;
                            break;
                        case "опч":
                            typo = 1;
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
                            abbreviation = 1;
                            break;
                        case "безл":
                            impersonalVerb = 1;
                            break;
                    }
            else
                for (String grammem : grammems)
                    switch (grammem) {
                        case "pred":
                            pnFormId = 1;
                            break;
                        case "attr":
                            pnFormId = 2;
                            break;
                        case "pos":
                            adDegreeId = 1;
                            break;
                        case "comp":
                            adDegreeId = 2;
                            break;
                        case "sup":
                            adDegreeId = 3;
                            break;
                        case "inf":
                            tenseId = 1;
                            break;
                        case "prsa":
                            tenseId = 2;
                            break;
                        case "pasa":
                            tenseId = 3;
                            break;
                        case "pp":
                            tenseId = 4;
                            break;
                        case "ing":
                            tenseId = 5;
                            break;
                        case "fut":
                            tenseId = 6;
                            break;
                        case "if":
                            tenseId = 7;
                            break;
                        case "1":
                            typeOfPersonId = 1;
                            break;
                        case "2":
                            typeOfPersonId = 2;
                            break;
                        case "3":
                            typeOfPersonId = 3;
                            break;
                        case "sg":
                            countId = 1;
                            break;
                        case "pl":
                            countId = 2;
                            break;
                        case "uncount":
                            countId = 3;
                            break;
                        case "mass":
                            countId = 4;
                            break;
                        case "pers":
                            pnTypeId = 1;
                            break;
                        case "poss":
                            pnTypeId = 2;
                            break;
                        case "ref":
                            pnTypeId = 3;
                            break;
                        case "dem":
                            pnTypeId = 4;
                            break;
                        case "nom":
                            caseId = 1;
                            break;
                        case "obj":
                            caseId = 2;
                            break;
                        case "m":
                            genderId = 1;
                            break;
                        case "f":
                            genderId = 2;
                            break;
                        case "narr":
                            commonName = 1;
                            break;
                        case "geo":
                            geographical = 1;
                            break;
                        case "prop":
                            properName = 1;
                            break;
                        case "plsq":
                            plsgId = 1;
                            break;
                        case "plsgs":
                            plsgId = 2;
                            break;
                        case "name":
                            name = 1;
                            break;
                        case "org":
                            organization = 1;
                            break;
                    }
        }
    }
    
    public PreparedStatement setAll(PreparedStatement grammemInfo) {
        try {
            grammemInfo.setInt(3, partOfSpeechId);
            if (lang.equalsIgnoreCase("rus")) {
                grammemInfo.setInt(4, genderId);
                grammemInfo.setInt(5, animacy);
                grammemInfo.setInt(6, count);
                grammemInfo.setInt(7, caseId);
                grammemInfo.setInt(8, aspect);
                grammemInfo.setInt(9, typeOfVerb);
                grammemInfo.setInt(10, typeOfVoice);
                grammemInfo.setInt(11, tenseId);
                grammemInfo.setInt(12, imperativeMood);
                grammemInfo.setInt(13, typeOfPronounId);
                grammemInfo.setInt(14, unchanging);
                grammemInfo.setInt(15, shortAd);
                grammemInfo.setInt(16, comparativeAdjective);
                grammemInfo.setInt(17, typeOfNameId);
                grammemInfo.setInt(18, locativeOrOrganization);
                grammemInfo.setInt(19, qualitativeAdjective);
                grammemInfo.setInt(20, interrogativeRelativeAdverb);
                grammemInfo.setInt(21, noPlural);
                grammemInfo.setInt(22, typo);
                grammemInfo.setInt(23, 
                        jargonArchaicProfessionalismId);
                grammemInfo.setInt(24, abbreviation);
                grammemInfo.setInt(25, impersonalVerb);
            } else {
                grammemInfo.setInt(4, genderId);
                grammemInfo.setInt(5, countId);
                grammemInfo.setInt(6, caseId);
                grammemInfo.setInt(7, pnFormId);
                grammemInfo.setInt(8, adDegreeId);
                grammemInfo.setInt(9, tenseId);
                grammemInfo.setInt(10, typeOfPersonId);
                grammemInfo.setInt(11, pnTypeId);
                grammemInfo.setInt(12, commonName);
                grammemInfo.setInt(13, geographical);
                grammemInfo.setInt(14, properName);
                grammemInfo.setInt(15, plsgId);
                grammemInfo.setInt(16, name);
                grammemInfo.setInt(17, organization);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return grammemInfo;
        }
    }
}