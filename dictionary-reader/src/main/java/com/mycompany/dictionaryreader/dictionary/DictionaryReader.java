package com.mycompany.dictionaryreader.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class contain logic how read
 * dictonary and produce word with it all forms.
 */
public class DictionaryReader {
    private String fileName;
    private String fileEncoding = "UTF-8";
    private List<List<FlexiaModel>> wordsFlexias = new ArrayList<List<FlexiaModel>>();
    private List<List<String>> wordPrefixes = new ArrayList<List<String>>();
    private Map<String, WordCard> dictionary;

    public DictionaryReader(String fileName) {
        this.fileName = fileName;
    }
    
    public void proccess() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), fileEncoding));
        readFlexias(bufferedReader);//считывание окончаний
        scipBlock(bufferedReader);//пропуск каких-то чисел (набор ударений?)
        scipBlock(bufferedReader); //и изменений
        readPrefix(bufferedReader);//чтение приставок
        readWords(bufferedReader);//чтение основ
    }

    private void readFlexias(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            ArrayList<FlexiaModel> flexiaModelArrayList = new ArrayList<FlexiaModel>();
            wordsFlexias.add(flexiaModelArrayList);
            for (String line : s.split("\\%")) {
                addFlexia(flexiaModelArrayList, line);
            }
        }
    }

    private void addFlexia(ArrayList<FlexiaModel> flexiaModelArrayList, String line) {
        String[] fl = line.split("\\*");
        // we ignored all forms thats
        if (fl.length == 3) {
            //System.out.println(line + " was ignored.");
        }
        if (fl.length == 2) flexiaModelArrayList.add(new FlexiaModel(fl[1], fl[0].toLowerCase(), "")); //анкод, суффикс, префикс
    }

    private void scipBlock(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
        }
    }

    private void readPrefix(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            wordPrefixes.add(Arrays.asList(s.toLowerCase()));
        }
    }
    
    private void readWords(BufferedReader reader) throws IOException {
        dictionary = new TreeMap<String, WordCard>();
        String s = reader.readLine();//morphs.mrd
        int count = Integer.valueOf(s); //число строк (псевдооснов)
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            String[] wd = s.split(" ");
            String wordBase = wd[0].toLowerCase(); //псевдооснова
            if (wordBase.startsWith("-")) continue;
            wordBase = "#".equals(wordBase) ? "" : wordBase; //псевдооснова пуста
            List<FlexiaModel> flexiaModelList = wordsFlexias.get(Integer.valueOf(wd[1])); //номер парадигмы (номер строки в первой секции)
            FlexiaModel flexiaModel = flexiaModelList.get(0);
            if (flexiaModelList.size() > 0) {
                WordCard card = new WordCard(flexiaModel.create(wordBase), wordBase, flexiaModel.getSuffix());
                for (FlexiaModel fm : flexiaModelList) {
                    if (!dictionary.containsKey(wordBase))
                        card.addFlexia(fm); 
                    else
                        dictionary.get(wordBase).addFlexia(fm);
                }
                if (!dictionary.containsKey(wordBase))
                    dictionary.put(wordBase, card);
            }
        }
    }
    
    public Map<String, WordCard> getWords(){
        return dictionary;
    }
    
    public List getPrefixes() {
        return wordPrefixes;
    }
}