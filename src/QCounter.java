/**
 * 
 * @author Geoff
 * @version 1.0
 * 
 * Specific scan for the Question and Answer Summary text
 * 
 */

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;
import org.apache.commons.lang.StringUtils;

public class QCounter extends Counter {

	// Parameters
	// Input company names and representatives
	// Output word counts for questions and responses
	protected int myMarketingQWords;
	private Map<String, Integer> myMarketingQWordsAudit = new HashMap<String, Integer>();
	protected int myMarketingWords;
	private Map<String, Integer> myMarketingWordsAudit = new HashMap<String, Integer>();
	protected int myStockQWords;
	private Map<String, Integer> myStockQWordsAudit = new HashMap<String, Integer>();
	protected int mySearchQWords;
	private Map<String, Integer> mySearchQWordsAudit = new HashMap<String, Integer>();
	protected int myAcquisitionQWords;
	private Map<String, Integer> myAcquisitionQWordsAudit = new HashMap<String, Integer>();
	protected int myProcessQWords;
	private Map<String, Integer> myProcessQWordsAudit = new HashMap<String, Integer>();
	protected int myStorageQWords;
	private Map<String, Integer> myStorageQWordsAudit = new HashMap<String, Integer>();
	protected int myTransferQWords;
	private Map<String, Integer> myTransferQWordsAudit = new HashMap<String, Integer>();
	protected int myApplicationQWords;
	private Map<String, Integer> myApplicationQWordsAudit = new HashMap<String, Integer>();
	protected int myProtectionQWords;
	private Map<String, Integer> myProtectionQWordsAudit = new HashMap<String, Integer>();
	protected int myStockWords;
	private Map<String, Integer> myStockWordsAudit = new HashMap<String, Integer>();
	protected int mySearchWords;
	private Map<String, Integer> mySearchWordsAudit = new HashMap<String, Integer>();
	protected int myAcquisitionWords;
	private Map<String, Integer> myAcquisitionWordsAudit = new HashMap<String, Integer>();
	protected int myProcessWords;
	private Map<String, Integer> myProcessWordsAudit = new HashMap<String, Integer>();
	protected int myStorageWords;
	private Map<String, Integer> myStorageWordsAudit = new HashMap<String, Integer>();
	protected int myTransferWords;
	private Map<String, Integer> myTransferWordsAudit = new HashMap<String, Integer>();
	protected int myApplicationWords;
	private Map<String, Integer> myApplicationWordsAudit = new HashMap<String, Integer>();
	protected int myProtectionWords;
	private Map<String, Integer> myProtectionWordsAudit = new HashMap<String, Integer>();
	protected ArrayList<String> myCompanies = new ArrayList<String>();
	protected ArrayList<ArrayList<String>> myNames = new ArrayList<ArrayList<String>>();
	protected ArrayList<String> myCompanySpeak = new ArrayList<String>();
	protected ArrayList<String> myAnalystSpeak = new ArrayList<String>();
	protected ArrayList<String> myOperatorSpeak = new ArrayList<String>();
	protected int totalNumberOfWordsPostCompany = 0;
	protected int totalNumberOfWordsPostAnalyst = 0;
	protected boolean loggingEnabled = true;

	protected Map<String, Integer> PreMKStock = new HashMap<String, Integer>(); // 0
	protected Map<String, Integer> PreMKSearch = new HashMap<String, Integer>(); // 1
	protected Map<String, Integer> PreMKAcquistion = new HashMap<String, Integer>(); // 2
	protected Map<String, Integer> PreMKProcess = new HashMap<String, Integer>(); // 3
	protected Map<String, Integer> PreMKStorage = new HashMap<String, Integer>(); // 4
	protected Map<String, Integer> PreMKTransfer = new HashMap<String, Integer>(); // 5
	protected Map<String, Integer> PreMKApplication = new HashMap<String, Integer>(); // 6
	protected Map<String, Integer> PreMKProtection = new HashMap<String, Integer>(); // 7

	protected Map<String, Integer> PostMKStock = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKSearch = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKAcquistion = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKProcess = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKStorage = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKTransfer = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKApplication = new HashMap<String, Integer>();
	protected Map<String, Integer> PostMKProtection = new HashMap<String, Integer>();

