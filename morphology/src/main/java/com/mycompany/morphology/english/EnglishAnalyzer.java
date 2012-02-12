package com.mycompany.morphology.english;

import com.mycompany.morphology.morph.analyzer.MorphologyAnalyzer;
import java.io.IOException;

public class EnglishAnalyzer extends MorphologyAnalyzer {

    public EnglishAnalyzer() throws IOException {
        super(new EnglishLuceneMorphology());
    }

}