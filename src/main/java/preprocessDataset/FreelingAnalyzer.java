package preprocessDataset;

import java.util.ArrayList;
import java.util.List;

import edu.upc.freeling4.Alternatives;
import edu.upc.freeling4.ChartParser;
import edu.upc.freeling4.HmmTagger;
import edu.upc.freeling4.ListPairStringInt;
import edu.upc.freeling4.ListSentence;
import edu.upc.freeling4.ListSentenceIterator;
import edu.upc.freeling4.ListWord;
import edu.upc.freeling4.Maco;
import edu.upc.freeling4.MacoOptions;
import edu.upc.freeling4.Nec;
import edu.upc.freeling4.Probabilities;
import edu.upc.freeling4.Sentence;
import edu.upc.freeling4.Splitter;
import edu.upc.freeling4.Tokenizer;
import edu.upc.freeling4.Util;
import edu.upc.freeling4.VectorWord;
import edu.upc.freeling4.Word;

public class FreelingAnalyzer {

    private static FreelingAnalyzer instance;

    private static final String LANG = "es";
    private static final String FREELING_DIR = "src/main/resources/freeling4.0/";
    private static final String FREELING_DATA = FREELING_DIR + "data/";

    private MacoOptions macoOptions;
    private Tokenizer tokenizer;
    private Splitter splitter;
    private Maco maco;
    private HmmTagger tagger;
    private ChartParser parser;
    private Nec neclass;
    private Probabilities probabilities;
    private Alternatives alternatives;

    // EAGLE tags
    private List<Integer> eagleTags;
    private static int EAGLE_TAGS_SIZE = 15;

    private static int EAGLE_TAG_POS_ADJECTIVES = 0;
    private static int EAGLE_TAG_POS_ADVERBS = 1;
    private static int EAGLE_TAG_POS_DETERMINANTS = 2;
    private static int EAGLE_TAG_POS_NAMES = 3;
    private static int EAGLE_TAG_POS_VERBS = 4;
    private static int EAGLE_TAG_POS_PRONOUNS = 5;
    private static int EAGLE_TAG_POS_CONJUNCTIONS = 6;
    private static int EAGLE_TAG_POS_INTERJECTIONS = 7;
    private static int EAGLE_TAG_POS_PREPOSITIONS = 8;
    private static int EAGLE_TAG_POS_PUNCTUATION = 9;
    private static int EAGLE_TAG_POS_NUMERALS = 10;
    private static int EAGLE_TAG_POS_DATES_AND_TIMES = 11;
    private static int EAGLE_TAG_POS_EMOTICON_POS = 12;
    private static int EAGLE_TAG_POS_EMOTICON_NEG = 13;
    private static int EAGLE_TAG_POS_EMOTICON_NEU = 14;

    private FreelingAnalyzer() {

        String userDir = System.getProperty("user.dir");
        System.load(userDir + "/" + FREELING_DIR + "libfreeling_javaAPI.dylib");

        Util.initLocale("default");

        eagleTags = new ArrayList<Integer>(EAGLE_TAGS_SIZE);
        for (int i = 0; i < EAGLE_TAGS_SIZE; i++) {
            eagleTags.add(i, new Integer(0));
        }

        createAnalyzers();
    }

    public static FreelingAnalyzer getInstance() {

        if (instance == null)
            instance = new FreelingAnalyzer();
        return instance;
    }

    private MacoOptions createMacoOptions() {

        macoOptions = new MacoOptions(LANG);

        macoOptions.setDataFiles(FREELING_DATA + LANG + "/twitter/usermap.dat", FREELING_DATA + "common/punct.dat",
                FREELING_DATA + LANG + "/dicc.src", FREELING_DATA + LANG + "/afixos.dat", FREELING_DATA + LANG + "/compounds.dat",
                FREELING_DATA + LANG + "/locucions.dat", FREELING_DATA + LANG + "/np.dat", FREELING_DATA + LANG + "/quantities.dat",
                FREELING_DATA + LANG + "/probabilitats.dat");

        return macoOptions;
    }

