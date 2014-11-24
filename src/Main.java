import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final String newline = "\n";
	private static final int allottedLength = 30;
	
	private static JFrame frame;
    JButton openButton;
    JButton saveButton;
    JTextArea textArea;
    
    JCheckBox includePhrases;
    JCheckBox enableLogging;
    JCheckBox dataLinesOnly;
    
    JButton marketingButton;
    JButton marketingExcludeButton;
    JButton knowledgeButton;
    JButton knowledgeExcludeButton;
//    JButton addedDictButton;
//    JButton addedDictExcludeButton; 
    JTextField textboxDistance;
    
    JButton companiesButton;
    JButton namesButton;
    
    JFileChooser fcOpenFile;
    JFileChooser fcSaveDirectory;
    JFileChooser fcSaveDirectoryPost;
    JFileChooser fcMarketingWords;
    JFileChooser fcMarketingExcludeWords;
    JFileChooser fcKnowledgeWords;
    JFileChooser fcKnowledgeExcludeWords;
//    JFileChooser fcAddedDictWords;
//    JFileChooser fcAddedDictExcludeWords;
    JFileChooser fcCompanies;
    JFileChooser fcNames;
    
    static String filename = "";
    static String directoryName = "";
    static String saveName = "";
    static String marketingFileName = "";
    static String marketingExcludeFileName = "";
    static String knowledgeFileName = "";
    static String knowledgeExcludeFileName = "";
//    static String addedDictFileName = "";
//    static String addedDictExcludeFileName = "";
    static String companiesFileName = "";
    static String namesFileName = "";
    static int distance = 0;
    
    JTextField textboxSavingName;
    JButton runButton;
    JButton runButton2;
    JButton runAllButton;
    
    public Main() {
        super(new BorderLayout());

//        filename = "C:\\Moorman\\Knowledge Program\\Sample Text Files";
//        directoryName = "C:\\Moorman\\Knowledge Program";
//        saveName = "";
//        marketingFileName = "C:\\Moorman\\Knowledge Program\\Dictionary Files\\MarketingWords";
//        marketingExcludeFileName = "C:\\Moorman\\Knowledge Program\\Dictionary Files\\MarketingExcludeWords";
//        knowledgeFileName = "C:\\Moorman\\Knowledge Program\\Dictionary Files\\KnowledgeWords";
//        knowledgeExcludeFileName = "C:\\Moorman\\Knowledge Program\\Dictionary Files\\KnowledgeExcludeWords";
//        companiesFileName = "C:\\Moorman\\Knowledge Program\\Dictionary Files\\Companies";
//        namesFileName = "C:\\Moorman\\Knowledge Program\\Dictionary Files\\Names";
        distance = 0;
        
        textArea = new JTextArea(2,30);
        textArea.setMargin(new Insets(5,5,15,10));
        textArea.setText("Update messages are shown here.");
        textArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textArea);

        fcOpenFile = new JFileChooser();
        fcOpenFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        fcMarketingWords = new JFileChooser();
        fcMarketingExcludeWords = new JFileChooser();
        fcKnowledgeWords = new JFileChooser();
        fcKnowledgeExcludeWords = new JFileChooser();
//        fcAddedDictWords = new JFileChooser();
//        fcAddedDictExcludeWords = new JFileChooser();
        fcCompanies = new JFileChooser();
        fcNames = new JFileChooser();

        fcSaveDirectory = new JFileChooser();
        fcSaveDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        openButton = new JButton("Select Files");
        openButton.addActionListener(this);
        
        includePhrases = new JCheckBox("Include D1-D2 Phrases");
        includePhrases.setSelected(true);
        includePhrases.addActionListener(this);
        
        enableLogging = new JCheckBox("Enable Logging");
        enableLogging.setSelected(true);
        enableLogging.addActionListener(this);
        
        dataLinesOnly = new JCheckBox("Data Lines Only");
        dataLinesOnly.setSelected(false);
        dataLinesOnly.addActionListener(this);

        runButton = new JButton("Run D1 or D2 or D3 Counter");
        runButton.addActionListener(this);
        
        runButton2 = new JButton("Run D1/D2 Counter");
        runButton2.addActionListener(this);
        
        runAllButton = new JButton("Run All");
        runAllButton.addActionListener(this);
        
        saveButton = new JButton("Choose Saving Directory");
        saveButton.addActionListener(this);
        
        marketingButton = new JButton("Dictionary 1");
        marketingButton.addActionListener(this);
        
        marketingExcludeButton = new JButton("Excluded Words 1");
        marketingExcludeButton.addActionListener(this);
        
        knowledgeButton = new JButton("Dictionary 2");
        knowledgeButton.addActionListener(this);
        
        knowledgeExcludeButton = new JButton("Excluded Words 2");
        knowledgeExcludeButton.addActionListener(this);
        
