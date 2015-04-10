/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

/**
 * Match is used to do the dictionary to dictionary word distance 
 * comparison.  The word numbers are used to determine distance
 * between words (the absolute value of the difference).
 * @author conder
 */
public class Match {
    
    public String word;
    public Term term;
    public Integer word_number;
    public Integer word_number_end;
    public Integer line_number;
    public Integer type;
    
    public void setWord(String word) { this.word = word; }
    public String getWord() { return word; }
    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }
    public Integer getWord_number() { return this.word_number; }
    public void setWord_number(Integer number) { this.word_number = number; }
    public Integer getWord_number_end() { return this.word_number_end == null ? this.getWord_number(): this.word_number_end; };
    public void setWord_number_end(Integer number) { this.word_number_end = number; }
    public Integer getLine_number() { return this.line_number; }
    public void setLine_number(Integer number) { this.line_number = number; } 
    public Integer getType() { return this.type; }
    public void setType(Integer type) { this.type = type; }
    public Boolean isPhrase() { return !(this.getWord_number() == this.getWord_number_end()); }
    
}
