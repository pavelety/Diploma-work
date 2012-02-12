package com.mycompany.morphology.morph;

import java.util.List;

public interface Morphology {

    List<String> getNormalForms(String s);

    List<String> getMorphInfo(String s);

}
