package com.mycompany.morphology.russian;

import com.mycompany.morphology.morph.analyzer.MorphologyAnalyzer;

import java.io.IOException;

public class RussianAnalyzer extends MorphologyAnalyzer {
    public RussianAnalyzer() throws IOException {
        super(new RussianLuceneMorphology());
    }
}