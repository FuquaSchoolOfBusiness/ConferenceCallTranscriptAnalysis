
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * Given: Lists of excluded, marketing, and knowledge words; 
 * List of companies, list of company members; 
 * Lists of what the analyst, company, and operator said.
 * 
 * Find: The number of marketing-knowledge words.
 *
 */
public class MKWCounter extends QCounter {

  protected ArrayList<ArrayList<String>> multipleWordKnowledge;
  private final int DISTANCE;
  private final boolean loggingEnabled;
  private final static int NUMBER_OF_TYPES = 8;
  private int totalPreWords = 0;
  private int totalPostCompanyWords = 0;
  private int totalPostAnalystWords = 0;
  private String date = "";
  private Pattern regexEW;

  public MKWCounter(KDictionary KW, Dictionary KEW, Dictionary MW, Dictionary MEW, ArrayList<String> firms,
          ArrayList<ArrayList<String>> members, int d, boolean logging) {
    super(KW, KEW, MW, MEW, firms, members, logging);

    totalPreWords = 0;
    totalPostCompanyWords = 0;
    totalPostAnalystWords = 0;
    date = "";

    DISTANCE = d;
    loggingEnabled = logging;

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
    
    StringBuilder regex = new StringBuilder();
    regex.append("\\b(");
    if (myFinalKnowledgeExcludedWords.size() == 0 && myFinalMarketingExcludedWords.size() == 0) {
    	regex.append("abcdef)\\b");
    } else {
    	for (String KEWord : myFinalKnowledgeExcludedWords) {
    		regex.append(KEWord);
    		regex.append('|');
    	}
    	for (String MEWord : myFinalMarketingExcludedWords) {
    		regex.append(MEWord);
    		regex.append('|');
    	}
    	regex.setLength(regex.length()-1);
    	regex.append(")\\b");
    }
    regexEW = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
  }

  /**
   * Counts the number of marketing-knowledge words spoken by analysts and corporate representatives.
   * in the Q&A section of the transcript.
   * Returns a comma-separated output to be opened in Excel.
   * @param inputFile
   * @param pre = 1 if for the pre part of the file.
   * @return
   * @throws FileNotFoundException
   */
  public String AnalyzeFile(String inputFile, boolean include) throws FileNotFoundException {

    FileReader reader = new FileReader(inputFile);
    Scanner in = new Scanner(reader);

    //Analysis of the Q&A Section
    SplitText(TextDivide(in), getIndex(inputFile));

    totalPreWords = 0;
    totalPostCompanyWords = 0;
    totalPostAnalystWords = 0;

    int[] finalCompanyCounts = new int[NUMBER_OF_TYPES];
    int[] finalAnalystCounts = new int[NUMBER_OF_TYPES];

    countWords(finalCompanyCounts, myCompanySpeak, include, 2);
    countWords(finalAnalystCounts, myAnalystSpeak, include, 3);


    //return comma-separated output.
    int totalPostWords = totalPostCompanyWords + totalPostAnalystWords;
    String ret = "";
    ret = totalPostWords + "," + totalPostCompanyWords + "," + totalPostAnalystWords + ",";
    for (int i : finalCompanyCounts) {
      ret += i + ",";
    }
    for (int i : finalAnalystCounts) {
      ret += i + ",";
    }
    String res = ret.substring(0, ret.length() - 1);
    return res;
  }

  public String AnalyzeFilePre(String inputFile, boolean include) throws FileNotFoundException {

    FileReader reader = new FileReader(inputFile);
    Scanner in1 = new Scanner(reader);

    totalPreWords = 0;
    totalPostCompanyWords = 0;
    totalPostAnalystWords = 0;

    //Analysis of the Pre-Q&A Section
    in1.useDelimiter("QUESTION AND ANSWER");
    String pre = in1.next();
    in1.close();
    ArrayList<String> al = new ArrayList<String>();
    al.add(pre);
    //System.out.println(pre);
    int[] finalPreCounts = new int[NUMBER_OF_TYPES];
    countWords(finalPreCounts, al, include, 1);

    String ret1 = "";
    ret1 = date + "," + totalPreWords + ",";
    for (int i : finalPreCounts) {
      ret1 += i + ",";
    }
    String res1 = ret1.substring(0, ret1.length() - 1);
    return res1;
  }

