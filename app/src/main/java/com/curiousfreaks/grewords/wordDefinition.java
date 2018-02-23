package com.curiousfreaks.grewords;

/**
 * Created by gasaini on 2/24/2018.
 */

public class wordDefinition {
    private String word,attr1;
    private long id;
    public wordDefinition()
    {

    }
    public wordDefinition(long id, String word, String attr1)
    {
        this.id=id;
        this.word=word;
        this.attr1=attr1;
    }
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



}
