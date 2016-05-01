package org.weka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.commons.ExcelToArffConversor;
import org.commons.PropertiesManager;
//import org.freeling.FreelingAnalyzer3;
import org.freeling.FreelingAnalyzer4;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stopwords.WordsFromFile;
import weka.filters.unsupervised.attribute.StringToWordVector;

public abstract class MachineLearningClassifierTechnique {

    // Extensiones de archivos
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String ARFF = ".arff";
    private static final String CSV = ".csv";

    // Archivos de propiedades
    private static final String RESOURCES = "src/main/resources";
    private static final String CLASSIFIER_OPTIONS_DESCRIPTION_PROP = "classifiers-options-description.properties";
    protected PropertiesManager properties;

    private AbstractClassifier classifier;
    private FilteredClassifier filteredClassifier;

    private Evaluation eval;
    private Instances trainDataset;
    private Instances testDataset;
    private Instances labeledDataset;

    // Elementos del header del ARFF
    // Nombre de atributos
    private static final String CLASS_AREA = "class_area";
    private static final String CLASS_REACCION = "class_reaccion";
    private static final String CLASS_CONDUCTA = "class_conducta";
    private static final String MENSAJE = "mensaje";
    // IPA
    private static final String AREA_SOCIO_EMOCIONAL = "socio-emocional";
    private static final String AREA_TAREA = "tarea";

    private static final String REACCION_POSITIVA = "positiva";
    private static final String REACCION_NEGATIVA = "negativa";
    private static final String REACCION_RESPUESTA = "respuesta";
    private static final String REACCION_PREGUNTA = "pregunta";

    // Resultados del entrenamiento
    private String phase1TrainingResults;
    private String phase2TrainingResults;
    private String phase3TrainingResults;
    
    // Resultados del clasificacion
    private String phase1ClassifyingResults;
    private String phase2ClassifyingResults;
    private String phase3ClassifyingResults;

    private boolean usePhases;
    private boolean useFreeling;
    private int folds;
    private static final int FREELING_ATTRIBUTES = 12; 

    public abstract String getValidOptions();

    public MachineLearningClassifierTechnique() {

        properties = new PropertiesManager(RESOURCES + "/" + CLASSIFIER_OPTIONS_DESCRIPTION_PROP);
    }

    protected void setClassifier(AbstractClassifier cls) {

        this.classifier = cls;
    }

