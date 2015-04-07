package edu.duke.fuqua.conferencecalltranscriptanalysis;

/**
 * 
 * @author Geoff Huang
 * @version 1.0 - Basic file scan for names
 * @version 1.1 - Added filelist scanner
 * @version 1.2 - Introduced ArrayList to remove duplicates
 * @version 2.0 - Recognize entire title up until presence of a colon
 * @version 2.1 - Create reference array for later use
 * 
 * Purpose is to find names within quarterly reports based on string capitalization
 * General format will list speaker in CAPS at start of paragraph
 * The method is then to search the first two words and check if they are completely capitalized
 * If so, add the name to array and output as text file for use in Q/A analysis
 * Based on program written by Professor Susan Rodger
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FindNames {
	
    // FindNames input is a files path and a save path
    //  the files path is a directory containing directories of transcripts..
    //      can we assume that all files in this context regardless of their location are valid?
    //      this routine crawls all subdirectories...
    //  From the filename it finds the company name.
    //  From the file it crawls each line and then looks for lines with ":" in them..
    //-> Is this necessary to crawl all files first--- could a company person show up in the QnA that was not in the top?
    
	private String filesPath;
	private String savePath;
	
	private PrintWriter namesOutput;
	private PrintWriter companiesOutput;
	
	private TreeMap<String, ArrayList<String>> results;
	private TreeSet<String> cresults;
    
	public FindNames (String files, String save) {
		filesPath = files;
		savePath = save;
	}
	
  private static Logger logger;
  static {
    InputStream inputStream = FindNames.class.getResourceAsStream("/logging.properties");
    if (null != inputStream) {
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "init logging system", e);
            e.printStackTrace();
        }
    }
    
  }    
    public static final void main(String[] argv) throws Exception {
        logger = Logger.getLogger(FindNames.class.getCanonicalName());
//        FindNames fn = new FindNames("/Users/conder/Documents/work/projects/ChrisMoormanProject/TestContent/Call Transcripts",
//        "/Users/conder/Documents/work/projects/ChrisMoormanProject/BaseLine/BaseAppCode");
        Logic l = new Logic();
        Date _start = new java.util.Date();
        System.out.println("Started: " + _start.toString());
        l.testLexiconPre();
         Date _end = new java.util.Date();
        System.out.println("Ended: " + _end.toString());
        System.out.println("Elapsed: " + (_end.getTime() - _start.getTime()));
        
       
        //fn.initializeAndRun();
//        l.doFindNames(
//                "/Users/conder/Documents/work/projects/ChrisMoormanProject/BaseLine/BaseAppCode",
//                "/Users/conder/Documents/work/projects/ChrisMoormanProject/TestContent/Call Transcripts"
//                );
 /*       Lexicon lex = l.parseDictionaryFromFile(new File("/Users/conder/knowledge_dictionary.txt"));
        //Lexicon lex = l.parseDictionaryFromFile(new File("/Users/conder/Marketing Dictionary.txt"));
        File sample = new File("/Users/conder/VERTEX PHARMACEUTICALS 2009 Q3.txt");
        
        FileReader reader = new FileReader(sample);
        BufferedReader readerx = new BufferedReader(reader);
		
        while (readerx.ready()) {
			String line = readerx.readLine();
            
            if (line.contains("QUESTION AND ANSWER")) {
				break;
			} else {
                lex.countWords(line, Lexicon.TYPE_PRE);
			}
            
        }
        
        readerx.close();
        
        System.out.println(String.valueOf(lex.preCountCategoryWords.get("Fourth")));
        System.out.println(lex.size());
        System.out.println(lex.preCountAllWords);
        System.out.println(lex.countAllWords);
        System.out.println(lex.preCountAllDictionaryWords);
 */       
        Date _finish = new java.util.Date();
        logger.fine("FIND NAMES RUN TIME IN MS = " + (_finish.getTime() - _start.getTime()));
    } 
    
	public void initializeAndRun() throws FileNotFoundException {
		//Array for output
		results = new TreeMap<String, ArrayList<String>>();
		cresults = new TreeSet<String>();
		
		namesOutput = new PrintWriter(savePath + "/Names.txt");
		companiesOutput = new PrintWriter(savePath + "/Companies.txt");

		File path = new File(filesPath);
		File[] files = path.listFiles();
		runProgram(files);
		synthesizeOutput();
	}
	
	public void runProgram(File[] files) throws FileNotFoundException {	
		for (File file : files) {
			if (file.isDirectory()) {
				runProgram(file.listFiles());
			}
			else {
				if (file.getName().endsWith("txt")) {
					String filename = file.getPath();
                    logger.fine("FindNames parsing file " + filename);
					
					ArrayList<String> companyReps = new ArrayList<String>();
					
					//Check individual files
					FileReader conferencecall = new FileReader(filename);
					Scanner into = new Scanner (conferencecall);
					
					//Check for termination point in file
					while (into.hasNextLine()) {
						//Split into lines for word analysis
						String line = into.nextLine();
						Scanner linesplit = new Scanner(line);
						linesplit.useDelimiter(":");
						
						//Breakpoint for the Q&A
						if (line.contains("QUESTION AND ANSWER")) {
							continue;
						}

						if (linesplit.hasNext())
						{
							String title = linesplit.next();
                            // Do not include any rep whose title 
                            // is not in upper case.
							if (title == title.toUpperCase())
							{
								// Do not add any company reps whose title
                                // contains a digit anywhere.
                                Pattern pattern = Pattern.compile("\\d");
								Matcher matcher = pattern.matcher(title);
								if (!matcher.find())
								{
									companyReps.add(title);
								}
							}		
						}
						linesplit.close();
					}
					into.close();
					String companyName = filename.substring((filename.lastIndexOf("/") + 1), (filename.length() - 12)).trim();
					if (results.containsKey(companyName)) {
						appendListToMap(companyName, companyReps);
					} else {
						results.put(companyName, companyReps);
					}
				}
			}
		}
	}
	
	private void synthesizeOutput() {
		//Print text file for unique names
		int index = 0;
		for (String company: results.keySet())
		{
			index++;
			companiesOutput.println(company);
			for (String rep: results.get(company)) {
				Scanner line2 = new Scanner(rep);
				line2.useDelimiter(",");
				if (line2.hasNext())
				{
					String name = line2.next();
                    // If there are any non-alpha characters starting...
					name = name.replaceFirst("^[^a-zA-Z]+", "");
                    // If there are any non-alpha caracters in the end of line
					name = name.replaceAll("[^a-zA-Z]+$", "");
					name = name.trim();
					
					//Remove unnecessary results
					if (name.contains("OPERATOR")){}
					else if (name.contains("FINANC")){}
					else if (name.contains("DISCLOSURE")){}
					else if (name.contains("CORPORATE")){}
					else if (name.contains("COMPANY")){}
					else if (name.contains("CONFERENCE")){}
					else if (name.contains("OVERVIEW")){}
					else if (name.contains("PARTICIPANT")){}
					else if (name.contains("UNIDENTIFIED")){}
					else if (name.contains("SUMMARY")){}
					else if (name.contains("REMARKS")){}
					else if (name.contains("UNKNOWN")){}
					else if (name.contains("FACILITATOR")){}
					else if (name.contains("PARTICIPANT")){}

					//Condense Array
					else if (!cresults.contains(name))
					{
						cresults.add(name);
						if (personName(name)) namesOutput.println(name + index);
					}
				}
				line2.close();
			}
		}
        logger.fine("FindNames:synthesizeOutput results size " + results.size());
        logger.fine("FindNames:synthesizeOutput cresults size " + cresults.size());
		namesOutput.close();
		companiesOutput.close();
	}
	
	private boolean personName(String name) {
		if (!name.contains(" ")) return false;
		String[] parts = name.split(" ");
		if (parts.length < 2 || parts.length > 4) return false;
		return true;
	}
	
	private void appendListToMap(String companyName, ArrayList<String> companyReps) {
		ArrayList<String> alreadyExistingReps = results.get(companyName);
		for (String rep: companyReps) {
			if (!alreadyExistingReps.contains(rep)) {
				alreadyExistingReps.add(rep);
			}
		}
		results.put(companyName, alreadyExistingReps);
	}
}