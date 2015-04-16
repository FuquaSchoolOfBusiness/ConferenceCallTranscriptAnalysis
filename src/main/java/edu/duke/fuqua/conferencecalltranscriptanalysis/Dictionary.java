/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Dictionary is a list of words each with zero or more categories.
 * This is a special HashMap where the key is the word and the value
 is a List of Strings for each category.
 
 Dictionary has a counting feature to process lines of text for words
 in the lexicon (including counts for each category).  This will 
 work for any arbitrary dictionary. 
 * 
 * @author conder
 */
public class Dictionary extends LinkedList {
    
    File file;
    Integer distanceBetweenWords = 3;               // Maximum number of words between stems
    
    public void setFile(File f) { this.file = f; }
    public File getFile() { return this.file; }
    
    // Excluded words
    public ArrayList<String> exclusions = new ArrayList<>();
    
    // Categories of terms
    public Boolean contains(String termString) {
        Iterator<Term> it = this.iterator();
        while (it.hasNext()) {
            Term term = it.next();
            if (termString.equals(term.getTerm())) return true;
        }
        return false;
    }
    public List<String> categories = null;
    public Integer getNumberOfTerms() { return this.size(); }
    public Integer getNumberOfTerms(String category) {
            int count = 0;
            Iterator it = this.iterator();
            while (it.hasNext()) {
                Term term = (Term)it.next();
                if (category.equals( term.getCategory() ) ) count++;
            }
            return count;
    }
    public Integer getNumberOfCategories() { return getCategories().size(); }
    public List<String> getCategories() {
        if (categories == null) {
            ArrayList<String> list = new ArrayList<>();
            Iterator it = this.iterator();
            while (it.hasNext()) {
                Term term = (Term)it.next();
                if (term.getCategory() != null &&
                        !list.contains(term.getCategory())) {
                    list.add(term.getCategory());
                }            
            }
            categories = list;
        }
        return categories;
    }
    
    public Object clone() { 
        
        Dictionary d = new Dictionary();
        d.distanceBetweenWords = this.distanceBetweenWords;
        d.file = this.getFile();
        d.exclusions = (ArrayList)this.exclusions.clone();
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Term term = (Term)it.next();
            d.add(term.clone());
        }
        return d;
    }
}
