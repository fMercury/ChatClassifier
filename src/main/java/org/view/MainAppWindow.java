
package org.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.controler.Controller;
import org.ipa.GroupAnalysisRow;
import org.ipa.GroupCreationRow;

public class MainAppWindow {

    private JFrame frame;
    private JPanel dataProcessing;
    private JPanel dataAnalysis;
    private JTabbedPane tabbedPane;
    private JTextField txtTrainFilePath;
    private JButton btnSelectTrainFile;
    private JButton btnSelectTestFile;
    private JLabel lblTrainFile;
    private JLabel lblTestFile;
    
    private JScrollPane scrollPaneOptions;
    private JTextPane textOptions;
    private JMenu mnLanguage;
    private JRadioButtonMenuItem mntmEnglish;
    private JRadioButtonMenuItem mntmSpanish;
    private JTabbedPane tabbedPaneResults;
    private JScrollPane scrollPaneTrainResults;
    private JScrollPane scrollPaneTestResults;
    private JTextArea textAreaTrainResults;
    private JTextArea textAreaTestResults;
    private JCheckBox chckbxUseFreeling;
    private JLabel lblCrossvalidationFolds;
    private JLabel lblNgramMin;
    private JLabel lblNgramMax;
    private JTextField txtNGramMin;
    private JTextField txtNGramMax;

    private Controller controller;

    private final int TAB_ORDER_TRAIN_RESULTS = 0;
    private final int TAB_ORDER_TEST_RESULTS = 1;
    private JTextField txtTestFilePath;
    private JTextField txtCrossValidationFolds;
    
    private JTabbedPane tabbedPanePhases;
    
    private JPanel panelDirectClassification;
    private JComboBox<String> cbBoxDirectClassifier;
    private JTextField txtDirectClassifierOptions;
    private JLabel lblClassifierDirect;
    private JLabel lblParmetersDirect;
    private JButton btnStartDirect;
    private JScrollPane scrollPaneDirect;
    
    private JPanel panelPhase1;
    private JComboBox<String> cbBoxPhase1Classifier1;
    private JTextField txtPhase1Classifier1Options;
    private JLabel lblClassifierPhase1;
    private JLabel lblParmetersPhase1;
    private JButton btnNextPhase1;
    private JScrollPane scrollPanePhase1;
    private JTextPane lblPhase1Note;
    
    private JPanel panelPhase2;
    private JComboBox<String> cbBoxPhase2Classifier1;
    private JComboBox<String> cbBoxPhase2Classifier2;
    private JTextField txtPhase2Classifier1Options;
    private JTextField txtPhase2Classifier2Options;
    private JLabel lblClassifier1Phase2;
    private JLabel lblParmeters1Phase2;
    private JLabel lblClassifier2Phase2;
    private JLabel lblParmeters2Phase2;
    private JButton btnNextPhase2;
    private JScrollPane scrollPanePhase2;
    private JTextPane lblPhase2Note;
    
    private JPanel panelPhase3;
    private JComboBox<String> cbBoxPhase3Classifier1;
    private JComboBox<String> cbBoxPhase3Classifier2;
    private JComboBox<String> cbBoxPhase3Classifier3;
    private JComboBox<String> cbBoxPhase3Classifier4;
    private JTextField txtPhase3Classifier1Options;
    private JTextField txtPhase3Classifier2Options;
    private JTextField txtPhase3Classifier3Options;
    private JTextField txtPhase3Classifier4Options;  
    private JLabel lblClassifier1Phase3;
    private JLabel lblParmeters1Phase3;
    private JLabel lblClassifier2Phase3;
    private JLabel lblParmeters2Phase3;
    private JLabel lblClassifier3Phase3;
    private JLabel lblParmeters3Phase3;
    private JLabel lblClassifier4Phase3;
    private JLabel lblParmeters4Phase3;
    private JButton btnStartPhases;
    private JScrollPane scrollPanePhase3;
    private JTextPane lblPhase3Note;
    
    private JPanel panelOptions;
    private JPanel panelNGram;
    private JTextPane lblDirectNote;
    private JTabbedPane classificationAnalysisTabbedPane;
    private JTabbedPane groupCreationTabbedPane;
    
    // Easy tab
    private JPanel easyDataProcessing;
    private JLabel lblEasyTestFile;
    private JTextField txtEasyTestFilePath;
    private JButton btnEasySelectTestFile;
    private JTabbedPane tabbedPaneEasyPhases;
    private JPanel panelEasyDirectClassification;
    private JLabel lblEasyClassifierDirect;
    private JComboBox<String> cbBoxEasyDirectClassifier;
    private JButton btnEasyStartDirect;
    private JScrollPane scrollPaneEasyDirect;
    private JTextPane lblEasyDirectNote;
    private JPanel panelEasyPhase1;
    private JLabel lblEasyClassifierPhase1;
    private JComboBox<String> cbBoxEasyPhase1Classifier1;
    private JButton btnEasyNextPhase1;
    private JScrollPane scrollPaneEasyPhase1;
    private JTextPane lblEasyPhase1Note;
    private JPanel panelEasyPhase2;
    private JLabel lblEasyClassifier1Phase2;
    private JComboBox<String> cbBoxEasyPhase2Classifier1;
    private JLabel lblEasyClassifier2Phase2;
    private JComboBox<String> cbBoxEasyPhase2Classifier2;
	private JButton btnEasyNextPhase2;
	private JScrollPane scrollPaneEasyPhase2;
	private JTextPane lblEasyPhase2Note;
	private JPanel panelEasyPhase3;
	private JComboBox<String> cbBoxEasyPhase3Classifier1;
	private JComboBox<String> cbBoxEasyPhase3Classifier2;
	private JComboBox<String> cbBoxEasyPhase3Classifier3;
	private JComboBox<String> cbBoxEasyPhase3Classifier4;
	private JLabel lblEasyClassifier1Phase3;
	private JLabel lblEasyClassifier2Phase3;
	private JLabel lblEasyClassifier3Phase3;
	private JLabel lblEasyClassifier4Phase3;
	private JButton btnEasyStartPhases;
	private JScrollPane scrollPaneEasyPhase3;
	private JTextPane lblEasyPhase3Note;
	private JTabbedPane tabbedPaneEasyResults;
	private JScrollPane scrollPaneEasyTestResults;
	private JTextArea textAreaEasyTestResults;
	private JSeparator separator;
	private JTextField txtGroupsSize;
	private JLabel lblGroupsSize;
	private JButton btnCreateGroupsGroups;
    
    public void setControler(Controller controler) {
        
        this.controller = controler;
    }
    
    public void setVisible() {
        
        frame.setVisible(true);
    }
    
    /**
     * Create the application.
     */
    public MainAppWindow() {
        
        initialize();
        initializeTabbedPane();
        
        initializeDataProcessingTab();
        initializeEasyDataProcessingTab();
        initializeDataAnalysisTab();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(50, 30, 1046, 749);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
    }
    
