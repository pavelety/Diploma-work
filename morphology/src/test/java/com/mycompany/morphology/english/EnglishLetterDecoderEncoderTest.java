package com.mycompany.morphology.english;

import com.mycompany.morphology.english.EnglishLetterDecoderEncoder;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Before;
import org.junit.Test;


public class EnglishLetterDecoderEncoderTest {
    private EnglishLetterDecoderEncoder decoderEncoder;

    @Before
    public void setUp() {
        decoderEncoder = new EnglishLetterDecoderEncoder();
    }

    @Test
    public void testDecodeEncodeToArray() {
        //пишет, что нет таких методов
        assertThat(decoderEncoder.decodeArray(decoderEncoder.encodeToArray("abcdefghijklmnopqrstuvwxyz")), equalTo("abcdefghijklmnopqrstuvwxyz"));
        assertThat(decoderEncoder.decodeArray(decoderEncoder.encodeToArray("xyz")), equalTo("xyz"));
        assertThat(decoderEncoder.decodeArray(decoderEncoder.encodeToArray("ytrrty")), equalTo("ytrrty"));
        assertThat(decoderEncoder.decodeArray(decoderEncoder.encodeToArray("ytrrtyz")), equalTo("ytrrtyz"));
        assertThat(decoderEncoder.decodeArray(decoderEncoder.encodeToArray("ytrrtyzqwqwe")), equalTo("ytrrtyzqwqwe"));

    }
}