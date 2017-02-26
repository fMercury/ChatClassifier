package org.preprocessDataset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.upc.freeling.Alternatives;
import edu.upc.freeling.ChartParser;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.ListPairStringInt;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.Probabilities;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.Util;
import edu.upc.freeling.VectorWord;
import edu.upc.freeling.Word;
import edu.upc.freeling.Word.Modules;

/**
 * Clase principal de Freeling encargada de hacer todo el procesado de los datos
 * @author martinmineo
 *
 */
public class FreelingAnalyzer {

    private static FreelingAnalyzer instance;

    private static final String LANG = "es";
    private static final String FREELING_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "freeling" + File.separator;
    private static final String FREELING_DATA = FREELING_DIR + "data" + File.separator;

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

    /**
     * Constructor. Se hace la carga de las librerias correspondientes
     */
    private FreelingAnalyzer() {

        String userDir = System.getProperty("user.dir");
        String os = System.getProperty("os.name");

        if (os.toLowerCase().contains("windows"))
        	System.load(userDir + File.separator + FREELING_DIR + "freeling_javaAPI.dll");
        else
          	System.load(userDir + File.separator + FREELING_DIR + "libfreeling_javaAPI.dylib");

        Util.initLocale("default");

        eagleTags = new ArrayList<Integer>(EAGLE_TAGS_SIZE);
        for (int i = 0; i < EAGLE_TAGS_SIZE; i++) {
            eagleTags.add(i, new Integer(0));
        }

        createAnalyzers();
    }

    /**
     * Devuelve una instancia de la clase
     * @return FreelingAnalyser Instancia de la clase
     */
    public static FreelingAnalyzer getInstance() {

        if (instance == null)
            instance = new FreelingAnalyzer();
        return instance;
    }

    /**
     * Crea y devuelve las opciones Maco (analizador morfológico) 
     * @return MacoOptions Opciones Maco
     */
    private MacoOptions createMacoOptions() {

        macoOptions = new MacoOptions(LANG);

        macoOptions.setDataFiles(FREELING_DATA + LANG + File.separator + "twitter" + File.separator + "usermap.dat", FREELING_DATA + "common" + File.separator + "punct.dat",
                FREELING_DATA + LANG + File.separator + "dicc.src", FREELING_DATA + LANG + File.separator + "afixos.dat", FREELING_DATA + LANG + File.separator + "compounds.dat",
                FREELING_DATA + LANG + File.separator + "locucions.dat", FREELING_DATA + LANG + File.separator + "np.dat", FREELING_DATA + LANG + File.separator + "quantities.dat",
                FREELING_DATA + LANG + File.separator + "probabilitats.dat");

        return macoOptions;
    }

    /**
     * Crea todos los analizadores que la herramienta utilizará
     */
    private void createAnalyzers() {

        tokenizer = new Tokenizer(FREELING_DATA + LANG + File.separator + "tokenizer.dat");
        splitter = new Splitter(FREELING_DATA + LANG + File.separator + "splitter.dat");

        MacoOptions macoOptions = createMacoOptions();
        maco = new Maco(macoOptions);

        tagger = new HmmTagger(FREELING_DATA + LANG + File.separator + "tagger.dat", true, 2);
        parser = new ChartParser(FREELING_DATA + LANG + File.separator + "chunker" + File.separator + "grammar-chunk.dat");
        neclass = new Nec(FREELING_DATA + LANG + File.separator + "nerc" + File.separator + "nec" + File.separator + "nec-ab-rich.dat");

        probabilities = new Probabilities(FREELING_DATA + LANG + File.separator + "probabilitats.dat", 0);
        alternatives = new Alternatives(FREELING_DATA + LANG + File.separator + "alternatives-ort.dat");
    }

    /**
     * Analiza un texto y devuelve la lista de sentencias
     * @param text String Texto
     * @return ListSentence Lista de sentencias analizadas
     */
    public ListSentence analyze(String text) {

        ListWord l = tokenizer.tokenize(text);

        ListSentence ls = splitter.split(l);
        
        maco.analyze(ls);
        alternatives.analyze(ls);
        probabilities.setActivateGuesser(true);
        probabilities.analyze(ls);

        tagger.analyze(ls);
        neclass.analyze(ls);
        parser.analyze(ls);

        countEagleCategories(ls);

        return ls;
    }
    
    /**
     * Devuelve si una palabra es encontrada en el diccionario
     * @param word Palabra
     * @return boolean true si la palabra es encontrada en el diccionario, false si no
     */
    private boolean findInDictionary(Word word) {
        
        // Si es un número no se debe buscar en el diccionario
        if (word.getTag().charAt(0) == 'Z')
            return false;
        // Si es un signo de puntuación no se debe buscar en el diccionario
        if (word.getTag().charAt(0) == 'F')
            return false;
        // Si es un signo de puntuación no se debe buscar en el diccionario
        if (word.getTag().charAt(0) == 'W')
            return false;
        
        return true;
    }

