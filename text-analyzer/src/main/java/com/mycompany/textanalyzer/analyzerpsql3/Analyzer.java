package com.mycompany.textanalyzer.analyzerpsql3;

import com.mycompany.textanalyzer.AnalyzerInterface;
import com.mycompany.textanalyzer.Statistics;
import com.mycompany.textanalyzer.Tokenizer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс анализатора: делает морфологический анализ слова, используя
 * БД (4 слова в секунду)
 * @author pavel
 */
public class Analyzer implements AnalyzerInterface {
    private final String selectGrammemId = "select lexems.id from "
            + "lexems, bases, suffixes, flexiamodels, ancodes where"
            + " ? like bases.baseStr||'%' and lexems.baseStrId="
            + "bases.id and bases.baseStr||suffixes.suffix=? and "
            + "lexems.paradigmid=flexiamodels.paradigmid and "
            + "flexiamodels.suffixid=suffixes.id and "
            + "flexiamodels.ancodeid=ancodes.id";
    private final String selectLexemEng = "select bases.basestr, "
            + "suffixes.suffix, partsOfSpeeches.type"/*
            + ", genders.type, count.type, cases.type, PNForms.type, "
            + "AdDegrees.type, tenses.type, typesOfPerson.type, "
            + "PNTypes.type, commonName.type, geographical.type, "
            + "properName.type, plsg.type, name.type, "
            +  "organization.type"*/
            + " from ancodes, lexems, flexiamodels, bases, suffixes"
            + ", partsOfSpeeches" /*
            + ", genders, count, cases, PNForms, AdDegrees, tenses, "
            + "typesOfPerson, PNTypes, commonName, geographical, "
            + "properName, plsg, name, organization"*/
            + " where bases.id=? and lexems.baseStrId=bases.id and "
            + "bases.basestr||suffixes.suffix=? and lexems.paradigmid="
            + "flexiamodels.paradigmid and flexiamodels.suffixid=suffixes.id "
            + "and flexiamodels.ancodeid=ancodes.id and "
            + "ancodes.partOfSpeechId=partsOfSpeeches.id"/*
            + "and ancodes.genderId=genders.id and ancodes.countId="
            + "count.id and ancodes.caseId=cases.id and "
            + "ancodes.PNFormId=PNForms.id and ancodes.AdDegreeId="
            + "AdDegrees.id and ancodes.tenseId=tenses.id and "
            + "ancodes.typeOfPersonId=typesOfPerson.id and "
            + "ancodes.PNTypeId=PNTypes.id and ancodes.commonName="
            + "commonName.id and ancodes.geographical="
            + "geographical.id and ancodes.properName=properName.id"
            + " and ancodes.plsgId=plsg.id and ancodes.name=name.id"
            + " and ancodes.organization=organization.id;"*/;
            /*"select bases.basestr, suffixes.suffix, "
            + "partsOfSpeeches.type, genders.type, count.type, "
            + "cases.type, PNForms.type, AdDegrees.type, "
            + "tenses.type, typesOfPerson.type, PNTypes.type, "
            + "commonName.type, geographical.type, properName.type,"
            + " plsg.type, name.type, organization.type from "
            + "ancodes, lexems, flexiamodels, bases, suffixes, "
            + "partsOfSpeeches, genders, count, cases, PNForms, "
            + "AdDegrees, tenses, typesOfPerson, PNTypes, "
            + "commonName, geographical, properName, plsg, name, "
            + "organization where ? like bases.baseStr||'%' and "
            + "lexems.baseStrId=bases.id and bases.baseStr||"
            + "suffixes.suffix=? and lexems.paradigmid="
            + "flexiamodels.paradigmid and flexiamodels.suffixid="
            + "suffixes.id and flexiamodels.ancodeid=ancodes.id and"
            + " ancodes.partOfSpeechId=partsOfSpeeches.id and "
            + "ancodes.genderId=genders.id and ancodes.countId="
            + "count.id and ancodes.caseId=cases.id and "
            + "ancodes.PNFormId=PNForms.id and ancodes.AdDegreeId="
            + "AdDegrees.id and ancodes.tenseId=tenses.id and "
            + "ancodes.typeOfPersonId=typesOfPerson.id and "
            + "ancodes.PNTypeId=PNTypes.id and ancodes.commonName="
            + "commonName.id and ancodes.geographical="
            + "geographical.id and ancodes.properName=properName.id"
            + " and ancodes.plsgId=plsg.id and ancodes.name=name.id"
            + " and ancodes.organization=organization.id";*/
    private final String selectLexemRus = "select bases.basestr, "
            + "suffixes.suffix, partsOfSpeeches.type"/*
            + ", genders.type, animacy.type, count.type, cases.type"
            + ", aspects.type, typesOfVerbs.type, "
            + "typesOfVoices.type, tenses.type, imperativeMood.type"
            + ", typesOfPronouns.type, unchanging.type, "
            + "shortAd.type, comparativeAdjective.type, "
            + "typesOfNames.type, locativeOrOrganization.type, "
            + "qualitativeAdjective.type, "
            + "interrogativeRelativeAdverb.type, noPlural.type, "
            + "typo.type, jargonArchaicProfessionalism.type, "
            + "abbreviation.type, impersonalVerb.type"*/
            + " from ancodes, lexems, bases, suffixes, flexiamodels"
            + ", partsOfSpeeches"/*
            + ", genders, animacy, count, cases, aspects, "
            + "typesOfVerbs, typesOfVoices, tenses, imperativeMood,"
            + " typesOfPronouns, unchanging, shortAd, "
            + "comparativeAdjective, typesOfNames, "
            + "locativeOrOrganization, qualitativeAdjective, "
            + "interrogativeRelativeAdverb, noPlural, typo, "
            + "jargonArchaicProfessionalism, abbreviation, "
            + "impersonalVerb"*/
            + " where bases.id=? and lexems.baseStrId=bases.id and "
            + "bases.basestr||suffixes.suffix=? and "
            + "lexems.paradigmid=flexiamodels.paradigmid and "
            + "flexiamodels.suffixid=suffixes.id and "
            + "flexiamodels.ancodeid=ancodes.id and "
            + "ancodes.partOfSpeechId=partsOfSpeeches.id"/*
            + " and ancodes.genderId=genders.id and "
            + "ancodes.animacyId=animacy.id and ancodes.countId="
            + "count.id and ancodes.caseId=cases.id and "
            + "ancodes.aspectId=aspects.id and ancodes.typeOfVerbId"
            + "=typesOfVerbs.id and ancodes.typeOfVoiceId="
            + "typesOfVoices.id and ancodes.tenseId=tenses.id and "
            + "ancodes.imperativeMood=imperativeMood.id and "
            + "ancodes.typeOfPronounId=typesOfPronouns.id and "
            + "ancodes.unchanging=unchanging.id and "
            + "ancodes.shortAdId=shortAd.id and "
            + "ancodes.comparativeAdjective=comparativeAdjective.id"
            + " and ancodes.typeOfNameId=typesOfNames.id and "
            + "ancodes.locativeOrOrganizationId="
            + "locativeOrOrganization.id and "
            + "ancodes.qualitativeAdjective=qualitativeAdjective.id"
            + " and ancodes.interrogativeRelativeAdverbId="
            + "interrogativeRelativeAdverb.id and ancodes.noPlural="
            + "noPlural.id and ancodes.typo=typo.id and "
            + "ancodes.jargonArchaicProfessionalismId="
            + "jargonArchaicProfessionalism.id and "
            + "ancodes.abbreviation=abbreviation.id and "
            + "ancodes.impersonalVerb=impersonalVerb.id"*/;
            /*"select bases.basestr, suffixes.suffix, "
            + "partsOfSpeeches.type, genders.type, animacy.type, "
            + "count.type, cases.type, aspects.type, "
            + "typesOfVerbs.type, typesOfVoices.type, tenses.type, "
            + "imperativeMood.type, typesOfPronouns.type, "
            + "unchanging.type, shortAd.type, "
            + "comparativeAdjective.type, typesOfNames.type, "
            + "locativeOrOrganization.type, "
            + "qualitativeAdjective.type, "
            + "interrogativeRelativeAdverb.type, noPlural.type, "
            + "typo.type, jargonArchaicProfessionalism.type, "
            + "abbreviation.type, impersonalVerb.type from ancodes,"
            + " lexems, bases, suffixes, flexiamodels, "
            + "partsOfSpeeches, genders, animacy, count, cases, "
            + "aspects, typesOfVerbs, typesOfVoices, tenses, "
            + "imperativeMood, typesOfPronouns, unchanging, shortAd"
            + ", comparativeAdjective, typesOfNames, "
            + "locativeOrOrganization, qualitativeAdjective, "
            + "interrogativeRelativeAdverb, noPlural, typo, "
            + "jargonArchaicProfessionalism, abbreviation, "
            + "impersonalVerb where ? like bases.baseStr||'%' and "
            + "lexems.baseStrId=bases.id and bases.baseStr||"
            + "suffixes.suffix=? and lexems.paradigmid="
            + "flexiamodels.paradigmid and flexiamodels.suffixid="
            + "suffixes.id and flexiamodels.ancodeid=ancodes.id and"
            + " ancodes.partOfSpeechId=partsOfSpeeches.id and "
            + "ancodes.genderId=genders.id and ancodes.animacyId="
            + "animacy.id and ancodes.countId=count.id and "
            + "ancodes.caseId=cases.id and ancodes.aspectId="
            + "aspects.id and ancodes.typeOfVerbId=typesOfVerbs.id "
            + "and ancodes.typeOfVoiceId=typesOfVoices.id and "
            + "ancodes.tenseId=tenses.id and ancodes.imperativeMood"
            + "=imperativeMood.id and ancodes.typeOfPronounId="
            + "typesOfPronouns.id and ancodes.unchanging="
            + "unchanging.id and ancodes.shortAdId=shortAd.id and "
            + "ancodes.comparativeAdjective=comparativeAdjective.id"
            + " and ancodes.typeOfNameId=typesOfNames.id and "
            + "ancodes.locativeOrOrganizationId="
            + "locativeOrOrganization.id and "
            + "ancodes.qualitativeAdjective=qualitativeAdjective.id"
            + " and ancodes.interrogativeRelativeAdverbId="
            + "interrogativeRelativeAdverb.id and ancodes.noPlural="
            + "noPlural.id and ancodes.typo=typo.id and "
            + "ancodes.jargonArchaicProfessionalismId="
            + "jargonArchaicProfessionalism.id and "
            + "ancodes.abbreviation=abbreviation.id and "
            + "ancodes.impersonalVerb=impersonalVerb.id";*/
    private DictionaryInitiation dictionaries;
    private Map<String, String> rusCache;
    private Map<String, String> engCache;
    private PreparedStatement psSelectGrammemIdRus;
    private PreparedStatement psSelectGrammemIdEng;
    private PreparedStatement psSelectLexemRus;
    private PreparedStatement psSelectLexemEng;
    private Statistics stats;
    
