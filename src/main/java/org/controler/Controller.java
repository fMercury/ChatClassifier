package org.controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.commons.Constants;
import org.commons.GoogleHangoutsJsonParser;
import org.enums.Classifier;
import org.ipa.GroupAnalysisRow;
import org.ipa.GroupAnalysisResult;
import org.ipa.GroupCreationRow;
import org.ipa.GroupCreationResult;
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

/**
 * Es el encargado de controlar todo el funcionamiento del sistema Conecta la vista con el modelo
 * 
 * @author martinmineo
 *
 */
public class Controller {

    private MainAppWindow mainWindowView;
    private List<String> labeledFileNames = new ArrayList<String>();
    private IpaAnalysis ipaAnalysis;

    /**
     * Constructor
     * 
     * @param  MainAppWindow mainWindowView Vista que se utilizará para el correcto funcionamiento del sistema
     */
    public Controller(MainAppWindow mainWindowView) {

        this.mainWindowView = mainWindowView;
    }

    /**
     * Inicializa la vista
     */
    public void initializeView() {

        mainWindowView.setCbBoxClassifierModel(getCbBoxClassifierContent());
        mainWindowView.setVisible();
    }

    /**
     * Método que permite avanzar una pestaña en la vista avanzada en la sección de elección de los clasificadores
     */
    public void clickNextPhase() {

        mainWindowView.nextTab();
    }

    /**
     * Método que permite avanzar una pestaña en la vista simple en la sección de elección de los clasificadores
     */
    public void clickNextEasyPhase() {

        mainWindowView.nextEasyTab();
    }

