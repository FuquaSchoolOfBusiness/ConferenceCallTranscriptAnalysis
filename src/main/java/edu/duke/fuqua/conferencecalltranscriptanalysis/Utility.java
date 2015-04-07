package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Utility {
	
	public final static int NUMBER_OF_COMPANIES = 226;
	
	public Utility() {
		
	}

	public ArrayList<String> makeCompanyList(String companyPath) throws FileNotFoundException {
		FileReader companylist = new FileReader(companyPath);
		Scanner companies = new Scanner(companylist);
		ArrayList<String> companynames = new ArrayList<String>();
		while (companies.hasNextLine())
		{
			String companyname = companies.nextLine();
			companynames.add(companyname);
		}
		companies.close();
		return companynames;
	}
	
	public static int getNumericSuffix(String s) {
		int result = 0;
		int exp = 1;
		for (int i = s.length()-1; i >= 0; i--) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') break;
			result += exp * (c - '0');
			exp = 10 * exp;
		}
		return result;
	}
	
	public static String stripNumericSuffix(String s) {
		int i = s.length() - 1;
		for (; i >= 0; i--) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') break;
		}
		return s.substring(0,  i+1);
	}
	
	public ArrayList<ArrayList<String>> makeNamesList(String namePath) throws FileNotFoundException{
		FileReader namelist = new FileReader(namePath);
		Scanner names = new Scanner(namelist);
		
		ArrayList<ArrayList<String>> members = new ArrayList<ArrayList<String>>();
		for (int i =0; i < NUMBER_OF_COMPANIES; i++)
		{
			ArrayList<String> component = new ArrayList<String>();
			members.add(component);
		}
		while (names.hasNextLine())
		{
			String name = names.nextLine();
			
			name = name.replaceAll("[^A-Za-z0-9 .]", "").trim();
			for (int i = 1; i <= NUMBER_OF_COMPANIES; i++)
			{	
				String type = Integer.toString(i);
				String addition = Integer.toString(i + 100);
				String addition2 = Integer.toString(i + 200);
				if (name.contains(type) && !name.contains(addition) && !name.contains(addition2))
				{
					members.get(i-1).add(name.replace(type, ""));
				}
			}
		}
		
		names.close();
		return members;
	}
	
	public static String[] splitAndMaskWordsAroundNot(String text) {
        String[] words = text.split("\\s+");
        //ignore words around "not" (next to, or one word away) - G.H. (this can be much more efficient with a regex)
        for (int i=0; i<words.length; i++) {
        	words[i] = stripEndingPunctuation(words[i]);
        	if (words[i].equalsIgnoreCase("not")) {
        		if (i - 2 > 0) words[i-2] = "NNNNN";
        		if (i - 1 > 0) words[i-1] = "NNNNN";
        		if (i + 1 < words.length) words[i+1] = "NNNNN";
        		if (i + 2 < words.length) words[i+2] = "NNNNN";
        		i += 2;
        	}
        }
		return words;
	}
	
	public static String[] splitAndMaskWordsAroundNotLeavePunctuation(String text) {
        String[] words = text.split("\\s+");
        //ignore words around "not" (next to, or one word away) - G.H. (this can be much more efficient with a regex)
        for (int i=0; i<words.length; i++) {
        	if (words[i].equalsIgnoreCase("not")) {
        		if (i - 2 > 0) words[i-2] = "NNNNN";
        		if (i - 1 > 0) words[i-1] = "NNNNN";
        		if (i + 1 < words.length) words[i+1] = "NNNNN";
        		if (i + 2 < words.length) words[i+2] = "NNNNN";
        		i += 2;
        	}
        }
		return words;
	}
    
    public static boolean sameSentence(Integer word1, Integer word2, String line) {
        String[] words = splitAndMaskWordsAroundNotLeavePunctuation(line);
        Integer start = word1 < word2 ? word1: word2;
        Integer end = word1 < word2 ? word2: word1;
        for (int i=start; i < end; i++) {
            if (words[i].contains(".") || 
                    words[i].contains("?") || 
                    words[i].contains("!") || 
                    words[i].contains(",") ||
                    words[i].contains(";") ||
                    words[i].contains(":"))
                return false;
        }
        return true;
    }
    
	public static boolean isExcludedWord(String word, List<String> pertinentExcludedWords) {
		for (String EWord : pertinentExcludedWords) {
			if (word.startsWith(EWord.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
    public static boolean isNumeric(String str)
    {
      return str.matches("-?\\d+(\\.\\d+)?");
    }
    
	public static boolean isMonth(String str) {
		str = str.toLowerCase();
		if (str.contains("january") | str.contains("february")
				| str.contains("march") | str.contains("april")
				| str.contains("may") | str.contains("june")
				| str.contains("july") | str.contains("august")
				| str.contains("september") | str.contains("october")
				| str.contains("november") | str.contains("december")
				| str.equals("jan") | str.equals("feb")
				| str.equals("mar") | str.equals("apr")
				| str.equals("jun") | str.equals("jul")
				| str.equals("aug") | str.equals("sep")
				| str.equals("oct") | str.equals("nov") | str.equals("dec") ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<String> isKnowledgeWord(ArrayList<ArrayList<String>> knowledgeWords, String word) {
		for (ArrayList<String> category : knowledgeWords) {
			for (String KWord : category) {
				if (word.startsWith(KWord.toLowerCase())) {
					return category;
				}
			}
		}
		return new ArrayList<String>();
	}
	
	public static String stripEndingPunctuation(String str) {
		str = str.replaceFirst("^[^a-zA-Z]+", "");
		str = str.replaceAll("[^a-zA-Z]+$", "");
		return str;
	}
    
    public static Boolean isPhrase(String s) {
        return s.contains(" ");
        // This is how it tested originally.. seems expensive to me.
        //return s.split(" ").length > 1;
    }
}