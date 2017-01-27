package org.weka;

import weka.classifiers.meta.LogitBoost;

public class WekaLogitBoost extends Weka {

    private static final String LOGIT_BOOST_PROPERTY_NAME = "LogitBoost";
    
    public WekaLogitBoost(int folds, int nGramMin, int nGramMax) {
        super(new LogitBoost(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(LOGIT_BOOST_PROPERTY_NAME);
    }
    
    @Override
    public StringBuilder getClassifierOptions() {

        String[] options = classifier.getOptions();
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < options.length; i++) {
            String s = options[i];
            if (s.contains("\""))
                s = s.replace("\"", "\\\"");
            if (s.contains("weka."))
                s = "\"" + s + "\"";
            if(!s.contains("-batch-size"))
                builder.append(s + " ");
        }
        return builder;
    }

	@Override
	public String getClassifierClassName() {
		return (new LogitBoost()).getClass().getName();
	}
    
    
}