//        addedDictButton = new JButton("Dictionary 3");
//        addedDictButton.addActionListener(this);
//        
//        addedDictExcludeButton = new JButton("Excluded Words 3");
//        addedDictExcludeButton.addActionListener(this);
                
        companiesButton = new JButton("Companies File");
        companiesButton.addActionListener(this);
        
        namesButton = new JButton("Names File");
        namesButton.addActionListener(this);
        
        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanelOpen = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT)); //use FlowLayout
        buttonPanelOpen.add(openButton);
        buttonPanelOpen.add(saveButton);
        textboxSavingName = new JTextField(20);
        textboxSavingName.setText("filename.csv");
        textboxSavingName.addActionListener(this);
        buttonPanelOpen.add(textboxSavingName);
        openButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        textboxSavingName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel buttonPanelParameters = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT));
        
        Box dict1Box = Box.createVerticalBox();
        dict1Box.add(marketingButton);
        dict1Box.add(marketingExcludeButton);
        dict1Box.add(Box.createVerticalGlue());
        
        Box dict2Box = Box.createVerticalBox();
        dict2Box.add(knowledgeButton);
        dict2Box.add(knowledgeExcludeButton);
        dict2Box.add(Box.createVerticalGlue());
        
        Box dict3Box = Box.createVerticalBox();
//        dict3Box.add(addedDictButton);
//        dict3Box.add(addedDictExcludeButton);
        dict3Box.add(Box.createVerticalGlue());
        
        buttonPanelParameters.add(dict1Box);
        buttonPanelParameters.add(dict2Box);
        buttonPanelParameters.add(dict3Box);
        buttonPanelParameters.add(companiesButton);
        buttonPanelParameters.add(namesButton);
        textboxDistance = new JTextField(5);
        textboxDistance.setText("3");
        textboxDistance.addActionListener(this);
        buttonPanelParameters.add(textboxDistance);
        Box toggleBox = Box.createVerticalBox();
        toggleBox.add(includePhrases);
        toggleBox.add(enableLogging);
        toggleBox.add(dataLinesOnly);
        toggleBox.add(Box.createVerticalGlue());
        buttonPanelParameters.add(toggleBox);
        
        JPanel buttonPanelRun = new JPanel();
        buttonPanelRun.add(textScrollPane);
        buttonPanelRun.add(runButton);
        buttonPanelRun.add(runButton2);
        buttonPanelRun.add(runAllButton);
        
        //Add the buttons and the log to this panel.
        add(buttonPanelOpen, BorderLayout.PAGE_START);
        add(buttonPanelParameters, BorderLayout.CENTER);
        add(buttonPanelRun, BorderLayout.PAGE_END);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		//Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fcOpenFile.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcOpenFile.getSelectedFile();
                filename = file.getAbsolutePath();
//                if (!filename.endsWith(".txt")) {
//                	textArea.append("Action canceled: Please open a .txt file.");
//                }
//                else {
                	textArea.append(newline + "Opening: " + file.getName() + ".");
