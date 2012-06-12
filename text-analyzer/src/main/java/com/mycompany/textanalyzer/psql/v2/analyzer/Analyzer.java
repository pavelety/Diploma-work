package com.mycompany.textanalyzer.psql.v2.analyzer;

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
 * БД dictionaryru/en
 * Иcпользован like (запрос разбит на 2)
 * (<1 слова в секунду)
 * @author pavel
 */
public class Analyzer implements AnalyzerInterface {
    private final String selectGrammemId = "select grammeminfo.id"
            + " from grammeminfo, bases, suffixes where ? like "
            + "bases.baseStr||'%' and grammeminfo.baseStrId=bases.id "
            + "and bases.baseStr||suffixes.suffix=? and "
            + "grammeminfo.suffixid=suffixes.id";
    private final String selectGrammemInfoEng = "select bases.basestr,"
            + " suffixes.suffix, partsOfSpeeches.type, genders.type"
            + ", count.type, cases.type, PNForms.type, "
            + "AdDegrees.type, tenses.type, typesOfPerson.type, "
            + "PNTypes.type, commonName.type, geographical.type, "
            + "properName.type, plsg.type, name.type, "
            + "organization.type from grammeminfo, bases, suffixes,"
            + " partsOfSpeeches, genders, count, cases, PNForms, "
            + "AdDegrees, tenses, typesOfPerson, PNTypes, "
            + "commonName, geographical, properName, plsg, name, "
            + "organization where grammeminfo.id=? and "
            + "grammeminfo.baseStrId=bases.id and "
            + "grammeminfo.suffixid=suffixes.id and "
            + "grammeminfo.partOfSpeechId=partsOfSpeeches.id and "
            + "grammeminfo.genderId=genders.id and "
            + "grammeminfo.countId=count.id and grammeminfo.caseId="
            + "cases.id and grammeminfo.PNFormId=PNForms.id and "
            + "grammeminfo.AdDegreeId=AdDegrees.id and "
            + "grammeminfo.tenseId=tenses.id and "
            + "grammeminfo.typeOfPersonId=typesOfPerson.id and "
            + "grammeminfo.PNTypeId=PNTypes.id and "
            + "grammeminfo.commonName=commonName.id and "
            + "grammeminfo.geographical=geographical.id and "
            + "grammeminfo.properName=properName.id and "
            + "grammeminfo.plsgId=plsg.id and grammeminfo.name="
            + "name.id and grammeminfo.organization="
            + "organization.id";
    private final String selectGrammemInfoRus = "select "
            + "bases.basestr, suffixes.suffix, partsOfSpeeches.type"
            + ", genders.type, animacy.type, count.type, cases.type"
            + ", aspects.type, typesOfVerbs.type, "
            + "typesOfVoices.type, tenses.type, imperativeMood.type"
            + ", typesOfPronouns.type, unchanging.type, "
            + "shortAd.type, comparativeAdjective.type, "
            + "typesOfNames.type, locativeOrOrganization.type, "
            + "qualitativeAdjective.type, "
            + "interrogativeRelativeAdverb.type, noPlural.type, "
            + "typo.type, jargonArchaicProfessionalism.type, "
            + "abbreviation.type, impersonalVerb.type from "
            + "grammeminfo, bases, suffixes, partsOfSpeeches, "
            + "genders, animacy, count, cases, aspects, "
            + "typesOfVerbs, typesOfVoices, tenses, "
            + "imperativeMood, typesOfPronouns, unchanging, shortAd"
            + ", comparativeAdjective, typesOfNames, "
            + "locativeOrOrganization, qualitativeAdjective, "
            + "interrogativeRelativeAdverb, noPlural, typo, "
            + "jargonArchaicProfessionalism, abbreviation, "
            + "impersonalVerb where grammeminfo.id=? and "
            + "grammeminfo.baseStrId=bases.id and "
            + "grammeminfo.suffixId=suffixes.id and "
            + "grammeminfo.partOfSpeechId=partsOfSpeeches.id and "
            + "grammeminfo.genderId=genders.id and "
            + "grammeminfo.animacyId=animacy.id and "
            + "grammeminfo.countId=count.id and grammeminfo.caseId="
            + "cases.id and grammeminfo.aspectId=aspects.id and "
            + "grammeminfo.typeOfVerbId=typesOfVerbs.id and "
            + "grammeminfo.typeOfVoiceId=typesOfVoices.id and "
            + "grammeminfo.tenseId=tenses.id and "
            + "grammeminfo.imperativeMood=imperativeMood.id and "
            + "grammeminfo.typeOfPronounId=typesOfPronouns.id and "
            + "grammeminfo.unchanging=unchanging.id and "
            + "grammeminfo.shortAdId=shortAd.id and "
            + "grammeminfo.comparativeAdjective="
            + "comparativeAdjective.id and grammeminfo.typeOfNameId"
            + "=typesOfNames.id and "
            + "grammeminfo.locativeOrOrganizationId="
            + "locativeOrOrganization.id and "
            + "grammeminfo.qualitativeAdjective="
            + "qualitativeAdjective.id and "
            + "grammeminfo.interrogativeRelativeAdverbId="
            + "interrogativeRelativeAdverb.id and "
            + "grammeminfo.noPlural=noPlural.id and "
            + "grammeminfo.typo=typo.id and "
            + "grammeminfo.jargonArchaicProfessionalismId="
            + "jargonArchaicProfessionalism.id and "
            + "grammeminfo.abbreviation=abbreviation.id and "
            + "grammeminfo.impersonalVerb=impersonalVerb.id";
    private DictionaryInitiation dictionaries;
    private Map<String, String> rusCache;
    private Map<String, String> engCache;
    private PreparedStatement psSelectGrammemIdRus;
    private PreparedStatement psSelectGrammemIdEng;
    private PreparedStatement psSelectGrammemInfoRus;
    private PreparedStatement psSelectGrammemInfoEng;
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
            psSelectGrammemInfoRus = dictionaries.getConnectionRus()
                    .prepareStatement(selectGrammemInfoRus);
            psSelectGrammemInfoEng = dictionaries.getConnectionEng()
                    .prepareStatement(selectGrammemInfoEng);
            Tokenizer token = new Tokenizer(textFilePath, encoding);
            String word = token.getWord();
            while (word != null) {
//            for (int i = 1; i < 50; i++) {
//                System.out.println(i);
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
        stats.increaseCountWords();
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
                    psSelectGrammemInfoEng, "eng", word);
        } else {
            searchWord(psSelectGrammemIdRus, 
                    psSelectGrammemInfoRus, "rus", word);
        }
    }

    private void analyzeInCache(String word) {
        String s;
        if (word.codePointAt(0) >= 65 & word.codePointAt(0) <= 90
                | word.codePointAt(0) >= 97 & word.codePointAt(0) 
                <= 122)
            if (searchInCache(engCache, word) == null) {
                s = searchWord(psSelectGrammemIdEng, 
                        psSelectGrammemInfoEng, "eng", word);
                engCache.put(word, s==null?"":s);
            } else
                stats.increaseCountCacheHit();
        else
            if (searchInCache(rusCache, word) == null) {
                s = searchWord(psSelectGrammemIdRus, 
                        psSelectGrammemInfoRus, "rus", word);
                rusCache.put(word, s==null?"":s);  
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
                ResultSet rsSelectGrammemInfo 
                        = psSelectGrammemInfo.executeQuery();
                while (rsSelectGrammemInfo.next()) {
                    if (lang.equalsIgnoreCase("eng"))
                        for (int i = 1; i <= 17; i++){
                            grammem = rsSelectGrammemInfo
                                    .getString(i);
                            if (!grammem.isEmpty())
                                result += grammem + " ";
                        }
                    else
                        for (int i = 1; i <= 25; i++) {
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
}