package com.mycompany.morphology.russian;

import com.mycompany.morphology.morph.SuffixToLongException;
import com.mycompany.morphology.morph.WrongCharaterException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class RussianLetterDecoderEncoderTest {
    private RussianLetterDecoderEncoder decoderEncoder;

    @Before
    public void setUp() {
        decoderEncoder = new RussianLetterDecoderEncoder();
    }


    @Test
    public void testShouldPreserStringComporision() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/decoder-test-monotonic.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String s = bufferedReader.readLine();
        while (s != null) {
            String[] qa = s.trim().split(" ");
            if (qa[0].length() <= RussianLetterDecoderEncoder.WORD_PART_LENGHT && qa[1].length() <= RussianLetterDecoderEncoder.WORD_PART_LENGHT) {
                assertThat(decoderEncoder.encode(qa[1]) > decoderEncoder.encode(qa[0]), equalTo(true));
            }
            s = bufferedReader.readLine();
        }
    }


    @Test
    public void testShouldCorretDecodeEncode() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/decoder-test-data.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String s = bufferedReader.readLine();
        while (s != null) {
            String[] qa = s.trim().split(" ");
            if (qa[0].length() <= RussianLetterDecoderEncoder.WORD_PART_LENGHT) {
                Integer ecodedSuffix = decoderEncoder.encode(qa[0]);
                assertThat(decoderEncoder.decode(ecodedSuffix), equalTo(qa[1]));
            }
            s = bufferedReader.readLine();
        }
    }

    @Test
    public void testShouldCorretDecodeEncodeStringToArray() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/decoder-test-data-for-array.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String s = bufferedReader.readLine();
        while (s != null) {
            String[] qa = s.trim().split(" ");
            int[] ecodedSuffix = decoderEncoder.encodeToArray(qa[0]);
            assertThat(decoderEncoder.decodeArray(ecodedSuffix), equalTo(qa[1]));
            s = bufferedReader.readLine();
        }
    }

    @Test(expected = SuffixToLongException.class)
    public void shouldThrownExeptionIfSuffixToLong() {
        decoderEncoder.encode("1234567890123");
    }

    @Test(expected = WrongCharaterException.class)
    public void shouldThrownExeptionIfSuffixContainWrongCharater() {
        decoderEncoder.encode("1");
    }
}
