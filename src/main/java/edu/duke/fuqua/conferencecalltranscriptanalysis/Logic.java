/*
 * Logic class to handle core application logic pieces.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author conder
 */
public class Logic {
    
  private static final Pattern pattern = Pattern.compile("\\d");  
  static Logger logger;
  static {
    InputStream inputStream = Logic.class.getResourceAsStream("/logging.properties");
    if (null != inputStream) {
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.INFO, "init logging for Logic", e);
            e.printStackTrace();
        }
    }
    logger = Logger.getLogger(FindNames.class.getCanonicalName());
  }
  
  TreeMap<String, List<String>> results;
  PrintWriter namesOutput;
  PrintWriter companiesOutput;
    
/**
 * Parse company name from file (name of file).
 * @param file
 * @return 
 */    
    public String parseCompanyNameFromFile(File file) {
        if (file.getName().endsWith("txt")) {
            String companyName = file.getPath().substring(
                (file.getPath().lastIndexOf("/") + 1), 
                (file.getPath().length() - 12)
            ).trim();
            if (companyName.length() < 1) {
                return null;
            } else {
                return companyName;
            }
        } else {
            return null;
        }
    }
  
/**
 * Parse names from transcript file.
 * Do not include any title that is not uppercase and do not
 * include any title with a digit. We only care about names 
 * before QUESTION AND ANSWER
 * Title must have a comma (name is first).
 * Replace non alpha characters at beginning and end of name
 * Ignore certain text
 * Name should have a whitespace " "
 * Name should have between 2-3 words
 * 
 * Use BufferedReader instead of Scanner since we are using the
 * whole line and the application may be multi-threaded.
 * 
 * @param f
 * @return 
 */    
    public List parseNamesFromFile(File file) throws FileNotFoundException, IOException {
        ArrayList<String> companyReps = new ArrayList<String>();
        if (file.getName().endsWith("txt")) {		
            String filename = file.getPath();
            //logger.fine("FindNames parsing file " + filename
			FileReader conferencecall = new FileReader(filename);
            BufferedReader into = new BufferedReader(conferencecall);
			while (into.ready()) {
                //Split into lines for word analysis
				String line = into.readLine();
                String[] linesplit = line.split(":");
						
				//Breakpoint for the Q&A
				if (line.contains("QUESTION AND ANSWER")) {
                    continue;
				}
                        
                String name = this.parseNameFromLine(linesplit[0]);
                if (name != null) companyReps.add(name);
			}
			into.close();
        }
        return companyReps;
    }
    
/**
 * Parse name from line.
 * @param line
 * @return 
 */
    public String parseNameFromLine(String line) {
        // Do not include any line that is not uppercased
        if (line.equals(line.toUpperCase())) {
            // Do not include any line with a digit
            Matcher matcher = pattern.matcher(line);
            if (!matcher.find())
			{
                // Line must have a , in it
                String[] st = line.split(","); // Replaced StringTokenizer, which is faster.. but is not recommended.
                String name = st[0];
                if (st.length > 1) {
                    // If there are any non-alpha characters starting...
					name = name.replaceFirst("^[^a-zA-Z]+", "");
                    // If there are any non-alpha caracters in the end of line
					name = name.replaceAll("[^a-zA-Z]+$", "");
					name = name.trim();
                    // If any of the following string are found, skip
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
                    else if (!name.contains(" ")) {}
                    else {
                        // name must have at least one space
                        String[] st2 = name.split("\\s");
                        if (st2.length < 2 || st2.length > 4) {
                            return null;
                        } else {
                            return name;
                        }
                    }
                }   
            }   
        } 
        return null;
    }
    
    // test method...
    public void doFindNames(String savePath, String filesPath) throws IOException {  	
		//Array for output
		results = new TreeMap<>();
		namesOutput = new PrintWriter(savePath + "/Names2.txt");
		companiesOutput = new PrintWriter(savePath + "/Companies2.txt");
		File path = new File(filesPath);
		File[] files = path.listFiles();
        parseFindNames(files);
		synthesizeOutput();
    }
    
    // dump output to file
    public void synthesizeOutput() {
        for (String key: results.keySet()) {
            List<String> names = results.get(key);
            names.stream().forEach(name -> namesOutput.println(name + "1"));
            companiesOutput.println(key);
        }
        namesOutput.close();
        companiesOutput.close();
    }
    
    // Parse all company names from all files.
    public void parseFindNames(File[] files) throws IOException {
        int compid = 1;
        for (File file : files) {
            if (file.isDirectory()) {
				parseFindNames(file.listFiles());
			} else {
                if (file.getName().endsWith("txt")) {	
                    logger.fine("Filename " + file.getPath());
                    String company = this.parseCompanyNameFromFile(file);
                    logger.fine("Found company " + company);
                    List<String> names = this.parseNamesFromFile(file);
                    logger.fine("Found " + names.size() + " names for " + company);
                    if (results.containsKey(company)) {
                        List<String> onames = results.get(company);
                        names.stream()
                            .filter(name -> !onames.contains(name))
                            .forEach(name -> onames.add(name));
                    } else {
                        results.put(company, names);
                    }
                }
            }   
        }
    }
    
