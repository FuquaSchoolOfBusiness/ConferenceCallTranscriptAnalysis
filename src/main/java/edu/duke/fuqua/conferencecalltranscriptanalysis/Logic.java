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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logic contains many functions of the application.
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
    logger = Logger.getLogger(MainFrame.class.getCanonicalName());
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
    public Dictionary parseDictionaryFromFile(File file) throws FileNotFoundException, IOException {
        Dictionary lexicon = new Dictionary();
        int linenum = 0;
        if (file.getName().endsWith("txt")) {		
            String filename = file.getPath();
            lexicon.setFile(file);
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
    private void addItemToDictionary(Dictionary lexicon, String word, String category, Integer order) {
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
        
        // Inputs:
        //  Generate Audit Files?
        //
        //  Location of transcripts?
        //
        //  Dictionary 1 (Marketing Terms)
        //  Dictionary 2 (Knowledge Words)
        //
        //  Output location?
        //
        //  Distance M-K?
        //  Output Distance M-K?
        //
        //  Output delimiter: Tab, Comma
        
        //FileWriter audit = new FileWriter("/Users/conder/audit.txt");
        
        
        File sample = new File("/Users/conder/workdata/VERTEX PHARMACEUTICALS 2008 Q2.txt");
        //File sample = new File("/Users/conder/workdata/VERTEX PHARMACEUTICALS 2009 Q3.txt");
        
        Dictionary dictionary1 = this.parseDictionaryFromFile(new File("/Users/conder/Marketing Dictionary.txt"));
        
        //Lexicon lex = this.parseDictionaryFromFile(
        //        new File("/Users/conder/Knowledge Dictionary copy.txt"));
        Dictionary dictionary2 = this.parseDictionaryFromFile(
                new File("/Users/conder/Knowledge Dictionary.txt"));
        dictionary2.exclusions = this.loadExclusionWords(new File("/Users/conder/Excluded Words.txt"));
      //  dictionary2.audit = audit; 
        //dictionary1.exclusions = this.loadExclusionWords("/Users/conder/Excluded Words.txt");
       // dictionary1.audit = audit;
                
        Writer output = new FileWriter("/Users/conder/data.txt");
        writeHeader(true, true, dictionary1, dictionary2, "\t", output);
        
        processFiles(true, true, new File("/Users/conder/Downloads/Coding Files 2/Call Transcripts"), dictionary1, dictionary2, "\t", true, null, output);
        
       // audit.close();
       // output.close();
    }
    
    public void processFiles(
            Boolean outputCounts,
            Boolean outputDistances,
            File directory, 
            Dictionary dictionary1, 
            Dictionary dictionary2, 
            String delimiter, 
            Boolean audit, 
            MainFrame mf,
            Writer output) {
        
        List<File> fileList = Arrays.asList(directory.listFiles());
        fileList.stream().parallel().forEach(
                (file) -> { 
            if (file.isDirectory()) {
                processFiles(outputCounts, 
                        outputDistances, 
                        file, 
                        (Dictionary)dictionary1.clone(), 
                        (Dictionary)dictionary2.clone(), 
                        delimiter, 
                        audit, 
                        mf, 
                        output);
            } else {
                if (file.getName().endsWith(".txt")) {
                    try {
                        String audit_name = file.getName();
                        audit_name = audit_name.substring(0, audit_name.lastIndexOf("."));

                        mf.writeToOutput("Processing " + file.getPath() + "...\n");

                        Counter counter1 = new Counter();
                        Counter counter2 = new Counter();
                        
                        //dictionary1.reset(); dictionary2.reset();
                        FileWriter auditFile = null;
                        if (audit) {
                            auditFile = new FileWriter(file.getParent() + "/" + audit_name + "_audit.dat");
                            counter1.audit = auditFile; counter2.audit = auditFile;
                            auditFile.write("FORMAT FOR WORD MATCHES:\n");
                            auditFile.write("[Line Number],[Word Number]:WORD MATCH,[PRE|POST-ANALYST|POST-COMPANY],[word found],[stem],[Dictionary]:[category]\n");
                            auditFile.write("\nFORMAT FOR DISTANCE MATCHES:\n");
                            auditFile.write("[Line Number]:DISTANCE MATCH,[PRE|POST-ANALYST|POST-COMPANY],[Dictionary 2 Word],[Dictionary 2 Word Number],[Dictionary 1 Word],[Dictionary 1 Word Number],[Number of words between],[Dictionary 2]:[category]\n\n");
                        }
                        
                        processFile(
                                outputCounts, 
                                outputDistances, 
                                dictionary1, 
                                dictionary2, 
                                counter1, 
                                counter2, 
                                file, 
                                delimiter, 
                                output);
                        try { auditFile.close(); } catch (Throwable _t) { }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
                });
                            
        
    };
       
    public void writeHeader(
            Boolean outputCounts,
            Boolean outputDistances,
            Dictionary dictionary1, 
            Dictionary dictionary2, 
            String delimiter, 
            Writer output) throws IOException {
        // PRE HEADERS
        output.write("Name" + delimiter +  "Year" + delimiter + "Quarter" + delimiter + "Date" + delimiter + "Pre_Total");
        
        if (outputCounts) {
        
        for (String category: dictionary1.getCategories()) {
            output.write(delimiter + "Pre_");
            output.write(category); 
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter + "Pre_");
            output.write(category); 
        }
        
        }
        
        output.write(delimiter + "Post_Total" + delimiter + "Post_Company_Total" + delimiter + "Post_Analyst_Total");
        
        if (outputCounts) {
        // POST COMPANY HEADERS
        for (String category: dictionary1.getCategories()) {
            output.write(delimiter + "Post_Company_");
            output.write(category); 
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter + "Post_Company_");
            output.write(category); 
        }
        // POST ANALYST HEADERS
        for (String category: dictionary1.getCategories()) {
            output.write(delimiter + "Post_Analyst_");
            output.write(category); 
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter +  "Post_Analyst_");
            output.write(category); 
        }      
        }
        
        if (outputDistances) {
        // DISTANCE HEADERS
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter +  "D1D2_Pre_");
            output.write(category);
        } 
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter +  "D1D2_Post_Company_");
            output.write(category);
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter +  "D1D2_Post_Analyst_");
            output.write(category);
        }
        }
        
        output.write("\n");
        
    }
    
    public void processFile(
            Boolean outputCounts,
            Boolean outputDistances,
            Dictionary dictionary1, 
            Dictionary dictionary2, 
            Counter counter1,
            Counter counter2,
            File transcript, 
            String delimiter,
            Writer output) throws IOException {
        
        Logic log = new Logic();
        
        FileReader reader = new FileReader(transcript);
        BufferedReader readerx = new BufferedReader(reader);
		
        Boolean pre_mode = true;
        
        //name, year, quarter, date, count outputs
		String file_name = transcript.getName();
		String[] temp = null;
		temp = file_name.split(" ");
		int h = 0;
		String company_name = "";
		String year = "";
		String quarter = "";
		while ((h < temp.length) && !(temp[h].startsWith("2") | temp[h].startsWith("1"))) {
            company_name = company_name + temp[h] + " ";
			h++;
		}
		company_name = company_name.substring(0, company_name.length()-1);
		if (h+1 < temp.length) {
            year = temp[h];
            quarter = temp[h+1].substring(0, temp[h+1].length()-4);
		}
					
                    
        LinkedList<Name> lines = new LinkedList<>();
        ArrayList<String> analysts = new ArrayList<>();
        ArrayList<String> companyReps = new ArrayList<>();
        
        int line_number = 0;
        int classify = 3;
        String date = null;
        
        while (readerx.ready()) {
			String line = readerx.readLine();
            line_number++;
            if (line_number < 20 && date == null && Logic.isMonth(line)) {
                date = log.parseLineForDate(line);
            }
            if (line.contains("QUESTION AND ANSWER")) {
                pre_mode = false;
			} else if (pre_mode) {
                List<Match> matches = counter1.countWords(line, Counter.TYPE_PRE, line_number, null);
                counter2.countWords(line, Counter.TYPE_PRE, line_number, matches);
			} else {
                String name = "";
                String title = "";
                
                // There is a problem where some lines have ":" in them
                // especially <sync time="01:26:12"/>
                line = line.replaceAll("<[^>]+>", "");
                if (line.contains(": ")) {
                    String[] pieces = line.split(": ");
                    if (pieces.length > 0) {
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
                            logger.fine(name + "classified as analyst - case 6");
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
                            logger.fine(name + "classified as company rep - case 7");
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
                            logger.fine(name + "classified as analyst - case 8");
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
                            logger.fine(name + "classified as company rep - case 10");
                        }
                        
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
            if (name.getType() == Name.TYPE_COMPANY_REP) { 
                List<Match> matches = counter1.countWords(name.getLine(), Counter.TYPE_POST_COMPANY, name.getNumber(), null);
                counter2.countWords(name.getLine(), Counter.TYPE_POST_COMPANY, name.getNumber(), matches);
            } else if (name.getType() == Name.TYPE_ANALYST) {
                List<Match> matches = counter1.countWords(name.getLine(), Counter.TYPE_POST_ANALYST, name.getNumber(), null);
                counter2.countWords(name.getLine(), Counter.TYPE_POST_ANALYST, name.getNumber(), matches);
            }
        }
        
        // TRANSCRIPT VALUES
/*        output.write(company_name);
        output.write( delimiter );
        output.write(year);
        output.write( delimiter );
        output.write(quarter);
        output.write( delimiter );
        output.write( date == null ? "": date );
        output.write( delimiter );
        
        // PRE VALUES
        output.write(String.valueOf(counter1.preCountAllWords));// +":" + dictionary2.preCountAllWords);
        if (outputCounts) {
        for (String category: dictionary1.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter1.preCountCategoryWords.get(category) == null ? "0":
                        String.valueOf(counter1.preCountCategoryWords.get(category))
            );  
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter2.preCountCategoryWords.get(category) == null ? "0":
                        String.valueOf(counter2.preCountCategoryWords.get(category))
            ); 
        }
        }
        output.write(delimiter);
        output.write(String.valueOf(counter1.postCountAllCompanyWords + counter1.postCountAllAnalystWords));// + ":" + (dictionary2.postCountAllCompanyWords + dictionary2.postCountAllAnalystWords));
        output.write(delimiter);
        output.write(String.valueOf(counter1.postCountAllCompanyWords));// +":" + dictionary2.postCountAllCompanyWords);
        output.write(delimiter);
        output.write(String.valueOf(counter1.postCountAllAnalystWords));// +":" + dictionary2.postCountAllAnalystWords);
        
        // POST COMPANY VALUES
        if (outputCounts) {
        for (String category: dictionary1.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter1.postCountCategoryCompanyWords.get(category) == null ? "0":
                    String.valueOf(counter1.postCountCategoryCompanyWords.get(category))
            ); 
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter2.postCountCategoryCompanyWords.get(category) == null ? "0":
                    String.valueOf(counter2.postCountCategoryCompanyWords.get(category))
            ); 
        }
        // POST ANALYST VALUES
        for (String category: dictionary1.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter1.postCountCategoryAnalystWords.get(category) == null ? "0":
                    String.valueOf(counter1.postCountCategoryAnalystWords.get(category))
            );
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter2.postCountCategoryAnalystWords.get(category) == null ? "0":
                    String.valueOf(counter2.postCountCategoryAnalystWords.get(category))
            ); 
        }           
        }
        
        // DIFFERENCE COUNTS
        if (outputDistances) {
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter2.distanceCounters.get("PRE_" + category) == null ? "0":
                            String.valueOf(counter2.distanceCounters.get("PRE_" + category))
            ); 
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter2.distanceCounters.get("POST_COMPANY_" + category) == null ? "0":
                            String.valueOf(counter2.distanceCounters.get("POST_COMPANY_" + category))
            ); 
        }
        for (String category: dictionary2.getCategories()) {
            output.write(delimiter);
            output.write(
                    counter2.distanceCounters.get("POST_ANALYST_" + category) == null ? "0":
                            String.valueOf(counter2.distanceCounters.get("POST_ANALYST_" + category))
            ); 
        }
        }
        output.write("\n");
  */      
        readerx.close();
        
        if (counter1.audit != null) {
            counter1.audit.write("\nCOMPANY REPRESENTATIVES\n");
            counter1.audit.write(companyReps.toString());
            counter1.audit.write("\n");
            counter1.audit.write("\nANALYSTS\n");
            counter1.audit.write(analysts.toString());
            counter1.audit.write("\n");
        }
        
              
//        audit.close();

        //System.out.println("size: " + dictionary2.size());
        //System.out.println("precountall:" + dictionary2.preCountAllWords);
        //System.out.println("allwords:" + dictionary2.countAllWords);
        //System.out.println("prealldictionary: " + dictionary2.preCountAllDictionaryWords);
        
        //System.out.println("postcountallan: " + dictionary2.postCountAllAnalystWords);
        //System.out.println("postcountallco: " + dictionary2.postCountAllCompanyWords);
        
    }
    
    public ArrayList<String> loadExclusionWords(File path) throws FileNotFoundException, IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        while (br.ready()) {
            String line = br.readLine();
            if (line.trim().length() > 0 && !list.contains(line.trim()))
                list.add(line.trim());
        }
        Logger.getGlobal().finest("Loaded exclusion " + list.size() + " words.");
        return list;
    }
    
    public String parseLineForDate(String line) {
        
			String[] lineArray = line.split(" ");
			if (!Logic.isMonth(lineArray[0]) && !Logic.isMonth(lineArray[1])) { 
                // example: Q1 2003 Abbott
				// Laboratories Earnings
				// Conference Call - Final FD
				// (Fair Disclosure) Wire April
				// 9, 2003 Wednesday
				int z = 0;
	  			String m = "", d = "", y = "";
				  
	  			while (z < lineArray.length) {
                    if (Logic.isMonth(lineArray[z])) {
                        m = lineArray[z];
	  					break;
	  				}
	  				z++;
	  			}
				  
	  			if (lineArray.length > (z + 2)) {
                    d = lineArray[z + 1].replace(",","");
					d = d.replaceAll("[^\\d]", "");
	  				y = lineArray[z + 2].replace(".","");
					if (Logic.isNumeric(d) && Logic.isNumeric(y)) {
                        return d + " " + m + " " + y;
					}
	  			}
			} else {
				// example: April 9, 2002 (need to transform it to 9 April 2002)
				if (line.contains(",")) {
                    lineArray[1] = lineArray[1].replace(",", "");
					return lineArray[1] + " " + lineArray[0] + " " + lineArray[2];
				} // example: 9 April 2002 
				else 
                {
                    if (line.contains("/")) {
                        String[] parts = line.split("/");
						line = parts[0].trim();
					}
					return line;
				}
			}
			return null;
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
    
	public static String stripEndingPunctuation(String str) {
		str = str.replaceFirst("^[^a-zA-Z]+", "");
		str = str.replaceAll("[^a-zA-Z]+$", "");
		return str;
	}

    public static Boolean isPhrase(String s) {
        return s.contains(" ");
    }
    
	public static boolean isExcludedWord(String word, List<String> pertinentExcludedWords) {
		for (String EWord : pertinentExcludedWords) {
			if (word.startsWith(EWord.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
    
}
