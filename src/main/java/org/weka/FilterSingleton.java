package org.weka;

import java.io.File;

import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.NGramTokenizer;
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
        
        SnowballStemmer stemmer = new SnowballStemmer();
        try {
            stemmer.setOptions(weka.core.Utils.splitOptions("-S spanish"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        stringToWordVectorFilter.setStemmer(stemmer);
        
        WordsFromFile stopwords = new WordsFromFile();
        stopwords.setStopwords(new File ("/Users/martinmineo/Desarrollo/Tesis/workspace/ChatClassifier/src/main/resources/stopwords/spanishST.txt"));
        stringToWordVectorFilter.setStopwordsHandler(stopwords);
      
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMaxSize(3);
        tokenizer.setNGramMinSize(1);
        tokenizer.setDelimiters(".,;:'\"()?!");
        //stringToWordVectorFilter.setTokenizer(tokenizer);

        return stringToWordVectorFilter;
    }
}
