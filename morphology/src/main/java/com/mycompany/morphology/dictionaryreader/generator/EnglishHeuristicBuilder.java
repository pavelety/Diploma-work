package com.mycompany.morphology.dictionaryreader.generator;

import com.mycompany.morphology.dictionaryreader.dictionary.DictonaryReader;
import com.mycompany.morphology.dictionaryreader.dictionary.GrammaReader;
import com.mycompany.morphology.dictionaryreader.dictionary.StatisticsCollector;
import com.mycompany.morphology.english.EnglishLetterDecoderEncoder;
import java.io.IOException;
import java.util.HashSet;


public class EnglishHeuristicBuilder {
    public static void main(String[] args) throws IOException {

        GrammaReader grammaInfo = new GrammaReader("dicts/eng/egramtab.tab");
        DictonaryReader dictonaryReader = new DictonaryReader("dicts/eng/morphs.mrd", new HashSet<String>());

        EnglishLetterDecoderEncoder decoderEncoder = new EnglishLetterDecoderEncoder();
        StatisticsCollector statisticsCollector = new StatisticsCollector(grammaInfo, decoderEncoder);
        dictonaryReader.proccess(statisticsCollector);
        statisticsCollector.saveHeuristic("dicts/eng/morph.info");

    }
}