/**
 * Parse dictionary file.  Terms may have
 * categories associated with them.  The file should 
 * be formatted like:
 *  [TERM],[CATEGORY]
 * or
 *  [TERM]
 *
 * @param f
 * @return 
 */    
    public Lexicon parseDictionaryFromFile(File file) throws FileNotFoundException, IOException {
        Lexicon lexicon = new Lexicon();
        if (file.getName().endsWith("txt")) {		
            String filename = file.getPath();
            //logger.fine("FindNames parsing file " + filename
			FileReader dictionary = new FileReader(filename);
            BufferedReader into = new BufferedReader(dictionary);
			while (into.ready()) {
                try {
                    String line = into.readLine().trim();
                    String[] linesplit = line.split(",");
                    if (linesplit.length > 1) {
                        if (linesplit[0].trim().length() > 0)
                            addItemToDictionary(lexicon,linesplit[0].trim(),linesplit[1].trim());
                    } else {
                        if (linesplit[0].trim().length() > 0)
                            addItemToDictionary(lexicon,linesplit[0].trim(),"NONE");
                    }
                } catch (Throwable _t) {
                    Logger.getGlobal().log(
                            Level.SEVERE, "Error reading dictionary.", _t);
                }
			}
			into.close();
        }
        return lexicon;
    }
    private void addItemToDictionary(Lexicon lexicon, String word, String category) {
        if (lexicon.containsKey(word)) {
            List list = (List)lexicon.get(word);
            if (!list.contains(category))
                list.add(category);
        } else {
            List categories = new ArrayList<>();
            categories.add(category);
            lexicon.put(word,categories);
        }
    }
    
    /**
     * 1. GET DICTIONARIES
     * 2. PARSE FILE FOR NAMES
     * 3. PARSE FILE FOR WORD COUNTS PRE/POST
     * 
     * PRE COUNTS
     * totalWordCountPre
     * marketingWordCountPre
     * categoryWordCountPre[]
     * wordCountArray[] (word: count)
     * 
     * POST COUNTS
     * totalWordCountCompanyPost
     * totalWordCountAnalystPost
     * marketingWordCountCompanyPost
     * marketingWordCountAnalystPost
     * categoryWordCountCompanyPost[]
     * categoryWordCountAnalystPost[]
     * wordCountArrayCompany[]
     * wordCountArrayAnalyst[]
     * 
     */
    
    /**
     * TESTS:
     * 
     * 1. Parsing names from file.
     * 2. Parsing name from filename.
     * 3. Parsing date info from top of file.
     * 4. Counting all words.
     * 5. Counting all pre-words.
     * 6. Counting all dictionary words/categories.
     * 7. Parsing analyst/company sections.
     * 8. Counting all post words/categories.
     * 9.
     * 10. MEW words...
     * 
     * 11. Test EXCLUSIONS 
     */
    
    
    
    public void testLexiconPre() throws IOException {
        Lexicon lex = this.parseDictionaryFromFile(new File("/Users/conder/Knowledge Dictionary.txt"));
        lex.exclusions = this.loadExclusionWords("/Users/conder/Excluded Words.txt");
        
        //Lexicon lex = this.parseDictionaryFromFile(new File("/Users/conder/Marketing Dictionary.txt"));
        File sample = new File("/Users/conder/VERTEX PHARMACEUTICALS 2008 Q2.txt");
        
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
        System.out.println("1: " + String.valueOf(lex.preCountCategoryWords.get("First")));
        System.out.println("2: " + String.valueOf(lex.preCountCategoryWords.get("Second")));
        System.out.println("3: " + String.valueOf(lex.preCountCategoryWords.get("Third")));
        System.out.println("4: " + String.valueOf(lex.preCountCategoryWords.get("Fourth")));
        System.out.println("5: " + String.valueOf(lex.preCountCategoryWords.get("Fifth")));
        System.out.println("6: " + String.valueOf(lex.preCountCategoryWords.get("Sixth")));
        System.out.println("7: " + String.valueOf(lex.preCountCategoryWords.get("Seventh")));
        System.out.println(lex.size());
        System.out.println(lex.preCountAllWords);
        System.out.println(lex.countAllWords);
        System.out.println(lex.preCountAllDictionaryWords);
    }
    
    public List<String> loadExclusionWords(String path) throws FileNotFoundException, IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        while (br.ready()) {
            String line = br.readLine();
            if (line.trim().length() > 0 && !list.contains(line.trim()))
                list.add(line.trim());
        }
        return list;
    }
    
}
