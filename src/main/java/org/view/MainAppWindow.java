package org.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.controler.Controller;

public class MainAppWindow {

    private JFrame frame;
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
    
    private JPanel panelPhase1;
    private JComboBox<String> cbBoxPhase1Classifier1;
    private JTextField txtPhase1Classifier1Options;
    private JLabel lblClassifierPhase1;
    private JLabel lblParmetersPhase1;
    private JButton btnNextPhase1;
    
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
    
    private JPanel panelOptions;
    private JPanel panelNGram;
    private JButton btnInfo;
    private JTextPane txtpnNotaElClasificador_1;
    private JButton btnTrainPhasesClassifiers;
    
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
        
        lblTrainFile = new JLabel("Archivo de entrenamiento:");
        lblTrainFile.setBounds(6, 24, 167, 16);
        frame.getContentPane().add(lblTrainFile);
        
        txtTrainFilePath = new JTextField();
        String userDir = System.getProperty("user.dir");
        txtTrainFilePath.setText(userDir + File.separator + "datasets" + File.separator + "Archivo de entrenamiento-chico.arff");
        txtTrainFilePath.setBounds(185, 19, 630, 26);
        frame.getContentPane().add(txtTrainFilePath);
        txtTrainFilePath.setColumns(10);
        
        btnSelectTrainFile = new JButton("Seleccionar");
        btnSelectTrainFile.setBounds(827, 19, 117, 29);
        
