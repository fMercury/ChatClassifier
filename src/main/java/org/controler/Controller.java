package org.controler;

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
    
    private Weka weka;
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
    private static final String MAIN_VIEW_BTN_START = "mainViewBtnStart";
    private static final String MAIN_VIEW_TAB_TRAIN_RESULTS = "mainViewTabTrainResults";
    private static final String MAIN_VIEW_TAB_TEST_RESULTS = "mainViewTabTestResults";
    private static final String MAIN_VIEW_PROCESSING = "mainViewProcessing";
    private static final String MAIN_VIEW_USE_FREELING = "mainViewUseFreeling";
    private static final String MAIN_VIEW_USE_PHASES = "mainViewUsePhases";

    
    public Controller(MainAppWindow mainWindowView) {

        this.mainWindowView = mainWindowView;

        selectedLanguage = Language.getSelectedLanguage();

        languageProp = new PropertiesManager(Constants.RESOURCES + "/" + selectedLanguage.getFilename());
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
        mainWindowView.setBtnStartText(languageProp.getProperty(MAIN_VIEW_BTN_START));
        mainWindowView.setTabTrainResultsText(languageProp.getProperty(MAIN_VIEW_TAB_TRAIN_RESULTS));
        mainWindowView.setTabTestResultsText(languageProp.getProperty(MAIN_VIEW_TAB_TEST_RESULTS));
        mainWindowView.setTextUseFreeling(languageProp.getProperty(MAIN_VIEW_USE_FREELING));
        mainWindowView.setTextUsePhases(languageProp.getProperty(MAIN_VIEW_USE_PHASES));

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

            languageProp = new PropertiesManager(Constants.RESOURCES + "/" + selectedLanguage.getFilename());
            initializeView();
        }
    }
    
    public void cbBoxClassifierChanged(int index) {
        
        if (index != 0) {
            setSelectedClassifier();
            
            StringBuilder options = getOptions();

            mainWindowView.setTxtTrainOptionsText(options.toString());
            mainWindowView.setTextOptions(getClassifierOptionDescription());
            
        } else
            mainWindowView.setTxtTrainOptionsText("");
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

    public void setSelectedClassifier() {
       
        int folds = new Integer(mainWindowView.getCrossValidationFolds()).intValue();
        int nGramMin = new Integer(mainWindowView.getNGramMin()).intValue();
        int nGramMax = new Integer (mainWindowView.getNGramMax()).intValue();
        
        switch (Classifier.getClassifier(mainWindowView.getSelectedClassifier())) {
        case J48:
            weka = new WekaJ48(folds, nGramMin, nGramMax);
            break;
        case NAIVE_BAYES:
            weka = new WekaNaiveBayes(folds, nGramMin, nGramMax);
            break;
        case SMO:
            weka = new WekaSMO(folds, nGramMin, nGramMax);
            break;
        case IBk:
            weka = new WekaIBk(folds, nGramMin, nGramMax);
            break;
        case KStar:
            weka = new WekaKStar(folds, nGramMin, nGramMax);
            break;
        case PART:
            weka = new WekaPART(folds, nGramMin, nGramMax);
            break;
        case JRip:
            weka = new WekaJRip(folds, nGramMin, nGramMax);
            break;
        case LogitBoost:
            weka = new WekaLogitBoost(folds, nGramMin, nGramMax);
            break;
        case LMT:
            weka = new WekaLMT(folds, nGramMin, nGramMax);
            break;
        case NBMU:
            weka = new WekaNaiveBayesMultinomialUpdateable(folds, nGramMin, nGramMax);
            break;
        case REPTree:
            weka = new WekaREPTree(folds, nGramMin, nGramMax);
            break;
        case DecisionTable:
            weka = new WekaDecisionTable(folds, nGramMin, nGramMax);
            break;
        case BayesNet:
            weka = new WekaBayesNet(folds, nGramMin, nGramMax);
            break;
            
        default:
            weka = new WekaSMO(folds, nGramMin, nGramMax);
        }
    }

    private StringBuilder getOptions() {

        return weka.getClassifierOptions();
    }

    private String getClassifierOptionDescription() {

        return weka.getClassifierOptionDescription();
    }

    public void btnStartPresed() {
        
        long startTime = System.currentTimeMillis();
        
        boolean useFreeling = mainWindowView.getUseFreeling();
        
        ProcessDataset process;
        String wekaClassifierOptions = mainWindowView.getTxtTrainOptionsText();
        setSelectedClassifier();
        weka.setClassifierOptions(wekaClassifierOptions);
        
        if (mainWindowView.getTrainByPhases()) {
            process =  new PhasesProcessing(weka, useFreeling);
        }
        else
            process = new DirectProcessing(weka, useFreeling);
        
        String trainFileName = mainWindowView.getTxtTrainFilePathText(); 
        String testFileName = mainWindowView.getTxtTestFilePathText();

        mainWindowView.setProcessingTextTrainResults(languageProp.getProperty(MAIN_VIEW_PROCESSING));
        String postTrainFileName = process.train(trainFileName);
        
        mainWindowView.setProcessingTextTestResults(languageProp.getProperty(MAIN_VIEW_PROCESSING));
        process.classify(testFileName, postTrainFileName);
        
        String classificationResults = process.getClassificationResults();
        
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        
        String options = "Opciones seleccionadas\n======================\n" + "Clasificador: " + mainWindowView.getSelectedClassifier()
        + '\n' + "Parámetros: " + mainWindowView.getTxtTrainOptionsText() + '\n' + "Cross-validation folds: "
        + mainWindowView.getCrossValidationFolds() + '\n' + "Entrenar en fases: " + mainWindowView.getTrainByPhases() + '\n'
        + "Usar FreeLing: " + mainWindowView.getUseFreeling()  + '\n'
        + "NGramMin: " + mainWindowView.getNGramMin() + ", NGramMax: " + mainWindowView.getNGramMax() + '\n'
        + "Tiempo de procesado: " + duration + " segundos"
        + "\n===============================================================\n\n";
        
        mainWindowView.setProcessingTextTrainResults(options + process.getTrainingResults());
        mainWindowView.setProcessingTextTestResults(classificationResults);
        
        try (PrintWriter out = new PrintWriter("results/(" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ") resultado-"
                + mainWindowView.getSelectedClassifier() + "-folds_" + mainWindowView.getCrossValidationFolds() + "-fases_"
                + mainWindowView.getTrainByPhases() + "-freeling_" + mainWindowView.getUseFreeling() + "-NGram(" + mainWindowView.getNGramMin()
                + ", " + mainWindowView.getNGramMax() + ").txt")) {
            out.println(mainWindowView.getTextAreaTestResults());
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}