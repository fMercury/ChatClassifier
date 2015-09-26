package org.weka;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveWithValues;

public abstract class MachineLearningTechnique {
    
    // Files extensions
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";

    protected AbstractClassifier cls;
    protected Instances dataset;
    protected Instances labeledDataset;

    public MachineLearningTechnique(AbstractClassifier cls, String sourceDataset, String options) {

        this.cls = cls;
        setDataset(sourceDataset);
        setOptions(options);
    }

    private void setOptions(String options) {

        if (!options.isEmpty()) {
            try {
                cls.setOptions(weka.core.Utils.splitOptions(options));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void setDataset(String sourceDataset) {

        if (sourceDataset.contains(XLSX) || sourceDataset.contains(XLS)) {

            ExcelConversor excel = new ExcelConversor();
            sourceDataset = excel.excelToCSV(sourceDataset);
        }

        try {
            dataset = DataSource.read(sourceDataset);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public abstract String getResults();

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
            Instances filteredDataset = removeUnlabeledInstances(dataset);

            // Convert from string to word vector
            filteredDataset = convertStringToWordVector(filteredDataset);
            filteredDataset.setClassIndex(0);

            cls.buildClassifier(filteredDataset);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
}
