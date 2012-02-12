package com.mycompany.morphology.russian;

import com.mycompany.morphology.morph.LetterDecoderEncoder;
import com.mycompany.morphology.morph.SuffixToLongException;
import com.mycompany.morphology.morph.WrongCharaterException;

import java.util.ArrayList;

/**
 * This helper class allow encode suffix of russian word
 * to long value and decode from it.
 * Assumed that suffix contains only small russian letters and dash.
 * Also assumed that letter � and � coinsed.
 */
public class RussianLetterDecoderEncoder implements LetterDecoderEncoder {
    public static final int RUSSIAN_SMALL_LETTER_OFFSET = 1071;
    public static final int WORD_PART_LENGHT = 6;
    public static final int EE_CHAR = 34;
    public static final int E_CHAR = 6;
    public static final int DASH_CHAR = 45;
    public static final int DASH_CODE = 33;

    public Integer encode(String string) {
        if (string.length() > WORD_PART_LENGHT)
            throw new SuffixToLongException("Suffix length should not be greater then " + WORD_PART_LENGHT + " " + string);
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            int c = 0 + string.charAt(i) - RUSSIAN_SMALL_LETTER_OFFSET;
            if (c == 45 - RUSSIAN_SMALL_LETTER_OFFSET) {
                c = DASH_CODE;
            }
            if (c == EE_CHAR) c = E_CHAR;
            if (c < 0 || c > 33)
                throw new WrongCharaterException("Symblo " + string.charAt(i) + " is not small cirillic letter");
            result = result * 34 + c;
        }
        for (int i = string.length(); i < WORD_PART_LENGHT; i++) {
            result *= 34;
        }
        return result;
    }

    public int[] encodeToArray(String s) {
        ArrayList<Integer> integers = new ArrayList<Integer>();
        while (s.length() > WORD_PART_LENGHT) {
            integers.add(encode(s.substring(0, WORD_PART_LENGHT)));
            s = s.substring(WORD_PART_LENGHT);
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
        while (suffixN > 33) {
            int c = suffixN % 34 + RUSSIAN_SMALL_LETTER_OFFSET;
            if (c == RUSSIAN_SMALL_LETTER_OFFSET) {
                suffixN /= 34;
                continue;
            }
            if (c == DASH_CODE + RUSSIAN_SMALL_LETTER_OFFSET) c = DASH_CHAR;
            result = (char) c + result;
            suffixN /= 34;
        }
        long c = suffixN + RUSSIAN_SMALL_LETTER_OFFSET;
        if (c == DASH_CODE + RUSSIAN_SMALL_LETTER_OFFSET) c = DASH_CHAR;
        result = (char) c + result;
        return result;
    }

    public boolean checkCharacter(char c) {
        int code = 0 + c;
        if (code == 45) return true;
        code -= RUSSIAN_SMALL_LETTER_OFFSET;
        if (code > 0 && code < 33) return true;
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
        return s.replace((char) (EE_CHAR + RussianLetterDecoderEncoder.RUSSIAN_SMALL_LETTER_OFFSET), (char) (E_CHAR + RussianLetterDecoderEncoder.RUSSIAN_SMALL_LETTER_OFFSET));
    }
}