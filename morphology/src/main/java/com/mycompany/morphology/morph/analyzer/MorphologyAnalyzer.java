package com.mycompany.morphology.morph.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.mycompany.morphology.morph.LetterDecoderEncoder;
import com.mycompany.morphology.morph.LuceneMorphology;

import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class MorphologyAnalyzer extends Analyzer {
    private LuceneMorphology luceneMorph;

    public MorphologyAnalyzer(LuceneMorphology luceneMorph) {
        this.luceneMorph = luceneMorph;
    }

    public MorphologyAnalyzer(String pathToMorph, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(pathToMorph, letterDecoderEncoder);
    }

    public MorphologyAnalyzer(InputStream inputStream, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(inputStream, letterDecoderEncoder);
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(Version.LUCENE_30, reader);
        result = new StandardFilter(result);
        result = new LowerCaseFilter(result);
        return new MorphologyFilter(result, luceneMorph);
    }
}