  /**
   * For each block in the list of company statements, check whether each word is marketing, knowledge, or neither.
   * Keeps track of the previous word type, and when there are marketing and knowledge words next to each other,
   * then increment the counter.  Updates the previous for the next iteration.
   *
   * type parameter: 1 means pre, 2 means post_company, 3 means post_analyst
   */
  public void countWords(int[] counter, ArrayList<String> text, boolean include, int type) {

    Set<String> regexSentences = new HashSet<String>();
    Set<String> mWordsToCheck = new HashSet<String>();
    ArrayList<Set<String>> kWordsToCheck = new ArrayList<Set<String>>();
    for (int b = 0; b < myFinalKnowledgeWords.size(); b++) {
      Set<String> al = new HashSet<String>();
      kWordsToCheck.add(al);
    }

    for (String block : text) {

      Scanner blockScanner = new Scanner(block);
      while (blockScanner.hasNext()) {
        blockScanner.next();

        if (type == 1) {
          totalPreWords++;
          //System.out.println("Total Pre Words: "+w + " type: "+type);
        }
        if (type == 2) {
          totalPostCompanyWords++;
          //System.out.println("Total Post Comapany Words: "+w + " type: "+type);
        }
        if (type == 3) {
          totalPostAnalystWords++;
          //System.out.println("Total Post Analysis: "+w + " type: "+type);
        }
      }
      blockScanner.close();

      String[] bySentence = block.split("\\.|\\?|\\!|\\,|\\;|\\:");

      if (include == true) {
        regexSentenceFinder(bySentence, regexSentences, mWordsToCheck, kWordsToCheck);
      }

      boolean dateFound = false;
      for (String origSentence : bySentence) {

          //q&d replace of "exclude words" - this needs to be done earlier in the process (G.H.)
          String sentence = regexEW.matcher(origSentence).replaceAll("XXXXX");
//          if (!sentence1.equals(sentence)) {
//        	  System.out.println(sentence);
//        	  System.out.println(sentence1);
//          }
//          sentence = sentence1;

          
        if (type == 1 && dateFound == false) {
          Scanner sentenceScanner = new Scanner(sentence);

          while (sentenceScanner.hasNextLine()) {
            String line = sentenceScanner.nextLine();
            
            String lcline = line.toLowerCase();
            if (dateFound == false && Utility.isMonth(lcline)) {

              try {
                
                //date = line;
                //dateFound = true;
				String[] lineArray = lcline.split(" ");				
                if (!Utility.isMonth(lineArray[0]) && !Utility.isMonth(lineArray[1])) { // example: Q1 2003 Abbott Laboratories Earnings Conference Call - Final FD (Fair Disclosure) Wire April 9, 2003 Wednesday
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
              } catch (Exception e) {
                e.printStackTrace();

                System.out.println("Pressing on");
              }
            }
          }
        }


        //sentence = sentence.trim();
        //System.out.println(sentence);

        //"previous" will be M, K#, or "".
        //String[] previous = new String[DISTANCE];
        ListEntry[] previous = new ListEntry[DISTANCE];
                
        String[] wordsInSentence = Utility.splitAndMaskWordsAroundNot(sentence);
        
        for (int i = 0; i < previous.length; i++) {
          //previous[i] = "";
          previous[i] = new ListEntry();
        }
        //compare types of all other pairs of words.
        //String current = "";
        ListEntry current = new ListEntry();
        ListEntry next = null;
        String nextword = null;

        for (int k = 0; k < wordsInSentence.length; k++) {

//					if (type==1){ totalPreWords++;}
//					if (type==2){ totalPostCompanyWords++;}
//					if (type==3){ totalPostAnalystWords++;}

          String word = null; //wordsInSentence[k].toLowerCase();
          
          if (next != null) {
        	current = next;
        	word = nextword;
          } else {
        	word = wordsInSentence[k].toLowerCase();
        	current.key = findWordType(word);
          }
          
    	  if (k + 1 < wordsInSentence.length) {
    		nextword = wordsInSentence[k+1].toLowerCase();
    		next = new ListEntry();
    		next.key = findWordType(nextword);
    	  } else {
    		next = null;
    	  }
          
          incrementCounter(counter, previous, current, next, word, type, origSentence);
          
          //update for next iteration.
          if (DISTANCE > 1) {
            for (int i = 1; i < previous.length; i++) {
              previous[i - 1] = previous[i];
            }
          }
          previous[DISTANCE - 1] = new ListEntry(current.key, word);
        }
      }
    }

    if (include == true) {
      regexSearch(regexSentences, mWordsToCheck, kWordsToCheck, counter);
    }
  }

  /**
   * Counts the number of times each type of marketing-knowledge word occurs.
   * @param counter
   * @param previous
   * @param current
   * @return
   */
  private void incrementCounter(int[] counter, ListEntry[] previous, ListEntry current, ListEntry next, String word, int atype, String sentence) {
    
    if (current.key.equals("M")) {
      for (int i = 0; i < previous.length; i++) {
        String pre = previous[i].key;
        if (pre.startsWith("K")) {
          if (next != null && next.key.equals("M")) {
            //if two consecutive marketing words combine with the same knowledge word, count them only once
           	if (i+1 < previous.length) continue; //let the next iteration catch this one
          }
          int indexk = Integer.parseInt(pre.substring(1));
          counter[indexk]++;
          //System.out.println("Increment counter K ("+pre+")"+ pre.substring(1)+ "|"+word+"|"+atype+"|"+indexk);
          if(word != null && !word.isEmpty()) {
            this.addWordMK(atype, indexk , word, previous[i].value);
            if (loggingEnabled) Log.write(previous[i].value + "+" + word + "\t" + (previous.length-i-1) + "\t" + sentence);
          }
        }
      }
    }
    if (current.key.startsWith("K")) {
      int type = Integer.parseInt(current.key.substring(1));
      for (int i = 0; i < previous.length; i++) {
        String pre = previous[i].key;
        if (pre.equals("M")) {
          //System.out.println("Increment counter M " + current+"|"+word+"|"+atype+"|"+type);
          if (next != null && next.key.startsWith("K")) {
          	//if two consecutive knowledge words combine with the same marketing word, count them only once
         	//THIS RULE IS DISABLED
        	//if (i+1 < previous.length) continue; //let the next iteration catch this one
          }
          counter[type]++;
          if(word != null && !word.isEmpty()) {
            this.addWordMK(atype, type , word, previous[i].value);
            if (loggingEnabled) Log.write(previous[i].value + "+" + word + "\t" + (previous.length-i-1) + "\t" + sentence);
          }
        }
      }
    }
  }

  /**
   * Determines whether a given word in a marketing or knowledge word or neither.
   * @param word
   * @return
   */
  private String findWordType(String word) {
    String type = "";
    for (String mWord : myFinalMarketingWords) {
      if (word.startsWith(mWord.toLowerCase()) && !Utility.isExcludedWord(word, myFinalMarketingExcludedWords)) {
        type = "M";
      }
    }
    for (int i = 0; i < myFinalKnowledgeWords.size(); i++) {
      for (String kWord : myFinalKnowledgeWords.get(i)) {
        if (word.startsWith(kWord.toLowerCase())) {
          if (!Utility.isExcludedWord(word, myFinalKnowledgeExcludedWords)) {
            type = "K" + i;
          }
        }
      }
    }
    
    
    return type;
  }

  public void regexSentenceFinder(String[] sentences, Set<String> ret,Set<String> mWordsToCheck, ArrayList<Set<String>> kWordsToCheck) {
    for (String s : sentences) {
      if (s.length() == 0) continue;
      for (String mWord : myFinalMarketingWords) {
        for (int i = 0; i < multipleWordKnowledge.size(); i++) {
          ArrayList<String> kList = multipleWordKnowledge.get(i);
          for (String kWord : kList) {
            if (s.toLowerCase().contains(mWord.toLowerCase()) && s.toLowerCase().contains(kWord.toLowerCase())) {

              //q&d replace of "exclude words" - this needs to be done earlier in the process (G.H.)
              String s1 = regexEW.matcher(s).replaceAll("XXXXX");
//              if (!s1.equals(s)) {
//            	  System.out.println(s);
//            	  System.out.println(s1);
//              }
              s = s1;
              
              ret.add(s);
              mWordsToCheck.add(mWord);
              kWordsToCheck.get(i).add(kWord);
            }
          }
        }
      }
    }
  }

  public void regexSearch(Set<String> sentences, Set<String> mWordsToCheck,
          ArrayList<Set<String>> kWordsToCheck, int[] counter) {

    for (String mWord : mWordsToCheck) {
      int j = 0; //type of knowledge word
      for (Set<String> kList : kWordsToCheck) {
        for (String kWord : kList) {

          String marketingWord = mWord.toLowerCase();
          String knowledgeWord = kWord.toLowerCase();

          for (int m = 0; m < DISTANCE; m++) {
            String MWFirst = getRegex(marketingWord, knowledgeWord, m);
            String KWFirst = getRegex(knowledgeWord, marketingWord, m);
            searchRegex(MWFirst, sentences, m, j, counter, marketingWord, knowledgeWord);
            searchRegex(KWFirst, sentences, m, j, counter, marketingWord, knowledgeWord);
          }
        }
        j++;
      }
    }
  }

  private String getRegex(String first, String second, int n) {
	String word = "\\b[\\w']+\\b[,;':]?\\s*";
	
    String ret = "\\b" + first + "[-\\w']*\\b[,;':]?\\s*";
    for (int i = 0; i < n; i++) {
      ret += word;
    }
    return ret + "\\b" + second + "[-\\w']*\\b[,;':]?\\s*";

  }

  private void searchRegex(String pat, Set<String> sentences, int numApart, int kIndex, int[] counter,
          String marketingWord, String knowledgeWord) {
    Pattern p = Pattern.compile(pat);
    for (String target : sentences) {

      target = target.toLowerCase();
      Matcher matcher = p.matcher(target);
      while (matcher.find()) {
        //System.out.println("Matched :"+target);
        counter[kIndex]++;
      }
    }
  }
  private void addWordMK(int type, int subtype , String word, String word2)
  {
    if(word.indexOf(",") >=0 )
      word = word.replace(',', ';');
    if(word2.indexOf(",")>=0)
      word2 = word2.replace(',', ';');
    //System.out.println("Add word");
    if(type == 1)
    {
      //System.out.println("")
      //protected Map<String,Integer> PreMKStock = new HashMap<String,Integer>();       //0
      //protected Map<String,Integer> PreMKSearch = new HashMap<String,Integer>();      //1
      //protected Map<String,Integer> PreMKAcquistion = new HashMap<String,Integer>();  //2  
      //protected Map<String,Integer> PreMKProcess = new HashMap<String,Integer>();     //3
      //protected Map<String,Integer> PreMKStorage = new HashMap<String,Integer>();     //4
      //protected Map<String,Integer> PreMKTransfer = new HashMap<String,Integer>();    //5  
      //protected Map<String,Integer> PreMKApplication = new HashMap<String,Integer>(); //6
      //protected Map<String,Integer> PreMKProtection = new HashMap<String,Integer>();  //7
      if(subtype == 0)
      {
        this.addWord2(this.PreMKStock, word2+"+"+word, "PreMKStock");
      }
      else if(subtype == 1)
      {
        this.addWord2(PreMKSearch, word2+"+"+word, "PreMKSearch");
      }
      else if(subtype == 2)
      {
        this.addWord2(PreMKAcquistion, word2+"+"+word, "PreMKAcquistion");
      }
      else if(subtype == 3)
      {
        this.addWord2(PreMKProcess, word2+"+"+word, "PreMKProcess");
      }
      else if(subtype == 4)
      {
        this.addWord2(PreMKStorage, word2+"+"+word, "PreMKStorage");
      }
      else if(subtype == 5)
      {
        this.addWord2(PreMKTransfer, word2+"+"+word, "PreMKTransfer");
      }
      else if(subtype == 6)
      {
        this.addWord2(PreMKApplication, word2+"+"+word, "PreMKApplication");
      }
      else if(subtype == 7)
      {
        this.addWord2(PreMKProtection, word2+"+"+word, "PreMKProtection");
      }
    }
    else if(type == 2)
    {
      if(subtype == 0)
      {
        this.addWord2(PostMKStock, word2+"+"+word, "PostMKStock");
      }
      else if(subtype == 1)
      {
        this.addWord2(PostMKSearch, word2+"+"+word, "PostMKSearch");
      }
      else if(subtype == 2)
      {
        this.addWord2(PostMKAcquistion, word2+"+"+word, "PostMKAcquistion");
      }
      else if(subtype == 3)
      {
        this.addWord2(PostMKProcess, word2+"+"+word, "PostMKProcess");
      }
      else if(subtype == 4)
      {
        this.addWord2(PostMKStorage, word2+"+"+word, "PostMKStorage");
      }
      else if(subtype == 5)
      {
        this.addWord2(PostMKTransfer, word2+"+"+word, "PostMKTransfer");
      }
      else if(subtype == 6)
      {
        this.addWord2(PostMKApplication, word2+"+"+word, "PostMKApplication");
      }
      else if(subtype == 7)
      {
        this.addWord2(PostMKProtection, word2+"+"+word, "PostMKProtection");
      }
    }
    else if(type == 3)
    {
      if(subtype == 0)
      {
        this.addWord2(AnalystMKStock, word2+"+"+word, "AnalystMKStock");
      }
      else if(subtype == 1)
      {
        this.addWord2(AnalystMKSearch, word2+"+"+word, "AnalystMKSearch");
      }
      else if(subtype == 2)
      {
        this.addWord2(AnalystMKAcquistion, word2+"+"+word, "AnalystMKAcquistion");
      }
      else if(subtype == 3)
      {
        this.addWord2(AnalystMKProcess, word2+"+"+word, "AnalystMKProcess");
      }
      else if(subtype == 4)
      {
        this.addWord2(AnalystMKStorage, word2+"+"+word, "AnalystMKStorage");
      }
      else if(subtype == 5)
      {
        this.addWord2(AnalystMKTransfer, word2+"+"+word, "AnalystMKTransfer");
      }
      else if(subtype == 6)
      {
        this.addWord2(AnalystMKApplication, word2+"+"+word, "AnalystMKApplication");
      }
      else if(subtype == 7)
      {
        this.addWord2(AnalystMKProtection, word2+"+"+word, "AnalystMKProtection");
      }
      
    }
  }
  
   protected void addWord2(Map<String, Integer> amap ,String MWord, String amapname)
  {
     if(amap.containsKey(MWord.toLowerCase()))
      {
        //System.out.println("Counting "+amapname+" found :" + MWord.toLowerCase());//xxxxx
        amap.put(MWord.toLowerCase(), (((Integer)amap.get(MWord.toLowerCase())).intValue() + 1));
      }
      else
      {
        //System.out.println("Counting "+amapname+" not found :" + MWord.toLowerCase());//xxxxx
        amap.put(MWord.toLowerCase(), 1);
      }
  }
  public void printCollections(PrintWriter output)
  {
    output.println("\nMKWCounter Collections:\n");
    /*
    this.printCollections(myMarketingQWordsAudit,"myMarketingQWordsAudit", output);
    this.printCollections(myMarketingWordsAudit,"myMarketingWordsAudit", output);
    this.printCollections(myStockQWordsAudit,"myStockQWordsAudit", output);
    this.printCollections(mySearchQWordsAudit,"mySearchQWordsAudit", output);
    this.printCollections(myAcquisitionQWordsAudit,"myAcquisitionQWordsAudit", output);
    this.printCollections(myProcessQWordsAudit,"myProcessQWordsAudit", output);
    this.printCollections(myStorageQWordsAudit,"myStorageQWordsAudit", output);
    this.printCollections(myTransferQWordsAudit,"myTransferQWordsAudit", output);
    this.printCollections(myApplicationQWordsAudit,"myApplicationQWordsAudit", output);
    this.printCollections(myProtectionQWordsAudit,"myProtectionQWordsAudit", output);
    this.printCollections(myStockWordsAudit,"myStockWordsAudit", output);
    this.printCollections(mySearchWordsAudit,"mySearchWordsAudit", output);
    this.printCollections(myAcquisitionWordsAudit,"myAcquisitionWordsAudit", output);
    this.printCollections(myProcessWordsAudit,"myProcessWordsAudit", output);
    this.printCollections(myStorageWordsAudit,"myStorageWordsAudit", output);
    this.printCollections(myTransferWordsAudit,"myTransferWordsAudit", output);
    this.printCollections(myApplicationWordsAudit,"myApplicationWordsAudit", output);
    this.printCollections(myProtectionWordsAudit,"myProtectionWordsAudit", output);
    */
    this.printCollections(PreMKStock,"PreMKStock", output);
    this.printCollections(PreMKSearch,"PreMKSearch", output);
    this.printCollections(PreMKAcquistion,"PreMKAcquistion", output);
    this.printCollections(PreMKProcess,"PreMKProcess", output);
    this.printCollections(PreMKStorage,"PreMKStorage", output);
    this.printCollections(PreMKTransfer,"PreMKTransfer", output);
    this.printCollections(PreMKApplication,"PreMKApplication", output);
    this.printCollections(PreMKProtection,"PreMKProtection", output);
    
    
    this.printCollections(PostMKStock,"PostMKStock", output);
    this.printCollections(PostMKSearch,"PostMKSearch", output);
    this.printCollections(PostMKAcquistion,"PostMKAcquistion", output);
    this.printCollections(PostMKProcess,"PostMKProcess", output);
    this.printCollections(PostMKStorage,"PostMKStorage", output);
    this.printCollections(PostMKTransfer,"PostMKTransfer", output);
    this.printCollections(PostMKApplication,"PostMKApplication", output);
    this.printCollections(PostMKProtection,"PostMKProtection", output);
    
    this.printCollections(AnalystMKStock,"AnalystMKStock", output);
    this.printCollections(AnalystMKSearch,"AnalystMKSearch", output);
    this.printCollections(AnalystMKAcquistion,"AnalystMKAcquistion", output);
    this.printCollections(AnalystMKProcess,"AnalystMKProcess", output);
    this.printCollections(AnalystMKStorage,"AnalystMKStorage", output);
    this.printCollections(AnalystMKTransfer,"AnalystMKTransfer", output);
    this.printCollections(AnalystMKApplication,"AnalystMKApplication", output);
    this.printCollections(AnalystMKProtection,"AnalystMKProtection", output);
    
  }
  
  private boolean isNumeric(String str)
  {
    return str.matches("-?\\d+(\\.\\d+)?");
  }

  private class ListEntry
  {
    public String key = "";
    public String value = "";
    public ListEntry(String key, String value)
    {
      this.key = key;
      this.value = value;
    }
    public ListEntry()
    {
      this.key = "";
      this.value = "";
    }
  }
}
