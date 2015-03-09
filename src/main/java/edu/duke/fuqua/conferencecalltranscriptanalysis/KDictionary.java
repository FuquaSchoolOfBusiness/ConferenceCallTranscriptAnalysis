package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class KDictionary extends Dictionary {
	
	//Constructors
	public KDictionary (String inputname, String outputname) throws FileNotFoundException
	{
		super(inputname, outputname);
		
	}
	
	//Create array for dictionary words, separated into subarrays for each category of knowledge words
	public void ConvertText()
	{
		ArrayList<ArrayList<String>> dictionaries = new ArrayList<ArrayList<String>>();
		ArrayList<String> Cat1 = new ArrayList<String>();
		ArrayList<String> Cat2 = new ArrayList<String>();
		ArrayList<String> Cat3 = new ArrayList<String>();
		ArrayList<String> Cat4 = new ArrayList<String>();
		ArrayList<String> Cat5 = new ArrayList<String>();
		ArrayList<String> Cat6 = new ArrayList<String>();
		ArrayList<String> Cat7 = new ArrayList<String>();
		ArrayList<String> Cat8 = new ArrayList<String>();
		
		while(myInput.hasNextLine())
		{
			String line = myInput.nextLine();
			if (line.contains("1"))
			{
				Cat1.add(line.replace("1", ""));
			}
			else if (line.contains("2"))
			{
				Cat2.add(line.replace("2", ""));
			}
			else if (line.contains("3"))
			{
				Cat3.add(line.replace("3", ""));
			}
			else if (line.contains("4"))
			{
				Cat4.add(line.replace("4", ""));
			}
			else if (line.contains("5"))
			{
				Cat5.add(line.replace("5", ""));
			}
			else if (line.contains("6"))
			{
				Cat6.add(line.replace("6", ""));
			}
			else if (line.contains("7"))
			{
				Cat7.add(line.replace("7", ""));
			}
			else if (line.contains("8"))
			{
				Cat8.add(line.replace("8", ""));
			}
			else {}
		}
		
		dictionaries.add(RemoveDuplicates(Cat1));
		dictionaries.add(RemoveDuplicates(Cat2));
		dictionaries.add(RemoveDuplicates(Cat3));
		dictionaries.add(RemoveDuplicates(Cat4));
		dictionaries.add(RemoveDuplicates(Cat5));
		dictionaries.add(RemoveDuplicates(Cat6));
		dictionaries.add(RemoveDuplicates(Cat7));
		dictionaries.add(RemoveDuplicates(Cat8));
		
		for (ArrayList<String> entry:dictionaries)
		{
			myDictionary.add(entry);
		}
	}
	
	//Create text file for dictionary, splitting by category
//	public void PrintOutput()
//	{
//		int i = 1;
//		for (ArrayList<String> entryArray:myDictionary)
//		{
//			myOutput.println("Category " + i);
//			for (String entry:entryArray)
//			{
//				myOutput.println(entry);
//			}
//			myOutput.println();
//			i++;
//		}
//		myOutput.close();
//	}
	
	//Pull words into usable arrays
	public ArrayList<ArrayList<String>> CreateArrays()
	{
		ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
		
		for (ArrayList<String> entry:myDictionary)
		{
			words.add(entry);
		}
		
		return words;
	}
	
	//Check to see the size of the dictionary and subcategories
	public void getSize()
	{
		int i = 1;
		System.out.println(myDictionary.size());
		for (ArrayList<String> category:myDictionary)
		{
			System.out.println("Category " + i + " : " + category.size());
			i++;
		}
	}
	
	
}
