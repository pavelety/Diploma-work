package com.mycompany.morphology.english;

import com.mycompany.morphology.morph.LetterDecoderEncoder;
import com.mycompany.morphology.morph.SuffixToLongException;
import com.mycompany.morphology.morph.WrongCharaterException;
import java.util.ArrayList;


public class EnglishLetterDecoderEncoder implements LetterDecoderEncoder {
    public static final int ENGLISH_SMALL_LETTER_OFFSET = 96;
    static public int SUFFIX_LENGTH = 6;
    public static final int DASH_CHAR = 45;
    public static final int DASH_CODE = 27;

    public Integer encode(String string) {
        if (string.length() > 6) throw new SuffixToLongException("Suffix length should not be greater then " + 12);
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            int c = 0 + string.charAt(i) - ENGLISH_SMALL_LETTER_OFFSET;
            if (c == 45 - ENGLISH_SMALL_LETTER_OFFSET) {
                c = DASH_CODE;
            }
            if (c < 0 || c > 27)
                throw new WrongCharaterException("Symblo " + string.charAt(i) + " is not small cirillic letter");
            result = result * 28 + c;
        }
        for (int i = string.length(); i < 6; i++) {
            result *= 28;
        }
        return result;
    }

    public int[] encodeToArray(String s) {

        ArrayList<Integer> integers = new ArrayList<Integer>();
        while (s.length() > 6) {
            integers.add(encode(s.substring(0, 6)));
            s = s.substring(6);
        }
        integers.add(encode(s));
        int[] ints = new int[integers.size()];
        int pos = 0;
        for (Integer i : integers) {
            ints[pos] = i;
            pos++;
        }
        return ints;
    }

    public String decodeArray(int[] array) {
        String result = "";
        for (int i : array) {
            result += decode(i);
        }
        return result;
    }


    public String decode(Integer suffixN) {
        String result = "";
        while (suffixN > 27) {
            int c = suffixN % 28 + ENGLISH_SMALL_LETTER_OFFSET;
            if (c == ENGLISH_SMALL_LETTER_OFFSET) {
                suffixN /= 28;
                continue;
            }
            if (c == DASH_CODE + ENGLISH_SMALL_LETTER_OFFSET) c = DASH_CHAR;
            result = (char) c + result;
            suffixN /= 28;
        }
        long c = suffixN + ENGLISH_SMALL_LETTER_OFFSET;
        if (c == DASH_CODE + ENGLISH_SMALL_LETTER_OFFSET) c = DASH_CHAR;
        result = (char) c + result;
        return result;
    }

    public boolean checkCharacter(char c) {
        int code = 0 + c;
        if (code == 45) return true;
        code -= ENGLISH_SMALL_LETTER_OFFSET;
        if (code > 0 && code < 27) return true;
        return false;
    }


    public boolean checkString(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!checkCharacter(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String cleanString(String s) {
        return s;
    }

}