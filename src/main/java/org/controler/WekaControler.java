package org.controler;

import org.weka.MachineLearningTechnique;
import org.weka.J48Technique;
import org.weka.NaiveBayesTechnique;

public class WekaControler {

    private MachineLearningTechnique mlt;

    // Techniques
    private static final int J48 = 1;
    private static final int NAIVE_BAYES = 2;

    public WekaControler() {

    }

    public String trainClassifier(int technique, String fileName, String options) {

        switch (technique) {
        case J48:
            mlt = new J48Technique(fileName, options);
            mlt.trainClassifier();
            break;
        case NAIVE_BAYES:
            mlt = new NaiveBayesTechnique(fileName, options);
            mlt.trainClassifier();
            break;
        }

        return mlt.getResults();

    }
}
