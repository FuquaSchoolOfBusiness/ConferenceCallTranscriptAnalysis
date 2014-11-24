/**
 * 
 * @author Geoff Huang
 * @version 1.0 - Basic file scan for words
 * @version 2.0 - Inheritance using separate extensions for each dictionary
 * @version 3.0 - Comments added for cleanup
 * 
 * Purpose is to extract dictionary words and create new text file with only unique entries
 * File will be used to create comparable arrays
 *
 */
	
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Dictionary {
	
	//Parameters:
	//Input dictionary
	//Output text file
	//Output words array
	//Output dictionary (for knowledge words)
	
	protected Scanner myInput;
	//protected PrintWriter myOutput;
	protected ArrayList<String> myWords = new ArrayList<String>();
	protected ArrayList<ArrayList<String>> myDictionary = new ArrayList<ArrayList<String>>();
	
	//Constructors:
	//Designate the location of the original dictionary words and a file for the organized output
	public Dictionary (String inputname, String outputname) throws FileNotFoundException
	{
		if (inputname.length() == 0) {
			createEmptyDictionary();
		} else {
			FileReader reader = new FileReader(inputname);
			myInput = new Scanner(reader);
			ConvertText();
		}
	}

	//Create array for dictionary words
	public void ConvertText()
	{	
		while(myInput.hasNextLine())
		{
			String line = myInput.nextLine();
			if (!myWords.contains(line)) {
				myWords.add(line);
			}
		}
	}
	
	//Create array for empty dictionary
	public void createEmptyDictionary()
	{	
		myWords = new ArrayList<String>();
	}
	
	//Remove the duplicate entries
	public static ArrayList<String> RemoveDuplicates(ArrayList<String> input)
	{
		ArrayList<String> unique = new ArrayList<String>();
		for (String entry:input)
		{
			if (unique.contains(entry))
			{}
			else
			{
				unique.add(entry);
			}
		}
		return unique;
	}
	
	//Pull words into a usable array
	public ArrayList<String> CreateArray()
	{
		ArrayList<String> words = new ArrayList<String>();
		
		for (String entry:myWords)
		{
			words.add(entry);
		}
		
		return words;
	}
	
	//Check to see size of the dictionaries
	public void getSize()
	{
		System.out.println(myWords.size());
	}
}
