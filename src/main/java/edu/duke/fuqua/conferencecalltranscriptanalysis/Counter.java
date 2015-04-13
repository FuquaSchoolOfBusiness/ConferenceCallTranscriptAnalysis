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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author conder
 */
public class Counter {
  
    /**
     * BEGIN COUNTING METHODS
     * 
     * These methods will manage counters for words in the dictionary.
     * There is only one set of counters, so one should reset them 
     * depending on the granularity of the document or set of documents
     * being parsed.
     */
    
    Dictionary dictionary = null;
    
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
    
    Map<String, Integer> distanceCounters = new HashMap<>();
    
    public void reset() {
        audit = null;
        countAllWords = 0;
        preCountAllWords = 0;
        preCountAllDictionaryWords = 0;
        postCountAllCompanyWords = 0;
        postCountAllAnalystWords = 0;
        preCountCategoryWords.clear();
        postCountCategoryCompanyWords.clear();
        postCountCategoryAnalystWords.clear();
        distanceCounters.clear();
    }
    
    public void setDictionary(Dictionary d) { this.dictionary = d; }
    public Dictionary getDictionary() { return this.dictionary; }
    
    public void incrementCountCategories(int type, String word, Term key, Map<String, Integer> map, Integer line_number, Integer word_number) {
        
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
            try {
                audit.write(String.valueOf(line_number));
                audit.write(",");
                audit.write(String.valueOf(word_number));
                audit.write(":");
                audit.write("WORD MATCH,");
                audit.write(type == TYPE_PRE ? "PRE": type == TYPE_POST_ANALYST ? "POST-ANALYST": "POST-COMPANY");
                audit.write(",");
                audit.write(word);
                audit.write(",");
                audit.write(key.getTerm());
                audit.write(",");
                audit.write(this.getDictionary() == null ? "":
                        this.getDictionary().getFile() == null ? "": this.getDictionary().getFile().getName());
                audit.write(":");
                audit.write(key.getCategory());
                audit.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
                
    }
    
    public void countPreWords(String line, int line_number) {
        countWords(line, TYPE_PRE, line_number, null);
    }
    public void countPostWords() {
        
    } 
    
    static Integer TYPE_PRE = 1;
    static Integer TYPE_POST_ANALYST = 2;
    static Integer TYPE_POST_COMPANY = 3;
    
    public void checkWordRelations(Match match, List<Match> wordRelations, String line) {
        
        // To check word relations, we must split the line
        // into constituent fragments.
        // Do not include if the punctuation .?!,;: exists between 
        // word1 and word2.  
        
        for (Match relation: wordRelations) {
            
            if (match.line_number.equals(relation.line_number) &&
                    !match.word_number.equals(relation.word_number)) { // &&
                
                Integer relationWordNumber = relation.word_number;
                Integer matchWordNumber = match.word_number;
                
                // Is the match word on the left of relation word?
                if (matchWordNumber < relationWordNumber) {
                    if (match.isPhrase()) matchWordNumber = match.getWord_number_end();
                } else {
                    if (relation.isPhrase()) relationWordNumber = relation.getWord_number_end();
                }
                
                if (Math.abs(matchWordNumber - relationWordNumber) < (this.getDictionary().distanceBetweenWords + 2) &&
                    Logic.sameSentence(matchWordNumber, relationWordNumber, line)) {
                
                    String the_type = match.getType() == TYPE_PRE ? 
                        "PRE": match.getType() == TYPE_POST_ANALYST ? "POST_ANALYST": "POST_COMPANY";
                
                    if (distanceCounters.containsKey(the_type + "_" + match.getTerm().getCategory())) {
                        Integer cc = distanceCounters.get(the_type + "_" + match.getTerm().getCategory());
                        cc++;
                        distanceCounters.put(
                            the_type + "_" + match.getTerm().getCategory(), cc);
                    } else {
                        distanceCounters.put(
                            the_type + "_" + match.getTerm().getCategory(), 
                            new Integer(1));
                    }
                
                    try {
                        if (audit != null){
                            audit.write(String.valueOf(match.getLine_number()));
                            audit.write(":");
                            audit.write("DISTANCE MATCH,");
                            audit.write(match.getType() == TYPE_PRE ? 
                                "PRE": match.getType() == TYPE_POST_ANALYST ? "POST_ANALYST": "POST_COMPANY");
                            audit.write(",");
                            audit.write(match.getWord());
                            audit.write(",");
                            audit.write(String.valueOf(matchWordNumber));
                            audit.write(",");
                            audit.write(relation.getWord());
                            audit.write(",");
                            audit.write(String.valueOf(relationWordNumber));
                            audit.write(",");
                            audit.write(String.valueOf(Math.abs(matchWordNumber - relationWordNumber) - 1));
                            audit.write(",");
                            audit.write(this.getDictionary() == null ? "":
                                this.getDictionary().getFile() == null ? "": this.getDictionary().getFile().getName());
                            audit.write(":");
                            audit.write(match.getTerm().getCategory());
                            audit.write("\n");
                        }
                    } catch (IOException io) {
                    
                    }
                
                }
                
            }
        }
        
    }
    
    public List<Match> countWords(String line, int type, int line_number, List<Match> wordRelations) {
              
        List<Match> matches = new LinkedList<>();
        
        String[] wordsInLine = Logic.splitAndMaskWordsAroundNot(line);
        
        // Handle phrases differently than words
        // Changing algorithm to look for phrases based on parsed words...
        // use the last word number found for phrase for use in counting
        // distances between word phrases and words.
        Iterator<Term> terms = dictionary.iterator();
        while (terms.hasNext()) {
            Term term = terms.next();
            if (Logic.isPhrase(term.getTerm())) {
                if (line.contains(term.getTerm())) {
                    // line has phrase.. now check out matches
                    String[] parts = term.getTerm().split(" ");
                    for (int word_count = 0; word_count < wordsInLine.length; word_count++ ) {
                        if (parts.length > 0 && wordsInLine[word_count].equalsIgnoreCase(parts[0])) {
                            Boolean matched = true;
                            // match on first word of phrase.. check for phrase
                            if (!matched && wordsInLine.length - word_count > parts.length - 1) {
                                for (int parts_count = 0; parts_count < parts.length; parts_count++) {
                                    if (!parts[parts_count].equalsIgnoreCase(wordsInLine[word_count + parts_count])) {
                                        matched = false;
                                    }
                                }
                            }
                            if (matched) {
                                
                                if (type == TYPE_PRE) {
                                    incrementCountCategories(type, line, term, preCountCategoryWords, line_number, word_count);
                                } else if (type == TYPE_POST_ANALYST) {
                                    incrementCountCategories(type, line, term, postCountCategoryAnalystWords, line_number, word_count);
                                } else if (type == TYPE_POST_COMPANY) {
                                    incrementCountCategories(type, line, term, postCountCategoryCompanyWords, line_number, word_count);
                                }
                                
                                Match match = new Match();
                                match.setLine_number(line_number);
                                match.setWord_number(word_count);               // if match is on left side of phrase
                                match.setWord_number_end(                       // use first word of phrase, else use
                                        word_count + parts.length - 1);             // right side of phrase... won't know  
                                                                                // this until comparing matches... so
                                                                                // will need to store BOTH.
                                match.setWord(term.getTerm());
                                match.setTerm(term);
                                match.setType(type);
                                matches.add(match);  
                                
                                if (wordRelations != null) {
                                    checkWordRelations(match, wordRelations, line);
                                }
                            }
                            
                        }
                    }
                }
                                
            } 
        }
        
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
               
            for ( int i = dictionary.size() - 1; i >=0; i--) {
            
                Term term = (Term)dictionary.get(i);
                if (word.startsWith(term.getTerm().toLowerCase()) && 
                            !Logic.isExcludedWord(word, dictionary.exclusions)) {
                        
                        if (type == TYPE_PRE) {
                            if (!processed) {
                                preCountAllDictionaryWords++;
                                processed = true;
                            }
                            incrementCountCategories(type, word, term, preCountCategoryWords, line_number, j);
                        } else if (type == TYPE_POST_ANALYST) {
                            incrementCountCategories(type, word, term, postCountCategoryAnalystWords, line_number, j);
                        } else if (type == TYPE_POST_COMPANY) {
                            incrementCountCategories(type, word, term, postCountCategoryCompanyWords, line_number, j);
                        }
                        
                        Match match = new Match();
                        match.setLine_number(line_number);
                        match.setWord_number(j);
                        match.setWord(word);
                        match.setTerm(term);
                        match.setType(type);
                        matches.add(match);
                        
                        if (wordRelations != null) {
                            checkWordRelations(match, wordRelations, line);
                        }
                        
                        break; 
                }    
            }
            
        }        
        
        return matches;
    }
    /**
     * END COUNTING METHODS
     */    
    
}
