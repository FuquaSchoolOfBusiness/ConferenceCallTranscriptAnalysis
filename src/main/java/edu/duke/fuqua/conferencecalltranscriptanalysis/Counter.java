package edu.duke.fuqua.conferencecalltranscriptanalysis;

/**
 * 
 * @author Geoff
 * @version 1.0
 * 
 * Basic scan to compare a file with the created Dictionaries
 * Returns a string output of commaseparated values
 * 
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.*;

public class Counter {
	// Parameters
	// Output integers counting each type of word
	// Input arrays for dictionaries

	protected int myMarketingPreWords;
	private Map<String, Integer> myMarketingPreWordsAudit = new HashMap<String, Integer>();
	protected int myStockPreWords;
	private Map<String, Integer> myStockPreWordsAudit = new HashMap<String, Integer>();
	protected int mySearchPreWords;
	private Map<String, Integer> mySearchPreWordsAudit = new HashMap<String, Integer>();
	protected int myAcquisitionPreWords;
	private Map<String, Integer> myAcquisitionPreWordsAudit = new HashMap<String, Integer>();
	protected int myProcessPreWords;
	private Map<String, Integer> myProcessPreWordsAudit = new HashMap<String, Integer>();
	protected int myStoragePreWords;
	private Map<String, Integer> myStoragePreWordsAudit = new HashMap<String, Integer>();
	protected int myTransferPreWords;
	private Map<String, Integer> myTransferPreWordsAudit = new HashMap<String, Integer>();
	protected int myApplicationPreWords;
	private Map<String, Integer> myApplicationPreWordsAudit = new HashMap<String, Integer>();
	protected int myProtectionPreWords;
	private Map<String, Integer> myProtectionPreWordsAudit = new HashMap<String, Integer>();

	protected ArrayList<String> myFinalKnowledgeExcludedWords;
	protected ArrayList<String> myFinalMarketingExcludedWords;
//	protected ArrayList<String> myFinalDict3ExcludedWords;
//	protected ArrayList<String> myFinalDict3Words;
	protected ArrayList<String> myFinalMarketingWords;
	protected ArrayList<ArrayList<String>> myFinalKnowledgeWords;
	protected ArrayList<ArrayList<String>> multipleWordKnowledge;
	protected String date = "";
	protected int totalNumberOfWords = 0;
	protected boolean loggingEnabled = true;
	protected Utility u;

	// Constructor
	public Counter(KDictionary KW, Dictionary KEW, Dictionary MW, Dictionary MEW, boolean logging) {
		date = "";
		totalNumberOfWords = 0;
		loggingEnabled = logging;
		u = new Utility();
		
		myFinalKnowledgeWords = KW.CreateArrays();
		myFinalKnowledgeExcludedWords = KEW.CreateArray();
		myFinalMarketingWords = MW.CreateArray();
		myFinalMarketingExcludedWords = MEW.CreateArray();
//		myFinalDict3Words = D3W.CreateArray();
//		myFinalDict3ExcludedWords = D3EW.CreateArray();

		multipleWordKnowledge = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < myFinalKnowledgeWords.size(); i++) {
			ArrayList<String> al = new ArrayList<String>();
			multipleWordKnowledge.add(al);
		}
		for (int i = 0; i < myFinalKnowledgeWords.size(); i++) {
			ArrayList<String> al = myFinalKnowledgeWords.get(i);
			for (int j = 0; j < al.size(); j++) {
				String s = al.get(j);
				if (s.split(" ").length > 1) {
					multipleWordKnowledge.get(i).add(s);
				}
			}
		}
	}

	// Analyze Pre Q/A text
	public String AnalyzeFile(Counter c, String inputFile)
			throws FileNotFoundException {
		FileReader reader = new FileReader(inputFile);
		Scanner in = new Scanner(reader);

		totalNumberOfWords = 0;

		myMarketingPreWords = 0;
		myStockPreWords = 0;
		mySearchPreWords = 0;
		myAcquisitionPreWords = 0;
		myProcessPreWords = 0;
		myStoragePreWords = 0;
		myTransferPreWords = 0;
		myApplicationPreWords = 0;
		myProtectionPreWords = 0;
		myMarketingPreWordsAudit = new HashMap<String, Integer>();
		myStockPreWordsAudit = new HashMap<String, Integer>();
		mySearchPreWordsAudit = new HashMap<String, Integer>();
		myAcquisitionPreWordsAudit = new HashMap<String, Integer>();
		myProcessPreWordsAudit = new HashMap<String, Integer>();
		myStoragePreWordsAudit = new HashMap<String, Integer>();
		myTransferPreWordsAudit = new HashMap<String, Integer>();
		myApplicationPreWordsAudit = new HashMap<String, Integer>();
		myProtectionPreWordsAudit = new HashMap<String, Integer>();

		int i = 0;
		boolean dateFound = false;
		while (in.hasNextLine()) {
			i++;
			String line = in.nextLine();

			String lcline = line.toLowerCase();
			try {
				if (i < 20 && dateFound == false && Utility.isMonth(lcline)) {
					String[] lineArray = lcline.split(" ");
					if (!Utility.isMonth(lineArray[0]) && !Utility.isMonth(lineArray[1])) { // example: Q1 2003 Abbott
												// Laboratories Earnings
												// Conference Call - Final FD
												// (Fair Disclosure) Wire April
												// 9, 2003 Wednesday
					  int z = 0;
	  				  String m = "", d = "", y = "";
				  
	  				  while (z < lineArray.length) {
	  					  if (Utility.isMonth(lineArray[z])) {
	  						  m = lineArray[z];
	  					  	  break;
	  					  }
	  					  z++;
	  				  }
				  
	  				  if (lineArray.length > (z + 2)) {
	  					  d = lineArray[z + 1].replace(",","");
						  d = d.replaceAll("[^\\d]", "");
	  					  y = lineArray[z + 2].replace(".","");
						  if (isNumeric(d) && isNumeric(y)) {
							  date = d + " " + m + " " + y;
							  dateFound = true;
						  }
	  				  }
					} else {
						// example: April 9, 2002 (need to transform it to 9 April 2002)
						if (line.contains(",")) {
							lineArray[1] = lineArray[1].replace(",", "");
							date = lineArray[1] + " " + lineArray[0] + " " + lineArray[2];
							dateFound = true;
						} // example: 9 April 2002 
						else {
							if (line.contains("/")) {
								String[] parts = line.split("/");
								line = parts[0].trim();
							}
							date = line;
							dateFound = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

				// System.out.println("Pressing on");
			}
			if (line.contains("QUESTION AND ANSWER")) {
				break;
			} else {
				countPhrases(line);
				
		        String[] wordsInLine = Utility.splitAndMaskWordsAroundNot(line);

		        for (int j = 0; j < wordsInLine.length; j++) {
					String word = wordsInLine[j].toLowerCase();
					totalNumberOfWords++;
					count(word, line);
				}
			}
		}
		
		in.close();
		// Print out counts
		return (date + ", " + totalNumberOfWords + ", " + myMarketingPreWords
				+ ", " + myStockPreWords + ", " + mySearchPreWords + ", "
				+ myAcquisitionPreWords + ", " + myProcessPreWords + ", "
				+ myStoragePreWords + ", " + myTransferPreWords + ", "
				+ myApplicationPreWords + ", " + myProtectionPreWords);
	}

	private void countPhrases(String line) {
		for (int i = 0; i < multipleWordKnowledge.size(); i++) {
			ArrayList<String> kList = multipleWordKnowledge.get(i);
			for (String kPhrase : kList) {
				int index = 0;
				String phrase = kPhrase;
				while (index >= 0) {
					index = line.indexOf(phrase);
					if (index >= 0) {
						assignKnowledgeType(i, phrase);
						System.out.println("phrase (" + i + "): " + phrase);
				        if (loggingEnabled) Log.write(phrase + "\t" + "\t" + line);
						line = line.substring(index + kPhrase.length());
					}
				}
			}
		}
	}

	private void count(String word, String line) {
		// Count marketing words
		for (String MWord : myFinalMarketingWords) {
			if (word.startsWith(MWord.toLowerCase()) && !Utility.isExcludedWord(word, myFinalMarketingExcludedWords)) {
				myMarketingPreWords++;
				this.addWord(myMarketingPreWordsAudit, MWord,
						"myMarketingPreWords");
		        if (loggingEnabled) Log.write(MWord + "\t" + "\t" + line);
			}
		}

		// Don't include excluded words list
		ArrayList<String> category = Utility.isKnowledgeWord(myFinalKnowledgeWords, word);
		boolean knowledgeWord = true;
		if (category.size() == 0) knowledgeWord = false;
		if (knowledgeWord && !Utility.isExcludedWord(word, myFinalKnowledgeExcludedWords)) {
			int type = myFinalKnowledgeWords.indexOf(category);
			assignKnowledgeType(type, word);
		}
	}

	private void assignKnowledgeType(int type, String MWord) {
		if (type == 0) {
			myStockPreWords++;
			this.addWord(myStockPreWordsAudit, MWord, "myStockPreWords");
		}
		if (type == 1) {
			mySearchPreWords++;
			this.addWord(mySearchPreWordsAudit, MWord, "mySearchPreWords");
		}
		if (type == 2) {
			myAcquisitionPreWords++;
			this.addWord(myAcquisitionPreWordsAudit, MWord,
					"myAcquisitionPreWords");
		}
		if (type == 3) {
			myProcessPreWords++;
			this.addWord(myProcessPreWordsAudit, MWord, "myProcessPreWords");
		}
		if (type == 4) {
			myStoragePreWords++;
			this.addWord(myStoragePreWordsAudit, MWord, "myStoragePreWords");
		}
		if (type == 5) {
			myTransferPreWords++;
			this.addWord(myTransferPreWordsAudit, MWord, "myTransferPreWords");
		}
		if (type == 6) {
			myApplicationPreWords++;
			this.addWord(myApplicationPreWordsAudit, MWord,
					"myApplicationPreWords");
		}
		if (type == 7) {
			myProtectionPreWords++;
			this.addWord(myProtectionPreWordsAudit, MWord,
					"myProtectionPreWords");
		}
	}

	private void addWord(Map<String,Integer> amap, String MWord, String amapname) {
		if (MWord.indexOf(",") >= 0)
			MWord = MWord.replace(',', ';');
		if (amap.containsKey(MWord.toLowerCase())) {
			// System.out.println("Counting "+amapname+" found :" +
			// MWord.toLowerCase());//xxxxx
			amap.put(MWord.toLowerCase(),
					(((Integer) amap.get(MWord.toLowerCase())).intValue() + 1));
		} else {
			// System.out.println("Counting "+amapname+" not found :" +
			// MWord.toLowerCase());//xxxxx
			amap.put(MWord.toLowerCase(), 1);
		}
	}

	public void printCollections() {

		this.printCollections(myMarketingPreWordsAudit, "Pre_Marketing");
		this.printCollections(myStockPreWordsAudit, "Pre_Stock");
		this.printCollections(mySearchPreWordsAudit, "Pre_Search");
		this.printCollections(myAcquisitionPreWordsAudit, "Pre_Acquisition");
		this.printCollections(myProcessPreWordsAudit, "Pre_Process");
		this.printCollections(myStoragePreWordsAudit, "Pre_Storage");
		this.printCollections(myTransferPreWordsAudit, "Pre_Transfer");
		this.printCollections(myApplicationPreWordsAudit, "Pre_Application");
		this.printCollections(myProtectionPreWordsAudit, "Pre_Protection");

	}

	public void printCollections(PrintWriter CombinedOutput) {
		CombinedOutput.println("\nCounter Collections:\n");
		this.printCollections(myMarketingPreWordsAudit, "Pre_Marketing",
				CombinedOutput);
		this.printCollections(myStockPreWordsAudit, "Pre_Stock", CombinedOutput);
		this.printCollections(mySearchPreWordsAudit, "Pre_Search",
				CombinedOutput);
		this.printCollections(myAcquisitionPreWordsAudit, "Pre_Acquisition",
				CombinedOutput);
		this.printCollections(myProcessPreWordsAudit, "Pre_Process",
				CombinedOutput);
		this.printCollections(myStoragePreWordsAudit, "Pre_Storage",
				CombinedOutput);
		this.printCollections(myTransferPreWordsAudit, "Pre_Transfer",
				CombinedOutput);
		this.printCollections(myApplicationPreWordsAudit, "Pre_Application",
				CombinedOutput);
		this.printCollections(myProtectionPreWordsAudit, "Pre_Protection",
				CombinedOutput);
	}

	private void printCollections(Map<String, Integer> amap, String name) {

		for (java.util.Map.Entry<String, Integer> entry : amap.entrySet()) {
			System.out.println("Collection Name=" + name + " Word="
					+ entry.getKey() + " , Count="
					+ entry.getValue().toString());
		}
	}

	private void printCollections(Map<String, Integer> amap, String name,
			PrintWriter output) {
		output.println("Collection Name: " + name);
		for (java.util.Map.Entry<String, Integer> entry : amap.entrySet()) {
			output.print("Word=" + entry.getKey() + " Count="
					+ entry.getValue().toString() + ",");
		}
		if (!amap.isEmpty())
			output.println();
	}
	
    private boolean isNumeric(String str)
    {
      return str.matches("-?\\d+(\\.\\d+)?");
    }
}
