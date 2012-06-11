package com.mycompany.textanalyzer.dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет слово и все его формы
 * @author pavel
 */
public class WordCard {
    private String canonicalForm;
    private String base;
    private String canonicalSuffix;
    private int paradigmId;
    private List<FlexiaModel> wordsForms 
            = new ArrayList<FlexiaModel>();

    public WordCard(String canonicalForm, 
            String base, String canonicalSuffix) {
        this.canonicalForm = canonicalForm;
        this.canonicalSuffix = canonicalSuffix;
        this.base = base;
    }

    public void addFlexia(FlexiaModel flexiaModel) {
        wordsForms.add(flexiaModel);
    }

    public String getCanonicalForm() {
        return canonicalForm;
    }

    public String getCanonicalSuffix() {
        return canonicalSuffix;
    }

    public String getBase() {
        return base;
    }

    public List<FlexiaModel> getWordsForms() {
        return wordsForms;
    }
    
    public int getParadigmId() {
        return paradigmId;
    }

    public void setCanonicalForm(String canonicalForm) {
        this.canonicalForm = canonicalForm;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setCanonicalSuffix(String canonicalSuffix) {
        this.canonicalSuffix = canonicalSuffix;
    }

    public void setWordsForms(List<FlexiaModel> wordsForms) {
        this.wordsForms = wordsForms;
    }
    
    public void setParadigmId(int paradigmId) {
        this.paradigmId = paradigmId;
    }
    
    @Override
    public String toString() {
        return "WordCard{" +
                "canonicalForm='" + canonicalForm + '\'' +
                ", base='" + base + '\'' +
                ", canonicalSuffix='" + canonicalSuffix + '\'' +
                ", wordsForms=" + wordsForms +
                '}';
    }
}