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

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FindNames {
	
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
					System.out.println(filename);
					
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
							if (title == title.toUpperCase())
							{
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
					name = name.replaceFirst("^[^a-zA-Z]+", "");
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
		System.out.println(results.size());
		System.out.println(cresults.size());
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