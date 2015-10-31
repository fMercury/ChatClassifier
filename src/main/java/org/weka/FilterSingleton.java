package org.weka;

import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class FilterSingleton {

    private static StringToWordVector stringToWordVectorFilter = null;

    private FilterSingleton() {
    }

    public static StringToWordVector getStringToWordVectorInstance(Instances data) {

        if (stringToWordVectorFilter == null) {
            stringToWordVectorFilter = new StringToWordVector();
            try {
                stringToWordVectorFilter.setInputFormat(data);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return stringToWordVectorFilter;
    }
}
