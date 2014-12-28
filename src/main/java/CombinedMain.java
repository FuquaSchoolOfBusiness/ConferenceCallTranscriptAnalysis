import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class CombinedMain {

	private String fileListPath;
	private String savePath;
	private int distance;
	private boolean loggingEnabled;
	private boolean dataLinesOnly;
	
	private String knowledgeWordsPath;
	private String knowledgeExcludedWordsPath;
	private String marketingWordsPath;
	private String marketingExcludedWordsPath;
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
	
	public CombinedMain(String fp, String sppre, String kp, String kep, String mp, String mep, 
			String cp, String np, int d, boolean logging, boolean dataLines) {
		fileListPath = fp;
		savePath = sppre;
		distance = d;
		loggingEnabled = logging;
		dataLinesOnly = dataLines;
		
		knowledgeWordsPath = kp;
		knowledgeExcludedWordsPath = kep;
		marketingWordsPath = mp;
		marketingExcludedWordsPath = mep;
		companiesPath = cp;
		namesPath = np;
	}
	
	public void callCounters(boolean include) throws FileNotFoundException
	{
		KW = new KDictionary(knowledgeWordsPath,kWordsToPath);
		KEW = new Dictionary(knowledgeExcludedWordsPath,keWordsToPath);
		MW = new Dictionary(marketingWordsPath,mWordsToPath);
		MEW = new Dictionary(marketingExcludedWordsPath,meWordsToPath);
		Utility u = new Utility();
		companies = u.makeCompanyList(companiesPath);
		members = u.makeNamesList(namesPath);
		
		PrintWriter CombinedOutput = new PrintWriter(savePath);

		
		String header1 = "Name, Year, Quarter, Date, Pre_Total, Pre_Marketing, Pre_Stock, Pre_Search, Pre_Acqusition, " +
		"Pre_Process, Pre_Storage, Pre_Transfer, Pre_Application, Pre_Protection, ";
		String header2 = "Post_Total, Post_Company_Total, Post_Analyst_Total, Post_Marketing, Post_Stock, Post_Search, Post_Acqusition, " +
		"Post_Process, Post_Storage, Post_Transfer, Post_Application, Post_Protection," +
		"Analyst_Marketing, Analyst_Stock, Analyst_Search, Analyst_Acquisition, " +
		"Analyst_Process, Analyst_Storage, Analyst_Transfer, Analyst_Application, Analyst_Protection,";
		String header3 = "Pre_MKStock, Pre_MKSearch, Pre_MKAcquistion, " +
		"Pre_MKProcess, Pre_MKStorage, Pre_MKTransfer, Pre_MKApplication, Pre_MKProtection, ";
		String header4 = "Post_MKStock, Post_MKSearch, Post_MKAcquisition, " +
		"Post_MKProcess, Post_MKStorage, Post_MKTransfer, Post_MKApplication, Post_MKProtection," +
		"Analyst_MKStock, Analyst_MKSearch, Analyst_MKAcquisition, " +
		"Analyst_MKProcess, Analyst_MKStorage, Analyst_MKTransfer, Analyst_MKApplication, Analyst_MKProtection";
		
		String header = header1 + header2 + header3 + header4;
		
		CombinedOutput.println(header);
		
		File path = new File(fileListPath);
		File [] files = path.listFiles();
		runProgram(files, CombinedOutput, include);

		CombinedOutput.close();
		System.out.println("Done");
	}
	
	public void runProgram(File[] files, PrintWriter CombinedOutput, boolean include) throws FileNotFoundException {
		for (File file : files) {
			if (file.isDirectory()) {
				runProgram(file.listFiles(), CombinedOutput, include);
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
					Counter Analysis = new Counter(KW, KEW, MW, MEW, loggingEnabled);
					QCounter QAnalysis = new QCounter(KW, KEW, MW, MEW, companies, members, loggingEnabled);

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
					String result3 = myMKWCounter.AnalyzeFilePre(filename, include);
					String result4 = myMKWCounter.AnalyzeFile(filename, include);
					
					int firstCommaIndex = result3.indexOf(","); int secondCommaIndex = result3.indexOf(",", firstCommaIndex+1);
					int commaIndex1 = result4.indexOf(","); int commaIndex2 = result4.indexOf(",", commaIndex1+1);
					int commaIndex3 = result4.indexOf(",", commaIndex2+1);
					result3 = result3.substring(secondCommaIndex+1);
					result4 = result4.substring(commaIndex3+1);
					
					CombinedOutput.println(cname + "," + year + "," + quarter + "," 
							+ result1 + "," + result2 + "," + result3 + "," + result4);
					
					System.out.println(name);
					
					if (!dataLinesOnly) {
						Analysis.printCollections(CombinedOutput);
						QAnalysis.printCollections(CombinedOutput);
						myMKWCounter.printCollections(CombinedOutput);
					}
				}
			}
		}
	}
}
