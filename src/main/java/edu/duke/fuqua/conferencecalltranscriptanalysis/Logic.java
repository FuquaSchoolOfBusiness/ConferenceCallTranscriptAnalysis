/*
 * Logic class to handle core application logic pieces.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
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
 * AUDIT FILES
 * Generate one audit file per company.
 * Audit file will be structured as:
 * 2009 Q3 (VERTEX PHARMACEUTICALS 2009 Q3.txt)
 * 21: PRE, word, matched term, category
 * 54: POST-COMPANY, word, matches, term, category
 * 55: POST-ANALYST, word, matches, term, category
 * CATEGORY: 3, CATEGORY: 5, CATEGORY: N
 * 
 */  
  
  
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
        int linenum = 0;
        if (file.getName().endsWith("txt")) {		
            String filename = file.getPath();
            Logger.getGlobal().finest("Parsing dictionary file " + file.getName());
			FileReader dictionary = new FileReader(filename);
            BufferedReader into = new BufferedReader(dictionary);
			while (into.ready()) {
                try {
                    String line = into.readLine().trim();
                    String[] linesplit = line.split(",");
                    if (linesplit.length > 1) {
                        if (linesplit[0].trim().length() > 0)
                            addItemToDictionary(lexicon,linesplit[0].trim(),linesplit[1].trim(),++linenum);
                    } else {
                        if (linesplit[0].trim().length() > 0)
                            addItemToDictionary(lexicon,linesplit[0].trim(),"NONE",++linenum);
                    }
                } catch (Throwable _t) {
                    Logger.getGlobal().log(
                            Level.SEVERE, "Error reading dictionary.", _t);
                }
			}
			into.close();
        }
        Logger.getGlobal().finest("Dictionary has " + lexicon.size() + " terms.");
        return lexicon;
    }
    private void addItemToDictionary(Lexicon lexicon, String word, String category, Integer order) {
        if (!lexicon.contains(word)) {
            Term term = new Term();
            term.setCategory(category);
            term.setTerm(word);
            term.setOrder(order);
            lexicon.add(term);
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
        
        FileWriter audit = new FileWriter("/Users/conder/audit.txt");
        
        
        File sample = new File("/Users/conder/VERTEX PHARMACEUTICALS 2008 Q2.txt");
        //File sample = new File("/Users/conder/VERTEX PHARMACEUTICALS 2009 Q3.txt");
        
        //Lexicon lex = this.parseDictionaryFromFile(new File("/Users/conder/Marketing Dictionary.txt"));
        
        //Lexicon lex = this.parseDictionaryFromFile(
        //        new File("/Users/conder/Knowledge Dictionary copy.txt"));
        Lexicon lex = this.parseDictionaryFromFile(
                new File("/Users/conder/Knowledge Dictionary.txt"));
        lex.exclusions = this.loadExclusionWords("/Users/conder/Excluded Words.txt");
        lex.audit = audit;       
        
        FileReader reader = new FileReader(sample);
        BufferedReader readerx = new BufferedReader(reader);
		
        Boolean pre_mode = true;
        
        LinkedList<Name> lines = new LinkedList<>();
        ArrayList<String> analysts = new ArrayList<>();
        ArrayList<String> companyReps = new ArrayList<>();
        
        int line_number = 0;
        int classify = 3;
        
        while (readerx.ready()) {
			String line = readerx.readLine();
            line_number++;
            if (line.contains("QUESTION AND ANSWER")) {
                pre_mode = false;
			} else if (pre_mode) {
                lex.countWords(line, Lexicon.TYPE_PRE, line_number);
			} else {
                String name = "";
                String title = "";
                
                if (line.contains(":")) {
                    String[] pieces = line.split(":");
                    name = pieces[0];
                    if (name.contains(",")) {
                        //Split on comma to remove the name's titles
                        String[] parts = name.split(",");
                        name = parts[0].trim();
                        if (parts.length > 1) title = parts[1].trim();
                    }
                    if (name.startsWith("OPERATOR")) {
                        Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_OPERATOR, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                        lines.add(nameObj);
                        classify = 0; // operator
                    }
                    else if (name.toUpperCase().startsWith("[COPYRIGHT")) {
                        classify = 3; // do nothing
                    }
                    else if (name.equals("")) {
                        if (classify == 0) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_OPERATOR, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                        } else if (classify == 1) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_COMPANY_REP, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                        } else if (classify == 2) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_ANALYST, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                        }
                    }
                    else if (analysts.contains(name) || name.contains("ANALYST") || title.contains("ANALYST")) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_ANALYST, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                            classify = 2;
                            if (!analysts.contains(name)) analysts.add(name);
                            System.out.println("classified as analyst - case 6");
                    }
                    else if (companyReps.contains(name)) { //|| arrayContains(myNames.get(i), name)) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_COMPANY_REP, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                            if (!companyReps.contains(name)) companyReps.add(name);
                            classify = 1;
                            System.out.println("classified as company rep - case 7");
                    }
                        // The operator introduces analysts
                    else if (classify == 0) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_ANALYST, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                            classify = 2;
                            if (!analysts.contains(name)) analysts.add(name);
                            System.out.println("classified as analyst - case 8");
                    }
                        // If name is listed in company representative array add to
                        // company statements
                    else {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_COMPANY_REP, 
                                pieces.length > 1 ? pieces[1].trim(): "", 
                                name);
                            lines.add(nameObj);
                            if (!companyReps.contains(name)) companyReps.add(name);
                            classify = 1;
                            System.out.println("classified as company rep - case 10");
                    }
                        
                    
                } else {
                        if (classify == 0) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_OPERATOR, 
                                line.trim(), 
                                name);
                            lines.add(nameObj);
                        } else if (classify == 1) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_COMPANY_REP, 
                                line.trim(), 
                                name);
                            lines.add(nameObj);
                        } else if (classify == 2) {
                            Name nameObj = Name.generateName(
                                line_number, 
                                Name.TYPE_ANALYST, 
                                line.trim(), 
                                name);
                            lines.add(nameObj);
                        }                    
                }
            }
        }
        
        for (Name name: lines) {
            if (name.getType() == Name.TYPE_COMPANY_REP) 
                lex.countWords(name.getLine(), Lexicon.TYPE_POST_COMPANY, name.getNumber());
            else if (name.getType() == Name.TYPE_ANALYST)
                lex.countWords(name.getLine(), Lexicon.TYPE_POST_ANALYST, name.getNumber());
        }
        
        readerx.close();
        audit.close();
        System.out.println("1: " + String.valueOf(lex.preCountCategoryWords.get("Stock")));
        System.out.println("2: " + String.valueOf(lex.preCountCategoryWords.get("Search")));
        System.out.println("3: " + String.valueOf(lex.preCountCategoryWords.get("Acquisition")));
        System.out.println("4: " + String.valueOf(lex.preCountCategoryWords.get("Process")));
        System.out.println("5: " + String.valueOf(lex.preCountCategoryWords.get("Storage")));
        System.out.println("6: " + String.valueOf(lex.preCountCategoryWords.get("Transfer")));
        System.out.println("7: " + String.valueOf(lex.preCountCategoryWords.get("Application")));
        System.out.println("1: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Stock")));
        System.out.println("2: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Search")));
        System.out.println("3: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Acquisition")));
        System.out.println("4: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Process")));
        System.out.println("5: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Storage")));
        System.out.println("6: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Transfer")));
        System.out.println("7: " + String.valueOf(lex.postCountCategoryCompanyWords.get("Application")));
        System.out.println("1: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Stock")));
        System.out.println("2: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Search")));
        System.out.println("3: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Acquisition")));
        System.out.println("4: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Process")));
        System.out.println("5: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Storage")));
        System.out.println("6: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Transfer")));
        System.out.println("7: " + String.valueOf(lex.postCountCategoryAnalystWords.get("Application")));
        System.out.println("size: " + lex.size());
        System.out.println("precountall:" + lex.preCountAllWords);
        System.out.println("allwords:" + lex.countAllWords);
        System.out.println("prealldictionary: " + lex.preCountAllDictionaryWords);
        
        System.out.println("postcountallan: " + lex.postCountAllAnalystWords);
        System.out.println("postcountallco: " + lex.postCountAllCompanyWords);
        
        
        System.out.println("ANALYSTS: " + analysts.toString());
        System.out.println("REPS:" + companyReps.toString());
    }
    
    public List<String> loadExclusionWords(String path) throws FileNotFoundException, IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        while (br.ready()) {
            String line = br.readLine();
            if (line.trim().length() > 0 && !list.contains(line.trim()))
                list.add(line.trim());
        }
        Logger.getGlobal().finest("Loaded exclusion " + list.size() + " words.");
        return list;
    }
    
}
