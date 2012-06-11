package com.mycompany.textanalyzer.dictionarypsql2;

import com.mycompany.textanalyzer.dictionary.FlexiaModel;
import com.mycompany.textanalyzer.dictionary.WordCard;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

/**
 * Этот класс содержит логику, как считывать словарь и сопоставлять
 * каждое слово с его формой
 */
public class DictionaryReader {
    private String fileName;
    private final String fileEncoding = "UTF-8";
    private List<List<FlexiaModel>> wordsFlexias = 
            new ArrayList<List<FlexiaModel>>();
    private Map<String, Integer> suffixes = 
            new HashMap<String, Integer>();
    private Map<String, WordCard> dictionary;
    private Integer suffixId = 0;

    public DictionaryReader(String fileName) 
            throws SQLException, IOException {
        this.fileName = fileName;
        process();
    }

    public void process() throws IOException {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(fileName), fileEncoding));
            readFlexias(bufferedReader);
            scipBlock(bufferedReader);
            scipBlock(bufferedReader);
            scipBlock(bufferedReader);
            readBases(bufferedReader);
    }
    
    private void readFlexias(BufferedReader reader) 
            throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            ArrayList<FlexiaModel> flexiaModelList =
                    new ArrayList<FlexiaModel>();
            wordsFlexias.add(flexiaModelList);
            for (String line : s.split("\\%"))
                addFlexia(flexiaModelList, line);
        }
    }

    private void addFlexia(ArrayList<FlexiaModel> flexiaModelList, 
            String line) {
        String[] fl = line.split("\\*");
        if (fl.length == 2) {
            String suffix = fl[0].toLowerCase();
            flexiaModelList.add(new FlexiaModel(fl[1], suffix));
            if (!suffixes.containsValue(suffix)){
                suffixId++;
                suffixes.put(suffix, suffixId);
            }    
        }
    }

    private void scipBlock(BufferedReader reader) 
            throws IOException {
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++)
            reader.readLine();
    }
    
    private void readBases(BufferedReader reader) 
            throws IOException {
        dictionary = new TreeMap<String, WordCard>();
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++) {
            String[] wd = reader.readLine().split(" ");
            String wordBase = wd[0].toLowerCase();
            if (wordBase.startsWith("-")) 
                continue;
            wordBase = "#".equals(wordBase) ? "" : wordBase;
            List<FlexiaModel> flexiaModelList = 
                    wordsFlexias.get(Integer.valueOf(wd[1]));
            FlexiaModel flexiaModel = flexiaModelList.get(0);
            if (flexiaModelList.size() > 0) {
                WordCard newWordCard = 
                        new WordCard(flexiaModel.create(wordBase), 
                        wordBase, flexiaModel.getSuffix());
                WordCard existingWordCard 
                        = dictionary.get(wordBase);
                for (FlexiaModel fm : flexiaModelList)
                    if (existingWordCard == null)
                        newWordCard.addFlexia(fm); 
                    else
                        existingWordCard.addFlexia(fm);
                if (existingWordCard == null)
                    dictionary.put(wordBase, newWordCard);
            }
        }
    }
    
    public Map<String, WordCard> getMainDictionary() {
        return dictionary;
    }
    
    public List<List<FlexiaModel>> getWordsFlexias() { 
        return wordsFlexias;
    }
    
    public Map<String, Integer> getSuffixes() { 
        return suffixes;
    }
}