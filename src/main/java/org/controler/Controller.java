package org.controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.enums.Classifier;
import org.ipa.IpaAnalysis;
import org.processDataset.DirectProcessing;
import org.processDataset.PhasesProcessing;
import org.processDataset.ProcessDataset;
import org.view.MainAppWindow;
import org.weka.Weka;
import org.weka.WekaBayesNet;
import org.weka.WekaDecisionTable;
import org.weka.WekaIBk;
import org.weka.WekaJ48;
import org.weka.WekaJRip;
import org.weka.WekaKStar;
import org.weka.WekaLMT;
import org.weka.WekaLogitBoost;
import org.weka.WekaNaiveBayes;
import org.weka.WekaNaiveBayesMultinomialUpdateable;
import org.weka.WekaPART;
import org.weka.WekaREPTree;
import org.weka.WekaSMO;


public class Controller {
    
    private MainAppWindow mainWindowView;
    private List<String> labeledFileNames = new ArrayList<String>();
    
    public Controller(MainAppWindow mainWindowView) {

        this.mainWindowView = mainWindowView;
    }

    public void initializeView() {

        mainWindowView.setCbBoxClassifierModel(getCbBoxClassifierContent());
        mainWindowView.setVisible();
    }

    public void clickNextPhase() {
        
        mainWindowView.nextTab();
    }
    
    public void cbBoxDirectClassifierChanged(int index) {
        
        if (index != 0) {
            Weka weka = getSelectedClassifier(mainWindowView.getDirectClassifier());
            
            StringBuilder options = weka.getClassifierOptions();

            mainWindowView.setTxtDirectClassifierOptionsText(options.toString());
            mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
            
        } else
            mainWindowView.setTxtDirectClassifierOptionsText("");
    }

    public void cbBoxClassifierChanged(String classifier, int index) {
        
        switch (classifier) {
        case "direct":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getDirectClassifier());
                
                mainWindowView.setTxtDirectClassifierOptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtDirectClassifierOptionsText("");            
            break;
        case "phase1Classifier1":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase1Classifier());
                
