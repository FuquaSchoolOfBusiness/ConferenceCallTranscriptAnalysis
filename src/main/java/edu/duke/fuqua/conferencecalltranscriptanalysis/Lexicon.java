/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class Lexicon extends HashMap {
    
    // Excluded words
    public List<String> exclusions = new ArrayList<>();
    
    // Categories of terms
    public List<String> categories = null;
    public Integer getNumberOfTerms() { return this.size(); }
    public Integer getNumberOfTerms(String category) {
            int count = 0;
            Iterator it = this.values().iterator();
            while (it.hasNext()) {
                List<String> categoriesList = (List<String>)it.next();
                Iterator<String> valueIt = categoriesList.iterator();
                while (valueIt.hasNext()) {
                    String value = valueIt.next();
                    if (value.equalsIgnoreCase(category)) count++;
                }
            }
            return count;
    }
    public Integer getNumberOfCategories() { return getCategories().size(); }
    public List<String> getCategories() {
        if (categories == null) {
            ArrayList<String> list = new ArrayList<>();
            Iterator it = this.values().iterator();
            while (it.hasNext()) {
                List<String> categoriesList = (List<String>)it.next();
                Iterator<String> valueIt = categoriesList.iterator();
                while (valueIt.hasNext()) {
                    String value = valueIt.next().toLowerCase();
                    if (!list.contains(value)) 
                        list.add(value);
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
    
    public void incrementCountCategories(String key, Map<String, Integer> map) {
        List<String> cats = (List)this.get(key);
        Iterator<String> iterator = cats.iterator();
        while (iterator.hasNext()) {
            String category = iterator.next();
System.out.println("," + category + "," + key);
            if (map.containsKey(category)) {
                Integer cc = map.get(category);
                cc++;
                map.put(category, cc);
            } else {
                // No counter for category
                Integer cc = 1;
                map.put(category, cc);
            }
        }
    }
    
    public void countPreWords(String line) {
        countWords(line, TYPE_PRE);
    }
    public void countPostWords() {
        
    } 
    
    static Integer TYPE_PRE = 1;
    static Integer TYPE_POST_ANALYST = 2;
    static Integer TYPE_POST_COMPANY = 3;
    
    public void countWords(String line, int type) {
                
        int iteration = 0;
        Iterator<String> keys = this.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            iteration++;
            // Handle phrases differently than words
            if (Utility.isPhrase(key)) {
                
                int index = 0;
				while (index >= 0) {
					index = line.indexOf(key);
					if (index >= 0) {
                        // In original code, phrases do not appear to increment
                        // overall pre word count.. should VERIFY
                        if (type == TYPE_PRE) {
                            incrementCountCategories(key, preCountCategoryWords);
                        } else if (type == TYPE_POST_ANALYST) {
                            incrementCountCategories(key, postCountCategoryAnalystWords);
                        } else if (type == TYPE_POST_COMPANY) {
                            incrementCountCategories(key, postCountCategoryCompanyWords);
                        }
						line = line.substring(index + key.length());
					}
				}                
            
            } 
            else // Handle as special array of words
            {

                String[] wordsInLine = Utility.splitAndMaskWordsAroundNot(line);

		        for (int j = 0; j < wordsInLine.length; j++) {
					String word = wordsInLine[j].toLowerCase();
                    if (iteration < 2) { 
                        countAllWords++;
                        if (type == TYPE_PRE) {
                            preCountAllWords++;
                        } else if (type == TYPE_POST_ANALYST) {
                            postCountAllAnalystWords++;
                        } else if (type == TYPE_POST_COMPANY) {
                            postCountAllCompanyWords++;
                        }
                    }     
                    
                    if (word.startsWith(key.toLowerCase()) && 
                            !Utility.isExcludedWord(word, exclusions)) {
                        
                        if (type == TYPE_PRE) {
                            preCountAllDictionaryWords++;
System.out.print(word);
                            incrementCountCategories(key, preCountCategoryWords);
                        } else if (type == TYPE_POST_ANALYST) {
                            incrementCountCategories(key, postCountCategoryAnalystWords);
                        } else if (type == TYPE_POST_COMPANY) {
                            incrementCountCategories(key, postCountCategoryCompanyWords);
                        }
                          
                    }
                                        
				}
                
            }
        }
        
    }
    /**
     * END COUNTING METHODS
     */
}
