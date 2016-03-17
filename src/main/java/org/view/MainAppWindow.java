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

public class MainAppWindow {

    private JFrame frame;
    private JTextField txtTrainFilePath;
    private JTextField txtTrainOptions;

    private JButton btnSelectTrainFile;
    private JButton btnSelectEvalFile;
    private JLabel lblTrainFile;
    private JLabel lblEvalFile;
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
    private JScrollPane scrollPaneEvalResults;
    private JTextArea textAreaTrainResults;
    private JTextArea textAreaEvalResults;

    private Controller controler;

    private final int TAB_ORDER_TRAIN_RESULTS = 0;
    private final int TAB_ORDER_EVAL_RESULTS = 1;
    private JTextField txtEvalFilePath;

    /**
     * Create the application.
     */
    public MainAppWindow() {
        initialize();
    }

    public void setControler(Controller controler) {

        this.controler = controler;
    }

    public void setVisible() {

        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setBounds(50, 30, 950, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        lblTrainFile = new JLabel("_train file:");
        lblTrainFile.setBounds(6, 24, 167, 16);
        frame.getContentPane().add(lblTrainFile);

        txtTrainFilePath = new JTextField();
        txtTrainFilePath.setText("/Users/martinmineo/Desarrollo/Tesis/workspace/ChatClassifier/labeled/1.xlsx");
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
                new DefaultComboBoxModel<String>(new String[] { "_choose a classifier", "_J48", "_Naive Bayes" }));
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
        separator_1.setBounds(6, 284, 938, 16);
        frame.getContentPane().add(separator_1);

        lblClassifierErrorMessage = new JLabel("_select a classifier");
        lblClassifierErrorMessage.setBounds(101, 194, 173, 16);
        lblClassifierErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblClassifierErrorMessage.setForeground(Color.RED);
        lblClassifierErrorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
        lblClassifierErrorMessage.setVisible(false);
        frame.getContentPane().add(lblClassifierErrorMessage);

        scrollPaneOptions = new JScrollPane();
        scrollPaneOptions.setBounds(645, 161, 299, 111);
        frame.getContentPane().add(scrollPaneOptions);

        textOptions = new JTextPane();
        textOptions.setEditable(false);
        scrollPaneOptions.setViewportView(textOptions);

        cbBoxClassifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (cbBoxClassifier.getSelectedIndex() != 0) {
                    controler.setSelectedClassifier((String) cbBoxClassifier.getSelectedItem());
                    StringBuilder options = controler.getOptions();

                    txtTrainOptions.setText(options.toString());
                    textOptions.setText(controler.getValidOptions());
                    textOptions.setCaretPosition(0);
                } else
                    textOptions.setText("");
                ;
            }
        });

        btnStart = new JButton("_start");
        btnStart.setBounds(516, 243, 117, 29);
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblClassifierErrorMessage.setVisible(false);
                if (cbBoxClassifier.getSelectedIndex() != 0) {
                    controler.btnStartPresed();
                } else
                    lblClassifierErrorMessage.setVisible(true);
            }
        });
        frame.getContentPane().add(btnStart);

        tabbedPaneResults = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneResults.setBounds(6, 312, 932, 335);
        frame.getContentPane().add(tabbedPaneResults);

        scrollPaneTrainResults = new JScrollPane();
        tabbedPaneResults.addTab("_train", scrollPaneTrainResults);

        textAreaTrainResults = new JTextArea();
        textAreaTrainResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneTrainResults.setViewportView(textAreaTrainResults);
        textAreaTrainResults.setEditable(false);

        scrollPaneEvalResults = new JScrollPane();
        tabbedPaneResults.addTab("_eval", scrollPaneEvalResults);

        textAreaEvalResults = new JTextArea();
        textAreaEvalResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneEvalResults.setViewportView(textAreaEvalResults);
        textAreaEvalResults.setEditable(false);

        txtEvalFilePath = new JTextField();
        txtEvalFilePath.setText("/Users/martinmineo/Desarrollo/Tesis/workspace/ChatClassifier/labeled/MAPEO - T3 WG -13 -14 -15.xlsx");
        txtEvalFilePath.setBounds(185, 57, 630, 26);
        frame.getContentPane().add(txtEvalFilePath);
        txtEvalFilePath.setColumns(10);

        lblEvalFile = new JLabel("_eval file:");
        lblEvalFile.setBounds(6, 62, 167, 16);
        frame.getContentPane().add(lblEvalFile);

        btnSelectEvalFile = new JButton("_selectEval");
        btnSelectEvalFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                fileChooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("WEKA dataset", "arff", "xls", "xlsx", "csv"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    txtEvalFilePath.setText(file.getPath());
                }
            }
        });
        btnSelectEvalFile.setBounds(827, 57, 117, 29);
        frame.getContentPane().add(btnSelectEvalFile);

        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        mnLanguage = new JMenu("_language");
        menuBar.add(mnLanguage);

        mntmEnglish = new JRadioButtonMenuItem("_english");
        mntmEnglish.setSelected(false);
        mntmEnglish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controler.changeLanguageTo(Language.ENGLISH);
                mntmEnglish.setSelected(true);
            }
        });
        mnLanguage.add(mntmEnglish);

        mntmSpanish = new JRadioButtonMenuItem("_spanish");
        mntmSpanish.setSelected(false);
        mntmSpanish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controler.changeLanguageTo(Language.SPANISH);
                mntmSpanish.setSelected(true);
            }
        });
        mnLanguage.add(mntmSpanish);

    }

    public void setTabTrainResultsText(String text) {

        tabbedPaneResults.setTitleAt(TAB_ORDER_TRAIN_RESULTS, text);
    }

    public void setTabEvalResultsText(String text) {

        tabbedPaneResults.setTitleAt(TAB_ORDER_EVAL_RESULTS, text);
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

    public void setBtnSelectEvalText(String text) {
        btnSelectEvalFile.setText(text);
    }

    public void setLblTrainFileText(String text) {
        lblTrainFile.setText(text);
    }

    public void setLblEvalFileText(String text) {
        lblEvalFile.setText(text);
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

    public void setTextAreaResultsText(String text) {
        textAreaTrainResults.setText(text);
        textAreaTrainResults.setCaretPosition(0);
    }

    public void setTextAreaEvalResults(String text) {
        textAreaEvalResults.setText(text);
        textAreaEvalResults.setCaretPosition(0);
    }

    public String getTxtTrainFilePathText() {

        return txtTrainFilePath.getText();
    }

    public String getTxtTrainOptionsText() {

        return txtTrainOptions.getText();
    }

    public String getTxtEvalFilePathText() {

        return txtEvalFilePath.getText();
    }

    public int getSelectedResultsTab() {

        return tabbedPaneResults.getSelectedIndex();
    }

    public void setProcessingTextTrainResults(String processingText) {

        textAreaTrainResults.setCaretPosition(0);
        textAreaTrainResults.setText(processingText);
        textAreaTrainResults.update(textAreaTrainResults.getGraphics());
    }

    public void setProcessingTextEvalResults(String processingText) {

        textAreaEvalResults.setCaretPosition(0);
        textAreaEvalResults.setText(processingText);
        textAreaEvalResults.update(textAreaEvalResults.getGraphics());
    }
}
