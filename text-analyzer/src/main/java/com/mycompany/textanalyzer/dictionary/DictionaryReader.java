package com.mycompany.textanalyzer.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class contain logic how read
 * dictonary and produce word with it all forms.
 */
public class DictionaryReader {
    private String fileName;
    private String fileEncoding = "UTF-8";
    private List<List<FlexiaModel>> wordsFlexias = 
            new ArrayList<List<FlexiaModel>>();
    private Map<String, WordCard> dictionary;

    public DictionaryReader(String fileName) {
        this.fileName = fileName;
    }
    
    public void proccess() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), 
                fileEncoding));
        readFlexias(bufferedReader);//считывание окончаний
        scipBlock(bufferedReader);//пропуск набора ударений
        scipBlock(bufferedReader); //пропуск изменений
        scipBlock(bufferedReader);//пропуск приставок (не реализовано словарем)
        readWords(bufferedReader);//чтение основ
    }

    private void readFlexias(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            ArrayList<FlexiaModel> flexiaModelList =
                    new ArrayList<FlexiaModel>();
            wordsFlexias.add(flexiaModelList);
            for (String line : s.split("\\%")) {
                addFlexia(flexiaModelList, line);
            }
        }
    }

    private void addFlexia(ArrayList<FlexiaModel> flexiaModelList, 
            String line) {
        String[] fl = line.split("\\*");
        if (fl.length == 2) flexiaModelList.add(new FlexiaModel(fl[1], 
                fl[0].toLowerCase(), "")); //анкод, суффикс, префикс
    }

    private void scipBlock(BufferedReader reader) throws IOException {
        int count = Integer.valueOf(reader.readLine());
        for (int i = 0; i < count; i++) {
            reader.readLine();
        }
    }
    
    private void readWords(BufferedReader reader) throws IOException {
        dictionary = new TreeMap<String, WordCard>();
        int count = Integer.valueOf(reader.readLine()); //число строк (псевдооснов)
        for (int i = 0; i < count; i++) {
            String[] wd = reader.readLine().split(" ");
            String wordBase = wd[0].toLowerCase(); //псевдооснова
            if (wordBase.startsWith("-")) continue;
            wordBase = "#".equals(wordBase) ? "" : wordBase; //псевдооснова пуста
            //номер парадигмы (номер строки в первой секции) - набор флексий (окончаний)
            List<FlexiaModel> flexiaModelList = 
                    wordsFlexias.get(Integer.valueOf(wd[1])); 
            FlexiaModel flexiaModel = flexiaModelList.get(0);
            if (flexiaModelList.size() > 0) {
                WordCard newWordCard = new WordCard(flexiaModel.create(wordBase), 
                        wordBase, flexiaModel.getSuffix());
                WordCard existingWordCard = dictionary.get(wordBase);
                for (FlexiaModel fm : flexiaModelList) {
                    if (existingWordCard == null)
                        newWordCard.addFlexia(fm); 
                    else
                        existingWordCard.addFlexia(fm);
                }
                if (existingWordCard == null)
                    dictionary.put(wordBase, newWordCard);
            }
        }
    }
    
    public Map<String, WordCard> getWords(){
        return dictionary;
    }
}