//                }
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        } 
        else if (e.getSource() == saveButton) {
        	int returnVal = fcSaveDirectory.showOpenDialog(Main.this);
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File directory = fcSaveDirectory.getSelectedFile();
            	directoryName = directory.getAbsolutePath();
            	textArea.append(newline + "Saving to directory: " + directoryName);
            }
        }
        else if (e.getSource() == marketingButton) {
        	int returnVal = fcMarketingWords.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcMarketingWords.getSelectedFile();
                marketingFileName = file.getAbsolutePath();
                textArea.append(newline + "Opening: " + file.getName() + ".");
                String fname = file.getName();
                if (fname.contains(".")) fname = file.getName().substring(0, fname.lastIndexOf("."));
                if (fname.length() > allottedLength) fname = shortenString(fname);
                marketingButton.setText(fname);
                frame.pack();
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        }
        else if (e.getSource() == marketingExcludeButton) {
        	int returnVal = fcMarketingWords.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcMarketingWords.getSelectedFile();
                marketingExcludeFileName = file.getAbsolutePath();
                textArea.append(newline + "Opening: " + file.getName() + ".");
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        }
        else if (e.getSource() == knowledgeButton) {
        	int returnVal = fcMarketingWords.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcMarketingWords.getSelectedFile();
                knowledgeFileName = file.getAbsolutePath();
                textArea.append(newline + "Opening: " + file.getName() + ".");
                String fname = file.getName();
                if (fname.contains(".")) fname = file.getName().substring(0, fname.lastIndexOf("."));
                if (fname.length() > allottedLength) fname = shortenString(fname);
                knowledgeButton.setText(fname);
                frame.pack();
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        }
        else if (e.getSource() == knowledgeExcludeButton) {
        	int returnVal = fcMarketingWords.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcMarketingWords.getSelectedFile();
                knowledgeExcludeFileName = file.getAbsolutePath();
                textArea.append(newline + "Opening: " + file.getName() + ".");
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        }
//        else if (e.getSource() == addedDictButton) {
//        	int returnVal = fcAddedDictWords.showOpenDialog(Main.this);
//
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                File file = fcAddedDictWords.getSelectedFile();
//                addedDictFileName = file.getAbsolutePath();
//                textArea.append(newline + "Opening: " + file.getName() + ".");
//                String fname = file.getName();
//                fname = file.getName().substring(0, fname.indexOf('.'));
//                if (fname.length() > allottedLength) fname = shortenString(fname);
//                addedDictButton.setText(fname);
//                frame.pack();
//            } 
//            else {
//                textArea.append(newline + "Open command canceled by user.");
//            }
//        }
//        else if (e.getSource() == addedDictExcludeButton) {
//        	int returnVal = fcAddedDictWords.showOpenDialog(Main.this);
//
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                File file = fcAddedDictWords.getSelectedFile();
//                addedDictExcludeFileName = file.getAbsolutePath();
//                textArea.append(newline + "Opening: " + file.getName() + ".");
//            } 
//            else {
//                textArea.append(newline + "Open command canceled by user.");
//            }
//        }
        else if (e.getSource() == companiesButton) {
        	int returnVal = fcMarketingWords.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcMarketingWords.getSelectedFile();
                companiesFileName = file.getAbsolutePath();
                textArea.append(newline + "Opening: " + file.getName() + ".");
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        }
        else if (e.getSource() == namesButton) {
        	int returnVal = fcMarketingWords.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcMarketingWords.getSelectedFile();
                namesFileName = file.getAbsolutePath();
                textArea.append(newline + "Opening: " + file.getName() + ".");
            } 
            else {
                textArea.append(newline + "Open command canceled by user.");
            }
        }
        else if (e.getSource() == runButton || e.getSource() == runButton2 || e.getSource() == runAllButton) {
        	saveName = directoryName + "/" + textboxSavingName.getText();
        	boolean loggingEnabled = enableLogging.isSelected();
        	boolean includeOnlyDataLines = dataLinesOnly.isSelected();
        	try {
        		if (loggingEnabled) {
        			String logName = saveName;
        			if (logName.endsWith(".csv")) {
        				logName = logName.substring(0, logName.indexOf(".csv"));
        			}
        			Log.open(logName+".tsv");
        		}
        	} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
        	distance = Integer.parseInt(textboxDistance.getText());
        	boolean ready = true;
        	if (filename.equals("")) {
        		textArea.append(newline + "Choose a file directory to analyze."); ready = false;
        	}
        	if (directoryName.equals("")) {
        		textArea.append(newline + "Choose a saving directory."); ready = false;
        	}
        	if (saveName.equals("")) {
        	 	textArea.append(newline + "Type a file name and extension"); ready = false;
        	}
        	if (marketingFileName.equals("")) {
        		textArea.append(newline + "Choose your dictionary 1 text file"); ready = false;
        	}
        	if (knowledgeFileName.equals("")) {
        		textArea.append(newline + "Choose your dictionary 2 text file"); ready = false;
        	}
//        	if (addedDictFileName.equals("")) {
//        		textArea.append(newline + "Choose your dictionary 3 text file"); ready = false;
//        	}
        	if (companiesFileName.equals("")) {
        		textArea.append(newline + "Choose your companies text file"); ready = false;
        	}
        	if (namesFileName.equals("")) {
        		textArea.append(newline + "Choose your names text file"); ready = false;
        	}
        	if (ready == true) {
        		textArea.append(newline + "Program running...");
        		if (e.getSource() == runButton) {
        			CounterMain counter = new CounterMain(filename, saveName,
        					knowledgeFileName, knowledgeExcludeFileName, marketingFileName, marketingExcludeFileName, 
        					//addedDictFileName, addedDictExcludeFileName, companiesFileName, namesFileName, 
        					companiesFileName, namesFileName, loggingEnabled, includeOnlyDataLines);
            		
        			try {
        				counter.callCounter();
        			} catch (FileNotFoundException e1) {
        				e1.printStackTrace();
        			}
        		} else if (e.getSource() == runButton2) {
        			MKWCounterMain mkwcounter = new MKWCounterMain(filename, saveName, 
        					knowledgeFileName, knowledgeExcludeFileName, marketingFileName, marketingExcludeFileName,  
            				companiesFileName, namesFileName, distance, loggingEnabled, includeOnlyDataLines);
        			
            		try {
            			if (includePhrases.isSelected()) { 
            				mkwcounter.callMKWCounter(true);
            			}
            			else {
            				mkwcounter.callMKWCounter(false);
            			}
    				} catch (FileNotFoundException e1) {
    					e1.printStackTrace();
    				}
        		} else {
        			CombinedMain combocounter = new CombinedMain(filename, saveName, 
        					knowledgeFileName, knowledgeExcludeFileName, marketingFileName, marketingExcludeFileName,  
            				companiesFileName, namesFileName, distance, loggingEnabled, includeOnlyDataLines);
        			
            		try {
            			if (includePhrases.isSelected()){ 
            				combocounter.callCounters(true);
            			}
            			else {
            				combocounter.callCounters(false);
            			}
    				} catch (FileNotFoundException e1) {
    					e1.printStackTrace();
    				}
        		}
        		textArea.append(newline + "Done.");
        		if (loggingEnabled) {
        			Log.close();
        		}
        	}
        }
	}
	
	private String shortenString(String fname) {
		fname = fname.substring(0, allottedLength-3);
		fname += "...";
		return fname;
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Text Analysis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //frame.setSize(new Dimension(500,200));
        //Add content to the window.
        frame.add(new Main());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