    private void initializeTabbedPane() {
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(6, 11, 1034, 688);
        frame.getContentPane().add(tabbedPane);
        
        easyDataProcessing = new JPanel();
        tabbedPane.addTab("Procesar datos (simple)", null, easyDataProcessing, null);
        easyDataProcessing.setLayout(null);
        
        
        
        dataProcessing = new JPanel();
        tabbedPane.addTab("Procesar datos (avanzado)", null, dataProcessing, null);
        dataProcessing.setLayout(null);
        
        dataAnalysis = new JPanel();
        tabbedPane.addTab("Análisis de datos", null, dataAnalysis, null);
        dataAnalysis.setLayout(null);
    }
    
    private void initializeDataProcessingTab() {
    	
    	initializeFilesSection();
        initializeClassificationSection();
        initializeOptionsSection();
        initializeResultsSection();
    } 
    
    private void initializeFilesSection() {
        
        initializeTrainFileSection();
        initializeTestFileSection();
        
        initializeFileSectionActionListeners();
    }
    
    private void initializeTrainFileSection() {
        
    	String userDir = System.getProperty("user.dir");
    	
    	lblTrainFile = new JLabel("Archivo de entrenamiento:");
        lblTrainFile.setBounds(10, 16, 167, 16);
        dataProcessing.add(lblTrainFile);
    	
    	txtTrainFilePath = new JTextField();
        txtTrainFilePath.setBounds(189, 11, 683, 26);
        dataProcessing.add(txtTrainFilePath);
        txtTrainFilePath.setText(userDir + File.separator + "datasets" + File.separator + "Archivo de entrenamiento-chico.arff");
        txtTrainFilePath.setColumns(10);
        
        btnSelectTrainFile = new JButton("Seleccionar");
        btnSelectTrainFile.setBounds(882, 10, 117, 29);
        dataProcessing.add(btnSelectTrainFile);
    }
    
    private void initializeTestFileSection() {
    	
        String userDir = System.getProperty("user.dir");
        
        lblTestFile = new JLabel("Archivo a clasificar:");
        lblTestFile.setBounds(10, 48, 167, 16);
        dataProcessing.add(lblTestFile);
        
        txtTestFilePath = new JTextField();
        txtTestFilePath.setBounds(189, 43, 683, 26);
        dataProcessing.add(txtTestFilePath);
        txtTestFilePath.setText(userDir + File.separator + "datasets" + File.separator + "Archivo de clasificacion1-nombres.arff");
        txtTestFilePath.setColumns(10);
        
        btnSelectTestFile = new JButton("Seleccionar");
        btnSelectTestFile.setBounds(882, 42, 117, 29);
        dataProcessing.add(btnSelectTestFile);
    }
    
