package org.controler;

import java.util.ArrayList;
import java.util.List;

import org.commons.PropertiesManager;
import org.enums.Classifier;
import org.enums.Language;
import org.view.MainAppWindow;
import org.weka.J48ClassifierTechnique;
import org.weka.MachineLearningClassifierTechnique;
import org.weka.NaiveBayesClassifierTechnique;

public class WekaControler {

    private MachineLearningClassifierTechnique model;
    private MainAppWindow mainWindowView;

    // Preferences
    private static final String RESOURCES = "resources";

    // Properties
    private PropertiesManager languageProp;

    private Language selectedLanguage;

    // Language items
    private static final String MAIN_VIEW_MENU_LANGUAGE = "mainViewMenuLanguage";
    private static final String MAIN_VIEW_MENU_LANGUAGE_ENGLISH = "mainViewMenuLanguageEnglish";
    private static final String MAIN_VIEW_MENU_LANGUAGE_SPANISH = "mainViewMenuLanguageSpanish";
    private static final String MAIN_VIEW_TRAIN_FILE = "mainViewTrainFile";
    private static final String MAIN_VIEW_CLASSIFIER = "mainViewClassifier";
    private static final String MAIN_VIEW_PARAMETER = "mainViewParameter";
    private static final String MAIN_VIEW_CBBOX_OPTION = "mainViewCbBoxOption";
    private static final String MAIN_VIEW_CLASSIFIER_ERROR_MESSAGE = "mainViewClassifierErrorMessage";
    private static final String MAIN_VIEW_BTN_SELECT = "mainViewBtnSelect";
    private static final String MAIN_VIEW_BTN_START = "mainViewBtnStart";
    private static final String MAIN_VIEW_TAB_TRAIN_RESULTS = "mainViewTabTrainResults";
    private static final String MAIN_VIEW_TAB_EVAL_RESULTS = "mainViewTabEvalResults";

    public WekaControler(MachineLearningClassifierTechnique model, MainAppWindow mainWindowView) {

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
        mainWindowView.setLblClassifierText(languageProp.getProperty(MAIN_VIEW_CLASSIFIER));
        mainWindowView.setLblParmetersText(languageProp.getProperty(MAIN_VIEW_PARAMETER));
        mainWindowView.setCbBoxClassifier(getCbBoxClassifierContent());
        mainWindowView.setLblClassifierErrorMessageText(languageProp.getProperty(MAIN_VIEW_CLASSIFIER_ERROR_MESSAGE));
        mainWindowView.setBtnSelectText(languageProp.getProperty(MAIN_VIEW_BTN_SELECT));
        mainWindowView.setBtnStartText(languageProp.getProperty(MAIN_VIEW_BTN_START));
        mainWindowView.setTabTrainResultsText(languageProp.getProperty(MAIN_VIEW_TAB_TRAIN_RESULTS));
        mainWindowView.setTabEvalResultsText(languageProp.getProperty(MAIN_VIEW_TAB_EVAL_RESULTS));

        switch (selectedLanguage) {
        case ENGLISH:
            mainWindowView.setMntmEnglishSelected(true);
            break;
        case SPANISH:
            mainWindowView.setMntmSpanishSelected(true);
            break;
        default:
            mainWindowView.setMntmEnglishSelected(true);
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
                selectedLanguage = Language.ENGLISH;
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
        }
    }

    public void btnStartPresed() {
        String trainDatasetPath = mainWindowView.getTxtTrainFilePathText();
        String evalDatasetPath = mainWindowView.getTxtEvalFilePathText();
        String options = mainWindowView.getTxtTrainOptionsText();

        trainClassifier(trainDatasetPath, options);
        evalClassifier(evalDatasetPath);
    }

    private void trainClassifier(String trainDatasetPath, String options) {

        model.setTrainDataset(trainDatasetPath);
        model.setOptions(options);
        model.trainClassifier();
        mainWindowView.setTextAreaResultsText(model.getTrainResults());
    }

    private void evalClassifier(String evalDatasetPath) {

        model.setEvalDataset(evalDatasetPath);
        model.evalClassifier();
        mainWindowView.setTextAreaEvalResults(model.getEvalResults());
    }

    public StringBuilder getOptions() {

        String[] options = model.getOptions();

        StringBuilder builder = new StringBuilder();
        for (String s : options) {
            builder.append(s + " ");
        }
        return builder;
    }

    public String getValidOptions() {

        return model.getValidOptions();
    }
}