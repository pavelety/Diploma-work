package com.mycompany.morphology.russian;

import com.mycompany.morphology.morph.LuceneMorphology;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class RussianLuceneMorphTest {
    private LuceneMorphology luceneMorph;

    @Before
    public void setUp() throws IOException {
        luceneMorph = new LuceneMorphology(this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/morph.info"), new RussianLetterDecoderEncoder());
    }

    @Test
    public void shoudGetCorrentMorphInfo() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-morphology-test.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String s = bufferedReader.readLine();
        while (s != null) {
            String[] qa = s.trim().split(" ");
            Set<String> result = new HashSet<String>();
            for (int i = 1; i < qa.length; i++) {
                result.add(qa[i]);
            }
            Set<String> stringList = new HashSet<String>(luceneMorph.getNormalForms(qa[0]));
            assertThat(stringList, equalTo(result));
            s = bufferedReader.readLine();
        }
    }
}