    public void setOptions(String options) {

        if (!options.isEmpty()) {
            try {
                classifier.setOptions(weka.core.Utils.splitOptions(options));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // TODO manejar los mensajes de error, mostrandolos por pantalla
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
    
    private double[] freelingValues(double[] values, int index, FreelingAnalyzer4 freelingAnalyzer) {
        
        // Atributo adjectives
        values[index++] = freelingAnalyzer.getAdjectivesCount();
        // Atributo adverbs
        values[index++] = freelingAnalyzer.getAdverbsCount();
        // Atributo determinants
        values[index++] = freelingAnalyzer.getDeterminantsCount();
        // Atributo names
        values[index++] = freelingAnalyzer.getNamesCount();
        // Atributo verbs
        values[index++] = freelingAnalyzer.getVerbsCount();
        // Atributo pronouns
        values[index++] = freelingAnalyzer.getPronounsCount();
        // Atributo conjuctions
        values[index++] = freelingAnalyzer.getConjunctionsCount();
        // Atributo interjections
        values[index++] = freelingAnalyzer.getInterjectionsCount();
        // Atributo prepositions
        values[index++] = freelingAnalyzer.getPrepositionsCount();
        // Atributo punctuation
        values[index++] = freelingAnalyzer.getPunctuationCount();
        // Atributo numerals
        values[index++] = freelingAnalyzer.getNumeralsCount();
        // Atributo dates_times
        values[index++] = freelingAnalyzer.getDatesAndTimesCount();
        
        return values;
    }
    
    private String freelingAnalisys(String fileName) {
        
        FreelingAnalyzer4 freelingAnalyzer = FreelingAnalyzer4.getInstance();
        
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        
        attributes.add(classConductaAttribute());
        Attribute attMensaje = new Attribute(MENSAJE, (ArrayList<String>) null);
        attributes.add(attMensaje);
        attributes.addAll(freelingAttributes());
        
        Instances freelingDataset = new Instances("chat", attributes, 0);
        Instances dataset = loadDataset(fileName);

        for (int i = 0; i < dataset.numInstances(); i++) {

            Instance instance = dataset.instance(i);
            String conducta = instance.stringValue(0);
            String mensaje = instance.stringValue(1);
            
            mensaje = freelingAnalyzer.getLemmas(freelingAnalyzer.analyze(mensaje));
            
            double[] values = new double[freelingDataset.numAttributes()];
            values[0] = freelingDataset.attribute(0).indexOfValue(conducta);
            values[1] = freelingDataset.attribute(1).addStringValue(mensaje);
            
            values = freelingValues(values, 2, freelingAnalyzer);

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(freelingDataset.attribute(0));

            freelingDataset.add(newInstance);
        }
            
        String freelingFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-freeling.arff";
        saveDataset(freelingDataset, freelingFileName);
        
        return freelingFileName;
    }
    
    private String separateSentences(String fileName) {
        
        FreelingAnalyzer4 freelingAnalyzer = FreelingAnalyzer4.getInstance();
        
        Instances dataset = loadDataset(fileName);
        Instances sentencesDataset = new Instances(dataset);
        
        sentencesDataset = new Instances(dataset,0);

        for (int i = 0; i < dataset.numInstances(); i++) {

            Instance instance = dataset.instance(i);
            String conducta = instance.stringValue(0);
            String mensaje = instance.stringValue(1);
            
            ArrayList<String> sentences = freelingAnalyzer.getSentences(mensaje);
            
            for (String sentence: sentences) {
                
                double[] values = new double[sentencesDataset.numAttributes()];
                values[0] = sentencesDataset.attribute(0).indexOfValue(conducta);
                values[1] = sentencesDataset.attribute(1).addStringValue(sentence);
                
                Instance newInstance = new DenseInstance(1.0, values);
                if (values[0] == -1.0)
                    newInstance.setMissing(sentencesDataset.attribute(0));
    
                sentencesDataset.add(newInstance);
            }
        }
            
        String sentencesFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-sentences.arff";
        saveDataset(sentencesDataset, sentencesFileName);
        
        return sentencesFileName;
    }
    
    private Instances loadDataset(String fileName) {

        Instances dataset = null;
        if (fileName.contains(XLSX) || fileName.contains(XLS)) {

            ExcelToArffConversor excel = new ExcelToArffConversor(useFreeling);
            fileName = excel.excelToARFF(fileName);
        }

        if (fileName.contains(ARFF) || fileName.contains(CSV)) {
            try {
                dataset = DataSource.read(fileName);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return dataset;
    }

    public void usePhases(boolean usePhases) {

        this.usePhases = usePhases;
    }
    
    public void setCrossValidationFolds(int folds) {
        
        this.folds = folds;
    }

    public void train(String fileName) {

        if (useFreeling)
            fileName = freelingAnalisys(fileName);

        if (usePhases) {
            
            // Fase 1
            String phase1FileName = phase1Dataset(fileName);

            evaluate(phase1FileName);
            train();
            String modelFileName = phase1FileName.substring(0, phase1FileName.lastIndexOf(".arff"));
            saveModel(modelFileName + ".dat");
            phase1TrainingResults = getTrainingResultsByPhase();

            // Fase 2
            String phase2FileName = phase2Dataset(fileName);

            evaluate(phase2FileName);
            train();
            modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(".arff"));
            saveModel(modelFileName + ".dat");
            phase2TrainingResults = getTrainingResultsByPhase();

            // Fase 3
            String phase3FileName = phase3Dataset(fileName);

            evaluate(phase3FileName);
            train();
            modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff"));
            saveModel(modelFileName + ".dat");
            phase3TrainingResults = getTrainingResultsByPhase();
            
        } else {
            
            evaluate(fileName);
            train();
            String modelFileName = fileName.substring(0, fileName.lastIndexOf(".arff"));
            saveModel(modelFileName + ".dat");
        }

    }

    private String conductaToArea(String conducta) {

        switch (conducta) {
        case "1":
        case "2":
        case "3":
        case "10":
        case "11":
        case "12":
            return AREA_SOCIO_EMOCIONAL;
        case "4":
        case "5":
        case "6":
        case "7":
        case "8":
        case "9":
            return AREA_TAREA;
        default:
            return "?";
        }
    }

    private String conductaToReaccion(String conducta) {

        switch (conducta) {
        case "1":
        case "2":
        case "3":
            return REACCION_POSITIVA;
        case "10":
        case "11":
        case "12":
            return REACCION_NEGATIVA;
        case "4":
        case "5":
        case "6":
            return REACCION_RESPUESTA;
        case "7":
        case "8":
        case "9":
            return REACCION_PREGUNTA;
        default:
            return "?";
        }
    }

    private ArrayList<Attribute> freelingAttributes() {

        // Atributos de freeling
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();

        attributes.add(new Attribute("adjectives"));
        attributes.add(new Attribute("adverbs"));
        attributes.add(new Attribute("determinants"));
        attributes.add(new Attribute("names"));
        attributes.add(new Attribute("verbs"));
        attributes.add(new Attribute("pronouns"));
        attributes.add(new Attribute("conjuctions"));
        attributes.add(new Attribute("interjections"));
        attributes.add(new Attribute("prepositions"));
        attributes.add(new Attribute("punctuation"));
        attributes.add(new Attribute("numerals"));
        attributes.add(new Attribute("dates_times"));

        return attributes;
    }
    
    private Attribute classConductaAttribute() {

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
        labels.add("12");
        Attribute attClassConducta = new Attribute(CLASS_CONDUCTA, labels);

        return attClassConducta;
    }

    private Attribute classReaccionAttribute() {

        ArrayList<String> labels = new ArrayList<String>();
        labels = new ArrayList<String>();
        labels.add(REACCION_POSITIVA);
        labels.add(REACCION_NEGATIVA);
        labels.add(REACCION_PREGUNTA);
        labels.add(REACCION_RESPUESTA);
        Attribute attClassReaccion = new Attribute(CLASS_REACCION, labels);

        return attClassReaccion;
    }

    private Attribute classAreaAttribute() {

        ArrayList<String> labels = new ArrayList<String>();
        labels = new ArrayList<String>();
        labels.add(AREA_SOCIO_EMOCIONAL);
        labels.add(AREA_TAREA);
        Attribute attClassArea = new Attribute(CLASS_AREA, labels);

        return attClassArea;
    }

    private void saveDataset(Instances instances, String phaseFileName) {

        try {
            DataSink.write(phaseFileName, instances);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ArrayList<Attribute> phase1Attributes() {

        // Atributo class_area
        Attribute attClassArea = classAreaAttribute();

        // Atributo mensaje
        Attribute attMensaje = new Attribute(MENSAJE, (ArrayList<String>) null);

        // Todos los atributos
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(attClassArea);
        attributes.add(attMensaje);

        if (useFreeling) {
            attributes.addAll(freelingAttributes());
        }
            
        return attributes;
    }

    private String phase1Dataset(String fileName) {

        trainDataset = loadDataset(fileName);

        ArrayList<Attribute> attributes = phase1Attributes();

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < trainDataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase1
            Instance instance = trainDataset.instance(i);
            String conducta = instance.stringValue(0);
            String mensaje = instance.stringValue(1);

            double[] values = new double[instances.numAttributes()];
            values[0] = instances.attribute(0).indexOfValue(conductaToArea(conducta));
            values[1] = instances.attribute(1).addStringValue(mensaje);
            
            if (useFreeling) {
                for (int j = 2; j < 2 + FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j);
                }
            }

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));
            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-phase1.arff";
        saveDataset(instances, phaseFileName);

        return phaseFileName;
    }

    private ArrayList<Attribute> phase2Attributes() {

        // Atributo class_reaccion
        Attribute attClassReaccion = classReaccionAttribute();

        // Atributo class_area
        Attribute attClassArea = classAreaAttribute();

        // Atributo mensaje
        Attribute attMensaje = new Attribute(MENSAJE, (ArrayList<String>) null);

        // Todos los atributos
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(attClassReaccion);
        attributes.add(attClassArea);
        attributes.add(attMensaje);
        
        if (useFreeling) {
            attributes.addAll(freelingAttributes());
        }

        return attributes;
    }

    private String phase2Dataset(String fileName) {

        trainDataset = loadDataset(fileName);

        ArrayList<Attribute> attributes = phase2Attributes();

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < trainDataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase2
            Instance instance = trainDataset.instance(i);
            String conducta = instance.stringValue(0);
            String mensaje = instance.stringValue(1);

            double[] values = new double[instances.numAttributes()];
            values[0] = instances.attribute(0).indexOfValue(conductaToReaccion(conducta));
            values[1] = instances.attribute(1).indexOfValue(conductaToArea(conducta));
            values[2] = instances.attribute(2).addStringValue(mensaje);
            
            if (useFreeling) {
                for (int j = 3; j < 3 + FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j-1);
                }
            }

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));
            if (values[1] == -1.0)
                newInstance.setMissing(instances.attribute(1));

            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-phase2.arff";
        saveDataset(instances, phaseFileName);

        return phaseFileName;
    }

