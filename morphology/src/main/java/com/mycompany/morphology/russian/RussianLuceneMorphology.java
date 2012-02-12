package com.mycompany.morphology.russian;

import com.mycompany.morphology.morph.LuceneMorphology;

import java.io.IOException;

public class RussianLuceneMorphology extends LuceneMorphology {

    public RussianLuceneMorphology() throws IOException {
        super(RussianLuceneMorphology.class.getResourceAsStream("/org/apache/lucene/morphology/russian/morph.info"), new RussianLetterDecoderEncoder());
    }
}
