package org.weka;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveWithValues;

public abstract class MachineLearningTechnique {

    protected AbstractClassifier cls;
    protected Instances labeledDataset;

    public MachineLearningTechnique() {

    }

    public abstract String getResults();

    public void setOptions(String[] options) {

        try {
            cls.setOptions(options);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public void trainClassifier(String sourceDataset) {

        try {
            // Load labeled instances
            Instances train = DataSource.read(sourceDataset);

            // Remove unlabeled instances
            train = removeUnlabeledInstances(train);

            // Convert from string to word vector
            Instances filtered = convertStringToWordVector(train);
            filtered.setClassIndex(0);

            cls.buildClassifier(filtered);

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
