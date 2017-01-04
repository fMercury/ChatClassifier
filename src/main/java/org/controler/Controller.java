package org.controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.commons.Constants;
import org.commons.PropertiesManager;
import org.enums.Classifier;
import org.enums.Language;
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
    
//    private Weka weka;
    private MainAppWindow mainWindowView;

    // Properties
    private PropertiesManager languageProp;

    // Language
    private Language selectedLanguage;

    // Language items
    private static final String MAIN_VIEW_MENU_LANGUAGE = "mainViewMenuLanguage";
    private static final String MAIN_VIEW_MENU_LANGUAGE_ENGLISH = "mainViewMenuLanguageEnglish";
    private static final String MAIN_VIEW_MENU_LANGUAGE_SPANISH = "mainViewMenuLanguageSpanish";
    private static final String MAIN_VIEW_TRAIN_FILE = "mainViewTrainFile";
    private static final String MAIN_VIEW_TEST_FILE = "mainViewTestFile";
    private static final String MAIN_VIEW_CLASSIFIER = "mainViewClassifier";
    private static final String MAIN_VIEW_PARAMETER = "mainViewParameter";
    private static final String MAIN_VIEW_CBBOX_OPTION = "mainViewCbBoxOption";
    private static final String MAIN_VIEW_BTN_SELECT = "mainViewBtnSelect";
    private static final String MAIN_VIEW_TAB_TRAIN_RESULTS = "mainViewTabTrainResults";
    private static final String MAIN_VIEW_TAB_TEST_RESULTS = "mainViewTabTestResults";
    private static final String MAIN_VIEW_PROCESSING = "mainViewProcessing";
    private static final String MAIN_VIEW_USE_FREELING = "mainViewUseFreeling";

    
    public Controller(MainAppWindow mainWindowView) {

        this.mainWindowView = mainWindowView;

        selectedLanguage = Language.getSelectedLanguage();

        languageProp = new PropertiesManager(Constants.RESOURCES + File.separator + selectedLanguage.getFilename());
    }

    public void initializeView() {

        mainWindowView.setMnLanguageText(languageProp.getProperty(MAIN_VIEW_MENU_LANGUAGE));
        mainWindowView.setMntmEnglishText(languageProp.getProperty(MAIN_VIEW_MENU_LANGUAGE_ENGLISH));
        mainWindowView.setMntmSpanishText(languageProp.getProperty(MAIN_VIEW_MENU_LANGUAGE_SPANISH));
        mainWindowView.setLblTrainFileText(languageProp.getProperty(MAIN_VIEW_TRAIN_FILE));
        mainWindowView.setLblTestFileText(languageProp.getProperty(MAIN_VIEW_TEST_FILE));
        mainWindowView.setLblClassifierText(languageProp.getProperty(MAIN_VIEW_CLASSIFIER));
        mainWindowView.setLblParmetersText(languageProp.getProperty(MAIN_VIEW_PARAMETER));
        mainWindowView.setCbBoxClassifierModel(getCbBoxClassifierContent());
        mainWindowView.setBtnSelectTrainText(languageProp.getProperty(MAIN_VIEW_BTN_SELECT));
        mainWindowView.setBtnSelectTestText(languageProp.getProperty(MAIN_VIEW_BTN_SELECT));
        mainWindowView.setTabTrainResultsText(languageProp.getProperty(MAIN_VIEW_TAB_TRAIN_RESULTS));
        mainWindowView.setTabTestResultsText(languageProp.getProperty(MAIN_VIEW_TAB_TEST_RESULTS));
        mainWindowView.setTextUseFreeling(languageProp.getProperty(MAIN_VIEW_USE_FREELING));

        switch (selectedLanguage) {
        case ENGLISH:
            mainWindowView.setMntmEnglishSelected(true);
            break;
        case SPANISH:
            mainWindowView.setMntmSpanishSelected(true);
            break;
        default:
            mainWindowView.setMntmSpanishSelected(true);
            break;
        }

        mainWindowView.setVisible();
    }

    public void changeLanguageTo(Language language) {

        if (selectedLanguage != language) {
            switch (language) {
            case ENGLISH:
                selectedLanguage = Language.ENGLISH;
                mainWindowView.setMntmEnglishSelected(true);
                mainWindowView.setMntmSpanishSelected(false);
                break;
            case SPANISH:
                selectedLanguage = Language.SPANISH;
                mainWindowView.setMntmEnglishSelected(false);
                mainWindowView.setMntmSpanishSelected(true);
                break;
            default:
                selectedLanguage = Language.SPANISH;
                mainWindowView.setMntmEnglishSelected(true);
                mainWindowView.setMntmSpanishSelected(false);
                break;
            }

            languageProp = new PropertiesManager(Constants.RESOURCES + File.separator + selectedLanguage.getFilename());
            initializeView();
        }
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
        content.add(languageProp.getProperty(MAIN_VIEW_CBBOX_OPTION));

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
    
    private void printResults(boolean trainByPhases, long duration, String trainingResults, String classificationResults) {
        
        String options = "Opciones seleccionadas\n======================\n" + "Clasificador: " + mainWindowView.getDirectClassifier()
        + '\n' + "Parámetros: " + mainWindowView.getDirectClassifierOptions() + '\n' + "Cross-validation folds: "
        + mainWindowView.getCrossValidationFolds() + '\n' + "Entrenar en fases: " + trainByPhases + '\n'
        + "Usar FreeLing: " + mainWindowView.getUseFreeling()  + '\n'
        + "NGramMin: " + mainWindowView.getNGramMin() + ", NGramMax: " + mainWindowView.getNGramMax() + '\n'
        + "Tiempo de procesado: " + duration + " segundos"
        + "\n===============================================================\n\n";
        
        mainWindowView.setProcessingTextTrainResults(options + trainingResults);
        mainWindowView.setProcessingTextTestResults(classificationResults);
    }
    
    private void saveResultsToFile(boolean trainByPhases) {
        
    	File folder = new File("results");
    	if (!folder.exists())
    		folder.mkdir(); 
    	
        try (PrintWriter out = new PrintWriter(folder.toString() + File.separator + "(" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ") resultado-"
                + mainWindowView.getDirectClassifier() + "-folds_" + mainWindowView.getCrossValidationFolds() + "-fases_"
                + trainByPhases + "-freeling_" + mainWindowView.getUseFreeling() + "-NGram(" + mainWindowView.getNGramMin()
                + ", " + mainWindowView.getNGramMax() + ").txt")) {
            out.println(mainWindowView.getTextAreaTestResults());
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void btnStartPressed(ProcessDataset process, boolean trainByPhases) {
        
        long startTime = System.currentTimeMillis();

        String trainFileName = mainWindowView.getTxtTrainFilePathText(); 
        mainWindowView.setProcessingTextTrainResults(languageProp.getProperty(MAIN_VIEW_PROCESSING));
        String postTrainFileName = process.train(trainFileName);
        
        String testFileName = mainWindowView.getTxtTestFilePathText();
        mainWindowView.setProcessingTextTestResults(languageProp.getProperty(MAIN_VIEW_PROCESSING));
        process.classify(testFileName, postTrainFileName);
        
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        printResults(trainByPhases, duration, process.getTrainingResults(), process.getClassificationResults());
        saveResultsToFile(trainByPhases);
    }
}