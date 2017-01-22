package org.weka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.commons.Constants;
import org.commons.PropertiesManager;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveWithValues;

/**
 * Hace todo el procesado de datos relacionado con WEKA
 * @author martinmineo
 *
 */
public abstract class Weka {
    
    // Archivos de propiedades
    private static final String CLASSIFIER_OPTIONS_DESCRIPTION_PROP = "classifiers-options-description.properties";
    protected PropertiesManager properties;
    
    // Extensiones de archivos
    private static final String ARFF = ".arff";
    private static final String CSV = ".csv";

    // Elementos del header del ARFF
    // Nombre de atributos
    private static final String CLASS_AREA = "class_area";
    private static final String CLASS_REACCION = "class_reaccion";
    private static final String CLASS_CONDUCTA = "class_conducta";
    public static final String MENSAJE = "mensaje";
    public static final String NOMBRE = "nombre"; 
    // IPA
    private static final String AREA_SOCIO_EMOCIONAL = "socio-emocional";
    private static final String AREA_TAREA = "tarea";
    private static final String REACCION_POSITIVA = "positiva";
    private static final String REACCION_NEGATIVA = "negativa";
    private static final String REACCION_RESPUESTA = "respuesta";
    private static final String REACCION_PREGUNTA = "pregunta";
    
    private FilteredClassifier filteredClassifier;
    protected AbstractClassifier classifier;
    private Evaluation eval;
    protected List<Filter> trainingFiltersList;
    
    private int folds;
    private int nGramMin;
    private int nGramMax;
    
    /**
     * Constructor
     * @param cls Clasificador a usar
     * @param options Opciones del clasificador
     * @param folds Cantidad de "folds" para el cross-validation
     * @param nGramMin Cantidad mínima de n-gramas
     * @param nGramMax Cantidad máxima de n-gramas
     */
    public Weka(AbstractClassifier cls, int folds, int nGramMin, int nGramMax) {
        
        this.classifier = cls;
        this.folds = folds;        
        this.nGramMin = nGramMin;
        this.nGramMax = nGramMax;
        
        trainingFiltersList = new ArrayList<Filter>();
        
        properties = new PropertiesManager(Constants.RESOURCES + File.separator + CLASSIFIER_OPTIONS_DESCRIPTION_PROP);
    }
    
