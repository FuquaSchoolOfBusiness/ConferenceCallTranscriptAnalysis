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
    Integer distanceBetweenWords = 3;               // Maximum number of words between stems
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
                audit.write(key.getCategory());
                audit.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
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
            /*if (match.line_number == 299) {
                System.out.println(
                        match.getWord()+ " (" + 
                                match.getWord_number() + 
                                ") relation " + 
                                relation.getWord() + 
                                " (" + 
                                relation.getWord_number() + 
                                ") " +
                                relation.line_number +
                                (match.line_number.equals(relation.line_number)) +
                                Math.abs(match.word_number - relation.word_number) +
                                Utility.sameSentence(match.word_number, relation.word_number, line)
            }*/
            if (match.line_number.equals(relation.line_number) &&
                    !match.word_number.equals(relation.word_number) &&
                    Math.abs(match.word_number - relation.word_number) < (distanceBetweenWords + 2) &&
                    Utility.sameSentence(match.word_number, relation.word_number, line)) {
                
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
                    audit.write(String.valueOf(match.getWord_number()));
                    audit.write(",");
                    audit.write(relation.getWord());
                    audit.write(",");
                    audit.write(String.valueOf(relation.getWord_number()));
                    audit.write(",");
                    audit.write(String.valueOf(Math.abs(match.getWord_number() - relation.getWord_number()) - 1));
                    audit.write(",");
                    audit.write(match.getTerm().getCategory());
                    audit.write("\n");
                    }
                } catch (IOException io) {
                    
                }
                
            }
        }
        
    }
    
    public List<Match> countWords(String line, int type, int line_number, List<Match> wordRelations) {
              
        List<Match> matches = new LinkedList<>();
        
        String[] wordsInLine = Utility.splitAndMaskWordsAroundNot(line);
        
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
                        if (type == TYPE_PRE) {
                            incrementCountCategories(type, nLine, term, preCountCategoryWords, line_number, null);
                        } else if (type == TYPE_POST_ANALYST) {
                            incrementCountCategories(type, nLine, term, postCountCategoryAnalystWords, line_number, null);
                        } else if (type == TYPE_POST_COMPANY) {
                            incrementCountCategories(type, nLine, term, postCountCategoryCompanyWords, line_number, null);
                        }
						nLine = nLine.substring(index + term.getTerm().length());
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
               
            for ( int i = this.size() - 1; i >=0; i--) {
            
                Term term = (Term)this.get(i);
                if (word.startsWith(term.getTerm().toLowerCase()) && 
                            !Utility.isExcludedWord(word, exclusions)) {
                        
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
                        
         //if (line_number == 299) System.out.println("LINE 299: " + match.getWord_number() + " " + match.getWord());
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
