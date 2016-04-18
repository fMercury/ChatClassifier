package org.freeling;

import java.util.ArrayList;
import java.util.List;

import edu.upc.freeling.Alternatives;
import edu.upc.freeling.ChartParser;
import edu.upc.freeling.Depnode;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.ListPairStringInt;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.ListWordIterator;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.PairStringInt;
import edu.upc.freeling.Probabilities;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.TreeDepnode;
import edu.upc.freeling.TreeNode;
import edu.upc.freeling.Ukb;
import edu.upc.freeling.Util;
import edu.upc.freeling.VectorWord;
import edu.upc.freeling.Word;

public class FreelingAnalyzer {
    
    private static FreelingAnalyzer instance;

    private static final String LANG = "es";
    private static final String FREELING_DIR = "src/main/resources/freeling/";
    private static final String FREELING_DATA = FREELING_DIR + "data/";

    private MacoOptions macoOptions;
    private Tokenizer tokenizer;
    private Splitter splitter;
    private Maco maco;
    private HmmTagger tagger;
    private ChartParser parser;
    private Nec neclass;
//    private DepTxala dep;
//    private Senses sen;
//    private Ukb disambiguator;
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
        
        createMacoOptions();
        createAnalyzers();
    }
    
    public static FreelingAnalyzer getInstance() {
        
        if (instance == null)
            instance = new FreelingAnalyzer();
        return instance;
    }
    
    private void createMacoOptions() {
        
        macoOptions = new MacoOptions(LANG);
//        macoOptions.setActiveModules(false, true, true, true, true, true, true, true, true, true);
        macoOptions.setUserMap(false);
        macoOptions.setAffixAnalysis(true);
        macoOptions.setMultiwordsDetection(true);
        macoOptions.setNumbersDetection(true);
        macoOptions.setPunctuationDetection(true);
        macoOptions.setDatesDetection(true);
        macoOptions.setQuantitiesDetection(true);
        macoOptions.setDictionarySearch(true);
        macoOptions.setProbabilityAssignment(true);
        macoOptions.setNERecognition(true);

        macoOptions.setDataFiles("", 
                FREELING_DATA + LANG + "/locucions.dat", 
                FREELING_DATA + LANG + "/quantities.dat",
                FREELING_DATA + LANG + "/afixos.dat", 
                FREELING_DATA + LANG + "/probabilitats.dat", 
                FREELING_DATA + LANG + "/dicc.src",
                FREELING_DATA + LANG + "/np.dat", 
                FREELING_DATA + "common/punct.dat");
    }
    
    private void createAnalyzers() {
        
        tokenizer = new Tokenizer(FREELING_DATA + LANG + "/tokenizer.dat");
        splitter = new Splitter(FREELING_DATA + LANG + "/splitter.dat");
        maco = new Maco(macoOptions);

        tagger = new HmmTagger(FREELING_DATA + LANG + "/tagger.dat", true, 2);
        parser = new ChartParser(FREELING_DATA + LANG + "/chunker/grammar-chunk.dat");
        neclass = new Nec(FREELING_DATA + LANG + "/nerc/nec/nec-ab-rich.dat");
        
        probabilities = new Probabilities(FREELING_DATA + LANG + "/probabilitats.dat", 0);
        alternatives = new Alternatives(FREELING_DATA + LANG + "/alternatives-ort.dat");

//        dep = new DepTxala(FREELING_DATA + LANG + "/dep/dependences.dat", parser.getStartSymbol());
//        sen = new Senses(FREELING_DATA + LANG + "/senses.dat"); // sense dictionary
//        disambiguator = new Ukb(FREELING_DATA + LANG + "/ukb.dat"); // sense disambiguator
    }
    
 
    // Analizador
    public ListSentence analyze(String text) {
        
        ListWord l = tokenizer.tokenize(text);

        ListSentence ls = splitter.split(l, true);
        maco.analyze(ls);
        ////
        alternatives.analyze(ls);
        probabilities.setActivateGuesser(true);
        probabilities.analyze(ls);
        /////
        
        tagger.analyze(ls);
        neclass.analyze(ls);
//        disambiguator.analyze(ls);
        parser.analyze(ls);
        
        countEagleCategories(ls);
        
        return ls;
    }
    
    public String getLemmas(ListSentence ls){
        
        ListSentenceIterator sIt = new ListSentenceIterator(ls);
        String lemmas = "";
        
        while (sIt.hasNext()) {
            Sentence sentences = sIt.next();
            VectorWord vectorWord = sentences.getWords();
            for (int i = 0; i < vectorWord.size(); i++) {
                Word word = vectorWord.get(i);
                lemmas += word.getLemma() + " ";
                
                if (word.foundInDict() == false) {
//                    System.out.println("Palabra no encontrada en el dic: " + word.getForm());
                    ListPairStringInt l = word.getAlternatives();
                    while (l.size() > 0) {
                        PairStringInt element = l.front();
                        String altString = element.getFirst();
                        int altInt = element.getSecond();
                        System.out.println(altString + ", " + altInt);
                        
                        l.popFront();
                    }
                }
                
            }
        }
        
        return lemmas;
    }
    
    private void addToList(List<Integer> list, int pos, int amount) {
        
        list.set(pos, list.get(pos) + amount);
    }
    
    private void resetList(List<Integer> list, int size) {
        
        for (int i = 0; i < size; i++)
            list.set(i, 0);
    }
    
    public void countEagleCategories(ListSentence ls) {
        
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
    
    
    // Imprimir resultados
    
    private void printSenses(Word w) {
        String ss = w.getSensesString();
        System.out.print(" " + ss);
    }
    
    public void printResults(ListSentence ls, String format) {

        String auxiliar = "";
        if (format == "parsed") {

            ListSentenceIterator sIt = new ListSentenceIterator(ls);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();
                TreeNode tree = s.getParseTree();
                printParseTree(0, tree);
            }
        } else if (format == "dep") {

            ListSentenceIterator sIt = new ListSentenceIterator(ls);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();
                TreeDepnode tree = s.getDepTree();
                printDepTree(0, tree);
            }
        } else {
            System.out.println("-------- TAGGER results -----------");

            ListSentenceIterator sIt = new ListSentenceIterator(ls);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();
                ListWordIterator wIt = new ListWordIterator(s);

                while (wIt.hasNext()) {
                    Word w = wIt.next();

                    if (w.getTag().length() > 2) {
                        System.out.print((w.getTag()).charAt(0) + "" + (w.getTag()).charAt(1) + " - ");
                        auxiliar = auxiliar.concat((w.getTag()).charAt(0) + "" + (w.getTag()).charAt(1) + " ");
                    }
                }
                System.out.println();
                System.out.println();
            }
        }
    }

    private void printParseTree(int depth, TreeNode tr) {
        Word w;
        TreeNode child;
        long nch;

        for (int i = 0; i < depth; i++) {
//            System.out.print("  ");
        }

        nch = tr.numChildren();

        if (nch == 0) {
            if (tr.getInfo().isHead()) {
//                System.out.print("+");
            }
            w = tr.getInfo().getWord();
            System.out.print("(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
            printSenses(w);
            System.out.println(")");
        } else {
            if (tr.getInfo().isHead()) {
//                System.out.print("+");
            }

//            System.out.println(tr.getInfo().getLabel() + "_[");

            for (int i = 0; i < nch; i++) {
                child = tr.nthChildRef(i);

                if (child != null) {
                    printParseTree(depth + 1, child);
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }
            for (int i = 0; i < depth; i++) {
//                System.out.print("  ");
            }

//            System.out.println("]");
        }
    }

    private void printDepTree(int depth, TreeDepnode tr) {
        TreeDepnode child = null;
        TreeDepnode fchild = null;
        Depnode childnode;
        long nch;
        int last, min;
        Boolean trob;

        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }

        System.out.print(tr.getInfo().getLinkRef().getInfo().getLabel() + "/" + tr.getInfo().getLabel() + "/");

        Word w = tr.getInfo().getWord();

        System.out.println(w.getTag().charAt(0));

        nch = tr.numChildren();

        if (nch > 0) {
            for (int i = 0; i < nch; i++) {
                child = tr.nthChildRef(i);
                if (child != null) {
                    if (!child.getInfo().isChunk()) {
                        if (depth == 0) {
                            if (child.getInfo().getWord().getTag().length() > 2) {
                                printDepTree(depth + 1, child);
                            }
                        }
                    }
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }

            last = 0;
            trob = true;

            while (trob) {
                trob = false;
                min = 9999;

                for (int i = 0; i < nch; i++) {
                    child = tr.nthChildRef(i);
                    childnode = child.getInfo();

                    if (childnode.isChunk()) {
                        if ((childnode.getChunkOrd() > last) && (childnode.getChunkOrd() < min)) {
                            min = childnode.getChunkOrd();
                            fchild = child;
                            trob = true;
                        }
                    }
                }
                if (trob && (child != null)) {

                    if (depth == 0) {
                        if (fchild.getInfo().getWord().getTag().length() > 2) {
                            printDepTree(depth + 1, fchild);
                        }
                    }
                }

                last = min;
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
        }
    }

    public static void printDepTree(TreeDepnode deptree){
        printDepTree(deptree, 0);
    }

    public static void printDepTree(TreeDepnode deptree, int level){
        
        System.out.println("");
        for(int i = 0; i < level; i++)
            System.out.print(" ");
        Word w = deptree.getInfo().getWord();
        
        System.out.print("(" + w.getForm() + " " + w.getLemma() + " '" + w.getTag()+"')");

        for(int i = 0 ; i < deptree.numChildren() ; i++){
            TreeDepnode child = deptree.nthChildRef(i);
            if(child != null){
                printDepTree(child, ++level);
            }
        }
        
    }
}

/*   private ListSentence analyzeSentence(String sentence) {
// tokenize
ListWord l = tk.tokenize(sentence);
// split sentences
ListSentence ls = sp.split(l, false);
// morphological analysis
mf.analyze(ls);                    
// PoS tagging
tg.analyze(ls);                       
//Disambiguator
//dis.analyze(ls);
// Chunk parser
parser.analyze(ls);
// Dependency parser
dep.analyze(ls);
return ls;
}

private ListWord tokenize(String line) {
return tk.tokenize(line);
}

public ListSentence split(ListWord l, Boolean b) {
return sp.split(l, b); 
}

public void analyzeMaco(ListSentence ls) {
mf.analyze(ls);
}

public void analyzeHmmTagger(ListSentence ls) {
tg.analyze(ls);
}

public void analyzeChartParser(ListSentence ls) {
parser.analyze(ls);
}

public void analyzeDepTxala(ListSentence ls) {
dep.analyze(ls);
}

public void analyzeNec(ListSentence ls) {
neclass.analyze(ls);
}

public void analyzeSenses(ListSentence ls) {
sen.analyze(ls);
}

public void analyzeUkb(ListSentence ls) {
dis.analyze(ls);
}
*/

//////////// otro metodo e impresion ////////
/*  public void freelingParser(String line) {
    
    ListSentence ls = analyzeSentence(line);
    
    ListSentenceIterator sIt = new ListSentenceIterator(ls);
    while (sIt.hasNext()) {
        Sentence s = sIt.next();
        TreeDepnode tree = s.getDepTree();
        printDepTree(tree);
    }
}
*/
///////////////////////////////////////////////