    private void createAnalyzers() {

        tokenizer = new Tokenizer(FREELING_DATA + LANG + "/tokenizer.dat");
        splitter = new Splitter(FREELING_DATA + LANG + "/splitter.dat");

        MacoOptions macoOptions = createMacoOptions();
        maco = new Maco(macoOptions);

        tagger = new HmmTagger(FREELING_DATA + LANG + "/tagger.dat", true, 2);
        parser = new ChartParser(FREELING_DATA + LANG + "/chunker/grammar-chunk.dat");
        neclass = new Nec(FREELING_DATA + LANG + "/nerc/nec/nec-ab-rich.dat");

        probabilities = new Probabilities(FREELING_DATA + LANG + "/probabilitats.dat", 0);
        alternatives = new Alternatives(FREELING_DATA + LANG + "/alternatives-ort.dat");
    }

    // Analizador
    public ListSentence analyze(String text) {

        ListWord l = tokenizer.tokenize(text);

        ListSentence ls = splitter.split(l);
        maco.analyze(ls);
        ////
        alternatives.analyze(ls);
        probabilities.setActivateGuesser(true);
        probabilities.analyze(ls);
        /////

        tagger.analyze(ls);
        neclass.analyze(ls);
        parser.analyze(ls);

        countEagleCategories(ls);

        return ls;
    }

    public String getLemmas(ListSentence ls) {

        ListSentenceIterator sIt = new ListSentenceIterator(ls);
        String lemmas = "";

        while (sIt.hasNext()) {
            Sentence sentence = sIt.next();
            VectorWord vectorWord = sentence.getWords();
            for (int i = 0; i < vectorWord.size(); i++) {
                Word word = vectorWord.get(i);
                String lemma = word.getLemma();

                // Si la palabra no está en el diccionario, se busca una similar
                // se evitan los números (que no están en el diccionario)
                if (word.foundInDict() == false && word.getTag().charAt(0) != 'Z') {
                    ListPairStringInt alternativesList = new ListPairStringInt();
                    alternatives.getSimilarWords(word.getForm(), alternativesList);
                    if (alternativesList.size() > 0) {
                        lemma = alternativesList.front().getFirst();
                    }
                }

                lemmas += lemma + " ";
            }
        }

        return lemmas;
    }

    public ArrayList<String> getSentences(String text) {

        ListWord l = tokenizer.tokenize(text);
        ListSentence ls = splitter.split(l);

        ListSentenceIterator sIt = new ListSentenceIterator(ls);
        ArrayList<String> sentences = new ArrayList<String>();

        while (sIt.hasNext()) {
            Sentence sentence = sIt.next();
            VectorWord vectorWord = sentence.getWords();

            // get character position in the original text where sentence first word starts
            int sentenceBegin = (int) vectorWord.get(0).getSpanStart();
            // get character position in the original text where sentence last word ends
            int sentenceEnd = (int) vectorWord.get((int) (vectorWord.size() - 1)).getSpanFinish();

            // extract sentence substring from original text
            String words = text.substring(sentenceBegin, sentenceEnd);

            sentences.add(words);
        }

        return sentences;
    }

    private void addToList(List<Integer> list, int pos, int amount) {

        list.set(pos, list.get(pos) + amount);
    }

    private void resetList(List<Integer> list, int size) {

        for (int i = 0; i < size; i++)
            list.set(i, 0);
    }

