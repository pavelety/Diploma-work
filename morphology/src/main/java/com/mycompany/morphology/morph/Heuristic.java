package com.mycompany.morphology.morph;

import java.io.Serializable;

public class Heuristic implements Serializable {
    /**
     * private static final long serialVersionUID = 5509423835767070525L;
     * Вот тут я не понял, как лучше сделать. Выдает ворнинг, если вот эту штуку не дописать.
     * А её не было изначально. 
     */
    
    byte actualSuffixLength;
    String actualNormalSuffix;
    short formMorphInfo;
    short normalFormMorphInfo;

    public Heuristic(String s) {
        String[] strings = s.split("\\|");
        actualSuffixLength = Byte.valueOf(strings[0]);
        actualNormalSuffix = strings[1];
        formMorphInfo = Short.valueOf(strings[2]);
        normalFormMorphInfo = Short.valueOf(strings[3]);
    }

    public Heuristic(byte actualSuffixLength, String actualNormalSuffix, short formMorphInfo, short normalFormMorphInfo) {
        this.actualSuffixLength = actualSuffixLength;
        this.actualNormalSuffix = actualNormalSuffix;
        this.formMorphInfo = formMorphInfo;
        this.normalFormMorphInfo = normalFormMorphInfo;
    }

    public String transformWord(String w) {
        if (w.length() - actualSuffixLength < 0) return w;
        return w.substring(0, w.length() - actualSuffixLength) + actualNormalSuffix;
    }

    public byte getActualSuffixLength() {
        return actualSuffixLength;
    }

    public String getActualNormalSuffix() {
        return actualNormalSuffix;
    }

    public short getFormMorphInfo() {
        return formMorphInfo;
    }

    public short getNormalFormMorphInfo() {
        return normalFormMorphInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Heuristic heuristic = (Heuristic) o;

        if (actualSuffixLength != heuristic.actualSuffixLength) return false;
        if (formMorphInfo != heuristic.formMorphInfo) return false;
        if (normalFormMorphInfo != heuristic.normalFormMorphInfo) return false;
        if (actualNormalSuffix != null ? !actualNormalSuffix.equals(heuristic.actualNormalSuffix) : heuristic.actualNormalSuffix != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) actualSuffixLength;
        result = 31 * result + (actualNormalSuffix != null ? actualNormalSuffix.hashCode() : 0);
        result = 31 * result + (int) formMorphInfo;
        result = 31 * result + (int) normalFormMorphInfo;
        return result;
    }

    @Override
    public String toString() {
        return "" + actualSuffixLength + "|" + actualNormalSuffix + "|" + formMorphInfo + "|" + normalFormMorphInfo;
    }
}
