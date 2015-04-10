/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

/**
 * Name is a class to track Company Reps and Analysts.
 * @author conder
 */
public class Name {
   
    public static final Integer TYPE_COMPANY_REP = 1;
    public static final Integer TYPE_ANALYST = 2;
    public static final Integer TYPE_OPERATOR = 3;
    
    String name;
    Integer type;
    String line;
    Integer number;

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setType(Integer type) { this.type = type; }
    public Integer getType() { return this.type; }
    public void setLine(String line) { this.line = line; }
    public String getLine() { return this.line; }
    public void setNumber(Integer number) { this.number = number; }
    public Integer getNumber() { return this.number; }
     
    public static Name generateName(Integer line_number, Integer type, String line, String name) {
        Name nameObj = new Name();
        nameObj.setNumber(line_number);
        nameObj.setType(type);
        nameObj.setLine(line);
        nameObj.setName(name);  
        return nameObj;
    }
}
