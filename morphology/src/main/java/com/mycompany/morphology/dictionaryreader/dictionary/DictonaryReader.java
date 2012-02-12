package com.mycompany.morphology.dictionaryreader.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class contain logic how read
 * dictonary and produce word with it all forms.
 */
public class DictonaryReader {
    private String fileName;
    private String fileEncoding = "UTF-8"; //было - "windows-1251";
    private List<List<FlexiaModel>> wordsFlexias = new ArrayList<List<FlexiaModel>>();
    private List<List<String>> wordPrefixes = new ArrayList<List<String>>();
    private Set<String> ignoredForm = new HashSet<String>();

    public DictonaryReader(String fileName, Set<String> ignoredForm) {
        this.fileName = fileName;
        this.ignoredForm = ignoredForm;
    }

    public DictonaryReader(String fileName, String fileEncoding, Set<String> ignoredForm) {
        this.fileName = fileName; //morphs.mrd
        this.fileEncoding = fileEncoding;
        this.ignoredForm = ignoredForm;
    }

    public void proccess(WordProccessor wordProccessor) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), fileEncoding));
        readFlexias(bufferedReader);//считывание окончаний
        scipBlock(bufferedReader);//пропуск каких-то чисел (набор ударений?)
        scipBlock(bufferedReader); //и изменений
        readPrefix(bufferedReader);//чтение приставок
        readWords(bufferedReader, wordProccessor);
    }

    private void readFlexias(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            ArrayList<FlexiaModel> flexiaModelArrayList = new ArrayList<FlexiaModel>();
            wordsFlexias.add(flexiaModelArrayList);
            for (String line : s.split("%")) {
                addFlexia(flexiaModelArrayList, line);
            }
        }
    }

    private void addFlexia(ArrayList<FlexiaModel> flexiaModelArrayList, String line) {
        String[] fl = line.split("*"); //было - String[] fl = line.split("\\*"); 
        // we ignored all forms thats
        if (fl.length == 3) {
            System.out.println(line + " was ignored.");
            // flexiaModelArrayList.add(new FlexiaModel(fl[1], cleanString(fl[0].toLowerCase()), cleanString(fl[2].toLowerCase())));
        }
        if (fl.length == 2) flexiaModelArrayList.add(new FlexiaModel(fl[1], fl[0].toLowerCase(), "")); //анкод, суффикс, префикс
    }

    private void scipBlock(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine(); //s нигде не используется
        }
    }


    private void readPrefix(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        int count = Integer.valueOf(s);
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            wordPrefixes.add(Arrays.asList(s.toLowerCase())); // было - wordPrefixes.add(Arrays.asList(s.toLowerCase().split(",")));
        }
    }
    
    private void readWords(BufferedReader reader, WordProccessor wordProccessor) throws IOException {
        String s = reader.readLine();//morphs.mrd
        int count = Integer.valueOf(s); //число строк одного вида
        for (int i = 0; i < count; i++) {
            s = reader.readLine();
            if (i % 10000 == 0) System.out.println("Proccess " + i + " wordBase of " + count);
            String[] wd = s.split(" ");
            String wordBase = wd[0].toLowerCase(); //псевдооснова
            if (wordBase.startsWith("-")) continue;
            wordBase = "#".equals(wordBase) ? "" : wordBase; //псевдооснова пуста
            List<FlexiaModel> models = wordsFlexias.get(Integer.valueOf(wd[1])); //номер парадигмы (номер строки в первой секции)
            FlexiaModel flexiaModel = models.get(0);
            if (models.size() > 0 && !ignoredForm.contains(flexiaModel.getCode())) {
                WordCard card = new WordCard(flexiaModel.create(wordBase), wordBase, flexiaModel.getSuffix());
                for (FlexiaModel fm : models) {
                    card.addFlexia(fm);
                }
                /*if(card.getBase().equals("face") || card.getBase().equals("fac")){
                    System.out.println(models);
                    System.out.println(card);*/
                    wordProccessor.process(card); //egramtab.tab
                //}
            }
        }
    }
}