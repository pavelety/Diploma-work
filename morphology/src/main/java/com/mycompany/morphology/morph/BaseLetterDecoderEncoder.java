package com.mycompany.morphology.morph;

import java.util.ArrayList;

public abstract class BaseLetterDecoderEncoder implements LetterDecoderEncoder {
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

    public boolean checkString(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!checkCharacter(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
