/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Lexicon is a list of words each with zero or more categories.
 * This is a special HashMap where the key is the word and the value
 * is a List of Strings for each category.
 * 
 * Lexicon has a counting feature to process lines of text for words
 * in the lexicon (including counts for each category).  This will 
 * work for any arbitrary dictionary. 
 * 
 * @author conder
 */
public class Lexicon extends LinkedList {
    
    // Excluded words
    public List<String> exclusions = new ArrayList<>();
    
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
    
    /**
     * BEGIN COUNTING METHODS
     * 
     * These methods will manage counters for words in the dictionary.
     * There is only one set of counters, so one should reset them 
     * depending on the granularity of the document or set of documents
     * being parsed.
     */
    
    FileWriter audit = null;                        // Audit file, if present use it.
    Integer countAllWords = 0;                      // Count of all words in document
    Integer preCountAllWords = 0;                   // Count of all words in PRE section
    Integer preCountAllDictionaryWords = 0;         // Count of all dictionary words in PRE section
    Map<String, Integer> preCountCategoryWords =    // Count of all dictionary words in a particular category
            new HashMap<>();
    
    Integer postCountAllCompanyWords = 0;
    Integer postCountAllAnalystWords = 0;
    Map<String, Integer> postCountCategoryCompanyWords =    // Count of all dictionary words in a particular category for company reps.
            new HashMap<>();
    Map<String, Integer> postCountCategoryAnalystWords =    // Count of all dictionary words in a particular category for analysts
            new HashMap<>(); 
    
    public void incrementCountCategories(int type, String word, Term key, Map<String, Integer> map, Integer line_number) {
        
        Logger.getGlobal().fine("," +type + "," + word + "," + key.getCategory() + "," + key.getTerm());
        if (map.containsKey(key.getCategory())) {
                    Integer cc = map.get(key.getCategory());
                    cc++;
                    map.put(key.getCategory(), cc);
        } else {
                    // No counter for category
                    Integer cc = 1;
                    map.put(key.getCategory(), cc);
        }
              
        // Write to audit file:
        // LINE#: type, word, term, category
        if (audit != null) {
        System.out.println( line_number );          
            try {
                audit.write(String.valueOf(line_number));
                audit.write(":");
                audit.write(type == TYPE_PRE ? "PRE": type == TYPE_POST_ANALYST ? "POST-ANALYST": "POST-COMPANY");
                audit.write(",");
                audit.write(word);
                audit.write(",");
                audit.write(key.getTerm());
                audit.write(",");
                audit.write(key.getCategory());
                audit.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
                
    }
    
    public void countPreWords(String line, int line_number) {
        countWords(line, TYPE_PRE, line_number);
    }
    public void countPostWords() {
        
    } 
    
    static Integer TYPE_PRE = 1;
    static Integer TYPE_POST_ANALYST = 2;
    static Integer TYPE_POST_COMPANY = 3;
    
    public void countWords(String line, int type, int line_number) {
                
        // Handle phrases differently than words
        Iterator<Term> terms = this.iterator();
        while (terms.hasNext()) {
            Term term = terms.next();
            String nLine = line;
            if (Utility.isPhrase(term.getTerm())) {
                int index = 0;
				while (index >= 0) {
					index = nLine.indexOf(term.getTerm());
					if (index >= 0) {
                        // In original code, phrases do not appear to increment
                        // overall pre word count.. should VERIFY
                        if (type == TYPE_PRE) {
                            incrementCountCategories(type, nLine, term, preCountCategoryWords, line_number);
                        } else if (type == TYPE_POST_ANALYST) {
                            incrementCountCategories(type, nLine, term, postCountCategoryAnalystWords, line_number);
                        } else if (type == TYPE_POST_COMPANY) {
                            incrementCountCategories(type, nLine, term, postCountCategoryCompanyWords, line_number);
                        }
						nLine = nLine.substring(index + term.getTerm().length());
					}
				}                
            } 
        }
        
        String[] wordsInLine = Utility.splitAndMaskWordsAroundNot(line);
        for (int j = 0; j < wordsInLine.length; j++) {
            
            boolean processed = false;
            List<String> processed_categories = new ArrayList<>();
            countAllWords++;
            String word = wordsInLine[j].toLowerCase();
            if (type == TYPE_PRE) {
                preCountAllWords++;
            } else if (type == TYPE_POST_ANALYST) {
                postCountAllAnalystWords++;
            } else if (type == TYPE_POST_COMPANY) {
                postCountAllCompanyWords++;
            }            
               
            for ( int i = this.size() - 1; i >=0; i--) {
            
                Term term = (Term)this.get(i);
                if (word.startsWith(term.getTerm().toLowerCase()) && 
                            !Utility.isExcludedWord(word, exclusions)) {
                        
                        if (type == TYPE_PRE) {
                            if (!processed) {
                                preCountAllDictionaryWords++;
                                processed = true;
                            }
                            incrementCountCategories(type, word, term, preCountCategoryWords, line_number);
                        } else if (type == TYPE_POST_ANALYST) {
                            incrementCountCategories(type, word, term, postCountCategoryAnalystWords, line_number);
                        } else if (type == TYPE_POST_COMPANY) {
                            incrementCountCategories(type, word, term, postCountCategoryCompanyWords, line_number);
                        }
                        break; 
                }    
            }
        }        
        
    }
    /**
     * END COUNTING METHODS
     */
}