	protected Map<String, Integer> AnalystMKStock = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKSearch = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKAcquistion = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKProcess = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKStorage = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKTransfer = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKApplication = new HashMap<String, Integer>();
	protected Map<String, Integer> AnalystMKProtection = new HashMap<String, Integer>();

	// Constructors
	public QCounter(KDictionary KW, Dictionary KEW, Dictionary MW, Dictionary MEW,
			ArrayList<String> firms, ArrayList<ArrayList<String>> members, boolean logging) {
		super(KW, KEW, MW, MEW, logging);
		myCompanies = firms;
		myNames = members;
		totalNumberOfWordsPostCompany = 0;
		totalNumberOfWordsPostAnalyst = 0;
		loggingEnabled = logging;
	}

	// Analyze Q/A text
	public String AnalyzeFile(QCounter q, String inputFile)
			throws FileNotFoundException {
		FileReader reader = new FileReader(inputFile);
		Scanner in = new Scanner(reader);

		totalNumberOfWordsPostCompany = 0;
		totalNumberOfWordsPostAnalyst = 0;

		myMarketingQWords = 0;
		myMarketingWords = 0;
		myStockQWords = 0;
		mySearchQWords = 0;
		myAcquisitionQWords = 0;
		myProcessQWords = 0;
		myStorageQWords = 0;
		myTransferQWords = 0;
		myApplicationQWords = 0;
		myProtectionQWords = 0;
		myStockWords = 0;
		mySearchWords = 0;
		myAcquisitionWords = 0;
		myProcessWords = 0;
		myStorageWords = 0;
		myTransferWords = 0;
		myApplicationWords = 0;
		myProtectionWords = 0;
		myMarketingQWordsAudit = new HashMap<String, Integer>();
		myMarketingWordsAudit = new HashMap<String, Integer>();
		myStockQWordsAudit = new HashMap<String, Integer>();
		mySearchQWordsAudit = new HashMap<String, Integer>();
		myAcquisitionQWordsAudit = new HashMap<String, Integer>();
		myProcessQWordsAudit = new HashMap<String, Integer>();
		myStorageQWordsAudit = new HashMap<String, Integer>();
		myTransferQWordsAudit = new HashMap<String, Integer>();
		myApplicationQWordsAudit = new HashMap<String, Integer>();
		myProtectionQWordsAudit = new HashMap<String, Integer>();
		myStockWordsAudit = new HashMap<String, Integer>();
		mySearchWordsAudit = new HashMap<String, Integer>();
		myAcquisitionWordsAudit = new HashMap<String, Integer>();
		myProcessWordsAudit = new HashMap<String, Integer>();
		myStorageWordsAudit = new HashMap<String, Integer>();
		myTransferWordsAudit = new HashMap<String, Integer>();
		myApplicationWordsAudit = new HashMap<String, Integer>();
		myProtectionWordsAudit = new HashMap<String, Integer>();
		PreMKStock = new HashMap<String, Integer>(); // 0
		PreMKSearch = new HashMap<String, Integer>(); // 1
		PreMKAcquistion = new HashMap<String, Integer>(); // 2
		PreMKProcess = new HashMap<String, Integer>(); // 3
		PreMKStorage = new HashMap<String, Integer>(); // 4
		PreMKTransfer = new HashMap<String, Integer>(); // 5
		PreMKApplication = new HashMap<String, Integer>(); // 6
		PreMKProtection = new HashMap<String, Integer>(); // 7

		PostMKStock = new HashMap<String, Integer>();
		PostMKSearch = new HashMap<String, Integer>();
		PostMKAcquistion = new HashMap<String, Integer>();
		PostMKProcess = new HashMap<String, Integer>();
		PostMKStorage = new HashMap<String, Integer>();
		PostMKTransfer = new HashMap<String, Integer>();
		PostMKApplication = new HashMap<String, Integer>();
		PostMKProtection = new HashMap<String, Integer>();

		AnalystMKStock = new HashMap<String, Integer>();
		AnalystMKSearch = new HashMap<String, Integer>();
		AnalystMKAcquistion = new HashMap<String, Integer>();
		AnalystMKProcess = new HashMap<String, Integer>();
		AnalystMKStorage = new HashMap<String, Integer>();
		AnalystMKTransfer = new HashMap<String, Integer>();
		AnalystMKApplication = new HashMap<String, Integer>();
		AnalystMKProtection = new HashMap<String, Integer>();

		// Split the text into analysts and company representative statements
		SplitText(TextDivide(in), getIndex(inputFile));
		
		for (String block : myCompanySpeak) {
			countPhrasesForCompany(block);

			String[] linesplit = Utility.splitAndMaskWordsAroundNot(block);

			for (int i=0; i<linesplit.length; i++) {
				String word = linesplit[i].toLowerCase();
				totalNumberOfWordsPostCompany++;

				// Count representative marketing words
				for (String MWord : myFinalMarketingWords) {
					if (word.startsWith(MWord.toLowerCase()) && !Utility.isExcludedWord(word, myFinalMarketingExcludedWords)) {
						myMarketingWords++;
						this.addWord(myMarketingWordsAudit, MWord,
								"myMarketingWords");
					} else {
					}
				}

				// Don't include excluded words list for representatives
				ArrayList<String> category = Utility.isKnowledgeWord(myFinalKnowledgeWords, word);
				boolean knowledgeWord = true;
				if (category.size() == 0) knowledgeWord = false;
				if (knowledgeWord && !Utility.isExcludedWord(word, myFinalKnowledgeExcludedWords)) {
					int type = myFinalKnowledgeWords.indexOf(category);
					assignKnowledgeTypeForCompany(type, word);
				}
			}
		}

		for (String block : myAnalystSpeak) {
			countPhrasesForAnalyst(block);
			
			String[] linesplit = Utility.splitAndMaskWordsAroundNot(block);

			for (int i=0; i<linesplit.length; i++) {
				String word = linesplit[i].toLowerCase();
				totalNumberOfWordsPostAnalyst++;

				// Count analyst marketing words
				for (String MWord : myFinalMarketingWords) {
					if (word.contains(MWord.toLowerCase()) && !Utility.isExcludedWord(word, myFinalMarketingExcludedWords)) {
						myMarketingQWords++;
						this.addWord(myMarketingQWordsAudit, MWord,
								"myMarketingQWords");
				        if (loggingEnabled) Log.write(MWord + "\t" + "\t" + block);
					} else {
					}
				}
				
				// Don't include excluded words list for analysts
				ArrayList<String> category = Utility.isKnowledgeWord(myFinalKnowledgeWords, word);
				boolean knowledgeWord = true;
				if (category.size() == 0) knowledgeWord = false;
				if (knowledgeWord && !Utility.isExcludedWord(word, myFinalKnowledgeExcludedWords)) {
					int type = myFinalKnowledgeWords.indexOf(category);
					assignKnowledgeTypeForAnalyst(type, word);
				}
			}
		}
		
		System.out.println("company words: " + totalNumberOfWordsPostCompany);
		System.out.println("analyst words: " + totalNumberOfWordsPostAnalyst);
		
		int totalPostWords = totalNumberOfWordsPostCompany
				+ totalNumberOfWordsPostAnalyst;
		return (totalPostWords + ", " + totalNumberOfWordsPostCompany + ", "
				+ totalNumberOfWordsPostAnalyst + ", " + myMarketingWords
				+ ", " + myStockWords + ", " + mySearchWords + ", "
				+ myAcquisitionWords + ", " + myProcessWords + ", "
				+ myStorageWords + ", " + myTransferWords + ", "
				+ myApplicationWords + ", " + myProtectionWords + ", "
				+ myMarketingQWords + ", " + myStockQWords + ", "
				+ mySearchQWords + ", " + myAcquisitionQWords + ", "
				+ myProcessQWords + ", " + myStorageQWords + ", "
				+ myTransferQWords + ", " + myApplicationQWords + ", " + myProtectionQWords);
	}

