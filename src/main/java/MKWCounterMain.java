import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MKWCounterMain {
	
	private String fileListPath;
	private String savePath;
	private int distance;
	private boolean loggingEnabled;
	private boolean dataLinesOnly;
	
	private String knowledgeWordsPath;
	private String knowledgeExcludeWordsPath;
	private String marketingWordsPath;
	private String marketingExcludeWordsPath;
	private String companiesPath;
	private String namesPath;
	
	private String kWordsToPath = "resources/knowledge.txt";
	private String keWordsToPath = "resources/knowledgeexclude.txt";
	private String mWordsToPath = "resources/marketing.txt";
	private String meWordsToPath = "resources/marketingexclude.txt";
	
	KDictionary KW;
	Dictionary KEW;
	Dictionary MW;
	Dictionary MEW;
	ArrayList<String> companies;
	ArrayList<ArrayList<String>> members;

	public MKWCounterMain(String fp, String sppre, String kp, String kep, String mp, String mep, 
			String cp, String np, int d, boolean logging, boolean dataLines) {
		fileListPath = fp;
		savePath = sppre;
		distance = d;
		loggingEnabled = logging;
		dataLinesOnly = dataLines;
		
		knowledgeWordsPath = kp;
		knowledgeExcludeWordsPath = kep;
		marketingWordsPath = mp;
		marketingExcludeWordsPath = mep;
		companiesPath = cp;
		namesPath = np;
	}


	public void callMKWCounter(boolean include) throws FileNotFoundException
	{
		KW = new KDictionary(knowledgeWordsPath,kWordsToPath);
		KEW = new Dictionary(knowledgeExcludeWordsPath,keWordsToPath);
		MW = new Dictionary(marketingWordsPath,mWordsToPath);
		MEW = new Dictionary(marketingExcludeWordsPath,meWordsToPath);
		Utility u = new Utility();
		companies = u.makeCompanyList(companiesPath);
		members = u.makeNamesList(namesPath);
	
		PrintWriter TotalOutputMK = new PrintWriter(savePath);
		
		String header1 = "Name, Year, Quarter, Date, Pre_Total, Pre_MKStock, Pre_MKSearch, Pre_MKAcquistion, " +
		"Pre_MKProcess, Pre_MKStorage, Pre_MKTransfer, Pre_MKApplication, Pre_MKProtection, ";
		String header2 = "Post_Total, Post_Company_Total, Post_Analyst_Total, Post_MKStock, Post_MKSearch, Post_MKAcquisition, " +
		"Post_MKProcess, Post_MKStorage, Post_MKTransfer, Post_MKApplication, Post_MKProtection," +
		"Analyst_MKStock, Analyst_MKSearch, Analyst_MKAcquisition, " +
		"Analyst_MKProcess, Analyst_MKStorage, Analyst_MKTransfer, Analyst_MKApplication, Analyst_MKProtection";
		String header = header1 + header2;
		TotalOutputMK.println(header);
		
		File path = new File(fileListPath);
		File [] files = path.listFiles();
		runProgram(files, TotalOutputMK, include);
		
		TotalOutputMK.close();
		System.out.println("Done");
	}
	
	public void runProgram(File[] files, PrintWriter TotalOutputMK, boolean include) throws FileNotFoundException {
		for (File file : files) {
			if (file.isDirectory()) {
				runProgram(file.listFiles(), TotalOutputMK, include);
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
					MKWCounter myMKWCounter = new MKWCounter(KW, KEW, MW, MEW, companies, members, distance, loggingEnabled);
					
					//name, year, quarter
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
					
					String result1 = myMKWCounter.AnalyzeFilePre(filename, include);
					String result2 = myMKWCounter.AnalyzeFile(filename, include);
					TotalOutputMK.println(cname + "," + year + "," + quarter + "," + result1 + "," + result2);
					
					System.out.println(name);
					if (!dataLinesOnly) {
						myMKWCounter.printCollections(TotalOutputMK);
					}
				}
			}
		}
	}
	
}