    private String phase2TrainDataset(String fileName) {

        Instances dataset = loadDataset(fileName);

        ArrayList<Attribute> attributes = phase2Attributes();

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < dataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase2
            Instance instance = dataset.instance(i);
            String classArea = instance.stringValue(0);
            String mensaje = instance.stringValue(1);

            double[] values = new double[instances.numAttributes()];
            values[0] = -1;
            values[1] = instances.attribute(1).indexOfValue(classArea);
            values[2] = instances.attribute(2).addStringValue(mensaje);

            if (useFreeling) {
                for (int j = 3; j < 3 + FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j-1);
                }
            }
            
            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));

            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf("-phase1.arff")) + "-phase2.arff";
        saveDataset(instances, phaseFileName);

        return phaseFileName;
    }

    private String phase3TrainDataset(String fileName) {

        Instances dataset = loadDataset(fileName);

        ArrayList<Attribute> attributes = phase3Attributes();

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < dataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase2
            Instance instance = dataset.instance(i);
            String classReaction = instance.stringValue(0);
            String classArea = instance.stringValue(1);
            String mensaje = instance.stringValue(2);

            double[] values = new double[instances.numAttributes()];
            values[0] = -1;
            values[1] = instances.attribute(1).indexOfValue(classReaction);
            values[2] = instances.attribute(2).indexOfValue(classArea);
            values[3] = instances.attribute(3).addStringValue(mensaje);
            
            if (useFreeling) {
                for (int j = 4; j < 4 + FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j-1);
                }
            }

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));

            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf("-phase2.arff")) + "-phase3.arff";
        saveDataset(instances, phaseFileName);

        return phaseFileName;
    }

    private ArrayList<Attribute> phase3Attributes() {

        // Atributo class_conducta
        Attribute attClassConducta = classConductaAttribute();

        // Atributo class_reaccion
        Attribute attClassReaccion = classReaccionAttribute();

        // Atributo class_area
        Attribute attClassArea = classAreaAttribute();

        // Atributo mensaje
        Attribute attMensaje = new Attribute(MENSAJE, (ArrayList<String>) null);

        // Todos los atributos
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(attClassConducta);
        attributes.add(attClassReaccion);
        attributes.add(attClassArea);
        attributes.add(attMensaje);
        
        if (useFreeling) {
            attributes.addAll(freelingAttributes());
        }

        return attributes;
    }

    private String phase3Dataset(String fileName) {

        trainDataset = loadDataset(fileName);

        ArrayList<Attribute> attributes = phase3Attributes();

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < trainDataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase2
            Instance instance = trainDataset.instance(i);
            String conducta = instance.stringValue(0);
            String mensaje = instance.stringValue(1);

            double[] values = new double[instances.numAttributes()];
            values[0] = instances.attribute(0).indexOfValue(conducta);
            values[1] = instances.attribute(1).indexOfValue(conductaToReaccion(conducta));
            values[2] = instances.attribute(2).indexOfValue(conductaToArea(conducta));
            values[3] = instances.attribute(3).addStringValue(mensaje);
            
            if (useFreeling) {
                for (int j = 4; j < 4 + FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j-2);
                }
            }

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));
            if (values[1] == -1.0)
                newInstance.setMissing(instances.attribute(1));
            if (values[2] == -1.0)
                newInstance.setMissing(instances.attribute(2));

            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-phase3.arff";
        saveDataset(instances, phaseFileName);

        return phaseFileName;
    }

    /**
     * This method evaluates the classifier. As recommended by WEKA
     * documentation, the classifier is defined but not trained yet. Evaluation
     * of previously trained classifiers can lead to unexpected results.
     */
    private void evaluate(String fileName) {

        trainDataset = loadDataset(fileName);
        try {
            trainDataset.setClassIndex(0);
            StringToWordVector filter = new StringToWordVector();
            filter.setAttributeIndices("2-last");
            
            // Cargar archivo de stopwords al filtro
            File stopwordsFile = new File(RESOURCES + "/stopwords/spanishST.txt");
            WordsFromFile stopwords = new WordsFromFile();
            stopwords.setStopwords(stopwordsFile);
            filter.setStopwordsHandler(stopwords);
            
            filteredClassifier = new FilteredClassifier();
            filteredClassifier.setFilter(filter);
            filteredClassifier.setClassifier(classifier);
            eval = new Evaluation(trainDataset);
            eval.crossValidateModel(filteredClassifier, trainDataset, folds, new Random(1));
        } catch (Exception e) {
            System.out.println("Problem found when evaluating");
        }
    }

    /**
     * This method trains the classifier on the loaded dataset.
     */
    private void train() {

        try {
            trainDataset.setClassIndex(0);
            StringToWordVector filter = new StringToWordVector();
            filter.setAttributeIndices("2-last");
            
            // Cargar archivo de stopwords al filtro
            File stopwordsFile = new File(RESOURCES + "/stopwords/spanishST.txt");
            WordsFromFile stopwords = new WordsFromFile();
            stopwords.setStopwords(stopwordsFile);
            filter.setStopwordsHandler(stopwords);
            
            filteredClassifier = new FilteredClassifier();
            filteredClassifier.setFilter(filter);
            filteredClassifier.setClassifier(classifier);
            filteredClassifier.buildClassifier(trainDataset);
        } catch (Exception e) {
            System.out.println("Problem found when training");
        }
    }

    /**
     * This method saves the trained model into a file. This is done by simple
     * serialization of the classifier object.
     * 
     * @param fileName
     *            The name of the file that will store the trained model.
     */
    private void saveModel(String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(filteredClassifier);
            out.close();
        } catch (IOException e) {
            System.out.println("Problem found when writing: " + fileName);
        }
    }

    public void classify(String fileName, String modelFileName) {
        
        fileName = separateSentences(fileName);
        
        if (useFreeling) {
            fileName = freelingAnalisys(fileName);
            modelFileName += "-freeling";
        }

        if (usePhases) {
            
            // Fase 1
            String phaseFileName = phase1Dataset(fileName);
            String labeledFileNamePhase1 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase1.arff";
            classifyPhaseX(modelFileName + "-phase1.dat", phaseFileName, labeledFileNamePhase1);
            phase1ClassifyingResults = getClassifyingResultsByPhase();

            // Fase 2
            phaseFileName = phase2TrainDataset(labeledFileNamePhase1);
            String labeledFileNamePhase2 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase2.arff";
            classifyPhaseX(modelFileName + "-phase2.dat", phaseFileName, labeledFileNamePhase2);
            phase2ClassifyingResults = getClassifyingResultsByPhase();

            // Fase 3
            phaseFileName = phase3TrainDataset(labeledFileNamePhase2);
            String labeledFileNamePhase3 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3.arff";
            classifyPhaseX(modelFileName + "-phase3.dat", phaseFileName, labeledFileNamePhase3);
            phase3ClassifyingResults = getClassifyingResultsByPhase();
            
        } else {
            
            loadModel(modelFileName + ".dat");
            testDataset = loadDataset(fileName);
            testDataset.setClassIndex(0);

            labeledDataset = new Instances(testDataset);
            try {
                for (int i = 0; i < testDataset.numInstances(); i++) {
                    double pred = classifier.classifyInstance(testDataset.instance(i));
                    labeledDataset.instance(i).setClassValue(pred);
                }
                // Save newly labeled data
                String labeledFileName = fileName.substring(0, fileName.lastIndexOf(".arff"));
                DataSink.write(labeledFileName + "-labeled.arff", labeledDataset);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void classifyPhaseX(String modelFileName, String phaseFileName, String labeledFileName) {

        loadModel(modelFileName);
        testDataset = loadDataset(phaseFileName);
        testDataset.setClassIndex(0);

        labeledDataset = new Instances(testDataset);

        try {
            for (int i = 0; i < testDataset.numInstances(); i++) {
                double pred = classifier.classifyInstance(testDataset.instance(i));
                labeledDataset.instance(i).setClassValue(pred);
            }
            // Save newly labeled data
            DataSink.write(labeledFileName, labeledDataset);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method loads the model to be used as classifier.
     * 
     * @param fileName
     *            The name of the file that stores the text.
     */
    private void loadModel(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
            classifier = (FilteredClassifier) tmp;
            in.close();
        } catch (Exception e) {
            // Given the cast, a ClassNotFoundException must be caught along
            // with the IOException
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    private String getTrainingResultsByPhase() {

        String results = eval.toSummaryString("Results\n=======\n", false);
        results += '\n';
        try {
            results += eval.toMatrixString();
            results += '\n';
            results += eval.toClassDetailsString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return results;

    }

    public String getTrainingResults() {

        if (usePhases) {
            
            String results = "===================\n      Phase 1\n===================\n" + phase1TrainingResults;
                results += "\n===================\n      Phase 2\n===================\n" + phase2TrainingResults;
                results += "\n===================\n      Phase 3\n===================\n" + phase3TrainingResults;
    
            return results;
        }
        else {
            
            return getTrainingResultsByPhase();
        }
    }

    private String getClassifyingResultsByPhase() {

        String results = labeledDataset.toString();
        return results;
    }
    
    public String getClassifyingResults() {

        if (usePhases) {
            
            String results = "===================\n      Phase 1\n===================\n" + phase1ClassifyingResults;
                results += "\n\n===================\n      Phase 2\n===================\n" + phase2ClassifyingResults;
                results += "\n\n===================\n      Phase 3\n===================\n" + phase3ClassifyingResults;
    
            return results;
        }
        else {
            
            return getClassifyingResultsByPhase();
        }
    }

    public String[] getOptions() {

        return classifier.getOptions();
    }

    public void setUseFreeling(boolean useFreeling) {

        this.useFreeling = useFreeling;
    }
}