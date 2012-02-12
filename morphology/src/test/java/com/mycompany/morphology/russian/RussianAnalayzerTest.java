package com.mycompany.morphology.russian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;


public class RussianAnalayzerTest {

    @Test
    public void shoudGiveCorretWords() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-analayzer-answer.txt");
        BufferedReader breader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String[] strings = breader.readLine().replaceAll(" +", " ").trim().split(" ");
        HashSet<String> answer = new HashSet<String>(Arrays.asList(strings));
        stream.close();

        RussianAnalyzer morphlogyAnalayzer = new RussianAnalyzer();
        stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-analayzer-data.txt");

        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

        TokenStream tokenStream = morphlogyAnalayzer.tokenStream(null, reader);
        HashSet<String> result = new HashSet<String>();
        while (tokenStream.incrementToken()) {
            TermAttribute attribute1 = tokenStream.getAttribute(TermAttribute.class);
            result.add(attribute1.term());
        }

        stream.close();

        assertThat(result, equalTo(answer));
    }
}