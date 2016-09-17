package org.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.controler.Controller;
import org.enums.Language;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class MainAppWindow {

    private JFrame frame;
    private JTextField txtTrainFilePath;
    private JTextField txtTrainOptions;

    private JButton btnSelectTrainFile;
    private JButton btnSelectTestFile;
    private JLabel lblTrainFile;
    private JLabel lblTestFile;
    private JComboBox<String> cbBoxClassifier;
    private JLabel lblClassifier;
    private JLabel lblParmeters;
    private JLabel lblClassifierErrorMessage;
    private JScrollPane scrollPaneOptions;
    private JTextPane textOptions;
    private JButton btnStart;
    private JMenuBar menuBar;
    private JMenu mnLanguage;
    private JRadioButtonMenuItem mntmEnglish;
    private JRadioButtonMenuItem mntmSpanish;
    private JTabbedPane tabbedPaneResults;
    private JScrollPane scrollPaneTrainResults;
    private JScrollPane scrollPaneTestResults;
    private JTextArea textAreaTrainResults;
    private JTextArea textAreaTestResults;
    private JCheckBox chckbxUseFreeling;
    private JCheckBox chckbxTrainByPhases;
    private JCheckBox chckbxTrain = new JCheckBox("_train");
    private JLabel lblCrossvalidationFolds = new JLabel("Cross-validation folds");
    private JLabel lblNgramMin = new JLabel("NGram min");
    private JLabel lblNgramMax = new JLabel("NGram max");
    private JTextField txtNGramMin;
    private JTextField txtNGramMax;

    private Controller controller;

    private final int TAB_ORDER_TRAIN_RESULTS = 0;
    private final int TAB_ORDER_TEST_RESULTS = 1;
    private JTextField txtTestFilePath;
    private JTextField txtCrossValidationFolds;
    

    /**
     * Create the application.
     */
    public MainAppWindow() {
        initialize();
    }

    public void setControler(Controller controler) {

        this.controller = controler;
    }

    public void setVisible() {

        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setBounds(50, 30, 950, 749);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        lblTrainFile = new JLabel("_train file:");
        lblTrainFile.setBounds(6, 24, 167, 16);
        frame.getContentPane().add(lblTrainFile);

        txtTrainFilePath = new JTextField();
        txtTrainFilePath.setText("/Users/martinmineo/Desarrollo/Tesis/workspace/ChatClassifier/datasets/Archivo de entrenamiento-chico.arff");
        txtTrainFilePath.setBounds(185, 19, 630, 26);
        frame.getContentPane().add(txtTrainFilePath);
        txtTrainFilePath.setColumns(10);

        btnSelectTrainFile = new JButton("_select");
        btnSelectTrainFile.setBounds(827, 19, 117, 29);
        btnSelectTrainFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                fileChooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("WEKA dataset", "arff", "xls", "xlsx", "csv"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    txtTrainFilePath.setText(file.getPath());
                }
            }
        });
        frame.getContentPane().add(btnSelectTrainFile);

        JSeparator separator = new JSeparator();
        separator.setBounds(6, 133, 938, 16);
        frame.getContentPane().add(separator);

        cbBoxClassifier = new JComboBox<String>();
        cbBoxClassifier.setBounds(101, 170, 198, 27);

        cbBoxClassifier.setModel(
                new DefaultComboBoxModel<String>(new String[] {"_choose a classifier", "_J48", "_Naive Bayes", "_SMO"}));
        frame.getContentPane().add(cbBoxClassifier);

        lblClassifier = new JLabel("_classifier:");
        lblClassifier.setBounds(6, 174, 86, 16);
        frame.getContentPane().add(lblClassifier);

        lblParmeters = new JLabel("_parameters:");
        lblParmeters.setBounds(314, 174, 84, 16);
        frame.getContentPane().add(lblParmeters);

        txtTrainOptions = new JTextField();
        txtTrainOptions.setBounds(410, 169, 223, 26);
        frame.getContentPane().add(txtTrainOptions);
        txtTrainOptions.setColumns(10);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(6, 307, 938, 16);
        frame.getContentPane().add(separator_1);

        lblClassifierErrorMessage = new JLabel("_select a classifier");
        lblClassifierErrorMessage.setBounds(101, 194, 173, 16);
        lblClassifierErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblClassifierErrorMessage.setForeground(Color.RED);
        lblClassifierErrorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
        lblClassifierErrorMessage.setVisible(false);
        frame.getContentPane().add(lblClassifierErrorMessage);

        scrollPaneOptions = new JScrollPane();
        scrollPaneOptions.setBounds(645, 161, 299, 134);
        frame.getContentPane().add(scrollPaneOptions);

        textOptions = new JTextPane();
        textOptions.setEditable(false);
        scrollPaneOptions.setViewportView(textOptions);

        cbBoxClassifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (cbBoxClassifier.getSelectedIndex() != 0) {
                    controller.setSelectedClassifier();
                    
                    StringBuilder options = controller.getOptions();

                    txtTrainOptions.setText(options.toString());
                    textOptions.setText(controller.getClassifierOptionDescription());
                    textOptions.setCaretPosition(0);
                    
                } else
                    textOptions.setText("");
                ;
            }
        });

        btnStart = new JButton("_start");
        btnStart.setBounds(516, 266, 117, 29);
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblClassifierErrorMessage.setVisible(false);
                if (cbBoxClassifier.getSelectedIndex() != 0) {
                    controller.btnStartPresed();
                } else
                    lblClassifierErrorMessage.setVisible(true);
            }
        });
        frame.getContentPane().add(btnStart);

        tabbedPaneResults = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneResults.setBounds(6, 335, 932, 335);
        frame.getContentPane().add(tabbedPaneResults);

        scrollPaneTrainResults = new JScrollPane();
        tabbedPaneResults.addTab("_train", scrollPaneTrainResults);

        textAreaTrainResults = new JTextArea();
        textAreaTrainResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneTrainResults.setViewportView(textAreaTrainResults);
        textAreaTrainResults.setEditable(false);

        scrollPaneTestResults = new JScrollPane();
        tabbedPaneResults.addTab("_test", scrollPaneTestResults);

        textAreaTestResults = new JTextArea();
        textAreaTestResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneTestResults.setViewportView(textAreaTestResults);
        textAreaTestResults.setEditable(false);

        txtTestFilePath = new JTextField();
        txtTestFilePath.setText(
                "/Users/martinmineo/Desarrollo/Tesis/workspace/ChatClassifier/datasets/Archivo de clasificacion-nombres.arff");
        txtTestFilePath.setBounds(185, 57, 630, 26);
        frame.getContentPane().add(txtTestFilePath);
        txtTestFilePath.setColumns(10);

        lblTestFile = new JLabel("_test file:");
        lblTestFile.setBounds(6, 62, 167, 16);
        frame.getContentPane().add(lblTestFile);

        btnSelectTestFile = new JButton("_selectTest");
        btnSelectTestFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                fileChooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("WEKA dataset", "arff", "xls", "xlsx", "csv"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    txtTestFilePath.setText(file.getPath());
                }
            }
        });
        btnSelectTestFile.setBounds(827, 57, 117, 29);
        frame.getContentPane().add(btnSelectTestFile);

        chckbxUseFreeling = new JCheckBox("_useFreeling");
        chckbxUseFreeling.setSelected(true);
        chckbxUseFreeling.setBounds(6, 222, 128, 23);
        frame.getContentPane().add(chckbxUseFreeling);
        
        chckbxTrainByPhases = new JCheckBox("_trainByPhases");
        chckbxTrainByPhases.setSelected(true);
        chckbxTrainByPhases.setBounds(146, 222, 217, 23);
        frame.getContentPane().add(chckbxTrainByPhases);
        
        lblCrossvalidationFolds.setBounds(396, 226, 142, 16);
        frame.getContentPane().add(lblCrossvalidationFolds);
        
        txtCrossValidationFolds = new JTextField();
        txtCrossValidationFolds.setText("10");
        txtCrossValidationFolds.setBounds(547, 221, 86, 26);
        frame.getContentPane().add(txtCrossValidationFolds);
        txtCrossValidationFolds.setColumns(10);
        
        JPanel panel = new JPanel();
        panel.setBounds(41, 257, 323, 39);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        txtNGramMax = new JTextField();
        txtNGramMax.setText("3");
        txtNGramMax.setBounds(251, 6, 64, 26);
        panel.add(txtNGramMax);
        txtNGramMax.setColumns(10);
        
        lblNgramMax.setBounds(165, 11, 74, 16);
        panel.add(lblNgramMax);
        
        txtNGramMin = new JTextField();
        txtNGramMin.setText("1");
        txtNGramMin.setBounds(89, 6, 64, 26);
        panel.add(txtNGramMin);
        txtNGramMin.setColumns(10);
        
        lblNgramMin.setBounds(6, 11, 81, 16);
        panel.add(lblNgramMin);
        chckbxTrain.setSelected(true);
        
        chckbxTrain.setBounds(376, 267, 128, 23);
        frame.getContentPane().add(chckbxTrain);

        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        mnLanguage = new JMenu("_language");
        menuBar.add(mnLanguage);

        mntmEnglish = new JRadioButtonMenuItem("_english");
        mntmEnglish.setSelected(false);
        mntmEnglish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.changeLanguageTo(Language.ENGLISH);
                mntmEnglish.setSelected(true);
            }
        });
        mnLanguage.add(mntmEnglish);

        mntmSpanish = new JRadioButtonMenuItem("_spanish");
        mntmSpanish.setSelected(false);
        mntmSpanish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.changeLanguageTo(Language.SPANISH);
                mntmSpanish.setSelected(true);
            }
        });
        mnLanguage.add(mntmSpanish);

    }

    public void setTabTrainResultsText(String text) {

        tabbedPaneResults.setTitleAt(TAB_ORDER_TRAIN_RESULTS, text);
    }

    public void setTabTestResultsText(String text) {

        tabbedPaneResults.setTitleAt(TAB_ORDER_TEST_RESULTS, text);
    }

    public void setMntmEnglishSelected(boolean b) {
        mntmEnglish.setSelected(b);
    }

    public void setMntmSpanishSelected(boolean b) {
        mntmSpanish.setSelected(b);
    }

    public void setBtnSelectTrainText(String text) {
        btnSelectTrainFile.setText(text);
    }

    public void setBtnSelectTestText(String text) {
        btnSelectTestFile.setText(text);
    }

    public void setLblTrainFileText(String text) {
        lblTrainFile.setText(text);
    }

    public void setLblTestFileText(String text) {
        lblTestFile.setText(text);
    }

    public void setCbBoxClassifier(String[] text) {
        cbBoxClassifier.setModel(new DefaultComboBoxModel<String>(text));
    }

    public void setLblClassifierText(String text) {
        lblClassifier.setText(text);
        ;
    }

    public void setLblParmetersText(String text) {
        lblParmeters.setText(text);
    }

    public void setLblClassifierErrorMessageText(String text) {
        lblClassifierErrorMessage.setText(text);
    }

    public void setBtnStartText(String text) {
        btnStart.setText(text);
    }

    public void setMnLanguageText(String text) {
        mnLanguage.setText(text);
    }

    public void setMntmEnglishText(String text) {
        mntmEnglish.setText(text);
    }

    public void setMntmSpanishText(String text) {
        mntmSpanish.setText(text);
    }

    public void setTextUseFreeling(String text) {
        chckbxUseFreeling.setText(text);
    }
    
    public void setTextUsePhases(String text) {
        chckbxTrainByPhases.setText(text);
    }

    public String getTxtTrainFilePathText() {

        return txtTrainFilePath.getText();
    }

    public String getTxtTrainOptionsText() {

        return txtTrainOptions.getText();
    }

    public String getTxtTestFilePathText() {

        return txtTestFilePath.getText();
    }

    public int getSelectedResultsTab() {

        return tabbedPaneResults.getSelectedIndex();
    }

    public void setProcessingTextTrainResults(String processingText) {

        textAreaTrainResults.setCaretPosition(0);
        textAreaTrainResults.setText(processingText);
        textAreaTrainResults.update(textAreaTrainResults.getGraphics());
        textAreaTrainResults.setCaretPosition(0);
    }

    public void setProcessingTextTestResults(String processingText) {

        textAreaTestResults.setCaretPosition(0);
        textAreaTestResults.setText(processingText);
        textAreaTestResults.update(textAreaTestResults.getGraphics());
        textAreaTestResults.setCaretPosition(0);
    }
    
    public String getTextAreaTestResults() {
        
        return textAreaTrainResults.getText();
    }

    public boolean getUseFreeling() {

        return chckbxUseFreeling.isSelected();
    }
    
    public boolean getTrainByPhases() {
        
        return chckbxTrainByPhases.isSelected();
    }
    
    public String getCrossValidationFolds() {
        
        return txtCrossValidationFolds.getText();
    }
    
    public String getSelectedClassifier() {
        
        return cbBoxClassifier.getSelectedItem().toString();
    }
    
    public String getNGramMin() {
        
        return txtNGramMin.getText();
    }
    
    public String getNGramMax() {
        
        return txtNGramMax.getText();
    }
    
    public boolean isTrainSelected() {
        
        return chckbxTrain.isSelected();
    }
}