    /**
     * Cada vez que un clasificador cambia su valor se completan los parámetros para el clasificador seleccionado y las opciones disponibles
     * @param classifier String Clasificador que se modificó
     * @param index int Índice del elemento seleccionado en el clasificador
     */
    public void cbBoxClassifierChanged(String classifier, int index) {

        int folds = new Integer(mainWindowView.getCrossValidationFolds()).intValue();
        int nGramMin = new Integer(mainWindowView.getNGramMin()).intValue();
        int nGramMax = new Integer(mainWindowView.getNGramMax()).intValue();

        switch (classifier) {
        case "direct":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getDirectClassifier(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtDirectClassifierOptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtDirectClassifierOptionsText("");
            break;
        case "phase1Classifier1":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase1Classifier(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase1ClassifierOptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase1ClassifierOptionsText("");
            break;
        case "phase2Classifier1":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase2Classifier1(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase2Classifier1OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase2Classifier1OptionsText("");
            break;
        case "phase2Classifier2":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase2Classifier2(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase2Classifier2OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase2Classifier2OptionsText("");
            break;
        case "phase3Classifier1":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier1(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase3Classifier1OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase3Classifier1OptionsText("");
            break;
        case "phase3Classifier2":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier2(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase3Classifier2OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase3Classifier2OptionsText("");
            break;
        case "phase3Classifier3":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier3(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase3Classifier3OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase3Classifier3OptionsText("");
            break;
        case "phase3Classifier4":
            if (index != 0) {
                Weka weka = getSelectedClassifier(mainWindowView.getPhase3Classifier4(), folds, nGramMin, nGramMax);

                mainWindowView.setTxtPhase3Classifier4OptionsText(weka.getClassifierOptions().toString());
                mainWindowView.setTextOptions(weka.getClassifierOptionDescription());

            } else
                mainWindowView.setTxtPhase3Classifier4OptionsText("");
            break;
        default:
            break;
        }
    }

    /**
     * Reune la información que va a contener cada combobox
     * @return String[] Devuelve una lista de String con el texto para cada combobox
     */
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

    /**
     * Crea y genera el objecto del clasificador seleccionado
     * @param selectedClassifier Nombre del clasificador a crear
     * @param folds Folds seleccionados
     * @param nGramMin nGram mínimo que usará el clasificador
     * @param nGramMax nGram máximo que usará el clasificador
     * @return Weka Devuelve el clasificador creado
     */
    public Weka getSelectedClassifier(String selectedClassifier, int folds, int nGramMin, int nGramMax) {

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

    /**
     * Comienza el procesado de la clasificación directa
     * @param useEasyProcessing boolean Determina si el prosesado se debe hacer con el set de datos simple o avanzado
     */
    public void btnStartDirectPressed(boolean useEasyProcessing) {

        int folds = new Integer(mainWindowView.getCrossValidationFolds()).intValue();
        int nGramMin = new Integer(mainWindowView.getNGramMin()).intValue();
        int nGramMax = new Integer(mainWindowView.getNGramMax()).intValue();
        boolean useFreeling;

        Weka weka;
        if (useEasyProcessing) {
            weka = getSelectedClassifier(mainWindowView.getEasyDirectClassifier(), folds, nGramMin, nGramMax);
            useFreeling = true;
        } else {
            weka = getSelectedClassifier(mainWindowView.getDirectClassifier(), folds, nGramMin, nGramMax);
            String wekaClassifierOptions = mainWindowView.getDirectClassifierOptions();
            weka.setClassifierOptions(wekaClassifierOptions);
            useFreeling = mainWindowView.getUseFreeling();
        }

        ProcessDataset process = new DirectProcessing(weka, useFreeling, useEasyProcessing);
        startProcessing(process, useEasyProcessing, false);
    }

    /**
     * Comienza el procesado de la clasificación en fases
     * @param useEasyProcessing boolean Determina si el prosesado se debe hacer con el set de datos simple o avanzado
     */
    public void btnStartPhasesPressed(boolean useEasyProcessing) {

        int folds = new Integer(mainWindowView.getCrossValidationFolds()).intValue();
        int nGramMin = new Integer(mainWindowView.getNGramMin()).intValue();
        int nGramMax = new Integer(mainWindowView.getNGramMax()).intValue();

        Weka wekaPhase1Classifier;
        Weka wekaPhase2Classifier1;
        Weka wekaPhase2Classifier2;
        Weka wekaPhase3Classifier1;
        Weka wekaPhase3Classifier2;
        Weka wekaPhase3Classifier3;
        Weka wekaPhase3Classifier4;
        boolean useFreeling;

        if (useEasyProcessing) {
            wekaPhase1Classifier = getSelectedClassifier(mainWindowView.getEasyPhase1Classifier(), folds, nGramMin, nGramMax);
            wekaPhase2Classifier1 = getSelectedClassifier(mainWindowView.getEasyPhase2Classifier1(), folds, nGramMin, nGramMax);
            wekaPhase2Classifier2 = getSelectedClassifier(mainWindowView.getEasyPhase2Classifier2(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier1 = getSelectedClassifier(mainWindowView.getEasyPhase3Classifier1(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier2 = getSelectedClassifier(mainWindowView.getEasyPhase3Classifier2(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier3 = getSelectedClassifier(mainWindowView.getEasyPhase3Classifier3(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier4 = getSelectedClassifier(mainWindowView.getEasyPhase3Classifier4(), folds, nGramMin, nGramMax);
            useFreeling = true;
        } else {
            wekaPhase1Classifier = getSelectedClassifier(mainWindowView.getPhase1Classifier(), folds, nGramMin, nGramMax);
            wekaPhase2Classifier1 = getSelectedClassifier(mainWindowView.getPhase2Classifier1(), folds, nGramMin, nGramMax);
            wekaPhase2Classifier2 = getSelectedClassifier(mainWindowView.getPhase2Classifier2(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier1 = getSelectedClassifier(mainWindowView.getPhase3Classifier1(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier2 = getSelectedClassifier(mainWindowView.getPhase3Classifier2(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier3 = getSelectedClassifier(mainWindowView.getPhase3Classifier3(), folds, nGramMin, nGramMax);
            wekaPhase3Classifier4 = getSelectedClassifier(mainWindowView.getPhase3Classifier4(), folds, nGramMin, nGramMax);

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
            useFreeling = mainWindowView.getUseFreeling();
        }

        ProcessDataset process = new PhasesProcessing(wekaPhase1Classifier, wekaPhase2Classifier1, wekaPhase2Classifier2,
                wekaPhase3Classifier1, wekaPhase3Classifier2, wekaPhase3Classifier3, wekaPhase3Classifier4, useFreeling, useEasyProcessing);
        startProcessing(process, useEasyProcessing, true);
    }

    /**
     * Comienza el procesado de los datos 
     * @param process ProcessDataset Objeto que contiene toda la información para realizar el procesado de los datos
     * @param useEasyProcessing boolean Determina si el prosesado se debe hacer con el set de datos simple o avanzado
     * @param trainByPhases boolean Determina si el procesado va a hacerse en fases o directo
     */
    private void startProcessing(ProcessDataset process, boolean useEasyProcessing, boolean trainByPhases) {

        long startTime = System.currentTimeMillis();
        String postTrainFileName;
        String testFile;

        if (useEasyProcessing) {
            postTrainFileName = "";
            testFile = mainWindowView.getEasyTxtTestFilePathText();
        } else {
            String trainFileName = mainWindowView.getTxtTrainFilePathText();
            mainWindowView.setProcessingTextTrainResults("Procesando...");
            postTrainFileName = process.train(trainFileName);
            testFile = mainWindowView.getTxtTestFilePathText();
        }

        List<String> items;
        if (testFile.contains(Constants.JSON_FILE)) {
            GoogleHangoutsJsonParser parser = new GoogleHangoutsJsonParser();
            items = Arrays.asList(parser.parseJson(testFile).split("\\s*,\\s*"));
        } else {
            items = Arrays.asList(testFile.split("\\s*,\\s*"));
        }

        String classificationResults = "";

        labeledFileNames = new ArrayList<String>();
        for (String item : items) {
            String testFileName = item;
            if (useEasyProcessing) {
                mainWindowView.setEasyProcessingTextTestResults("Procesando...");
            } else {
                mainWindowView.setProcessingTextTestResults("Procesando...");
            }
            String labeledFileName = process.classify(testFileName, postTrainFileName);

            classificationResults += item + '\n' + process.getClassificationResults() + "\n\n";

            labeledFileNames.add(labeledFileName);
        }

        long duration = (System.currentTimeMillis() - startTime) / 1000;

        if (!useEasyProcessing) {
            printTrainResults(trainByPhases, duration, process.getTrainingResults());
            saveResultsToFile(trainByPhases);
        }

        printTestResults(useEasyProcessing, classificationResults);
        analizeData();
    }

    /**
     * Obtiene el clasificador utilizado para el entrenamiento directo
     * @return String Nombre del clasificador seleccionado
     */
    private String getDirectClassifierAndParameters() {

        return "Clasiffier: " + mainWindowView.getDirectClassifier() + '\n' + "Paremeters: " + mainWindowView.getDirectClassifierOptions();
    }

    /**
     * Obtiene los clasificadores utilizados para el entrenamiento en fases
     * @return String Nombre de los clasificadores seleccionados
     */
    private String getPhasesClassifiersAndParameters() {

        String str = "Classifiers:\n" + "Phase 1:\n" + "\tClassifier: " + mainWindowView.getPhase1Classifier() + "\n" + "\tParameters: "
                + mainWindowView.getTxtPhase1Classifier1Options() + "\n\n" +

                "Phase 2:\n" + "\tClassifier: " + mainWindowView.getPhase2Classifier1() + "\n" + "\tParameters: "
                + mainWindowView.getTxtPhase2Classifier1Options() + "\n\n" + "\tClassifier: " + mainWindowView.getPhase2Classifier2() + "\n"
                + "\tParameters: " + mainWindowView.getTxtPhase2Classifier2Options() + "\n\n" +

                "Phase 3:\n" + "\tClassifier: " + mainWindowView.getPhase3Classifier1() + "\n" + "\tParameters: "
                + mainWindowView.getTxtPhase3Classifier1Options() + "\n\n" + "\tClassifier: " + mainWindowView.getPhase3Classifier2() + "\n"
                + "\tParameters: " + mainWindowView.getTxtPhase3Classifier2Options() + "\n\n" + "\tClassifier: "
                + mainWindowView.getPhase3Classifier3() + "\n" + "\tParameters: " + mainWindowView.getTxtPhase3Classifier3Options() + "\n\n"
                + "\tClassifier: " + mainWindowView.getPhase3Classifier4() + "\n" + "\tParameters: "
                + mainWindowView.getTxtPhase3Classifier4Options() + "\n";

        return str;
    }

    /**
     * Imprime por pantalla los resultados del entrenamiento
     * @param trainByPhases boolean Determina si el entrenamiento se hará por fases i directo
     * @param duration long Duración de todo el entrenamiento
     * @param trainingResults String Resultados del entrenamiento
     */
    private void printTrainResults(boolean trainByPhases, long duration, String trainingResults) {

        String classifierAndParameter;
        if (trainByPhases)
            classifierAndParameter = getPhasesClassifiersAndParameters();
        else
            classifierAndParameter = getDirectClassifierAndParameters();

        String trainMode = trainByPhases ? "phases" : "direct";

        String options = "Selected options\n================\n" + classifierAndParameter + '\n' + "Cross-validation folds: "
                + mainWindowView.getCrossValidationFolds() + '\n' + "Train mode: " + trainMode + '\n' + "Use FreeLing: "
                + mainWindowView.getUseFreeling() + '\n' + "NGramMin: " + mainWindowView.getNGramMin() + ", NGramMax: "
                + mainWindowView.getNGramMax() + '\n' + "Process time: " + duration + " seconds"
                + "\n===============================================================\n\n";

        mainWindowView.setProcessingTextTrainResults(options + trainingResults);
    }

    /**
     * Imprime por pantalla los resultados del test
     * @param useEasyProcessing boolean Determina si el prosesado se debe hacer con el set de datos simple o avanzado
     * @param classificationResults String Resultado del test
     */
    private void printTestResults(boolean useEasyProcessing, String classificationResults) {

        if (useEasyProcessing)
            mainWindowView.setEasyProcessingTextTestResults(classificationResults);
        else
            mainWindowView.setProcessingTextTestResults(classificationResults);
    }

    /**
     * Genera el nombre del archivo que tendrá el resultado del entrenamiento
     * @return String nombre del archivo del entrenamiento directo
     */
    private String getDirectResultFileName() {

        return "(" + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + ") result-" + mainWindowView.getDirectClassifier()
                + "-folds_" + mainWindowView.getCrossValidationFolds() + "-fases_yes" + "-freeling_" + mainWindowView.getUseFreeling()
                + "-NGram(" + mainWindowView.getNGramMin() + ", " + mainWindowView.getNGramMax() + ").txt";
    }
    
    /**
     * Genera el nombre del archivo que tendrá el resultado del entrenamiento
     * @return String nombre del archivo del entrenamiento en fases
     */
    private String getPhasesResultFileName() {

        String fileName = "(" + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + ") result-" + "Multiple_classifiers"
                + "-folds_" + mainWindowView.getCrossValidationFolds() + "-fases_yes" + "-freeling_" + mainWindowView.getUseFreeling()
                + "-NGram(" + mainWindowView.getNGramMin() + ", " + mainWindowView.getNGramMax() + ").txt";

        return fileName;
    }

    /**
     * Guarda en un archivo los resultados del entrenamiento
     * @param trainByPhases boolean Determina si el procesado se va a hacer directo o en fases
     */
    private void saveResultsToFile(boolean trainByPhases) {

        File folder = new File(Constants.CLASSIFICATION_FOLDER);
        if (!folder.exists())
            folder.mkdir();

        String fileName;
        if (trainByPhases)
            fileName = getPhasesResultFileName();
        else
            fileName = getDirectResultFileName();

        try (PrintWriter out = new PrintWriter(Constants.CLASSIFICATION_FOLDER + File.separator + fileName)) {
            out.println(mainWindowView.getTextAreaTestResults());
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Guarda en un archivo los resultados del análisis de los datos
     * @param groupAnalysisResults List<GroupAnalysisResult> Resultados del análisis del grupo
     */
    private void saveAnalysisResultsToFile(List<GroupAnalysisResult> groupAnalysisResults) {

        File folder = new File(Constants.ANALYSIS_FOLDER);
        if (!folder.exists())
            folder.mkdir();

        String result = "";
        for (GroupAnalysisResult item : groupAnalysisResults) {
            result += "Nombre del grupo: " + item.getName() + "\n\n";
            result += fixedLengthStringGroupAnalysis("Conflicto", "Conducta", "Lím. Inf.", "Lím. Sup.", "Resultado del análisis") + "\n";
            for (GroupAnalysisRow analysisResult : item.getAnalysisResults()) {
                result += fixedLengthStringGroupAnalysis(analysisResult.getConflict(), analysisResult.getBehavior(),
                        analysisResult.getInfLimit(), analysisResult.getSupLimit(), analysisResult.getPercentage()) + '\n';
            }
            result += "\n\n";
        }

        String fileName = "(" + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + ") group-analysis.txt";
        try (PrintWriter out = new PrintWriter(Constants.ANALYSIS_FOLDER + fileName)) {
            out.println(result);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Guarda en un archivo los resultados de la creación de los grupos
     * @param groupCreationResults List<GroupCreationResult> Resultados de la creación de los grupos
     */
    private void saveGroupsCreatedToFile(List<GroupCreationResult> groupCreationResults) {

        File folder = new File(Constants.ANALYSIS_FOLDER);
        if (!folder.exists())
            folder.mkdir();

        String result = "";
        for (GroupCreationResult item : groupCreationResults) {
            result += "Nombre del grupo: " + item.getName() + "\n";
            result += "Tamaño del grupo: " + item.getGroupSize() + "\n\n";

            result += fixedLengthStringGroupCreation("Nombre", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11", "C12",
                    "Desviación total") + "\n";
            for (GroupCreationRow groupCreation : item.getAnalysisResults()) {
                result += fixedLengthStringGroupCreation(groupCreation.getName(), groupCreation.getC1(), groupCreation.getC2(),
                        groupCreation.getC3(), groupCreation.getC4(), groupCreation.getC5(), groupCreation.getC6(), groupCreation.getC7(),
                        groupCreation.getC8(), groupCreation.getC9(), groupCreation.getC10(), groupCreation.getC11(),
                        groupCreation.getC12(), groupCreation.getTotalDeviation()) + '\n';
            }
            result += "\n\n";
        }

        String fileName = "(" + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + ") group-creation.txt";
        try (PrintWriter out = new PrintWriter(Constants.ANALYSIS_FOLDER + fileName)) {
            out.println(result);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Permite generar un String de tamaño fijo para que en un archivo de texto plano los datos sean más legibles
     * @param conflict String Conflicto
     * @param behavior String Comportamiento
     * @param infLimit String Límite inferior
     * @param supLimit String Límite superior
     * @param percentaje String Porcentaje
     * @return String Texto de largo fijo con la concatenación de todos los datos para una línea en el archivo de texto
     */
    private String fixedLengthStringGroupAnalysis(String conflict, String behavior, String infLimit, String supLimit, String percentaje) {

        return fixedLengthString(conflict, 22, false) + fixedLengthString(behavior, 33, false) + fixedLengthString(infLimit, 11, true)
                + fixedLengthString(supLimit, 11, true) + fixedLengthString(percentaje, 25, true);
    }

    /**
     * Permite generar un String de tamaño fijo para que en un archivo de texto plano los datos sean más legibles
     * @param name String 
     * @param c1 String C1
     * @param c2 String C2
     * @param c3 String C3
     * @param c4 String C4
     * @param c5 String C5
     * @param c6 String C6
     * @param c7 String C7
     * @param c8 String C8
     * @param c9 String C9
     * @param c10 String C10
     * @param c11 String C11
     * @param c12 String C12
     * @param totalDeviation String Desviación total 
     * @return String Texto de largo fijo con la concatenación de todos los datos para una línea en el archivo de texto
     */
    private String fixedLengthStringGroupCreation(String name, String c1, String c2, String c3, String c4, String c5, String c6, String c7,
            String c8, String c9, String c10, String c11, String c12, String totalDeviation) {

        return fixedLengthString(name, 30, false) + fixedLengthString(c1, 9, true) + fixedLengthString(c2, 9, true)
                + fixedLengthString(c3, 9, true) + fixedLengthString(c4, 9, true) + fixedLengthString(c5, 9, true)
                + fixedLengthString(c6, 9, true) + fixedLengthString(c7, 9, true) + fixedLengthString(c8, 9, true)
                + fixedLengthString(c9, 9, true) + fixedLengthString(c10, 9, true) + fixedLengthString(c11, 9, true)
                + fixedLengthString(c12, 9, true) + fixedLengthString(totalDeviation, 18, true);
    }

    /**
     * Genera un texto de largo fijo rellenando con espacios en blanco
     * @param toPad String Texto a rellenar
     * @param width String ancho esperado del texto
     * @param alignRight boolean Determina si la alineación del texto se hará a derecha o izquierda
     * @return
     */
    private String fixedLengthString(String toPad, int width, boolean alignRight) {

        char fill = ' ';
        String padded;
        
        if(toPad.length() > width) {
            toPad = toPad.substring(0, width);
        }
        
        if (alignRight)
            padded = new String(new char[width - toPad.length()]).replace('\0', fill) + toPad;
        else
            padded = toPad + new String(new char[width - toPad.length()]).replace('\0', fill);

        return padded;
    }

    // Sección de Análisis de datos
    /**
     * Analiza todos lod datos obtenidos en la clasificación y lo puestra por pantalla
     */
    private void analizeData() {

        ipaAnalysis = new IpaAnalysis(labeledFileNames);

        mainWindowView.cleanAnalysisTable();
        mainWindowView.cleanGroupsTable();
        mainWindowView.cleanTxtGroupsSize();

        List<GroupAnalysisResult> groupAnalysisResults = ipaAnalysis.analizeGroups();

        for (GroupAnalysisResult item : groupAnalysisResults) {
            mainWindowView.addTabToClassificationAnalysisTable(item.getName(), item.getAnalysisResults());
        }

        saveAnalysisResultsToFile(groupAnalysisResults);
    }

    /**
     * Gerera los grupos de trabajo de acuerdo al tamaño seleccionado
     */
    public void btnCreateGroupsPressed() {

        mainWindowView.cleanGroupsTable();
        int groupsSize = 0;
        try {
            groupsSize = new Integer(mainWindowView.getGroupsSize()).intValue();
        } catch (Exception e) {
            mainWindowView.cleanTxtGroupsSize();
        }

        if (ipaAnalysis != null) {
            List<GroupCreationResult> groupCreationResults = ipaAnalysis.createGroups(groupsSize);

            for (GroupCreationResult item : groupCreationResults) {
                mainWindowView.addTabToGroupCreationTable(item.getName(), item.getAnalysisResults());
            }

            saveGroupsCreatedToFile(groupCreationResults);
        }
    }
}