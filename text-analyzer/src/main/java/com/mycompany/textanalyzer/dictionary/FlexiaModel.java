package com.mycompany.textanalyzer.dictionary;

/**
 * Представляет информацию о создании слова: акнод, суффикс
 * @author pavel
 */
public class FlexiaModel {
    private String code;
    private String suffix;

    public FlexiaModel(String code, String suffix) {
        this.code = code;
        this.suffix = suffix;
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

    public String create(String s) {
        return s + suffix;
    }

    @Override
    public String toString() {
        return "FlexiaModel{" +
                "code='" + code + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}