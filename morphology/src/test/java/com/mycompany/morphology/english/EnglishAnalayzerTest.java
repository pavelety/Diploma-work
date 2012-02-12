package com.mycompany.morphology.english;

import com.mycompany.morphology.english.EnglishAnalyzer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

public class EnglishAnalayzerTest {

    @Test
    public void shouldGiveCorrectWords() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/english/englsih-analayzer-answer.txt");
        BufferedReader breader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String[] strings = breader.readLine().replaceAll(" +", " ").trim().split(" ");
        HashSet<String> answer = new HashSet<String>(Arrays.asList(strings));
        stream.close();

        EnglishAnalyzer morphlogyAnalayzer = new EnglishAnalyzer();
        stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/english/englsih-analayzer-data.txt");

        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

        TokenStream tokenStream = morphlogyAnalayzer.tokenStream(null, reader);
        HashSet<String> result = new HashSet<String>();
        while (tokenStream.incrementToken()) {
            TermAttribute attribute1 = tokenStream.getAttribute(TermAttribute.class);
            result.add(attribute1.term());
        }

        stream.close();

        assertThat(result, equalTo(answer)); //пишет, что нет такого метода
    }
}
