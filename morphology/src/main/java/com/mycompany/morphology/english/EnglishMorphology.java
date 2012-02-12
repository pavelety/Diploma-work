package com.mycompany.morphology.english;

import com.mycompany.morphology.morph.MorphologyImpl;
import java.io.IOException;

public class EnglishMorphology extends MorphologyImpl {

    public EnglishMorphology() throws IOException {
        super(EnglishLuceneMorphology.class.getResourceAsStream("/org/apache/lucene/morphology/english/morph.info"), new EnglishLetterDecoderEncoder());
    }
}