    private void countEagleCategories(ListSentence ls) {

        resetList(eagleTags, EAGLE_TAGS_SIZE);
        ListSentenceIterator sIt = new ListSentenceIterator(ls);

        while (sIt.hasNext()) {
            Sentence sentences = sIt.next();
            VectorWord vectorWord = sentences.getWords();
            for (int i = 0; i < vectorWord.size(); i++) {
                Word word = vectorWord.get(i);
                char firstChar = word.getTag().charAt(0);

                switch (firstChar) {
                case 'A':
                    addToList(eagleTags, EAGLE_TAG_POS_ADJECTIVES, 1);
                    break;
                case 'R':
                    addToList(eagleTags, EAGLE_TAG_POS_ADVERBS, 1);
                    break;
                case 'D':
                    addToList(eagleTags, EAGLE_TAG_POS_DETERMINANTS, 1);
                    break;
                case 'N':
                    addToList(eagleTags, EAGLE_TAG_POS_NAMES, 1);
                    break;
                case 'V':
                    addToList(eagleTags, EAGLE_TAG_POS_VERBS, 1);
                    break;
                case 'P':
                    addToList(eagleTags, EAGLE_TAG_POS_PRONOUNS, 1);
                    break;
                case 'C':
                    addToList(eagleTags, EAGLE_TAG_POS_CONJUNCTIONS, 1);
                    break;
                case 'I':
                    addToList(eagleTags, EAGLE_TAG_POS_INTERJECTIONS, 1);
                    break;
                case 'S':
                    addToList(eagleTags, EAGLE_TAG_POS_PREPOSITIONS, 1);
                    break;
                case 'F':
                    addToList(eagleTags, EAGLE_TAG_POS_PUNCTUATION, 1);
                    break;
                case 'Z':
                    addToList(eagleTags, EAGLE_TAG_POS_NUMERALS, 1);
                    break;
                case 'W':
                    addToList(eagleTags, EAGLE_TAG_POS_DATES_AND_TIMES, 1);
                    break;
                case 'E':
                    switch (word.getTag().charAt(1)) {
                    case 'P':
                        addToList(eagleTags, EAGLE_TAG_POS_EMOTICON_POS, 1);
                        break;
                    case 'N':
                        addToList(eagleTags, EAGLE_TAG_POS_EMOTICON_NEG, 1);
                        break;
                    case '0':
                        addToList(eagleTags, EAGLE_TAG_POS_EMOTICON_NEU, 1);
                        break;
                    }
                    break;
                }
            }
        }

    }

    public int getAdjectivesCount() {

        return eagleTags.get(EAGLE_TAG_POS_ADJECTIVES);
    }

    public int getAdverbsCount() {

        return eagleTags.get(EAGLE_TAG_POS_ADVERBS);
    }

    public int getDeterminantsCount() {

        return eagleTags.get(EAGLE_TAG_POS_DETERMINANTS);
    }

    public int getNamesCount() {

        return eagleTags.get(EAGLE_TAG_POS_NAMES);
    }

    public int getVerbsCount() {

        return eagleTags.get(EAGLE_TAG_POS_VERBS);
    }

    public int getPronounsCount() {

        return eagleTags.get(EAGLE_TAG_POS_PRONOUNS);
    }

    public int getConjunctionsCount() {

        return eagleTags.get(EAGLE_TAG_POS_CONJUNCTIONS);
    }

    public int getInterjectionsCount() {

        return eagleTags.get(EAGLE_TAG_POS_INTERJECTIONS);
    }

    public int getPrepositionsCount() {

        return eagleTags.get(EAGLE_TAG_POS_PREPOSITIONS);
    }

    public int getPunctuationCount() {

        return eagleTags.get(EAGLE_TAG_POS_PUNCTUATION);
    }

    public int getNumeralsCount() {

        return eagleTags.get(EAGLE_TAG_POS_NUMERALS);
    }

    public int getDatesAndTimesCount() {

        return eagleTags.get(EAGLE_TAG_POS_DATES_AND_TIMES);
    }

    public int getEmoticonPosCount() {

        return eagleTags.get(EAGLE_TAG_POS_EMOTICON_POS);
    }

    public int getEmoticonNegCount() {

        return eagleTags.get(EAGLE_TAG_POS_EMOTICON_NEG);
    }

    public int getEmoticonNeuCount() {

        return eagleTags.get(EAGLE_TAG_POS_EMOTICON_NEU);
    }
}