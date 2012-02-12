package com.mycompany.morphology.morph;

public interface LetterDecoderEncoder {
    public Integer encode(String string);

    public int[] encodeToArray(String s);

    public String decodeArray(int[] array);

    public String decode(Integer suffixN);

    public boolean checkCharacter(char c);

    public boolean checkString(String word);

    public String cleanString(String s);
}