    /**
     * Devuelve los lemas de una lista de sentencias
     * @param ls ListSentence Lista de sentencias
     * @return String Lemas de la lista de sentencias
     */
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
                boolean findInDictionary = findInDictionary(word);
                if (findInDictionary && word.isAnalyzedBy(Modules.DICTIONARY.swigValue()) == false) {
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

    /**
     * Devuelve las sentencias de un texto
     * @param text String texto
     * @return ArrayList<String> Sentencias
     */
    public ArrayList<String> getSentences(String text) {

        ListWord l = tokenizer.tokenize(text);
        ListSentence ls = splitter.split(l);

        ListSentenceIterator sIt = new ListSentenceIterator(ls);
        ArrayList<String> sentences = new ArrayList<String>();

        while (sIt.hasNext()) {
            Sentence sentence = sIt.next();
            VectorWord vectorWord = sentence.getWords();

            int sentenceBegin = (int) vectorWord.get(0).getSpanStart();
            int sentenceEnd = (int) vectorWord.get((int) (vectorWord.size() - 1)).getSpanFinish();

            String words = text.substring(sentenceBegin, sentenceEnd);

            sentences.add(words);
        }

        return sentences;
    }

    /**
     * Agrega a la lista la cantidad de categorías EAGLES en la posición especificada
     * @param list Lista
     * @param pos Posición (categoría EAGLES)
     * @param amount Cantidad
     */
    private void addToList(List<Integer> list, int pos, int amount) {

        list.set(pos, list.get(pos) + amount);
    }

    /**
     * Vuelve a cero los elementos de la lista
     * @param list Lista
     * @param size Tamaño
     */
    private void resetList(List<Integer> list, int size) {

        for (int i = 0; i < size; i++)
            list.set(i, 0);
    }

    /**
     * Cuenta la apariencia de cada categoría EAGLES
     * @param ls Lista
     */
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
                }
            }
        }
    }

    /**
     * Devuelve la cantidad de Adjetivos contados
     * @return int Cantidad de Adjetivos contados
     */
    public int getAdjectivesCount() {

        return eagleTags.get(EAGLE_TAG_POS_ADJECTIVES);
    }

    /**
     * Devuelve la cantidad de Advervios contados
     * @return int Cantidad de Advervios contados
     */
    public int getAdverbsCount() {

        return eagleTags.get(EAGLE_TAG_POS_ADVERBS);
    }

    /**
     * Devuelve la cantidad de Determinantes contados
     * @return int Cantidad de Determinantes contados
     */
    public int getDeterminantsCount() {

        return eagleTags.get(EAGLE_TAG_POS_DETERMINANTS);
    }

    /**
     * Devuelve la cantidad de Nombres propios contados
     * @return int Cantidad de Nombres propios contados
     */
    public int getNamesCount() {

        return eagleTags.get(EAGLE_TAG_POS_NAMES);
    }

    /**
     * Devuelve la cantidad de Vervos contados
     * @return int Cantidad de Vervos contados
     */
    public int getVerbsCount() {

        return eagleTags.get(EAGLE_TAG_POS_VERBS);
    }

    /**
     * Devuelve la cantidad de Pronombres contados
     * @return int Cantidad de Pronombres contados
     */
    public int getPronounsCount() {

        return eagleTags.get(EAGLE_TAG_POS_PRONOUNS);
    }

    /**
     * Devuelve la cantidad de Conjunciones contadas
     * @return int Cantidad de Conjunciones contadas
     */
    public int getConjunctionsCount() {

        return eagleTags.get(EAGLE_TAG_POS_CONJUNCTIONS);
    }

    /**
     * Devuelve la cantidad de Interjecciones contadas
     * @return int Cantidad de Interjecciones contadas
     */
    public int getInterjectionsCount() {

        return eagleTags.get(EAGLE_TAG_POS_INTERJECTIONS);
    }

    /**
     * Devuelve la cantidad de Preposiciones contadas
     * @return int Cantidad de Preposiciones contadas
     */
    public int getPrepositionsCount() {

        return eagleTags.get(EAGLE_TAG_POS_PREPOSITIONS);
    }

    /**
     * Devuelve la cantidad de signos de puntiación contados
     * @return int Cantidad de signos de puntuación contados
     */
    public int getPunctuationCount() {

        return eagleTags.get(EAGLE_TAG_POS_PUNCTUATION);
    }

    /**
     * Devuelve la cantidad de Números contados
     * @return int Cantidad de Números contados
     */
    public int getNumeralsCount() {

        return eagleTags.get(EAGLE_TAG_POS_NUMERALS);
    }

    /**
     * Devuelve la cantidad de Fechas contadas
     * @return int Cantidad de Fechas contadas
     */
    public int getDatesAndTimesCount() {

        return eagleTags.get(EAGLE_TAG_POS_DATES_AND_TIMES);
    }
}