/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.util.LinkedList;
import java.util.List;

/**
 * Term is a component of a Dictionary.
 * It can have up to one category.
 * It also has a sort order.
 * Terms may much multiple words, but 
 * the word with the highest sort order
 * is considered the best match.
 * @author conder
 */
public class Term {
    
    String term;
    String category;
    Integer order;
    
    List<String> preMatches = new LinkedList<>();
    List<String> postCompanyMatches = new LinkedList<>();
    List<String> postAnalystMatches = new LinkedList<>();
    
    public void setTerm(String term) { this.term = term; }
    public void setCategory(String category) { this.category = category; }
    public void setOrder(int order) { this.order = order; }
    
    public String getTerm() { return this.term; }
    public String getCategory() { return this.category; }
    public Integer getOrder() { return this.order; }
    
    public List<String> getPreMatches() { return this.preMatches; }
    public List<String> getPostCompanyMatches() { return this.postCompanyMatches; }
    public List<String> getPostAnalystMatches() { return this.postAnalystMatches; }
    
    public String toString() { return this.getTerm(); }
    
}
