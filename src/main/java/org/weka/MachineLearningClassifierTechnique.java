package org.weka;

import org.commons.ExcelConversor;
import org.commons.PropertiesManager;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveWithValues;

public abstract class MachineLearningClassifierTechnique {

    // Files extensions
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String ARFF = ".arff";
    private static final String CSV = ".csv";

    // Properties file name
    private static final String RESOURCES = "resources";
    private static final String CLASSIFIER_OPTIONS_DESCRIPTION_PROP = "classifiers-options-description.properties";
    protected PropertiesManager properties;

    protected AbstractClassifier cls;
    protected Evaluation eval;
    protected Instances trainInstances;
    protected Instances trainDataset;
    protected Instances evalDataset;
    protected Instances labeledDataset;

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

    public void setTrainDataset(String trainDatasetPath) {

        trainDataset = getDataset(trainDatasetPath);
    }

    public void setEvalDataset(String evalDatasetPath) {

        evalDataset = getDataset(evalDatasetPath);
    }

    // ...................... Filters ..........................//
    private Instances removeUnlabeledInstances(Instances data) {

        Instances dataFiltered = null;
        // Remove unlabeled instances
        try {
            RemoveWithValues removeWithValuesfilter = FilterSingleton.getRemoveWithValuesInstance();

            removeWithValuesfilter.setNominalIndices("1");
            removeWithValuesfilter.setInputFormat(data);

            dataFiltered = Filter.useFilter(data, removeWithValuesfilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataFiltered;
    }

    private Instances convertStringToWordVector(Instances data) {

        Instances dataFiltered = null;
        // Convert from string to word vector
        try {
            StringToWordVector stringToWordVectorFilter = FilterSingleton.getStringToWordVectorInstance();
            stringToWordVectorFilter.setInputFormat(data);
            int[] attributesToFilter = { 0, 1 };
            stringToWordVectorFilter.setAttributeIndicesArray(attributesToFilter);

            dataFiltered = Filter.useFilter(data, stringToWordVectorFilter);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dataFiltered;
    }

    // ..........................................................//

    public void trainClassifier() {

        try {
            // Remove unlabeled instances
            Instances filteredDataset = removeUnlabeledInstances(trainDataset);

            // Convert from string to word vector
            filteredDataset = convertStringToWordVector(filteredDataset);
            filteredDataset.setClassIndex(0);

            trainInstances = filteredDataset;
            cls.buildClassifier(trainInstances);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void evalClassifier() {

        try {
            evalDataset = removeUnlabeledInstances(evalDataset);
            evalDataset = convertStringToWordVector(evalDataset);
            evalDataset.setClassIndex(0);

            eval = new Evaluation(trainInstances);
            eval.evaluateModel(cls, evalDataset);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getEvalResults() {

        return eval.toSummaryString("\nResults\n=======\n", false);
    }

    public void classifyUnlabeledDataset(String unlabeledDataset) {

        try {
            Instances unlabeled = DataSource.read(unlabeledDataset);

            Instances filtered;

            filtered = Filter.useFilter(unlabeled, FilterSingleton.getStringToWordVectorInstance());

            // set class attribute
            filtered.setClassIndex(0);

            // create copy
            labeledDataset = new Instances(filtered);

            // label instances
            for (int i = 0; i < filtered.numInstances(); i++) {
                double clsLabel = cls.classifyInstance(filtered.instance(i));
                labeledDataset.instance(i).setClassValue(clsLabel);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String[] getOptions() {

        return cls.getOptions();
    }
}
