package com.mycompany.morphology.english;

import com.mycompany.morphology.morph.LuceneMorphology;
import java.io.IOException;


public class EnglishLuceneMorphology extends LuceneMorphology {

    public EnglishLuceneMorphology() throws IOException {
        super(EnglishLuceneMorphology.class.getResourceAsStream("/org/apache/lucene/morphology/english/morph.info"), new EnglishLetterDecoderEncoder());
    }
}