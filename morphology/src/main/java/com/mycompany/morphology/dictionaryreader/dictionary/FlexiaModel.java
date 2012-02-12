package com.mycompany.morphology.dictionaryreader.dictionary;

/**
 * Represent information of how word form created form it imutible part.
 */
public class FlexiaModel {
    private String code;
    private String suffix;
    private String prefix;

    public FlexiaModel(String code, String suffix, String prefix) {
        this.code = code;
        this.suffix = suffix;
        this.prefix = prefix;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String create(String s) {
        return prefix + s + suffix;
    }

    @Override
    public String toString() {
        return "FlexiaModel{" +
                "code='" + code + '\'' +
                ", suffix='" + suffix + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
