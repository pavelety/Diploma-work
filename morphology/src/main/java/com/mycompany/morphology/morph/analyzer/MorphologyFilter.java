/**
 * @author pavlin
 *
 */
package com.mycompany.morphology.morph.analyzer;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.mycompany.morphology.morph.LuceneMorphology;

import java.io.IOException;
import java.util.Iterator;


public class MorphologyFilter extends TokenFilter {
    private LuceneMorphology luceneMorph;
    private Iterator<String> iterator;
    private TermAttribute termAtt;

    public MorphologyFilter(TokenStream tokenStream, LuceneMorphology luceneMorph) {
        super(tokenStream);
        this.luceneMorph = luceneMorph;
        termAtt = addAttribute(TermAttribute.class);
    }


    public boolean incrementToken() throws IOException {
        while (iterator == null || !iterator.hasNext()) {
            boolean b = input.incrementToken();
            if (!b) {
                return false;
            }
            String s = termAtt.term();
            if (luceneMorph.checkString(s)) {
                iterator = luceneMorph.getNormalForms(termAtt.term()).iterator();
            } else {
                return true;
            }
        }
        String s = iterator.next();
        termAtt.setTermBuffer(s);
        return true;
    }

}