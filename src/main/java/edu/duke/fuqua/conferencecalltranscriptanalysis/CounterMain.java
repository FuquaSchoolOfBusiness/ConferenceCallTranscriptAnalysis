package edu.duke.fuqua.conferencecalltranscriptanalysis;

/**
 * 
 * @author Geoff
 * @version 1.0
 * @version 2.0 - Comments for cleanup
 * 
 * Main file to access run other components
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CounterMain {
	
	public final static int NUMBER_OF_COMPANIES = 226;
	
	private String fileListPath;
	private String savePath;
	
	private String knowledgeWordsPath;
	private String knowledgeExcludeWordsPath;
	private String marketingWordsPath;
	private String marketingExcludeWordsPath;
//	private String dict3WordsPath;
//	private String dict3ExcludeWordsPath;
	private String companiesPath;
	private String namesPath;
	
	private String kWordsToPath = "resources/knowledge.txt";
	private String mWordsToPath = "resources/marketing.txt";
//	private String d3WordsToPath = "resources/dictionary3.txt";
	private String keWordsToPath = "resources/knowledgeexclude.txt";
	private String meWordsToPath = "resources/marketingexclude.txt";
//	private String d3eWordsToPath = "resources/dictionary3exclude.txt";
	
	KDictionary KW;
	Dictionary KEW;
	Dictionary MW;
	Dictionary MEW;
//	Dictionary D3W;
//	Dictionary D3EW;
	ArrayList<String> companynames;
	ArrayList<ArrayList<String>> members;
	boolean loggingEnabled;
	boolean dataLinesOnly;
	
	public CounterMain(String fp, String sppre, String kp, String kep, String mp, String mep,
			String cp, String np, boolean logging, boolean dataLines) {
		fileListPath = fp;
		savePath = sppre;
		
		knowledgeWordsPath = kp;
		knowledgeExcludeWordsPath = kep;
		marketingWordsPath = mp;
		marketingExcludeWordsPath = mep;
//		dict3WordsPath = d3p;
//		dict3ExcludeWordsPath = d3ep;
		companiesPath = cp;
		namesPath = np;
		loggingEnabled = logging;
		dataLinesOnly = dataLines;
	}
	
	public void callCounter() throws FileNotFoundException
	{
		Utility u = new Utility();
		KW = new KDictionary(knowledgeWordsPath,kWordsToPath);
		KEW = new Dictionary(knowledgeExcludeWordsPath, keWordsToPath);
		MW = new Dictionary(marketingWordsPath,mWordsToPath);
		MEW = new Dictionary(marketingExcludeWordsPath, meWordsToPath);
//		D3W = new Dictionary(dict3WordsPath, d3WordsToPath);
//		D3EW = new Dictionary(dict3ExcludeWordsPath, d3eWordsToPath);

		companynames = u.makeCompanyList(companiesPath);
		members = u.makeNamesList(namesPath);
		
		PrintWriter TotalOutput = new PrintWriter(savePath);
		
		String header1 = "Name, Year, Quarter, Date, Pre_Total, Pre_Marketing, Pre_Stock, Pre_Search, Pre_Acqusition, " +
				"Pre_Process, Pre_Storage, Pre_Transfer, Pre_Application, Pre_Protection, ";

		String header2 = "Post_Total, Post_Company_Total, Post_Analyst_Total, Post_Marketing, Post_Stock, Post_Search, Post_Acqusition, " +
				"Post_Process, Post_Storage, Post_Transfer, Post_Application, Post_Protection," +
				"Analyst_Marketing, Analyst_Stock, Analyst_Search, Analyst_Acquisition, " +
				"Analyst_Process, Analyst_Storage, Analyst_Transfer, Analyst_Application, Analyst_Protection";

		String header = header1 + header2;
		TotalOutput.println(header);		
		
		File path = new File(fileListPath);
		File [] files = path.listFiles();
		runProgram(files, TotalOutput);

		TotalOutput.close();
		
		System.out.println("Done");
	}
	
	public void runProgram(File[] files, PrintWriter TotalOutput) throws FileNotFoundException {
		for (File file : files) {
			if (file.isDirectory()) {
				runProgram(file.listFiles(), TotalOutput);
			}
			else {
				if (file.getName().endsWith("txt")) {			
					if (loggingEnabled) {
						Log.write("");
						Log.write("");
						Log.write(file.getName());
						Log.write("");
					}
					
					String filename = file.getPath();
					
					//Construct counters for each analysis, Pre Q/A, Post Q/A, and MKW 
					Counter Analysis = new Counter(KW, KEW, MW, MEW, loggingEnabled);
					QCounter QAnalysis = new QCounter(KW, KEW, MW, MEW, companynames, members, loggingEnabled);

					//name, year, quarter, date, count outputs
					String name = file.getName();
					String[] temp = null;
					temp = name.split(" ");
					int h = 0;
					String cname = "";
					String year = "";
					String quarter = "";
					while ((h < temp.length) && !(temp[h].startsWith("2") | temp[h].startsWith("1"))) {
						cname = cname + temp[h] + " ";
						h++;
					}
					cname = cname.substring(0, cname.length()-1);
					if (h+1 < temp.length) {
						year = temp[h];
						quarter = temp[h+1].substring(0, temp[h+1].length()-4);
					}
					
					String result1 = Analysis.AnalyzeFile(Analysis, filename);
					String result2 = QAnalysis.AnalyzeFile(QAnalysis, filename);
					TotalOutput.println(cname + "," + year + "," + quarter + "," + result1 + "," + result2);
					
					System.out.println(name);
					if (!dataLinesOnly) {
						Analysis.printCollections(TotalOutput);
						QAnalysis.printCollections(TotalOutput);
					}
				}
			}
		}
	}
}
