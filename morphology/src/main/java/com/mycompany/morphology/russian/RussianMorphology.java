package com.mycompany.morphology.russian;

import com.mycompany.morphology.morph.MorphologyImpl;

import java.io.IOException;

public class RussianMorphology extends MorphologyImpl {

    public RussianMorphology() throws IOException {
        super(RussianMorphology.class.getResourceAsStream("/org/apache/lucene/morphology/russian/morph.info"), new RussianLetterDecoderEncoder());
    }
}