/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textanalyzer.dictionarypsql;

/**
 * Represent information of how word form created from it imutible part.
 * code, prefix, suffix
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