        frame.getContentPane().add(btnSelectTrainFile);
    }
    
    private void initializeTestFileSection() {
     
        txtTestFilePath = new JTextField();
        String userDir = System.getProperty("user.dir");
        txtTestFilePath.setText(userDir + File.separator + "datasets" + File.separator + "Archivo de clasificacion-nombres.arff");
        txtTestFilePath.setBounds(185, 57, 630, 26);
        frame.getContentPane().add(txtTestFilePath);
        txtTestFilePath.setColumns(10);

        lblTestFile = new JLabel("Archivo a clasificar:");
        lblTestFile.setBounds(6, 62, 167, 16);
        frame.getContentPane().add(lblTestFile);

        btnSelectTestFile = new JButton("Seleccionar");
        
        btnSelectTestFile.setBounds(827, 57, 117, 29);
        frame.getContentPane().add(btnSelectTestFile);
    }
    
    private void initializeFileSectionActionListeners() {
        
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
        tabbedPanePhases.setBounds(6, 100, 638, 212);
        frame.getContentPane().add(tabbedPanePhases);
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
        btnStartDirect.setBounds(477, 131, 117, 29);
        panelDirectClassification.add(btnStartDirect);
        
        txtpnNotaElClasificador_1 = new JTextPane();
        txtpnNotaElClasificador_1.setEditable(false);
        txtpnNotaElClasificador_1.setText("Nota: el clasificador seleccionado se utilizá para la clasificación");
        txtpnNotaElClasificador_1.setFont(new Font("Lucida Grande", Font.ITALIC, 8));
        txtpnNotaElClasificador_1.setBackground(UIManager.getColor("Panel.background"));
        txtpnNotaElClasificador_1.setBounds(6, 121, 469, 39);
        panelDirectClassification.add(txtpnNotaElClasificador_1);
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
        btnNextPhase1.setBounds(477, 131, 117, 29);
        panelPhase1.add(btnNextPhase1);
        
        JTextPane txtpnNotaElClasificador = new JTextPane();
        txtpnNotaElClasificador.setEditable(false);
        txtpnNotaElClasificador.setText("Nota: el clasificador se usará para clasificar cada interacción entre las sub-categorías \"Socio-emocional\" y \"Tarea\" (pertenecientes a la categoría \"Área\").");
        txtpnNotaElClasificador.setFont(new Font("Lucida Grande", Font.ITALIC, 8));
        txtpnNotaElClasificador.setBackground(UIManager.getColor("Panel.background"));
        txtpnNotaElClasificador.setBounds(6, 121, 469, 39);
        panelPhase1.add(txtpnNotaElClasificador);
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
        btnNextPhase2.setBounds(477, 131, 117, 29);
        panelPhase2.add(btnNextPhase2);
        
        JTextPane txtpnNotaCadaClasificador = new JTextPane();
        txtpnNotaCadaClasificador.setEditable(false);
        txtpnNotaCadaClasificador.setText("Nota: cada clasificador se usará para clasificar cada interacción entre las categorías \"Positiva\", \"Respuesta\", \"Pregunta\" y \"Negativa\" (pertenecientes a la sub-categoría \"Reacción\").\n1º clasificador: para las interacciones con categoría \"Socio-emocional\"\n2º clasificador: para las interacciones con categoría \"Tarea\"");
        txtpnNotaCadaClasificador.setFont(new Font("Lucida Grande", Font.ITALIC, 8));
        txtpnNotaCadaClasificador.setBackground(UIManager.getColor("Panel.background"));
        txtpnNotaCadaClasificador.setBounds(6, 121, 522, 39);
        panelPhase2.add(txtpnNotaCadaClasificador);
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
        btnStartPhases.setBounds(477, 131, 117, 29);
        panelPhase3.add(btnStartPhases);
        
        JTextPane txtpnKjjjlkj = new JTextPane();
        txtpnKjjjlkj.setEditable(false);
        txtpnKjjjlkj.setBackground(UIManager.getColor("Panel.background"));
        txtpnKjjjlkj.setFont(new Font("Lucida Grande", Font.ITALIC, 8));
        txtpnKjjjlkj.setText("Nota: cada clasificador se usará para clasificar cada interacción entre las 3 sub-categorías pertenecientes a la sub-categoría \"Reacción\".\n1º clasificador: para las interacciones con sub-categoría \"Positiva\"\n2º clasificador: para las interacciones con sub-categoría \"Reacción\"\n3º clasificador: para las interacciones con sub-categoría \"Pregunta\"\n4º clasificador: para las interacciones con sub-categoría \"Negativa\"");
        txtpnKjjjlkj.setBounds(6, 121, 522, 39);
        panelPhase3.add(txtpnKjjjlkj);
        
        btnInfo = new JButton("info");
        btnInfo.setBounds(362, 131, 117, 29);
        panelPhase3.add(btnInfo);
        
        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBounds(302, 159, 1, 1);
        panelPhase3.add(desktopPane);
        
        JPanel panelAutomatic = new JPanel();
        tabbedPanePhases.addTab("Automático", null, panelAutomatic, null);
        panelAutomatic.setLayout(null);
        
        JButton btnTrainDirectClassifiers = new JButton("Entrenar clasificadores directos");
        btnTrainDirectClassifiers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnTrainDirectClassifiersPressed();
            }
        });
        btnTrainDirectClassifiers.setBounds(6, 56, 242, 29);
        panelAutomatic.add(btnTrainDirectClassifiers);
        
        btnTrainPhasesClassifiers = new JButton("Entrenar clasificadores en fases");
        btnTrainPhasesClassifiers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.btnTrainPhasesClassifiersPressed();
            }
        });
        btnTrainPhasesClassifiers.setBounds(375, 56, 236, 29);
        panelAutomatic.add(btnTrainPhasesClassifiers);
    }

    private void initializeScrollPaneOptions() {
        
        textOptions = new JTextPane();
        textOptions.setEditable(false);
        
        scrollPaneOptions = new JScrollPane(textOptions);
        scrollPaneOptions.setBounds(656, 115, 282, 165);
        frame.getContentPane().add(scrollPaneOptions);
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
                controller.btnStartDirectdPressed();
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
                controller.btnStartPhasesPressed();
            }
        });
    }
    
    private void initializeOptionsSection() {
        
        panelOptions = new JPanel();
        panelOptions.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Opciones", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelOptions.setBounds(16, 312, 922, 70);
        frame.getContentPane().add(panelOptions);
        panelOptions.setLayout(null);

        chckbxUseFreeling = new JCheckBox("Usar Freeling");
        chckbxUseFreeling.setBounds(592, 33, 128, 23);
        panelOptions.add(chckbxUseFreeling);
        chckbxUseFreeling.setSelected(true);
        
        lblCrossvalidationFolds = new JLabel("Cross-validation folds");
        lblCrossvalidationFolds.setBounds(341, 37, 142, 16);
        panelOptions.add(lblCrossvalidationFolds);
        
        txtCrossValidationFolds = new JTextField();
        txtCrossValidationFolds.setBounds(495, 32, 86, 26);
        panelOptions.add(txtCrossValidationFolds);
        txtCrossValidationFolds.setText("10");
        txtCrossValidationFolds.setColumns(10);
        
        initializeNGramPanel();
    }
    
    private void initializeNGramPanel() {
     
        panelNGram = new JPanel();
        panelNGram.setBounds(6, 14, 323, 46);
        panelOptions.add(panelNGram);
        panelNGram.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "NGram", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelNGram.setLayout(null);
        
        lblNgramMin = new JLabel("NGram min");
        lblNgramMax = new JLabel("NGram max");
        
        txtNGramMax = new JTextField();
        txtNGramMax.setText("3");
        txtNGramMax.setBounds(251, 16, 64, 26);
        panelNGram.add(txtNGramMax);
        txtNGramMax.setColumns(10);
        panelNGram.add(lblNgramMax);
        
        txtNGramMin = new JTextField();
        txtNGramMin.setText("1");
        txtNGramMin.setBounds(89, 16, 64, 26);
        panelNGram.add(txtNGramMin);
        txtNGramMin.setColumns(10);
        panelNGram.add(lblNgramMin);
        
        lblNgramMax.setBounds(165, 21, 74, 16);
        lblNgramMin.setBounds(6, 21, 81, 16);
    }
    
    private void initializeResultsSection() {
        
        tabbedPaneResults = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneResults.setBounds(6, 394, 938, 269);
        frame.getContentPane().add(tabbedPaneResults);

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
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setBounds(50, 30, 967, 749);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
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
    
    public String getCrossValidationFolds() {
        
        return txtCrossValidationFolds.getText();
    }
    
    public String getDirectClassifier() {        
        return cbBoxDirectClassifier.getSelectedItem().toString();
    }
    
    public String getPhase1Classifier() {
        return cbBoxPhase1Classifier1.getSelectedItem().toString();
    }
    
    public String getPhase2Classifier1() {
        return cbBoxPhase2Classifier1.getSelectedItem().toString();
    }
    
    public String getPhase2Classifier2() {
        return cbBoxPhase2Classifier2.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier1() {
        return cbBoxPhase3Classifier1.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier2() {
        return cbBoxPhase3Classifier2.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier3() {
        return cbBoxPhase3Classifier3.getSelectedItem().toString();
    }
    
    public String getPhase3Classifier4() {
        return cbBoxPhase3Classifier4.getSelectedItem().toString();
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
            index = 0
            ;
        tabbedPanePhases.setSelectedIndex(index);
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
}
