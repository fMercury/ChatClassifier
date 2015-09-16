package org.weka;

import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class FilterSingleton {

    private static StringToWordVector stringToWordVectorFilter = null;
    private static RemoveWithValues removeWithValuesFilter = null;

    private FilterSingleton() {
    }

    public static StringToWordVector getStringToWordVectorInstance() {

        if (stringToWordVectorFilter == null)
            stringToWordVectorFilter = new StringToWordVector();

        return stringToWordVectorFilter;
    }

    public static RemoveWithValues getRemoveWithValuesInstance() {

        if (removeWithValuesFilter == null)
            removeWithValuesFilter = new RemoveWithValues();

        return removeWithValuesFilter;
    }
}
