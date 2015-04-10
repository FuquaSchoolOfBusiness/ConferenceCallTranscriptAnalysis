/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.fuqua.conferencecalltranscriptanalysis;

import java.awt.Color;
import java.awt.Window;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author conder
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.wordDistanceSpinner.setValue(3);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        transcriptDirectoryButton = new javax.swing.JButton();
        auditCheckbox = new javax.swing.JCheckBox();
        dictionary1Button = new javax.swing.JButton();
        exclusion1Button = new javax.swing.JButton();
        dictionary2Button = new javax.swing.JButton();
        exclusion2Button = new javax.swing.JButton();
        commaSeparatedValuesButton = new javax.swing.JRadioButton();
        tabSeparatedValuesRadioButton = new javax.swing.JRadioButton();
        runWordCountsButton = new javax.swing.JButton();
        runDistanceCountsButton = new javax.swing.JButton();
        runBothButton = new javax.swing.JButton();
        dictionary1Label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputDisplay = new javax.swing.JTextArea();
        exclusion1Label = new javax.swing.JLabel();
        dictionary2Label = new javax.swing.JLabel();
        exclusion2Label = new javax.swing.JLabel();
        transcriptDirectoryLabel = new javax.swing.JLabel();
        header = new javax.swing.JLabel();
        wordDistanceSpinner = new javax.swing.JSpinner();
        wordDistanceLabel = new javax.swing.JLabel();
        outputFileLocation = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        quitApplicationMenuItem = new javax.swing.JMenuItem();

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("File");
        jMenuBar3.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar3.add(jMenu6);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        transcriptDirectoryButton.setForeground(new java.awt.Color(0, 51, 204));
        transcriptDirectoryButton.setText("Choose directory of transcripts");
        transcriptDirectoryButton.setName("transcriptButton"); // NOI18N
        transcriptDirectoryButton.setOpaque(true);
        transcriptDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transcriptDirectoryButtonActionPerformed(evt);
            }
        });

        auditCheckbox.setText("Audit?");

        dictionary1Button.setForeground(new java.awt.Color(0, 51, 204));
        dictionary1Button.setText("Choose Dictionary 1");
        dictionary1Button.setName("dictionary1Button"); // NOI18N
        dictionary1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dictionary1ButtonActionPerformed(evt);
            }
        });

        exclusion1Button.setForeground(new java.awt.Color(0, 51, 204));
        exclusion1Button.setText("Choose exclusions for Dictionary 1");
        exclusion1Button.setName("exclusion1Button"); // NOI18N
        exclusion1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exclusion1ButtonActionPerformed(evt);
            }
        });

        dictionary2Button.setForeground(new java.awt.Color(0, 51, 204));
        dictionary2Button.setText("Choose Dictionary 2");
        dictionary2Button.setName("dictionary2Button"); // NOI18N
        dictionary2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dictionary2ButtonActionPerformed(evt);
            }
        });

        exclusion2Button.setForeground(new java.awt.Color(0, 51, 204));
        exclusion2Button.setText("Choose exclusions for Dictionary 2");
        exclusion2Button.setName("exclusion2Button"); // NOI18N
        exclusion2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exclusion2ButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(commaSeparatedValuesButton);
        commaSeparatedValuesButton.setText("Comma-Separated-Values");

        buttonGroup1.add(tabSeparatedValuesRadioButton);
        tabSeparatedValuesRadioButton.setSelected(true);
        tabSeparatedValuesRadioButton.setText("Tab-Separated-Values");

        runWordCountsButton.setForeground(new java.awt.Color(0, 51, 204));
        runWordCountsButton.setText("Run word counts");
        runWordCountsButton.setName("runWordCountsButton"); // NOI18N
        runWordCountsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runWordCountsButtonActionPerformed(evt);
            }
        });

        runDistanceCountsButton.setForeground(new java.awt.Color(0, 51, 204));
        runDistanceCountsButton.setText("Run distance counts");
        runDistanceCountsButton.setToolTipText("");
        runDistanceCountsButton.setName("runDistanceCountsButton"); // NOI18N
        runDistanceCountsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runDistanceCountsButtonActionPerformed(evt);
            }
        });

        runBothButton.setForeground(new java.awt.Color(0, 51, 204));
        runBothButton.setText("Run both");
        runBothButton.setName("runBothButton"); // NOI18N
        runBothButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runBothButtonActionPerformed(evt);
            }
        });

        dictionary1Label.setForeground(new java.awt.Color(153, 0, 51));
        dictionary1Label.setText("No file for Dictionary 1 selected.");

        outputDisplay.setColumns(20);
        outputDisplay.setRows(5);
        jScrollPane1.setViewportView(outputDisplay);

        exclusion1Label.setForeground(new java.awt.Color(153, 0, 51));
        exclusion1Label.setText("No file for Dictionary 1 exclusions selected.");

        dictionary2Label.setForeground(new java.awt.Color(153, 0, 51));
        dictionary2Label.setText("No file for Dictionary 2 selected.");

        exclusion2Label.setForeground(new java.awt.Color(153, 0, 51));
        exclusion2Label.setText("No file for Dictionary 2 exclusions selected.");

        transcriptDirectoryLabel.setForeground(new java.awt.Color(153, 0, 51));
        transcriptDirectoryLabel.setText("No directory selected for transcripts.");

        header.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        header.setText("Conference Call Transcript Analyzer");

        wordDistanceLabel.setText("Maximum number of words between Dictionary 1 and Dictionary 2 stems?");

        outputFileLocation.setText("Output file location: ");

        jMenu1.setText("File");

        quitApplicationMenuItem.setText("Quit Application");
        quitApplicationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitApplicationMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(quitApplicationMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(dictionary2Button)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(exclusion2Button))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 6, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dictionary2Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(exclusion2Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(wordDistanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(wordDistanceLabel)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(transcriptDirectoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dictionary1Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exclusion1Button)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(runWordCountsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(runDistanceCountsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(runBothButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dictionary1Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exclusion1Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(transcriptDirectoryButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(outputFileLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(commaSeparatedValuesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabSeparatedValuesRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(auditCheckbox)))
                        .addGap(14, 14, 14))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transcriptDirectoryButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transcriptDirectoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dictionary1Button)
                    .addComponent(exclusion1Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dictionary1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exclusion1Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dictionary2Button)
                    .addComponent(exclusion2Button))
                .addGap(1, 1, 1)
                .addComponent(dictionary2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exclusion2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wordDistanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wordDistanceLabel))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(auditCheckbox)
                    .addComponent(commaSeparatedValuesButton)
                    .addComponent(tabSeparatedValuesRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputFileLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(runWordCountsButton)
                    .addComponent(runDistanceCountsButton)
                    .addComponent(runBothButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    JFileChooser transcriptLocationChooser = new JFileChooser();
    File transcriptLocationFile;
    private void transcriptDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transcriptDirectoryButtonActionPerformed
        transcriptLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = transcriptLocationChooser.showOpenDialog(MainFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            transcriptLocationFile = transcriptLocationChooser.getSelectedFile();
            transcriptDirectoryLabel.setText(transcriptLocationFile.getPath());
            transcriptDirectoryLabel.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_transcriptDirectoryButtonActionPerformed

    JFileChooser dictionary1Chooser = new JFileChooser();
    File dictionary1File;
    private void dictionary1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dictionary1ButtonActionPerformed
        dictionary1Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = dictionary1Chooser.showOpenDialog(MainFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dictionary1File = dictionary1Chooser.getSelectedFile();
            dictionary1Label.setText(dictionary1File.getPath());
            dictionary1Label.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_dictionary1ButtonActionPerformed

    JFileChooser dictionary2Chooser = new JFileChooser();
    File dictionary2File;
    private void dictionary2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dictionary2ButtonActionPerformed
        dictionary2Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = dictionary2Chooser.showOpenDialog(MainFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dictionary2File = dictionary2Chooser.getSelectedFile();
            dictionary2Label.setText(dictionary2File.getPath());
            dictionary2Label.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_dictionary2ButtonActionPerformed

    JFileChooser exclusion1Chooser = new JFileChooser();
    File exclusion1File;
    private void exclusion1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exclusion1ButtonActionPerformed
        exclusion1Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = exclusion1Chooser.showOpenDialog(MainFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            exclusion1File = exclusion1Chooser.getSelectedFile();
            exclusion1Label.setText(exclusion1File.getPath());
            exclusion1Label.setForeground(Color.BLACK);
        }   
    }//GEN-LAST:event_exclusion1ButtonActionPerformed

    JFileChooser exclusion2Chooser = new JFileChooser();
    File exclusion2File;
    private void exclusion2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exclusion2ButtonActionPerformed
        exclusion1Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = exclusion2Chooser.showOpenDialog(MainFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            exclusion2File = exclusion2Chooser.getSelectedFile();
            exclusion2Label.setText(exclusion2File.getPath());
            exclusion2Label.setForeground(Color.BLACK);
        }   
    }//GEN-LAST:event_exclusion2ButtonActionPerformed

    Integer word_distance = 3;
    private void runDistanceCountsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runDistanceCountsButtonActionPerformed

                runOutput(false, true);

    }//GEN-LAST:event_runDistanceCountsButtonActionPerformed

    private void quitApplicationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitApplicationMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitApplicationMenuItemActionPerformed

    private void runOutput(Boolean outputCounts, Boolean outputDistances) {
        
SwingWorker worker = new SwingWorker<Void, Void>() {
    @Override
    public Void doInBackground() {
        
 Date started = new java.util.Date();
        outputDisplay.setText("Started processing files at " + (started) + ".\n");
        Logic logic = new Logic();
        Lexicon dictionary1 = null;
        Lexicon dictionary2 = null;
                
        Writer output;
        try {
      
            // Load dictionary 1
            dictionary1 = logic.parseDictionaryFromFile(dictionary1File);
            dictionary1.distanceBetweenWords = (Integer) wordDistanceSpinner.getValue();
            if (exclusion1File != null && dictionary1 != null) 
                try {
                    dictionary1.exclusions = logic.loadExclusionWords(exclusion1File);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            // Load dictionary 2
            dictionary2 = logic.parseDictionaryFromFile(dictionary2File);
            dictionary2.distanceBetweenWords = (Integer) wordDistanceSpinner.getValue();
            if (exclusion2File != null && dictionary2 != null) 
                try {
                    dictionary2.exclusions = logic.loadExclusionWords(exclusion2File);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
        
            File outfile = new File("data.txt");
            output = new FileWriter(outfile);
            
            outputFileLocation.setText("Output file location: " + outfile.getAbsolutePath());
            
            logic.writeHeader(
                    outputCounts,
                    outputDistances,
                    dictionary1, 
                    dictionary2, 
                    commaSeparatedValuesButton.isSelected() ? ",": "\t",
                    output);
            
            logic.processFiles(
                    outputCounts,
                    outputDistances,
                    transcriptLocationFile, 
                    dictionary1, 
                    dictionary2,
                    commaSeparatedValuesButton.isSelected() ? ",": "\t",
                    auditCheckbox.isSelected(),
                    MainFrame.this,
                    output);
        
            output.close();
            
            Date finished = new java.util.Date();
            
            MainFrame.this.writeToOutput("Finished processing files at " + (finished) + ".\n");
            MainFrame.this.writeToOutput("Total processing time: " + (finished.getTime() - started.getTime()) + "ms\n");
        
            JOptionPane.showMessageDialog(MainFrame.this, "Finished processing files.");
        
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            
            JOptionPane.showMessageDialog(MainFrame.this, "Error when processing files.", "Error", JOptionPane.ERROR_MESSAGE);
        }         
        
        return null;
    }
};        
        
        worker.execute();
        
        
    }
    private void runWordCountsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runWordCountsButtonActionPerformed

                runOutput(true, false);

    }//GEN-LAST:event_runWordCountsButtonActionPerformed

    private void runBothButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runBothButtonActionPerformed

                runOutput(true, true);
 
    }//GEN-LAST:event_runBothButtonActionPerformed

    public void writeToOutput(String text) {
        this.outputDisplay.append(text);
        this.revalidate();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox auditCheckbox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton commaSeparatedValuesButton;
    private javax.swing.JButton dictionary1Button;
    private javax.swing.JLabel dictionary1Label;
    private javax.swing.JButton dictionary2Button;
    private javax.swing.JLabel dictionary2Label;
    private javax.swing.JButton exclusion1Button;
    private javax.swing.JLabel exclusion1Label;
    private javax.swing.JButton exclusion2Button;
    private javax.swing.JLabel exclusion2Label;
    private javax.swing.JLabel header;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputDisplay;
    private javax.swing.JLabel outputFileLocation;
    private javax.swing.JMenuItem quitApplicationMenuItem;
    private javax.swing.JButton runBothButton;
    private javax.swing.JButton runDistanceCountsButton;
    private javax.swing.JButton runWordCountsButton;
    private javax.swing.JRadioButton tabSeparatedValuesRadioButton;
    private javax.swing.JButton transcriptDirectoryButton;
    private javax.swing.JLabel transcriptDirectoryLabel;
    private javax.swing.JLabel wordDistanceLabel;
    private javax.swing.JSpinner wordDistanceSpinner;
    // End of variables declaration//GEN-END:variables
}