	private void assignKnowledgeTypeForAnalyst(int type, String word) {
		// Count types of analyst knowledge words
		if (type == 0) {
			myStockQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 1) {
			mySearchQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 2) {
			myAcquisitionQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 3) {
			myProcessQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 4) {
			myStorageQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 5) {
			myTransferQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 6) {
			myApplicationQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 7) {
			myProtectionQWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
	}

	private void assignKnowledgeTypeForCompany(int type, String word) {
		// Count each type of knowledge word for representatives
		if (type == 0) {
			myStockWords++;
			this.addWord(myStockQWordsAudit, word, "myStockQWords");
		}
		if (type == 1) {
			mySearchWords++;
			this.addWord(mySearchWordsAudit, word, "mySearchWords");
		}
		if (type == 2) {
			myAcquisitionWords++;
			this.addWord(myAcquisitionWordsAudit, word, "myAcquisitionWords");
		}
		if (type == 3) {
			myAcquisitionWords++;
			this.addWord(myAcquisitionWordsAudit, word, "myAcquisitionWords");
		}
		if (type == 4) {
			myStorageWords++;
			this.addWord(myStorageWordsAudit, word, "myStorageWords");
		}
		if (type == 5) {
			myTransferWords++;
			this.addWord(myTransferWordsAudit, word, "myTransferWords");
		}
		if (type == 6) {
			myApplicationWords++;
			this.addWord(myApplicationWordsAudit, word, "myApplicationWords");
		}
		if (type == 7) {
			myProtectionWords++;
			this.addWord(myProtectionWordsAudit, word, "myProtectionWords");
		}
	}

	// Split text into analyst, representative, and operator statements
	public void SplitText(Scanner in, int i) {
		myAnalystSpeak.clear();
		myCompanySpeak.clear();
		myOperatorSpeak.clear();
		TreeSet<String> usedAnalysts = new TreeSet<String>();
		TreeSet<String> usedCompanyReps = new TreeSet<String>();
		
		int classify = 3;
		while (in.hasNextLine()) {
			String QuestionLine = in.nextLine();
			//System.out.println("Looking at line: " + QuestionLine);
			Scanner linesplit = new Scanner(QuestionLine);
			linesplit.useDelimiter(":");
			if (linesplit.hasNext()) {
				String name = linesplit.next().trim();
				String title = "";
				String QLine = "";
				if (linesplit.hasNext()) {
					QLine = linesplit.next();
				} else {
					QLine = name;
					name = "";
				}
				if (name.contains(",")) {
					//Split on comma to remove the name's titles
					String[] parts = name.split(",");
					name = parts[0].trim();
					if (parts.length > 1) title = parts[1].trim();
				}
				if (name.length() != 0) System.out.println("NAME: " + name);
				// Count operators
				if (name.startsWith("OPERATOR")) {
					myOperatorSpeak.add(QLine);
					classify = 0;
					System.out.println("classified as operator - case 0");
				}
				//do nothing
				else if (name.toUpperCase().startsWith("[COPYRIGHT")) {
					classify = 3;
				}
				else if (name.equals("")) {
					if (QLine.length() > 0) {
						if (classify == 0) {
							myOperatorSpeak.add(QuestionLine);
							System.out.println("classified as operator - case 3");
						} else if (classify == 1) {
							myCompanySpeak.add(QuestionLine);
							System.out.println("classified as company rep - case 4");
						} else if (classify == 2) {
							myAnalystSpeak.add(QuestionLine);
							System.out.println("classified as analyst - case 5");
						}
					}
				}
				//We've already seen this name used as an analyst or title signifies an analyst
				else if (usedAnalysts.contains(name) || name.contains("ANALYST") || title.contains("ANALYST")) {
					myAnalystSpeak.add(QLine);
					classify = 2;
					usedAnalysts.add(name);
					System.out.println("classified as analyst - case 6");
				}
				//We've already seen this name used as a company rep or it's in the company rep sub-array
				else if (usedCompanyReps.contains(name)) { //|| arrayContains(myNames.get(i), name)) {
					myCompanySpeak.add(QLine);
					usedCompanyReps.add(name);
					classify = 1;
					System.out.println("classified as company rep - case 7");
//					for (String n : myNames.get(i)) {
//						System.out.print(n + ", ");
//					}
//					System.out.println();
				}
				// The operator introduces analysts
				else if (classify == 0) {
					myAnalystSpeak.add(QLine);
					classify = 2;
					usedAnalysts.add(name);
					System.out.println("classified as analyst - case 8");
				}
				// If name is listed in company representative array add to
				// company statements
				else {
					myCompanySpeak.add(QLine);
					usedCompanyReps.add(name);
					classify = 1;
					System.out.println("classified as company rep - case 10");
				}
			}
			linesplit.close();
		}
	}

	// Get index of company name to get representative names
	public int getIndex(String filename) {
		for (String company : myCompanies) {
			if (filename.contains(company)) {
				return myCompanies.indexOf(company);
			}
		}
		return -1;
	}

	// Divide the text to only count Q/A text
	public Scanner TextDivide(Scanner in) {
		in.useDelimiter("QUESTION AND ANSWER");
		if (in.hasNext()) {
			in.next();
			if (in.hasNext()) {
				Scanner post = new Scanner(in.next());
				return post;
			} else {
				Scanner post = new Scanner("EMPTY");
				return post;
			}
		} else {
			Scanner post = new Scanner("EMPTY");
			return post;
		}
	}

	private void countPhrasesForCompany(String line) {
		for (int i = 0; i < multipleWordKnowledge.size(); i++) {
			ArrayList<String> kList = multipleWordKnowledge.get(i);
			for (String kPhrase : kList) {
				int index = 0;
				String phrase = kPhrase;
				while (index >= 0) {
					index = line.indexOf(phrase);
					if (index >= 0) {
						assignKnowledgeTypeForCompany(i, phrase);
						if (loggingEnabled) Log.write(phrase + "\t" + "\t" + line);
						line = line.substring(index + kPhrase.length() - 1);
					}
				}
			}
		}
	}

	private void countPhrasesForAnalyst(String line) {
		for (int i = 0; i < multipleWordKnowledge.size(); i++) {
			ArrayList<String> kList = multipleWordKnowledge.get(i);
			for (String kPhrase : kList) {
				int index = 0;
				String phrase = kPhrase;
				while (index >= 0) {
					index = line.indexOf(phrase);
					if (index >= 0) {
						assignKnowledgeTypeForAnalyst(i, phrase);
						if (loggingEnabled) Log.write(phrase + "\t" + "\t" + line);

				        line = line.substring(index + kPhrase.length());
					}
				}
			}
		}
	}
	
	private boolean arrayContains(ArrayList<String> companyRepresentatives, String name) {
		if (name.length() == 0) return false;
		for (String rep : companyRepresentatives) {
			if (rep.equals(name)) return true;
			
			if (StringUtils.getLevenshteinDistance(rep, name) <= 3) return true;
			
			String[] repParts = rep.split(" ");
			String[] otherParts = name.split(" ");
			String repFirstName = "", repLastName = "", otherFirstName = "", otherLastName = "";
			
			if (repParts.length == 2) {
				repFirstName = repParts[0];
				repLastName = repParts[1];
			} else if (repParts.length == 3) {
				repFirstName = repParts[0];
				repLastName = repParts[2];
			} else continue;
			
			if (otherParts.length == 2) {
				otherFirstName = otherParts[0];
				otherLastName = otherParts[1];
			} else if (otherParts.length == 3) {
				otherFirstName = otherParts[0];
				otherLastName = otherParts[2];
			} else continue;

			if (StringUtils.getLevenshteinDistance(repLastName, otherLastName) <= 2) {
				if (otherFirstName.startsWith(repFirstName) || repFirstName.startsWith(otherFirstName)) {
					return true;
				}
			}
		}
		
		return false;
	}

	protected void addWord(Map<String, Integer> amap, String MWord, String amapname) {

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
		this.printCollections(myMarketingQWordsAudit, "myMarketingQWordsAudit");
		this.printCollections(myMarketingWordsAudit, "myMarketingWordsAudit");
		this.printCollections(myStockQWordsAudit, "myStockQWordsAudit");
		this.printCollections(mySearchQWordsAudit, "mySearchQWordsAudit");
		this.printCollections(myAcquisitionQWordsAudit,
				"myAcquisitionQWordsAudit");
		this.printCollections(myProcessQWordsAudit, "myProcessQWordsAudit");
		this.printCollections(myStorageQWordsAudit, "myStorageQWordsAudit");
		this.printCollections(myTransferQWordsAudit, "myTransferQWordsAudit");
		this.printCollections(myApplicationQWordsAudit,
				"myApplicationQWordsAudit");
		this.printCollections(myProtectionQWordsAudit,
				"myProtectionQWordsAudit");
		this.printCollections(myStockWordsAudit, "myStockWordsAudit");
		this.printCollections(mySearchWordsAudit, "mySearchWordsAudit");
		this.printCollections(myAcquisitionWordsAudit,
				"myAcquisitionWordsAudit");
		this.printCollections(myProcessWordsAudit, "myProcessWordsAudit");
		this.printCollections(myStorageWordsAudit, "myStorageWordsAudit");
		this.printCollections(myTransferWordsAudit, "myTransferWordsAudit");
		this.printCollections(myApplicationWordsAudit,
				"myApplicationWordsAudit");
		this.printCollections(myProtectionWordsAudit, "myProtectionWordsAudit");

		this.printCollections(PreMKStock, "PreMKStock");
		this.printCollections(PreMKSearch, "PreMKSearch");
		this.printCollections(PreMKAcquistion, "PreMKAcquistion");
		this.printCollections(PreMKProcess, "PreMKProcess");
		this.printCollections(PreMKStorage, "PreMKStorage");
		this.printCollections(PreMKTransfer, "PreMKTransfer");
		this.printCollections(PreMKApplication, "PreMKApplication");
		this.printCollections(PreMKProtection, "PreMKProtection");

		this.printCollections(PostMKStock, "PostMKStock");
		this.printCollections(PostMKSearch, "PostMKSearch");
		this.printCollections(PostMKAcquistion, "PostMKAcquistion");
		this.printCollections(PostMKProcess, "PostMKProcess");
		this.printCollections(PostMKStorage, "PostMKStorage");
		this.printCollections(PostMKTransfer, "PostMKTransfer");
		this.printCollections(PostMKApplication, "PostMKApplication");
		this.printCollections(PostMKProtection, "PostMKProtection");

		this.printCollections(AnalystMKStock, "AnalystMKStock");
		this.printCollections(AnalystMKSearch, "AnalystMKSearch");
		this.printCollections(AnalystMKAcquistion, "AnalystMKAcquistion");
		this.printCollections(AnalystMKProcess, "AnalystMKProcess");
		this.printCollections(AnalystMKStorage, "AnalystMKStorage");
		this.printCollections(AnalystMKTransfer, "AnalystMKTransfer");
		this.printCollections(AnalystMKApplication, "AnalystMKApplication");
		this.printCollections(AnalystMKProtection, "AnalystMKProtection");

	}

	public void printCollections(PrintWriter output) {
		output.println("\nQCounter Collections:\n");

		this.printCollections(myMarketingWordsAudit, "Post_Marketing", output);
		this.printCollections(myStockWordsAudit, "Post_Stock", output);
		this.printCollections(mySearchWordsAudit, "Post_Search", output);
		this.printCollections(myAcquisitionWordsAudit, "Post_Acquisition",
				output);
		this.printCollections(myProcessWordsAudit, "Post_Process", output);
		this.printCollections(myStorageWordsAudit, "Post_Storage", output);
		this.printCollections(myTransferWordsAudit, "Post_Transfer", output);
		this.printCollections(myApplicationWordsAudit, "Post_Application",
				output);
		this.printCollections(myProtectionWordsAudit, "Post_Protection", output);
		this.printCollections(myMarketingQWordsAudit, "Analyst_Marketing",
				output);
		this.printCollections(myStockQWordsAudit, "Analyst_Stock", output);
		this.printCollections(mySearchQWordsAudit, "Analyst_Search", output);
		this.printCollections(myAcquisitionQWordsAudit, "Analyst_Acquisition",
				output);
		this.printCollections(myProcessQWordsAudit, "Analyst_Process", output);
		this.printCollections(myStorageQWordsAudit, "Analyst_Storage", output);
		this.printCollections(myTransferQWordsAudit, "Analyst_Transfer", output);
		this.printCollections(myApplicationQWordsAudit, "Analyst_Application",
				output);
		this.printCollections(myProtectionQWordsAudit, "Analyst_Protection",
				output);
	}

	protected void printCollections(Map<String, Integer> amap, String name) {
		for (java.util.Map.Entry<String, Integer> entry : amap.entrySet()) {
			System.out.println("Collection Name=" + name + " Word="
					+ entry.getKey() + " , Count="
					+ entry.getValue().toString());
		}
	}

	protected void printCollections(Map<String, Integer> amap, String name,
			PrintWriter output) {
		output.println("Collection Name: " + name);
		for (java.util.Map.Entry<String, Integer> entry : amap.entrySet()) {
			output.print("Word=" + entry.getKey() + " Count="
					+ entry.getValue().toString() + ",");
		}
		if (!amap.isEmpty())
			output.println();
	}
}
