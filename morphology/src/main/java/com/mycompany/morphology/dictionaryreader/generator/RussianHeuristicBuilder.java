package com.mycompany.morphology.dictionaryreader.generator;

import com.mycompany.morphology.dictionaryreader.dictionary.DictonaryReader;
import com.mycompany.morphology.dictionaryreader.dictionary.GrammaReader;
import com.mycompany.morphology.dictionaryreader.dictionary.StatisticsCollector;
import com.mycompany.morphology.russian.RussianLetterDecoderEncoder;
import java.io.IOException;
import java.util.HashSet;


public class RussianHeuristicBuilder {
    public static void main(String[] args) throws IOException {
        GrammaReader grammaInfo = new GrammaReader("dicts/rus/rgramtab.tab");
        DictonaryReader dictonaryReader = new DictonaryReader("dicts/rus/morphs.mrd", new HashSet<String>());

        RussianLetterDecoderEncoder decoderEncoder = new RussianLetterDecoderEncoder();
        StatisticsCollector statisticsCollector = new StatisticsCollector(grammaInfo, decoderEncoder);
        dictonaryReader.proccess(statisticsCollector);
        statisticsCollector.saveHeuristic("dicts/rus/morph.info");

    }
}