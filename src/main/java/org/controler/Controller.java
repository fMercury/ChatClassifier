package org.controler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.commons.PropertiesManager;
import org.enums.Classifier;
import org.enums.Language;
import org.view.MainAppWindow;
import org.weka.J48ClassifierTechnique;
import org.weka.MachineLearningClassifierTechnique;
import org.weka.NaiveBayesClassifierTechnique;
import org.weka.SMOClassifierTechnique;

public class Controller {

    private MachineLearningClassifierTechnique model;
    private MainAppWindow mainWindowView;

    // Preferences
    private static final String RESOURCES = "src/main/resources";

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
    private static final String MAIN_VIEW_CLASSIFIER_ERROR_MESSAGE = "mainViewClassifierErrorMessage";
    private static final String MAIN_VIEW_BTN_SELECT = "mainViewBtnSelect";
    private static final String MAIN_VIEW_BTN_START = "mainViewBtnStart";
    private static final String MAIN_VIEW_TAB_TRAIN_RESULTS = "mainViewTabTrainResults";
    private static final String MAIN_VIEW_TAB_TEST_RESULTS = "mainViewTabTestResults";
    private static final String MAIN_VIEW_PROCESSING = "mainViewProcessing";
    private static final String MAIN_VIEW_USE_FREELING = "mainViewUseFreeling";
    private static final String MAIN_VIEW_USE_PHASES = "mainViewUsePhases";

    public Controller(MachineLearningClassifierTechnique model, MainAppWindow mainWindowView) {

        this.model = model;
        this.mainWindowView = mainWindowView;

        selectedLanguage = Language.getSelectedLanguage();

        languageProp = new PropertiesManager(RESOURCES + "/" + selectedLanguage.getFilename());
    }

    public void initializeView() {

        mainWindowView.setMnLanguageText(languageProp.getProperty(MAIN_VIEW_MENU_LANGUAGE));
        mainWindowView.setMntmEnglishText(languageProp.getProperty(MAIN_VIEW_MENU_LANGUAGE_ENGLISH));
        mainWindowView.setMntmSpanishText(languageProp.getProperty(MAIN_VIEW_MENU_LANGUAGE_SPANISH));
        mainWindowView.setLblTrainFileText(languageProp.getProperty(MAIN_VIEW_TRAIN_FILE));
        mainWindowView.setLblTestFileText(languageProp.getProperty(MAIN_VIEW_TEST_FILE));
        mainWindowView.setLblClassifierText(languageProp.getProperty(MAIN_VIEW_CLASSIFIER));
        mainWindowView.setLblParmetersText(languageProp.getProperty(MAIN_VIEW_PARAMETER));
        mainWindowView.setCbBoxClassifier(getCbBoxClassifierContent());
        mainWindowView.setLblClassifierErrorMessageText(languageProp.getProperty(MAIN_VIEW_CLASSIFIER_ERROR_MESSAGE));
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

            languageProp = new PropertiesManager(RESOURCES + "/" + selectedLanguage.getFilename());
            initializeView();
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

    public void setSelectedClassifier(String selectedClassifier) {

        switch (Classifier.getClassifier(selectedClassifier)) {
        case J48:
            model = new J48ClassifierTechnique();
            break;
        case NAIVE_BAYES:
            model = new NaiveBayesClassifierTechnique();
            break;
        case SMO:
            model = new SMOClassifierTechnique();
            break;
        }
    }

    public StringBuilder getOptions() {

        String[] options = model.getOptions();

        StringBuilder builder = new StringBuilder();
        for (String s : options) {
            if (s.contains("weka."))
                s = "\"" + s + "\"";
            builder.append(s + " ");
        }
        return builder;
    }

    public String getValidOptions() {

        return model.getValidOptions();
    }

    public void train() {

        mainWindowView.setProcessingTextTrainResults(languageProp.getProperty(MAIN_VIEW_PROCESSING));

        String fileName = mainWindowView.getTxtTrainFilePathText();
        model.train(fileName);

        String options = "Opciones seleccionadas\n======================\n" + "Clasificador: " + mainWindowView.getSelectedClassifier()
                + '\n' + "Parámetros: " + mainWindowView.getTxtTrainOptionsText() + '\n' + "Cross-validation folds: "
                + mainWindowView.getCrossValidationFolds() + '\n' + "Entrenar en fases: " + mainWindowView.getTrainByPhases() + '\n'
                + "Usar FreeLing: " + mainWindowView.getUseFreeling() + '\n'
                + "\n===============================================================\n\n";
        mainWindowView.setProcessingTextTrainResults(options + model.getTrainingResults());
    }

    public void classify() {

        mainWindowView.setProcessingTextTestResults(languageProp.getProperty(MAIN_VIEW_PROCESSING));

        String fileName = mainWindowView.getTxtTestFilePathText();
        String trainFileName = mainWindowView.getTxtTrainFilePathText();
        String modelFileName = trainFileName.substring(0, trainFileName.lastIndexOf(".arff"));
        model.classify(fileName, modelFileName);

        mainWindowView.setProcessingTextTestResults(model.getClassifyingResults());
    }

    public void btnStartPresed() {

        model.usePhases(mainWindowView.getTrainByPhases());
        model.setCrossValidationFolds((new Integer(mainWindowView.getCrossValidationFolds()).intValue()));
        model.setUseFreeling(mainWindowView.getUseFreeling());
        train();
        classify();

        try (PrintWriter out = new PrintWriter("results/(" + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()) + ") resultado-"
                + mainWindowView.getSelectedClassifier() + "-folds_" + mainWindowView.getCrossValidationFolds() + "-fases_"
                + mainWindowView.getTrainByPhases() + "-freeling_" + mainWindowView.getUseFreeling() + ".txt")) {
            out.println(mainWindowView.getTextAreaTestResults());
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}