    private void initializeFileSectionActionListeners() {
        
        btnSelectTrainFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                fileChooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("WEKA dataset", "arff", "json"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    txtTrainFilePath.setText(file.getPath());
                }
            }
        });    

        btnSelectTestFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                fileChooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("WEKA dataset", "arff", "json"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    
                    File[] files = fileChooser.getSelectedFiles();
                    String filesPath = "";
                    for (File file : files) {
                    	filesPath += file.getPath() + ", ";
                    }
                    txtTestFilePath.setText(filesPath.substring(0, filesPath.lastIndexOf(", ")));
                }
            }
        });
    }
    
    private void initializeClassificationSection() {
        
        initializeClassificationTabbedPanels();
        
        initializeDirectClassificationPane();
        initializePhase1ClassificationPane();
        initializePhase2ClassificationPane();
        initializePhase3ClassificationPane();
        
        initializeScrollPaneOptions();
        
        initializeClassificationSectionActionListeners();
    }  
    
    private void initializeClassificationTabbedPanels() {
    	
    	tabbedPanePhases = new JTabbedPane(JTabbedPane.TOP);
        tabbedPanePhases.setBounds(10, 80, 697, 214);
        dataProcessing.add(tabbedPanePhases);
    } 
    
    private void initializeDirectClassificationPane() {
    	
    	panelDirectClassification = new JPanel();
        tabbedPanePhases.addTab("Directo", null, panelDirectClassification, null);
        panelDirectClassification.setLayout(null);
        
        lblClassifierDirect = new JLabel("Clasificador:");
        lblClassifierDirect.setBounds(6, 10, 84, 16);
        panelDirectClassification.add(lblClassifierDirect);
        
        cbBoxDirectClassifier = new JComboBox<String>();
        cbBoxDirectClassifier.setBounds(90, 6, 200, 27);
        panelDirectClassification.add(cbBoxDirectClassifier);
        
        lblParmetersDirect = new JLabel("Parámetros:");
        lblParmetersDirect.setBounds(302, 10, 84, 16);
        panelDirectClassification.add(lblParmetersDirect);
        
        txtDirectClassifierOptions = new JTextField();
        txtDirectClassifierOptions.setBounds(387, 6, 207, 26);
        panelDirectClassification.add(txtDirectClassifierOptions);
        txtDirectClassifierOptions.setColumns(10);
        
        btnStartDirect = new JButton("Comenzar");
        btnStartDirect.setBounds(561, 140, 117, 29);
        panelDirectClassification.add(btnStartDirect);
        
        scrollPaneDirect = new JScrollPane();
        scrollPaneDirect.setBounds(6, 127, 549, 35);
        panelDirectClassification.add(scrollPaneDirect);
        
        lblDirectNote = new JTextPane();
        scrollPaneDirect.setViewportView(lblDirectNote);
        lblDirectNote.setEditable(false);
        lblDirectNote.setText("Nota: el clasificador seleccionado se utilizá para la clasificación");
        lblDirectNote.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblDirectNote.setBackground(UIManager.getColor("Panel.background"));
    } 
    
    private void initializePhase1ClassificationPane() {
    	
    	panelPhase1 = new JPanel();
        tabbedPanePhases.addTab("Fase 1", null, panelPhase1, null);
        panelPhase1.setLayout(null);
        
        lblClassifierPhase1 = new JLabel("Clasificador:");
        lblClassifierPhase1.setBounds(6, 10, 84, 16);
        panelPhase1.add(lblClassifierPhase1);
        
        cbBoxPhase1Classifier1 = new JComboBox<String>();
        cbBoxPhase1Classifier1.setBounds(90, 6, 200, 27);
        panelPhase1.add(cbBoxPhase1Classifier1);
        
        lblParmetersPhase1 = new JLabel("Parámetros:");
        lblParmetersPhase1.setBounds(302, 10, 84, 16);
        panelPhase1.add(lblParmetersPhase1);
        
        txtPhase1Classifier1Options = new JTextField();
        txtPhase1Classifier1Options.setBounds(387, 6, 207, 26);
        panelPhase1.add(txtPhase1Classifier1Options);
        txtPhase1Classifier1Options.setColumns(10);
        
        btnNextPhase1 = new JButton("Siguiente");
        btnNextPhase1.setBounds(561, 140, 117, 29);
        panelPhase1.add(btnNextPhase1);
        
        scrollPanePhase1 = new JScrollPane();
        scrollPanePhase1.setBounds(6, 127, 549, 35);
        panelPhase1.add(scrollPanePhase1);
        
        lblPhase1Note = new JTextPane();
        scrollPanePhase1.setViewportView(lblPhase1Note);
        lblPhase1Note.setEditable(false);
        lblPhase1Note.setText("Nota: el clasificador se usará para clasificar cada interacción entre las sub-categorías \"Socio-emocional\" y \"Tarea\" (pertenecientes a la categoría \"Área\").");
        lblPhase1Note.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblPhase1Note.setBackground(UIManager.getColor("Panel.background"));
    }    
    
    private void initializePhase2ClassificationPane() {
    	
    	panelPhase2 = new JPanel();
        tabbedPanePhases.addTab("Fase 2", null, panelPhase2, null);
        panelPhase2.setLayout(null);
        
        lblClassifier1Phase2 = new JLabel("Clasificador:");
        lblClassifier1Phase2.setBounds(6, 10, 84, 16);
        panelPhase2.add(lblClassifier1Phase2);
        
        cbBoxPhase2Classifier1 = new JComboBox<String>();
        cbBoxPhase2Classifier1.setBounds(90, 6, 200, 27);
        panelPhase2.add(cbBoxPhase2Classifier1);
        
        lblParmeters1Phase2 = new JLabel("Parámetros:");
        lblParmeters1Phase2.setBounds(302, 10, 84, 16);
        panelPhase2.add(lblParmeters1Phase2);
        
        txtPhase2Classifier1Options = new JTextField();
        txtPhase2Classifier1Options.setBounds(387, 6, 207, 26);
        panelPhase2.add(txtPhase2Classifier1Options);
        txtPhase2Classifier1Options.setColumns(10);
        
        lblClassifier2Phase2 = new JLabel("Clasificador:");
        lblClassifier2Phase2.setBounds(6, 40, 84, 16);
        panelPhase2.add(lblClassifier2Phase2);
        
        cbBoxPhase2Classifier2 = new JComboBox<String>();
        cbBoxPhase2Classifier2.setBounds(90, 36, 200, 27);
        panelPhase2.add(cbBoxPhase2Classifier2);
        
        lblParmeters2Phase2 = new JLabel("Parámetros:");
        lblParmeters2Phase2.setBounds(302, 40, 84, 16);
        panelPhase2.add(lblParmeters2Phase2);
        
        txtPhase2Classifier2Options = new JTextField();
        txtPhase2Classifier2Options.setBounds(387, 36, 207, 26);
        panelPhase2.add(txtPhase2Classifier2Options);
        txtPhase2Classifier2Options.setColumns(10);
        
        btnNextPhase2 = new JButton("Siguiente");
        btnNextPhase2.setBounds(561, 140, 117, 29);
        panelPhase2.add(btnNextPhase2);
        
        scrollPanePhase2 = new JScrollPane();
        scrollPanePhase2.setBounds(6, 127, 549, 35);
        panelPhase2.add(scrollPanePhase2);
        
        lblPhase2Note = new JTextPane();
        scrollPanePhase2.setViewportView(lblPhase2Note);
        lblPhase2Note.setEditable(false);
        lblPhase2Note.setText("Nota: cada clasificador se usará para clasificar cada interacción entre las categorías \"Positiva\", \"Respuesta\", \"Pregunta\" y \"Negativa\" (pertenecientes a la sub-categoría \"Reacción\").\n1º clasificador: para las interacciones con categoría \"Socio-emocional\"\n2º clasificador: para las interacciones con categoría \"Tarea\"");
        lblPhase2Note.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblPhase2Note.setBackground(UIManager.getColor("Panel.background"));
    }    
    
    private void initializePhase3ClassificationPane() {
    	
    	panelPhase3 = new JPanel();
        tabbedPanePhases.addTab("Fase 3", null, panelPhase3, null);
        panelPhase3.setLayout(null);
        
        cbBoxPhase3Classifier1 = new JComboBox<String>();
        cbBoxPhase3Classifier1.setBounds(90, 6, 200, 27);
        panelPhase3.add(cbBoxPhase3Classifier1);
        
        cbBoxPhase3Classifier2 = new JComboBox<String>();
        cbBoxPhase3Classifier2.setBounds(90, 36, 200, 27);
        panelPhase3.add(cbBoxPhase3Classifier2);
        
        cbBoxPhase3Classifier3 = new JComboBox<String>();
        cbBoxPhase3Classifier3.setBounds(90, 66, 200, 27);
        panelPhase3.add(cbBoxPhase3Classifier3);
        
        cbBoxPhase3Classifier4 = new JComboBox<String>();
        cbBoxPhase3Classifier4.setBounds(90, 96, 200, 27);
        panelPhase3.add(cbBoxPhase3Classifier4);
        
        txtPhase3Classifier1Options = new JTextField();
        txtPhase3Classifier1Options.setBounds(387, 6, 207, 26);
        panelPhase3.add(txtPhase3Classifier1Options);
        txtPhase3Classifier1Options.setColumns(10);
        
        txtPhase3Classifier2Options = new JTextField();
        txtPhase3Classifier2Options.setBounds(387, 36, 207, 26);
        panelPhase3.add(txtPhase3Classifier2Options);
        txtPhase3Classifier2Options.setColumns(10);
        
        txtPhase3Classifier3Options = new JTextField();
        txtPhase3Classifier3Options.setBounds(387, 66, 207, 26);
        panelPhase3.add(txtPhase3Classifier3Options);
        txtPhase3Classifier3Options.setColumns(10);
        
        txtPhase3Classifier4Options = new JTextField();
        txtPhase3Classifier4Options.setBounds(387, 96, 207, 26);
        panelPhase3.add(txtPhase3Classifier4Options);
        txtPhase3Classifier4Options.setColumns(10);
        
        lblClassifier1Phase3 = new JLabel("Clasificador:");
        lblClassifier1Phase3.setBounds(6, 10, 84, 16);
        panelPhase3.add(lblClassifier1Phase3);
        
        lblParmeters1Phase3 = new JLabel("Parámetros:");
        lblParmeters1Phase3.setBounds(302, 10, 84, 16);
        panelPhase3.add(lblParmeters1Phase3);
        
        lblClassifier2Phase3 = new JLabel("Clasificador:");
        lblClassifier2Phase3.setBounds(6, 40, 84, 16);
        panelPhase3.add(lblClassifier2Phase3);
        
        lblParmeters2Phase3 = new JLabel("Parámetros:");
        lblParmeters2Phase3.setBounds(302, 40, 84, 16);
        panelPhase3.add(lblParmeters2Phase3);
        
        lblClassifier3Phase3 = new JLabel("Clasificador:");
        lblClassifier3Phase3.setBounds(6, 70, 84, 16);
        panelPhase3.add(lblClassifier3Phase3);
        
        lblParmeters3Phase3 = new JLabel("Parámetros:");
        lblParmeters3Phase3.setBounds(302, 70, 84, 16);
        panelPhase3.add(lblParmeters3Phase3);
        
        lblClassifier4Phase3 = new JLabel("Clasificador:");
        lblClassifier4Phase3.setBounds(6, 100, 84, 16);
        panelPhase3.add(lblClassifier4Phase3);
        
        lblParmeters4Phase3 = new JLabel("Parámetros:");
        lblParmeters4Phase3.setBounds(302, 100, 84, 16);
        panelPhase3.add(lblParmeters4Phase3);
        
        btnStartPhases = new JButton("Comenzar");
        btnStartPhases.setBounds(561, 140, 117, 29);
        panelPhase3.add(btnStartPhases);
        
        scrollPanePhase3 = new JScrollPane();
        scrollPanePhase3.setBounds(6, 127, 549, 35);
        panelPhase3.add(scrollPanePhase3);
        
        lblPhase3Note = new JTextPane();
        scrollPanePhase3.setViewportView(lblPhase3Note);
        lblPhase3Note.setEditable(false);
        lblPhase3Note.setBackground(UIManager.getColor("Panel.background"));
        lblPhase3Note.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblPhase3Note.setText("Nota: cada clasificador se usará para clasificar cada interacción entre las 3 sub-categorías pertenecientes a la sub-categoría \"Reacción\".\n1º clasificador: para las interacciones con sub-categoría \"Positiva\"\n2º clasificador: para las interacciones con sub-categoría \"Reacción\"\n3º clasificador: para las interacciones con sub-categoría \"Pregunta\"\n4º clasificador: para las interacciones con sub-categoría \"Negativa\"");
    }  
    
    private void initializeScrollPaneOptions() {
    	
    	textOptions = new JTextPane();
        textOptions.setEditable(false);
        
        scrollPaneOptions = new JScrollPane(textOptions);
        scrollPaneOptions.setBounds(717, 102, 282, 271);
        dataProcessing.add(scrollPaneOptions);
    }
    
    private void initializeClassificationSectionActionListeners() {
        
        cbBoxDirectClassifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("direct", cbBoxDirectClassifier.getSelectedIndex());
            }
        });
        
        cbBoxPhase1Classifier1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase1Classifier1", cbBoxPhase1Classifier1.getSelectedIndex());
            }
        });
        
        cbBoxPhase2Classifier1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase2Classifier1", cbBoxPhase2Classifier1.getSelectedIndex());
            }
        });
        
        cbBoxPhase2Classifier2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase2Classifier2", cbBoxPhase2Classifier2.getSelectedIndex());
            }
        });
        
        cbBoxPhase3Classifier1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier1", cbBoxPhase3Classifier1.getSelectedIndex());
            }
        });
        
        cbBoxPhase3Classifier2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier2", cbBoxPhase3Classifier2.getSelectedIndex());
            }
        });
        
        cbBoxPhase3Classifier3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier3", cbBoxPhase3Classifier3.getSelectedIndex());
            }
        });
        
        cbBoxPhase3Classifier4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier4", cbBoxPhase3Classifier4.getSelectedIndex());
            }
        });
        
        btnStartDirect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnStartDirectPressed(false);
            }
        });
        
        btnNextPhase1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.clickNextPhase();
            }
        });
        
        btnNextPhase2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.clickNextPhase();
            }
        });
        
        btnStartPhases.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnStartPhasesPressed(false);
            }
        });
    }
    
    private void initializeOptionsSection() {
        
        panelOptions = new JPanel();
        panelOptions.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Opciones", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelOptions.setBounds(10, 303, 697, 70);
        dataProcessing.add(panelOptions);
        panelOptions.setLayout(null);
        
        chckbxUseFreeling = new JCheckBox("Usar Freeling");
        chckbxUseFreeling.setBounds(543, 33, 128, 23);
        panelOptions.add(chckbxUseFreeling);
        chckbxUseFreeling.setSelected(true);
        
        lblCrossvalidationFolds = new JLabel("Cross-validation folds");
        lblCrossvalidationFolds.setBounds(300, 36, 151, 16);
        panelOptions.add(lblCrossvalidationFolds);
                                            
        txtCrossValidationFolds = new JTextField();
        txtCrossValidationFolds.setBounds(463, 32, 44, 26);
        panelOptions.add(txtCrossValidationFolds);
        txtCrossValidationFolds.setText("10");
        txtCrossValidationFolds.setColumns(10);
        
        initializeNGramPanel();
    }
    
    private void initializeNGramPanel() {
    	
    	panelNGram = new JPanel();
        panelNGram.setBounds(6, 14, 272, 50);
        panelOptions.add(panelNGram);
        panelNGram.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "NGram", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelNGram.setLayout(null);
        
        lblNgramMin = new JLabel("NGram min");
        
        txtNGramMin = new JTextField();
        txtNGramMin.setText("1");
        txtNGramMin.setBounds(93, 16, 30, 26);
        panelNGram.add(txtNGramMin);
        txtNGramMin.setColumns(10);
        panelNGram.add(lblNgramMin);
        
        lblNgramMin.setBounds(6, 21, 75, 16);

        lblNgramMax = new JLabel("NGram max");

        txtNGramMax = new JTextField();
        txtNGramMax.setText("3");
        txtNGramMax.setBounds(222, 16, 30, 26);
        panelNGram.add(txtNGramMax);
        txtNGramMax.setColumns(10);
        panelNGram.add(lblNgramMax);

        lblNgramMax.setBounds(135, 21, 89, 16);
    }
        
    private void initializeResultsSection() {  
        
        tabbedPaneResults = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneResults.setBounds(10, 384, 989, 265);
        dataProcessing.add(tabbedPaneResults);
        
        scrollPaneTrainResults = new JScrollPane();
        tabbedPaneResults.addTab("Resultados de entrenamiento", scrollPaneTrainResults);
    
        textAreaTrainResults = new JTextArea();
        textAreaTrainResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneTrainResults.setViewportView(textAreaTrainResults);
        textAreaTrainResults.setEditable(false);
    
 	    scrollPaneTestResults = new JScrollPane();
 	    tabbedPaneResults.addTab("Resultados de clasificación", scrollPaneTestResults);
 	   
        textAreaTestResults = new JTextArea();
        textAreaTestResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneTestResults.setViewportView(textAreaTestResults);
        textAreaTestResults.setEditable(false);
     }

    private void initializeEasyDataProcessingTab() {
    	
    	initializeEasyFilesSection();
        initializeEasyClassificationSection();
        initializeEasyResultsSection();
    }
    
    private void initializeEasyFilesSection() {
        
        initializeEasyTestFileSection();
        initializeEasyFileSectionActionListeners();
    }
    
    private void initializeEasyTestFileSection() {
    	
    	String userDir = System.getProperty("user.dir");
    	
    	lblEasyTestFile = new JLabel("Archivo a clasificar:");
        lblEasyTestFile.setBounds(10, 17, 167, 16);
        easyDataProcessing.add(lblEasyTestFile);
        
        txtEasyTestFilePath = new JTextField();
        txtEasyTestFilePath.setBounds(189, 12, 683, 26);
        easyDataProcessing.add(txtEasyTestFilePath);
        txtEasyTestFilePath.setText(userDir + File.separator + "datasets" + File.separator + "Archivo de clasificacion1-nombres.arff");
        txtEasyTestFilePath.setColumns(10);
        
        btnEasySelectTestFile = new JButton("Seleccionar");
        btnEasySelectTestFile.setBounds(882, 11, 117, 29);
        easyDataProcessing.add(btnEasySelectTestFile);
    }
    
    private void initializeEasyFileSectionActionListeners() {
        
        btnEasySelectTestFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("WEKA dataset", "arff", "json"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    
                    File[] files = fileChooser.getSelectedFiles();
                    String filesPath = "";
                    for (File file : files) {
                    	filesPath += file.getPath() + ", ";
                    }
                    txtEasyTestFilePath.setText(filesPath.substring(0, filesPath.lastIndexOf(", ")));
                }
            }
        });
    }

    private void initializeEasyClassificationSection() {
    	
    	initializeEasyClassificationTabbedPanels();
        
        initializeEasyDirectClassificationPane();
        initializeEasyPhase1ClassificationPane();
        initializeEasyPhase2ClassificationPane();
        initializeEasyPhase3ClassificationPane();
        
        initializeEasyClassificationSectionActionListeners();
    }
    
    private void initializeEasyClassificationTabbedPanels() {
    	
    	tabbedPaneEasyPhases = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneEasyPhases.setBounds(10, 80, 989, 214);
        easyDataProcessing.add(tabbedPaneEasyPhases);
    }
    
    private void initializeEasyDirectClassificationPane() {
    	
    	panelEasyDirectClassification = new JPanel();
        tabbedPaneEasyPhases.addTab("Directo", null, panelEasyDirectClassification, null);
        panelEasyDirectClassification.setLayout(null);
        
        lblEasyClassifierDirect = new JLabel("Clasificador:");
        lblEasyClassifierDirect.setBounds(6, 10, 84, 16);
        panelEasyDirectClassification.add(lblEasyClassifierDirect);
        
        cbBoxEasyDirectClassifier = new JComboBox<String>();
        cbBoxEasyDirectClassifier.setBounds(90, 6, 200, 27);
        panelEasyDirectClassification.add(cbBoxEasyDirectClassifier);
        
        btnEasyStartDirect = new JButton("Comenzar");
        btnEasyStartDirect.setBounds(561, 140, 117, 29);
        panelEasyDirectClassification.add(btnEasyStartDirect);
        
        scrollPaneEasyDirect = new JScrollPane();
        scrollPaneEasyDirect.setBounds(6, 127, 549, 35);
        panelEasyDirectClassification.add(scrollPaneEasyDirect);
        
        lblEasyDirectNote = new JTextPane();
        scrollPaneEasyDirect.setViewportView(lblEasyDirectNote);
        lblEasyDirectNote.setEditable(false);
        lblEasyDirectNote.setText("Nota: el clasificador seleccionado se utilizá para la clasificación");
        lblEasyDirectNote.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblEasyDirectNote.setBackground(UIManager.getColor("Panel.background"));
    }
    
    private void initializeEasyPhase1ClassificationPane() {
    	
    	panelEasyPhase1 = new JPanel();
        tabbedPaneEasyPhases.addTab("Fase 1", null, panelEasyPhase1, null);
        panelEasyPhase1.setLayout(null);
        
        lblEasyClassifierPhase1 = new JLabel("Clasificador:");
        lblEasyClassifierPhase1.setBounds(6, 10, 84, 16);
        panelEasyPhase1.add(lblEasyClassifierPhase1);
        
        cbBoxEasyPhase1Classifier1 = new JComboBox<String>();
        cbBoxEasyPhase1Classifier1.setBounds(90, 6, 200, 27);
        panelEasyPhase1.add(cbBoxEasyPhase1Classifier1);
        
        btnEasyNextPhase1 = new JButton("Siguiente");
        btnEasyNextPhase1.setBounds(561, 140, 117, 29);
        panelEasyPhase1.add(btnEasyNextPhase1);
        
        scrollPaneEasyPhase1 = new JScrollPane();
        scrollPaneEasyPhase1.setBounds(6, 127, 549, 35);
        panelEasyPhase1.add(scrollPaneEasyPhase1);
        
        lblEasyPhase1Note = new JTextPane();
        scrollPaneEasyPhase1.setViewportView(lblEasyPhase1Note);
        lblEasyPhase1Note.setEditable(false);
        lblEasyPhase1Note.setText("Nota: el clasificador se usará para clasificar cada interacción entre las sub-categorías \"Socio-emocional\" y \"Tarea\" (pertenecientes a la categoría \"Área\").");
        lblEasyPhase1Note.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblEasyPhase1Note.setBackground(UIManager.getColor("Panel.background"));
    }
    
    private void initializeEasyPhase2ClassificationPane() {
    	
    	panelEasyPhase2 = new JPanel();
        tabbedPaneEasyPhases.addTab("Fase 2", null, panelEasyPhase2, null);
        panelEasyPhase2.setLayout(null);
        
        lblEasyClassifier1Phase2 = new JLabel("Clasificador:");
        lblEasyClassifier1Phase2.setBounds(6, 10, 84, 16);
        panelEasyPhase2.add(lblEasyClassifier1Phase2);
        
        cbBoxEasyPhase2Classifier1 = new JComboBox<String>();
        cbBoxEasyPhase2Classifier1.setBounds(90, 6, 200, 27);
        panelEasyPhase2.add(cbBoxEasyPhase2Classifier1);
        
        lblEasyClassifier2Phase2 = new JLabel("Clasificador:");
        lblEasyClassifier2Phase2.setBounds(6, 40, 84, 16);
        panelEasyPhase2.add(lblEasyClassifier2Phase2);
        
        cbBoxEasyPhase2Classifier2 = new JComboBox<String>();
        cbBoxEasyPhase2Classifier2.setBounds(90, 36, 200, 27);
        panelEasyPhase2.add(cbBoxEasyPhase2Classifier2);
        
        btnEasyNextPhase2 = new JButton("Siguiente");
        btnEasyNextPhase2.setBounds(561, 140, 117, 29);
        panelEasyPhase2.add(btnEasyNextPhase2);
        
        scrollPaneEasyPhase2 = new JScrollPane();
        scrollPaneEasyPhase2.setBounds(6, 127, 549, 35);
        panelEasyPhase2.add(scrollPaneEasyPhase2);
        
        lblEasyPhase2Note = new JTextPane();
        scrollPaneEasyPhase2.setViewportView(lblEasyPhase2Note);
        lblEasyPhase2Note.setEditable(false);
        lblEasyPhase2Note.setText("Nota: cada clasificador se usará para clasificar cada interacción entre las categorías \"Positiva\", \"Respuesta\", \"Pregunta\" y \"Negativa\" (pertenecientes a la sub-categoría \"Reacción\").\n1º clasificador: para las interacciones con categoría \"Socio-emocional\"\n2º clasificador: para las interacciones con categoría \"Tarea\"");
        lblEasyPhase2Note.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblEasyPhase2Note.setBackground(UIManager.getColor("Panel.background"));
    }
    
    private void initializeEasyPhase3ClassificationPane() {
    	
    	panelEasyPhase3 = new JPanel();
        tabbedPaneEasyPhases.addTab("Fase 3", null, panelEasyPhase3, null);
        panelEasyPhase3.setLayout(null);
        
        cbBoxEasyPhase3Classifier1 = new JComboBox<String>();
        cbBoxEasyPhase3Classifier1.setBounds(90, 6, 200, 27);
        panelEasyPhase3.add(cbBoxEasyPhase3Classifier1);
        
        cbBoxEasyPhase3Classifier2 = new JComboBox<String>();
        cbBoxEasyPhase3Classifier2.setBounds(90, 36, 200, 27);
        panelEasyPhase3.add(cbBoxEasyPhase3Classifier2);
        
        cbBoxEasyPhase3Classifier3 = new JComboBox<String>();
        cbBoxEasyPhase3Classifier3.setBounds(90, 66, 200, 27);
        panelEasyPhase3.add(cbBoxEasyPhase3Classifier3);
        
        cbBoxEasyPhase3Classifier4 = new JComboBox<String>();
        cbBoxEasyPhase3Classifier4.setBounds(90, 96, 200, 27);
        panelEasyPhase3.add(cbBoxEasyPhase3Classifier4);
        
        lblEasyClassifier1Phase3 = new JLabel("Clasificador:");
        lblEasyClassifier1Phase3.setBounds(6, 10, 84, 16);
        panelEasyPhase3.add(lblEasyClassifier1Phase3);
        
        lblEasyClassifier2Phase3 = new JLabel("Clasificador:");
        lblEasyClassifier2Phase3.setBounds(6, 40, 84, 16);
        panelEasyPhase3.add(lblEasyClassifier2Phase3);
        
        lblEasyClassifier3Phase3 = new JLabel("Clasificador:");
        lblEasyClassifier3Phase3.setBounds(6, 70, 84, 16);
        panelEasyPhase3.add(lblEasyClassifier3Phase3);
        
        lblEasyClassifier4Phase3 = new JLabel("Clasificador:");
        lblEasyClassifier4Phase3.setBounds(6, 100, 84, 16);
        panelEasyPhase3.add(lblEasyClassifier4Phase3);
        
        btnEasyStartPhases = new JButton("Comenzar");
        btnEasyStartPhases.setBounds(561, 140, 117, 29);
        panelEasyPhase3.add(btnEasyStartPhases);
        
        scrollPaneEasyPhase3 = new JScrollPane();
        scrollPaneEasyPhase3.setBounds(6, 127, 549, 35);
        panelEasyPhase3.add(scrollPaneEasyPhase3);
        
        lblEasyPhase3Note = new JTextPane();
        scrollPaneEasyPhase3.setViewportView(lblEasyPhase3Note);
        lblEasyPhase3Note.setEditable(false);
        lblEasyPhase3Note.setBackground(UIManager.getColor("Panel.background"));
        lblEasyPhase3Note.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        lblEasyPhase3Note.setText("Nota: cada clasificador se usará para clasificar cada interacción entre las 3 sub-categorías pertenecientes a la sub-categoría \"Reacción\".\n1º clasificador: para las interacciones con sub-categoría \"Positiva\"\n2º clasificador: para las interacciones con sub-categoría \"Reacción\"\n3º clasificador: para las interacciones con sub-categoría \"Pregunta\"\n4º clasificador: para las interacciones con sub-categoría \"Negativa\"");
    }
    
    private void initializeEasyClassificationSectionActionListeners() {
    	
    	cbBoxEasyDirectClassifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("directEasy", cbBoxEasyDirectClassifier.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase1Classifier1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase1Classifier1Easy", cbBoxEasyPhase1Classifier1.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase2Classifier1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase2Classifier1Easy", cbBoxEasyPhase2Classifier1.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase2Classifier2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase2Classifier2Easy", cbBoxEasyPhase2Classifier2.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase3Classifier1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier1Easy", cbBoxEasyPhase3Classifier1.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase3Classifier2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier2Easy", cbBoxEasyPhase3Classifier2.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase3Classifier3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier3Easy", cbBoxEasyPhase3Classifier3.getSelectedIndex());
            }
        });
        
        cbBoxEasyPhase3Classifier4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.cbBoxClassifierChanged("phase3Classifier4Easy", cbBoxEasyPhase3Classifier4.getSelectedIndex());
            }
        });
        
        btnEasyStartDirect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnStartDirectPressed(true);
            }
        });
        
        btnEasyNextPhase1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.clickNextEasyPhase();
            }
        });
        
        btnEasyNextPhase2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.clickNextEasyPhase();
            }
        });
        
        btnEasyStartPhases.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnStartPhasesPressed(true);
            }
        });
    }
    
    private void initializeEasyResultsSection() {
    	
        tabbedPaneEasyResults = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneEasyResults.setBounds(10, 305, 989, 344);
        easyDataProcessing.add(tabbedPaneEasyResults);
        
        scrollPaneEasyTestResults = new JScrollPane();
        tabbedPaneEasyResults.addTab("Resultados de clasificación", scrollPaneEasyTestResults);
        
        textAreaEasyTestResults = new JTextArea();
        textAreaEasyTestResults.setFont(new Font("Courier New", Font.PLAIN, 13));
        scrollPaneEasyTestResults.setViewportView(textAreaEasyTestResults);
        textAreaEasyTestResults.setEditable(false);
    }
    
    private void initializeDataAnalysisTab() {
    	
    	classificationAnalysisTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        classificationAnalysisTabbedPane.setBounds(0, 0, 1019, 275);
        dataAnalysis.add(classificationAnalysisTabbedPane);
        
        groupCreationTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        groupCreationTabbedPane.setBounds(0, 361, 1019, 275);
        dataAnalysis.add(groupCreationTabbedPane);
        
        separator = new JSeparator();
        separator.setBounds(10, 287, 997, 12);
        dataAnalysis.add(separator);
        
        txtGroupsSize = new JTextField();        
        txtGroupsSize.setBounds(171, 328, 65, 26);
        dataAnalysis.add(txtGroupsSize);
        txtGroupsSize.setColumns(10);
        
        lblGroupsSize = new JLabel("Tamaño de los grupos:");
        lblGroupsSize.setBounds(10, 333, 149, 16);
        dataAnalysis.add(lblGroupsSize);
        
        btnCreateGroupsGroups = new JButton("Crear grupos");
        btnCreateGroupsGroups.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnCreateGroupsPressed();
            }
        });
        btnCreateGroupsGroups.setBounds(267, 328, 116, 29);
        dataAnalysis.add(btnCreateGroupsGroups);
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

    public void setCbBoxClassifierModel(String[] text) {
    	
        cbBoxDirectClassifier.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxPhase1Classifier1.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxPhase2Classifier1.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxPhase2Classifier2.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxPhase3Classifier1.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxPhase3Classifier2.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxPhase3Classifier3.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxPhase3Classifier4.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxEasyDirectClassifier.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxEasyPhase1Classifier1.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxEasyPhase2Classifier1.setModel(new DefaultComboBoxModel<String>(text));        
        cbBoxEasyPhase2Classifier2.setModel(new DefaultComboBoxModel<String>(text));
        
        cbBoxEasyPhase3Classifier1.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxEasyPhase3Classifier2.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxEasyPhase3Classifier3.setModel(new DefaultComboBoxModel<String>(text));
        cbBoxEasyPhase3Classifier4.setModel(new DefaultComboBoxModel<String>(text));        
    }  
    
    public void setLblClassifierText(String text) {
        lblClassifierDirect.setText(text);
    }

    public void setLblParmetersText(String text) {
        lblParmetersDirect.setText(text);
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
    
    public void setTxtDirectClassifierOptionsText(String text) {
        txtDirectClassifierOptions.setText(text);
    }
    
    public void setTxtPhase1ClassifierOptionsText(String text) {
        txtPhase1Classifier1Options.setText(text);
    }
    
    public void setTxtPhase2Classifier1OptionsText(String text) {
        txtPhase2Classifier1Options.setText(text);
    }
    
    public void setTxtPhase2Classifier2OptionsText(String text) {
        txtPhase2Classifier2Options.setText(text);
    }
    
    public void setTxtPhase3Classifier1OptionsText(String text) {
        txtPhase3Classifier1Options.setText(text);
    }
    
    public void setTxtPhase3Classifier2OptionsText(String text) {
        txtPhase3Classifier2Options.setText(text);
    }
    
    public void setTxtPhase3Classifier3OptionsText(String text) {
        txtPhase3Classifier3Options.setText(text);
    }
       
    public void setTxtPhase3Classifier4OptionsText(String text) {
        txtPhase3Classifier4Options.setText(text);
    }
    
    public void setTextOptions(String text) {
        textOptions.setText(text);
        textOptions.setCaretPosition(0);
    }
    
    public String getTxtTrainFilePathText() {
        return txtTrainFilePath.getText();
    }

    public String getDirectClassifierOptions() {
        return txtDirectClassifierOptions.getText();
    }

    public String getTxtTestFilePathText() {
        return txtTestFilePath.getText();
    }

    public String getEasyTxtTestFilePathText() {
        return txtEasyTestFilePath.getText();
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
    
    public void setEasyProcessingTextTestResults(String processingText) {

        textAreaEasyTestResults.setCaretPosition(0);
        textAreaEasyTestResults.setText(processingText);
        textAreaEasyTestResults.update(textAreaEasyTestResults.getGraphics());
        textAreaEasyTestResults.setCaretPosition(0);
    }
    
    public String getTextAreaTestResults() {
        
        return textAreaTrainResults.getText();
    }

    public boolean getUseFreeling() {

        return chckbxUseFreeling.isSelected();
    }
    
    public String getCrossValidationFolds() {
        
        return txtCrossValidationFolds.getText();
    }
    
    public String getDirectClassifier() {        
        return cbBoxDirectClassifier.getSelectedItem().toString();
    }
    
    public String getEasyDirectClassifier() {        
        return cbBoxEasyDirectClassifier.getSelectedItem().toString();
    }
    
    public String getPhase1Classifier() {
        return cbBoxPhase1Classifier1.getSelectedItem().toString();
    }
    
    public String getEasyPhase1Classifier() {
        return cbBoxEasyPhase1Classifier1.getSelectedItem().toString();
    }
    
    public String getPhase2Classifier1() {
        return cbBoxPhase2Classifier1.getSelectedItem().toString();
    }
    
    public String getEasyPhase2Classifier1() {
        return cbBoxEasyPhase2Classifier1.getSelectedItem().toString();
    }
    
    public String getPhase2Classifier2() {
        return cbBoxPhase2Classifier2.getSelectedItem().toString();
    }
    
    public String getEasyPhase2Classifier2() {
        return cbBoxEasyPhase2Classifier2.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier1() {
        return cbBoxPhase3Classifier1.getSelectedItem().toString();
    }
    
    public String getEasyPhase3Classifier1() {
        return cbBoxEasyPhase3Classifier1.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier2() {
        return cbBoxPhase3Classifier2.getSelectedItem().toString();
    }
    
    public String getEasyPhase3Classifier2() {
        return cbBoxEasyPhase3Classifier2.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier3() {
        return cbBoxPhase3Classifier3.getSelectedItem().toString();
    }

    public String getEasyPhase3Classifier3() {
        return cbBoxEasyPhase3Classifier3.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier4() {
        return cbBoxPhase3Classifier4.getSelectedItem().toString();
    }
    
    public String getEasyPhase3Classifier4() {
        return cbBoxEasyPhase3Classifier4.getSelectedItem().toString();
    }
    
    public String getNGramMin() {
        
        return txtNGramMin.getText();
    }
    
    public String getNGramMax() {
        
        return txtNGramMax.getText();
    }
    
    public void nextTab() {
        int index = tabbedPanePhases.getSelectedIndex() + 1;
        
        if (index >= tabbedPanePhases.getTabCount())
            index = 0;
        tabbedPanePhases.setSelectedIndex(index);
    }
    
    public void nextEasyTab() {
        int index = tabbedPaneEasyPhases.getSelectedIndex() + 1;
        
        if (index >= tabbedPaneEasyPhases.getTabCount())
            index = 0;
        tabbedPaneEasyPhases.setSelectedIndex(index);
    }

	public String getTxtPhase1Classifier1Options() {
		return txtPhase1Classifier1Options.getText();
	}

	public String getTxtPhase2Classifier1Options() {
		return txtPhase2Classifier1Options.getText();
	}

	public String getTxtPhase2Classifier2Options() {
		return txtPhase2Classifier2Options.getText();
	}

	public String getTxtPhase3Classifier1Options() {
		return txtPhase3Classifier1Options.getText();
	}

	public String getTxtPhase3Classifier2Options() {
		return txtPhase3Classifier2Options.getText();
	}

	public String getTxtPhase3Classifier3Options() {
		return txtPhase3Classifier3Options.getText();
	}

	public String getTxtPhase3Classifier4Options() {
		return txtPhase3Classifier4Options.getText();
	}

    public int getDirectClassifierItemCount() {
        return cbBoxDirectClassifier.getItemCount();
    }
    
    public void setDirectClassifierSelectedItem(int index) {
        cbBoxDirectClassifier.setSelectedIndex(index);
    }
    
    public void pressBtnStartDirect() {
        btnStartDirect.doClick();
    }
    
    public int getPhase1ClassifierItemCount() {
        return cbBoxPhase1Classifier1.getItemCount();
    }
    
    public void setPhase1ClassifierSelectedItem(int index) {
        cbBoxPhase1Classifier1.setSelectedIndex(index);
    }
    
    public void setPhase2Classifier1SelectedItem(int index) {
        cbBoxPhase2Classifier1.setSelectedIndex(index);
    }
    
    public void setPhase2Classifier2SelectedItem(int index) {
        cbBoxPhase2Classifier2.setSelectedIndex(index);
    }
    
    public void setPhase3Classifier1SelectedItem(int index) {
        cbBoxPhase3Classifier1.setSelectedIndex(index);
    }
    
    public void setPhase3Classifier2SelectedItem(int index) {
        cbBoxPhase3Classifier2.setSelectedIndex(index);
    }
    
    public void setPhase3Classifier3SelectedItem(int index) {
        cbBoxPhase3Classifier3.setSelectedIndex(index);
    }
    
    public void setPhase3Classifier4SelectedItem(int index) {
        cbBoxPhase3Classifier4.setSelectedIndex(index);
    }
    
    public void pressBtnStartPhases() {
        btnStartPhases.doClick();
    }
    
    public void addTabToClassificationAnalysisTable(String tabName, List<GroupAnalysisRow> items) {
    	
    	JScrollPane scrollPane = new JScrollPane();
    	classificationAnalysisTabbedPane.addTab(tabName, null, scrollPane, null);
    	
    	DefaultTableModel defaultTableModel = createNewDefaultClassificationAnalysisTableModel();
    	JTable table = createNewClassificationAnalysisTable(defaultTableModel);
    	scrollPane.setViewportView(table);
    	
    	for (GroupAnalysisRow analysisResult : items) {
    		defaultTableModel.addRow(analysisResult.getDataToTable());
    	}
    }

    public void addTabToGroupCreationTable(String tabName, List<GroupCreationRow> items) {
    	
    	JScrollPane scrollPane = new JScrollPane();
    	groupCreationTabbedPane.addTab(tabName, null, scrollPane, null);
    	
    	DefaultTableModel defaultTableModel = createNewDefaultGroupCreationTableModel();
    	JTable table = createNewGroupCreationTable(defaultTableModel);
    	scrollPane.setViewportView(table);
    	
    	for (GroupCreationRow groupCreation : items) {
    		defaultTableModel.addRow(groupCreation.getDataToTable());
    	}
    }
    
    public void cleanAnalysisTable() {
    	classificationAnalysisTabbedPane.removeAll();
    }
    
    public void cleanGroupsTable() {
        groupCreationTabbedPane.removeAll();        
    }
    
    private DefaultTableModel createNewDefaultClassificationAnalysisTableModel() {
    	
    	DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        String header0 = "<html><center>Conflicto</center></html>";
        String header1 = "<html><center>Conducta</center></html>";
        String header2 = "<html><center>Límite<br>Inferior</br></center></html>";
        String header3 = "<html><center>Límite<br>Superior</br></center></html>";
        String header4 = "<html><center>Resultado del<br>Análisis</br></center></html>";
        String strHeader[] = new String[] {header0, header1, header2, header3, header4};
        
        defaultTableModel.setColumnIdentifiers(strHeader);
        
        return defaultTableModel;
    }
    
    private DefaultTableModel createNewDefaultGroupCreationTableModel() {
    	
    	DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        String header0 = "<html><center>Nombre</center></html>";
        String header1 = "<html><center>C1</center></html>";
        String header2 = "<html><center>C2</center></html>";
        String header3 = "<html><center>C3</center></html>";
        String header4 = "<html><center>C4</center></html>";
        String header5 = "<html><center>C5</center></html>";
        String header6 = "<html><center>C6</center></html>";
        String header7 = "<html><center>C7</center></html>";
        String header8 = "<html><center>C8</center></html>";
        String header9 = "<html><center>C9</center></html>";
        String header10 = "<html><center>C10</center></html>";
        String header11 = "<html><center>C11</center></html>";
        String header12 = "<html><center>C12</center></html>";
        String header13 = "<html><center>Desviación total</center></html>";
        
        String strHeader[] = new String[] {header0, header1, header2, header3, header4, header5, header6, header7, header8, header9, header10, header11, header12, header13};
        
        defaultTableModel.setColumnIdentifiers(strHeader);
        
        return defaultTableModel;
    }
    
    private JTable createNewClassificationAnalysisTable(DefaultTableModel defaultTableModel) {
    	
    	JTable table = new JTable();
    	
        table.setCellSelectionEnabled(true);   
        table.setModel(defaultTableModel);
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 32));
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(55);
        table.getColumnModel().getColumn(3).setPreferredWidth(55);
        table.getColumnModel().getColumn(4).setPreferredWidth(85);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        return table;
    }
    
    private JTable createNewGroupCreationTable(DefaultTableModel defaultTableModel) {
    	
    	JTable table = new JTable();
    	
        table.setCellSelectionEnabled(true);   
        table.setModel(defaultTableModel);
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 32));
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getColumnModel().getColumn(8).setPreferredWidth(60);
        table.getColumnModel().getColumn(9).setPreferredWidth(60);
        table.getColumnModel().getColumn(10).setPreferredWidth(60);
        table.getColumnModel().getColumn(11).setPreferredWidth(60);
        table.getColumnModel().getColumn(12).setPreferredWidth(60);
        table.getColumnModel().getColumn(13).setPreferredWidth(100);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(12).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(13).setCellRenderer(rightRenderer);
        
        return table;
    }
    
    public String getGroupsSize() {
        
        return txtGroupsSize.getText();
    }
    
    public void cleanTxtGroupsSize() {
    	txtGroupsSize.setText("");
    }
}