    public Analyzer(Statistics stats) {
        this.stats = stats;
    }
    
    public void analyze(Boolean useCache, String textFilePath, 
            String encoding) {
        dictionaries = new DictionaryInitiation();
        stats.setTimeDictRead(System.currentTimeMillis());
        if (useCache) { 
            rusCache = new HashMap<String, String>(); 
            engCache = new HashMap<String, String>(); 
        }
        try {
            psSelectGrammemIdRus = dictionaries.getConnectionRus()
                    .prepareStatement(selectGrammemId);
            psSelectGrammemIdEng = dictionaries.getConnectionEng()
                    .prepareStatement(selectGrammemId);
            psSelectLexemRus = dictionaries.getConnectionRus()
                    .prepareStatement(selectLexemRus);
            psSelectLexemEng = dictionaries.getConnectionEng()
                    .prepareStatement(selectLexemEng);
            Tokenizer token = new Tokenizer(textFilePath, encoding);
            String word = token.getWord();
            //while (word != null) {
            for (int i = 1; i < 50; i++) {
                System.out.println(i);
                analyzeWord(word, useCache);
                word = token.getWord();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dictionaries.closeConnections();
        }
    }

    private void analyzeWord(String word, boolean useCache) {
        if (useCache)
            analyzeInCache(word);
        else 
            analyze(word);
    }

    private void analyze(String word) {
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) 
                <= 122) {
            searchWord(psSelectGrammemIdEng, 
                    psSelectLexemEng, "eng", word);
//            searchWord(psSelectLexemEng, "eng", word);
        } else {
            searchWord(psSelectGrammemIdRus, 
                    psSelectLexemRus, "rus", word);
//            searchWord(psSelectLexemRus, "rus", word);
        }
    }

    private void analyzeInCache(String word) {
        String s;
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) 
                <= 122)
            if (searchInCache(engCache, word) == null) {
                stats.increaseCountCacheMiss();
                s = searchWord(psSelectGrammemIdEng, 
                        psSelectLexemEng, "eng", word);
//                s = searchWord(psSelectLexemEng, "eng", word);
                engCache.put(word, s == null ? "" : s);
            } else
                stats.increaseCountCacheHit();
        else
            if (searchInCache(rusCache, word) == null) {
                stats.increaseCountCacheMiss();
                s = searchWord(psSelectGrammemIdRus, 
                        psSelectLexemRus, "rus", word);
//                s = searchWord(psSelectLexemRus, "rus", word);
                rusCache.put(word, s == null ? "" : s);  
            } else
                stats.increaseCountCacheHit();
    }
    
    private String searchInCache(Map<String, String> map, 
            String word) {
        return map.get(word);
    }
    
    private String searchWord(PreparedStatement psSelectGrammemId,
            PreparedStatement psSelectGrammemInfo, String lang, 
            String word) {
        String result = "";
        String grammem;
        stats.increaseCountRequest();
        try {
            psSelectGrammemId.setString(1, word);
            psSelectGrammemId.setString(2, word);
            ResultSet rsSelectGrammemId 
                    = psSelectGrammemId.executeQuery();
            while (rsSelectGrammemId.next()) {
                psSelectGrammemInfo.setInt(1, 
                        rsSelectGrammemId.getInt(1));
                psSelectGrammemInfo.setString(2, word);
                ResultSet rsSelectGrammemInfo 
                        = psSelectGrammemInfo.executeQuery();
                while (rsSelectGrammemInfo.next()) {
                    if (lang.equalsIgnoreCase("eng"))
                        for (int i = 1; i <= 3/*17*/; i++){
                            grammem = rsSelectGrammemInfo
                                    .getString(i);
                            if (!grammem.isEmpty())
                                result += grammem + " ";
                        }
                    else
                        for (int i = 1; i <= 3/*25*/; i++) {
                            grammem = rsSelectGrammemInfo
                                    .getString(i);
                            if (!grammem.isEmpty())
                                result += grammem + " ";
                        }
                    result +="; ";
                }
            }
            if (!result.isEmpty()) {
                result = word + ": " + result + ".";
                stats.increaseCountSuccess();
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    /*private String searchWord(PreparedStatement psSelectLexem, 
            String lang, String word) {
        String result = "";
        String grammem;
        stats.increaseCountRequest();
        try {
            psSelectLexem.setString(1, word);
            psSelectLexem.setString(2, word);
            ResultSet rsSelectLexem = psSelectLexem.executeQuery();
            while (rsSelectLexem.next()) {
                if (lang.equalsIgnoreCase("eng"))
                    for (int i = 1; i <= 17; i++){
                        grammem = rsSelectLexem.getString(i);
                        if (!grammem.isEmpty())
                            result += grammem + " ";
                    }
                else
                    for (int i = 1; i <= 25; i++) {
                        grammem = rsSelectLexem.getString(i);
                        if (!grammem.isEmpty())
                            result += grammem + " ";
                    }
                result +="; ";
            }
            if (!result.isEmpty()) {
                result = word + ": " + result + ".";
                stats.increaseCountSuccess();
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }*/
}