    /**
     * Setea las opciones del clasificador
     * @param options Opciones seleccionadas para la clasificación 
     */
    public void setClassifierOptions(String options) {
        
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
    
    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return Un String con las opciones por defecto del clasificador
     */
    public abstract String getClassifierOptionDescription();
    
    /**
     * Crea un Attribute con los atributos para la clase Conducta
     * @return Attribute attClassConducta
     */
    public static Attribute classConductaAttribute() {

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
    
    /**
     * Crea un Attribute con los atributos para la clase Reaccion
     * @return Attribute attClassReaccion
     */
    public static Attribute classReaccionAttribute() {

        ArrayList<String> labels = new ArrayList<String>();
        labels = new ArrayList<String>();
        labels.add(REACCION_POSITIVA);
        labels.add(REACCION_NEGATIVA);
        labels.add(REACCION_PREGUNTA);
        labels.add(REACCION_RESPUESTA);
        Attribute attClassReaccion = new Attribute(CLASS_REACCION, labels);

        return attClassReaccion;
    }
    
    /**
     * Crea un Attribute con los atributos para la clase Área
     * @return Attribute attClassArea
     */
    public static Attribute classAreaAttribute() {

        ArrayList<String> labels = new ArrayList<String>();
        labels = new ArrayList<String>();
        labels.add(AREA_SOCIO_EMOCIONAL);
        labels.add(AREA_TAREA);
        Attribute attClassArea = new Attribute(CLASS_AREA, labels);

        return attClassArea;
    }
    
    /**
     * Hace la conversión directa de una Conduca a un Área
     * @param String conducta
     * @return String Area
     */
    public static String conductaToArea(String conducta) {

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
    
    /**
     * Hace la converción directa de una Conducta a una Reacción
     * @param String conducta
     * @return String Reaccion
     */
    public static String conductaToReaccion(String conducta) {

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
    
    /**
     * Carga un dataset pasado por parámetro
     * @param fileName Nombre del archivo a cargar
     * @return Instances dataset
     */
    public static Instances loadDataset(String fileName) {

        Instances dataset = null;

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
    
    /**
     * Guarda un dataset en un archivo pasado por parámetro
     * @param instances Dataset a guardar
     * @param fileName Nombre del archivo para guardar el dataset
     */
    public static void saveDataset(Instances instances, String fileName) {

        try {
            DataSink.write(fileName, instances);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Devuelve un booleano especificando si el clasificador tiene algún filtro especial para usar
     * @return Boolean Especificando si el clasificador tiene algún filtro especial para usar
     */
    private boolean hasSpecialFilter() {
        
        return !trainingFiltersList.isEmpty();
    }
    
    /**
     * Devuelve los filtros que se deben aplicar al clasificador
     * @param dataset 
     * @return Filter
     */
    private Filter getTrainingFilter(Instances dataset) {
        
        StringToWordVector stringToWordVectorFilter = new StringToWordVector();
        
        NGramTokenizer tokenizer=new NGramTokenizer();
        tokenizer.setNGramMinSize(nGramMin);
        tokenizer.setNGramMaxSize(nGramMax);
        stringToWordVectorFilter.setTokenizer(tokenizer);
        
        Attribute attNombre = new Attribute(NOMBRE, (ArrayList<String>) null);
        if (dataset.contains(attNombre))
            stringToWordVectorFilter.setAttributeIndices("3-last");
        else
            stringToWordVectorFilter.setAttributeIndices("2-last");
        
        // Cargar archivo de stopwords al filtro
        File stopwordsFile = new File(Constants.RESOURCES + File.separator + "stopwords" + File.separator + "spanishST.txt");
        WordsFromFile stopwords = new WordsFromFile();
        stopwords.setStopwords(stopwordsFile);
        stringToWordVectorFilter.setStopwordsHandler(stopwords);
               
        if (hasSpecialFilter()){ 
            
            int size = trainingFiltersList.size() + 1;
            Filter[] filters = new Filter[size];
            filters[0] = stringToWordVectorFilter;
            int index = 1;
            for (Filter filter : trainingFiltersList) {
                filters[index++] = filter;
            }

            MultiFilter multiFilter = new MultiFilter();
            multiFilter.setFilters(filters);  
            
            return multiFilter;
        }
        else
            return stringToWordVectorFilter;
    }
    
    /**
     * Este método evalua al clasificador
     * @param fileName Nombre del archivo a evaluar
     * @return Instances evaluationDataset
     */
    public Instances evaluate(String fileName) {
        
        return evaluate(fileName, "", "");
    }
    
    /**
     * Este método evalua al clasificador
     * @param fileName Nombre del archivo a evaluar
     * @param attributeIndex
     * @param nominalIndices
     * @return Instances evaluationDataset
     */
    public Instances evaluate(String fileName, String attributeIndex, String nominalIndices) {

        Instances evaluationDataset = loadDataset(fileName);
        try {
            evaluationDataset.setClassIndex(0);
            
            if (!attributeIndex.isEmpty() && !nominalIndices.isEmpty())
                evaluationDataset = removeInstances(evaluationDataset, attributeIndex, nominalIndices);
            
            Filter filter = getTrainingFilter(evaluationDataset);
            
            filteredClassifier = new FilteredClassifier();
            filteredClassifier.setFilter(filter);
            filteredClassifier.setClassifier(classifier);
            eval = new Evaluation(evaluationDataset);
            eval.crossValidateModel(filteredClassifier, evaluationDataset, folds, new Random(1));
            
        } catch (Exception e) {
            System.out.println("Problem found when evaluating. " + e);
        }
        
        return evaluationDataset;
    }
    
    /**
     * Este método entrena al clasificador con el dataset cargado y guarda el resultado en un archivo
     * @param trainDataset dataset a usar para entrenar
     * @param modelFileName nombre del archivo donde se guardará el modelo
     */
    public void train(Instances trainDataset, String modelFileName) {

        try {
            trainDataset.setClassIndex(0);
            
            Filter filter = getTrainingFilter(trainDataset);
            
            filteredClassifier = new FilteredClassifier();
            filteredClassifier.setFilter(filter);
            filteredClassifier.setClassifier(classifier);
            filteredClassifier.buildClassifier(trainDataset);
            
            saveModel(modelFileName);
            saveModel(modelFileName + classifier.getClass());
            
        } catch (Exception e) {
            System.out.println("Problem found when training. " + e);
        }
    }

    /**
     * Este método carga el modelo a ser usado como clasificador
     * 
     * @param fileName El nombre del archivo que almacena el modelo
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
 
    /**
     * Este método guarda el modelo entrenado en un archivo
     * 
     * @param fileName El nombre del archivo que va a almacenar el modelo entrenado
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
    
    /**
     * Elimina el atributo pasado por parámetro del dataset
     * @param dataset Dataset al que se le va a eliminar el atributo
     * @param attributeIndex Ándice del atributo a eliminar
     * @return Instances dataset
     */
    private Instances removeAttribute(Instances dataset, String attributeIndex) {
        
        Instances newDataset = null;
        // Elimina el atributo Nombre
        Remove remove;
        remove = new Remove();
        remove.setAttributeIndices(attributeIndex);
        remove.setInvertSelection(false);
        try {
            remove.setInputFormat(dataset);
            newDataset = Filter.useFilter(dataset, remove);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return newDataset;
    }
    
    /**
     * Elimina instancias del dataset
     * @param dataset del que se van a eliminar las instancias
     * @param attributeIndex índice del atributo a eliminar
     * @param nominalIndices valor nominal que se quiere eliminar 
     * @return Instances nuevo dataset con las instancias que se quieren conservar
     */
    private Instances removeInstances(Instances dataset, String attributeIndex, String nominalIndices) {
        
        Instances newDataset = null;
        RemoveWithValues removeWithValuesFilter = new RemoveWithValues();
        removeWithValuesFilter.setAttributeIndex(attributeIndex);
        removeWithValuesFilter.setNominalIndices(nominalIndices);
        try {
            removeWithValuesFilter.setInputFormat(dataset);
            newDataset = Filter.useFilter(dataset, removeWithValuesFilter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newDataset;
    }
    
    /**
     * Clasifica un archivo sin clasificar usando el modelo pasado por parámetro
     * @param modelFileName Nombre del archivo del modelo
     * @param datasetFilename Nombre del archivo del dataset a clasificar
     * @param labeledFileName Nombre del archivo de destino del dataset clasificado
     * @param attributeIndexToRemove Atributo a eliminar del dataset (usado para eliminar el atributo Nombre)
     * @return String Devuelve los resultados de la clasificación
     */
    public String classify(String modelFileName, String datasetFilename, String labeledFileName, String attributeIndexToRemove) {
        
        return classify(modelFileName, datasetFilename, labeledFileName, attributeIndexToRemove, "", "");
    }
    
    /**
     * Clasifica un archivo sin clasificar usando el modelo pasado por parámetro
     * @param modelFileName Nombre del archivo del modelo
     * @param datasetFilename Nombre del archivo del dataset a clasificar
     * @param labeledFileName Nombre del archivo de destino del dataset clasificado
     * @param attributeIndexToRemove Atributo a eliminar del dataset (usado para eliminar el atributo Nombre)
     * @param useFilterRemoveWithValues Booleano que determina si se deben eliminar ciertas instancias
     * @return String Devuelve los resultados de la clasificación
     */
    public String classify(String modelFileName, String datasetFilename, String labeledFileName, String attributeIndexToRemove, String attributeIndex, String nominalIndices ) {

        loadModel(modelFileName);
        Instances testDataset = loadDataset(datasetFilename);
        testDataset.setClassIndex(0);

        if(!attributeIndex.isEmpty() && !nominalIndices.isEmpty()){
            testDataset = removeInstances(testDataset, attributeIndex, nominalIndices);
        }

        Instances labeledDataset = new Instances(testDataset);
        
        if (attributeIndexToRemove != null) {
            testDataset = removeAttribute(testDataset, attributeIndexToRemove);
        }
        
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
        
        return labeledDataset.toString();
    }
    
    /**
     * Devuelve el resultado de la evaluación del clasificador
     * @return String resultado de la evaluación del clasificador
     */
    public String getEvaluationResults() {
        
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

    /**
     * Devuelve el resultado de la clasificación del clasificador
     * @return String el resultado de la clasificación del clasificador
     */
    public StringBuilder getClassifierOptions() {
        
        String[] options = classifier.getOptions();
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < options.length; i++) {
            String s = options[i];
            if (s.contains("\""))
                s = s.replace("\"", "\\\"");
            if (s.contains("weka."))
                s = "\"" + s + "\"";
            builder.append(s + " ");
        }
        return builder;
    }
    
    /**
     * Devuelve la cantidad de instancias clasificadas correctamente en la evaluación
     * @return double cantidad de instancias clasificadas correctamente en la evaluación
     */
    public double getCorrectClassifiedInstances() {
        
        return eval.correct();
    }
    
    /**
     * Devuelve la cantidad de instancias clasificadas incorrectamente en la evaluación
     * @return double cantidad de instancias clasificadas incorrectamente en la evaluación
     */
    public double getIncorrectClassifiedInstances() {
        
        return eval.incorrect();
    }
    
    public static void mergeInstances(String file1, String file2, String file3, String file4, String saveTo) {
        
        Instances instances1 = loadDataset(file1);
        Instances instances2 = loadDataset(file2);
        Instances instances3 = loadDataset(file3);
        Instances instances4 = loadDataset(file4);
        
        Instances merged = merge(instances1, instances2);
        merged = merge(merged, instances3);
        merged = merge(merged, instances4);
        
        saveDataset(merged, saveTo);
    }
    
    private static Instances merge(Instances data1, Instances data2) {
        
            // Check where are the string attributes
            int asize = data1.numAttributes();
            boolean strings_pos[] = new boolean[asize];
            for(int i=0; i<asize; i++)
            {
                Attribute att = data1.attribute(i);
                strings_pos[i] = ((att.type() == Attribute.STRING) || (att.type() == Attribute.NOMINAL));
            }

            // Create a new dataset
            Instances dest = new Instances(data1);
            dest.setRelationName(data1.relationName() + "+" + data2.relationName());

            DataSource source = new DataSource(data2);
            Instances instances;
            try {
                instances = source.getStructure();
            
                Instance instance = null;
                while (source.hasMoreElements(instances)) {
                    instance = source.nextElement(instances);
                    dest.add(instance);
    
                    // Copy string attributes
                    for(int i=0; i<asize; i++) {
                        if(strings_pos[i]) {
                            dest.instance(dest.numInstances()-1)
                                .setValue(i,instance.stringValue(i));
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return dest;
        }

    public static void copyDataset(String source, String dest) {
        
        Instances instances = loadDataset(source);
        saveDataset(instances, dest);
    }
}
