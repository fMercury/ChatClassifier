package org.weka;

import org.commons.ExcelConversor;
import org.commons.PropertiesManager;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public abstract class MachineLearningClassifierTechnique {

    // Files extensions
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String ARFF = ".arff";
    private static final String CSV = ".csv";

    // Properties file name
    private static final String RESOURCES = "src/main/resources";
    private static final String CLASSIFIER_OPTIONS_DESCRIPTION_PROP = "classifiers-options-description.properties";
    protected PropertiesManager properties;

    protected AbstractClassifier cls;
    protected Evaluation eval;
    protected Instances trainDataset;
    protected Instances evalDataset;

    // no se usa todavia//
    protected Instances labeledDataset;
    //

    public abstract String getValidOptions();

    public abstract String getTrainResults();

    public MachineLearningClassifierTechnique() {

        properties = new PropertiesManager(RESOURCES + "/" + CLASSIFIER_OPTIONS_DESCRIPTION_PROP);
    }

    protected void setClassifier(AbstractClassifier cls) {

        this.cls = cls;
    }

    public void setOptions(String options) {

        if (!options.isEmpty()) {
            try {
                cls.setOptions(weka.core.Utils.splitOptions(options));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // TODO manejar los mensajes de error, mostrandolos por pantalla
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    private Instances getDataset(String filePath) {

        Instances dataset = null;
        if (filePath.contains(XLSX) || filePath.contains(XLS)) {

            ExcelConversor excel = new ExcelConversor();
            filePath = excel.excelToARFF(filePath);
        }

        if (filePath.contains(ARFF) || filePath.contains(CSV)) {
            try {
                dataset = DataSource.read(filePath);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return dataset;
    }

    public void standardizeDatasets(String trainDatasetPath, String evalDatasetPath) {

        Instances train = getDataset(trainDatasetPath);
        Instances eval = getDataset(evalDatasetPath);

        // No usar el Singleton, no es buena idea, mejor crear el objeto aca y setearle 
        // las opciones que quiera como Stemmer, Tokenizer y Stopwords
        StringToWordVector filter = FilterSingleton.getStringToWordVectorInstance(train);//new StringToWordVector();
        int[] attributesToFilter = { 0, 1 };
        filter.setAttributeIndicesArray(attributesToFilter);

        try {
            filter.setInputFormat(train);
            trainDataset = Filter.useFilter(train, filter);
            evalDataset = Filter.useFilter(eval, filter);

            trainDataset.setClassIndex(0);
            evalDataset.setClassIndex(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void trainClassifier() {

        try {
            cls.buildClassifier(trainDataset);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void evalClassifier() {

        try {
            eval = new Evaluation(trainDataset);
            eval.evaluateModel(cls, evalDataset);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getEvalResults() {

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

    // public void classifyUnlabeledDataset(String unlabeledDataset) {
    //
    // try {
    // Instances unlabeled = DataSource.read(unlabeledDataset);
    //
    // Instances filtered;
    //
    // filtered = Filter.useFilter(unlabeled,
    // FilterSingleton.getStringToWordVectorInstance());
    //
    // // set class attribute
    // filtered.setClassIndex(0);
    //
    // // create copy
    // labeledDataset = new Instances(filtered);
    //
    // // label instances
    // for (int i = 0; i < filtered.numInstances(); i++) {
    // double clsLabel = cls.classifyInstance(filtered.instance(i));
    // labeledDataset.instance(i).setClassValue(clsLabel);
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public String[] getOptions() {

        return cls.getOptions();
    }
}