                mainWindowView.setTxtPhase1ClassifierOptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase1ClassifierOptionsText("");
            break;
        case "phase2Classifier1":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase2Classifier1());
                
                mainWindowView.setTxtPhase2Classifier1OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase2Classifier1OptionsText("");
            break;
        case "phase2Classifier2":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase2Classifier2());
                
                mainWindowView.setTxtPhase2Classifier2OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase2Classifier2OptionsText("");
            break;
        case "phase3Classifier1":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier1());
                
                mainWindowView.setTxtPhase3Classifier1OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase3Classifier1OptionsText("");            
            break;
        case "phase3Classifier2":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier2());
                
                mainWindowView.setTxtPhase3Classifier2OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase3Classifier2OptionsText("");   
            break;
        case "phase3Classifier3":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier3());
                
                mainWindowView.setTxtPhase3Classifier3OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase3Classifier3OptionsText("");   
            break;
        case "phase3Classifier4":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier4());
                
                mainWindowView.setTxtPhase3Classifier4OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());
                
            } else
                mainWindowView.setTxtPhase3Classifier4OptionsText("");   
            break;

        default:
            break;
        }
            
    }

    public String[] getCbBoxClassifierContent() {

        List<String> content = new ArrayList<String>();
        content.add("Seleccione un clasificador");

        Classifier classifiers[] = Classifier.values();

        for (Classifier classifier : classifiers) {
            content.add(classifier.getDescription());
        }

        String[] simpleArray = new String[content.size()];
        content.toArray(simpleArray);
        return simpleArray;
    }

    public Weka getSelectedClassifier(String selectedClassifier) {
       
        int folds = new Integer(mainWindowView.getCrossValidationFolds()).intValue();
        int nGramMin = new Integer(mainWindowView.getNGramMin()).intValue();
        int nGramMax = new Integer (mainWindowView.getNGramMax()).intValue();
        
        switch (Classifier.getClassifier(selectedClassifier)) {
        case J48:
            return new WekaJ48(folds, nGramMin, nGramMax);
        case NAIVE_BAYES:
            return new WekaNaiveBayes(folds, nGramMin, nGramMax);
        case SMO:
            return new WekaSMO(folds, nGramMin, nGramMax);
        case IBk:
            return new WekaIBk(folds, nGramMin, nGramMax);
        case KStar:
            return new WekaKStar(folds, nGramMin, nGramMax);
        case PART:
            return new WekaPART(folds, nGramMin, nGramMax);
        case JRip:
            return new WekaJRip(folds, nGramMin, nGramMax);
        case LogitBoost:
            return new WekaLogitBoost(folds, nGramMin, nGramMax);
        case LMT:
            return new WekaLMT(folds, nGramMin, nGramMax);
        case NBMU:
            return new WekaNaiveBayesMultinomialUpdateable(folds, nGramMin, nGramMax);
        case REPTree:
            return new WekaREPTree(folds, nGramMin, nGramMax);
        case DecisionTable:
            return new WekaDecisionTable(folds, nGramMin, nGramMax);
        case BayesNet:
            return new WekaBayesNet(folds, nGramMin, nGramMax);
            
        default:
            return new WekaSMO(folds, nGramMin, nGramMax);
        }
    }
    
    public void btnStartDirectdPressed() {
        
        Weka weka = getSelectedClassifier(mainWindowView.getDirectClassifier());
        String wekaClassifierOptions = mainWindowView.getDirectClassifierOptions();
        weka.setClassifierOptions(wekaClassifierOptions);
        
        boolean useFreeling = mainWindowView.getUseFreeling();
        ProcessDataset process = new DirectProcessing(weka, useFreeling);
        btnStartPressed(process, false);
    }
    
    public void btnStartPhasesPressed() {
        
        Weka wekaPhase1Classifier = getSelectedClassifier(mainWindowView.getPhase1Classifier());
        Weka wekaPhase2Classifier1 = getSelectedClassifier(mainWindowView.getPhase2Classifier1());
        Weka wekaPhase2Classifier2 = getSelectedClassifier(mainWindowView.getPhase2Classifier2());
        Weka wekaPhase3Classifier1 = getSelectedClassifier(mainWindowView.getPhase3Classifier1());
        Weka wekaPhase3Classifier2 = getSelectedClassifier(mainWindowView.getPhase3Classifier2());
        Weka wekaPhase3Classifier3 = getSelectedClassifier(mainWindowView.getPhase3Classifier3());
        Weka wekaPhase3Classifier4 = getSelectedClassifier(mainWindowView.getPhase3Classifier4());
                
        String wekaClassifierOptions = mainWindowView.getTxtPhase1Classifier1Options();
        wekaPhase1Classifier.setClassifierOptions(wekaClassifierOptions);
        wekaClassifierOptions = mainWindowView.getTxtPhase2Classifier1Options();
        wekaPhase2Classifier1.setClassifierOptions(wekaClassifierOptions);
        wekaClassifierOptions = mainWindowView.getTxtPhase2Classifier2Options();
        wekaPhase2Classifier2.setClassifierOptions(wekaClassifierOptions);
        wekaClassifierOptions = mainWindowView.getTxtPhase3Classifier1Options();
        wekaPhase3Classifier1.setClassifierOptions(wekaClassifierOptions);
        wekaClassifierOptions = mainWindowView.getTxtPhase3Classifier2Options();
        wekaPhase3Classifier2.setClassifierOptions(wekaClassifierOptions);
        wekaClassifierOptions = mainWindowView.getTxtPhase3Classifier3Options();
        wekaPhase3Classifier3.setClassifierOptions(wekaClassifierOptions);
        wekaClassifierOptions = mainWindowView.getTxtPhase3Classifier4Options();
        wekaPhase3Classifier4.setClassifierOptions(wekaClassifierOptions);
        
        boolean useFreeling = mainWindowView.getUseFreeling();
        ProcessDataset process = new PhasesProcessing(wekaPhase1Classifier, wekaPhase2Classifier1, wekaPhase2Classifier2,
                wekaPhase3Classifier1, wekaPhase3Classifier2, wekaPhase3Classifier3, wekaPhase3Classifier4, useFreeling);
        btnStartPressed(process, true);
    }
    
    private String getDirectClassifierAndParameters() {
    	
    	return "Clasiffier: " + mainWindowView.getDirectClassifier() + '\n' + "Paremeters: " + mainWindowView.getDirectClassifierOptions();
    }
    
    private String getPhasesClassifiersAndParameters() {
    	
    	String str = "Classifiers:\n" + 
    				 "Phase 1:\n" +
    				 "\tClassifier: " + mainWindowView.getPhase1Classifier() + "\n" +
    				 "\tParameters: " + mainWindowView.getTxtPhase1Classifier1Options() + "\n\n" + 
    	
			    	 "Phase 2:\n" +
					 "\tClassifier: " + mainWindowView.getPhase2Classifier1() + "\n" +
					 "\tParameters: " + mainWindowView.getTxtPhase2Classifier1Options() + "\n\n" + 
					 "\tClassifier: " + mainWindowView.getPhase2Classifier2() + "\n" +
					 "\tParameters: " + mainWindowView.getTxtPhase2Classifier2Options() + "\n\n" + 
    	
			    	 "Phase 3:\n" +
					 "\tClassifier: " + mainWindowView.getPhase3Classifier1() + "\n" +
					 "\tParameters: " + mainWindowView.getTxtPhase3Classifier1Options() + "\n\n" + 
					 "\tClassifier: " + mainWindowView.getPhase3Classifier2() + "\n" +
					 "\tParameters: " + mainWindowView.getTxtPhase3Classifier2Options() + "\n\n" + 
					 "\tClassifier: " + mainWindowView.getPhase3Classifier3() + "\n" +
					 "\tParameters: " + mainWindowView.getTxtPhase3Classifier3Options() + "\n\n" + 
					 "\tClassifier: " + mainWindowView.getPhase3Classifier4() + "\n" +
					 "\tParameters: " + mainWindowView.getTxtPhase3Classifier4Options() + "\n";
    	
    	return str;
    }
    
    private void printResults(boolean trainByPhases, long duration, String trainingResults, String classificationResults) {
        
    	String classifierAndParameter;
    	if (trainByPhases)
    		classifierAndParameter = getPhasesClassifiersAndParameters();
    	else 
    		classifierAndParameter = getDirectClassifierAndParameters();
    	
    	String trainMode = trainByPhases ? "phases" :  "direct";
    	
        String options = "Selected options\n================\n" + classifierAndParameter + '\n' + "Cross-validation folds: "
        + mainWindowView.getCrossValidationFolds() + '\n' + "Train mode: " + trainMode + '\n' + "Use FreeLing: " + mainWindowView.getUseFreeling() + '\n'
        + "NGramMin: " + mainWindowView.getNGramMin() + ", NGramMax: " + mainWindowView.getNGramMax() + '\n'
        + "Process time: " + duration + " seconds"
        + "\n===============================================================\n\n";
        
        mainWindowView.setProcessingTextTrainResults(options + trainingResults);
        mainWindowView.setProcessingTextTestResults(classificationResults);
    }
    
    private String getDirectResultFileName() {
    	
    	return "(" + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + ") result-"
                + mainWindowView.getDirectClassifier() + "-folds_" + mainWindowView.getCrossValidationFolds() 
                + "-fases_yes" + "-freeling_" + mainWindowView.getUseFreeling() + "-NGram(" + mainWindowView.getNGramMin()
                + ", " + mainWindowView.getNGramMax() + ").txt";
    }
    
    private String getPhasesResultFileName() {
    	
    	String fileName = "(" + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + ") result-"
                + "Multiple_classifiers" + "-folds_" + mainWindowView.getCrossValidationFolds() 
                + "-fases_yes" + "-freeling_" + mainWindowView.getUseFreeling() + "-NGram(" + mainWindowView.getNGramMin()
                + ", " + mainWindowView.getNGramMax() + ").txt";
    	
    	return fileName;
    }
    
    private void saveResultsToFile(boolean trainByPhases) {
        
    	File folder = new File("results");
    	if (!folder.exists())
    		folder.mkdir(); 
    	
    	String fileName;
    	if (trainByPhases)
    		fileName = getPhasesResultFileName();
    	else
    		fileName = getDirectResultFileName();
    	
        try (PrintWriter out = new PrintWriter(folder.toString() + File.separator + fileName)) {
            out.println(mainWindowView.getTextAreaTestResults());
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void btnStartPressed(ProcessDataset process, boolean trainByPhases) {
        
        long startTime = System.currentTimeMillis();

        String trainFileName = mainWindowView.getTxtTrainFilePathText(); 
        mainWindowView.setProcessingTextTrainResults("Procesando...");
        String postTrainFileName = process.train(trainFileName);
        
        List<String> items = Arrays.asList(mainWindowView.getTxtTestFilePathText().split("\\s*,\\s*"));
        
        String classificationResults = "";
        
        labeledFileNames = new ArrayList<String>();
        for (String item : items) {
        	String testFileName = item;
            mainWindowView.setProcessingTextTestResults("Procesando...");
            String labeledFileName = process.classify(testFileName, postTrainFileName);
            
            classificationResults += item + '\n' + process.getClassificationResults() + "\n\n";
            
            labeledFileNames.add(labeledFileName);            
        }
        
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        printResults(trainByPhases, duration, process.getTrainingResults(), classificationResults);
        saveResultsToFile(trainByPhases);
        analizeData();
    }
    
    public void btnTrainDirectClassifiersPressed() {
        
        mainWindowView.nextTab();
        
        for (int index = 1; index < mainWindowView.getDirectClassifierItemCount(); index++) {
            
            mainWindowView.setDirectClassifierSelectedItem(index);
            mainWindowView.pressBtnStartDirect();
        }
    }
    
    public void btnTrainPhasesClassifiersPressed() {
        
        mainWindowView.nextTab();
        mainWindowView.nextTab();
        
        for (int index = 1; index < mainWindowView.getPhase1ClassifierItemCount(); index++) {
            
            mainWindowView.setPhase1ClassifierSelectedItem(index);
            mainWindowView.nextTab();
            mainWindowView.setPhase2Classifier1SelectedItem(index);
            mainWindowView.setPhase2Classifier2SelectedItem(index);
            mainWindowView.nextTab();
            mainWindowView.setPhase3Classifier1SelectedItem(index);
            mainWindowView.setPhase3Classifier2SelectedItem(index);
            mainWindowView.setPhase3Classifier3SelectedItem(index);
            mainWindowView.setPhase3Classifier4SelectedItem(index);
      
            mainWindowView.pressBtnStartPhases();
        }
    }
    
    // Sección de Análisis de datos
    private void analizeData() {
        
        IpaAnalysis ipaAnalysis = new IpaAnalysis();
        
        mainWindowView.cleanAnalysisTable();
        for (String item : labeledFileNames) {
            Vector<Object[]> vector = ipaAnalysis.analyze(item);
            
            mainWindowView.addTabToTable(item.substring(item.lastIndexOf(File.separator) + 1, item.length()), vector);
        }
    }
}