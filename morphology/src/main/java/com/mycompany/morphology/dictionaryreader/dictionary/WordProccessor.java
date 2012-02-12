package com.mycompany.morphology.dictionaryreader.dictionary;

import java.io.IOException;

/**
 * Interface allows get information from
 * {@com.mycompany.morphology.dictionaryreader.dictionary.DirtonaryReader}.
 */
public interface WordProccessor {

    public void process(WordCard wordCard) throws IOException;
}
