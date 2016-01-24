package org.freeling;

import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.Depnode;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.ListWordIterator;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.TreeDepnode;
import edu.upc.freeling.TreeNode;
import edu.upc.freeling.Ukb;
import edu.upc.freeling.Util;
import edu.upc.freeling.Word;

public class FreelingAnalyzer {

    private static final String LANG = "es";
    private static final String FREELING_DIR = "src/main/resources/freeling/";
    private static final String FREELING_DATA = FREELING_DIR + "data/";

    private MacoOptions op;
    private Tokenizer tk;
    private Splitter sp;
    private Maco mf;
    private HmmTagger tg;
    private ChartParser parser;
    private DepTxala dep;
    private Nec neclass;
    private Senses sen;
    private Ukb dis;

    public FreelingAnalyzer() {

        String userDir = System.getProperty("user.dir");
        System.load(userDir + "/" + FREELING_DIR + "libfreeling_javaAPI.dylib");

        Util.initLocale("default");

        createMacoOptions();
        createAnalyzers();
    }
    
    private void createMacoOptions() {
        
        op = new MacoOptions(LANG);
        op.setActiveModules(false, true, true, true, true, true, true, true, true, true);
        op.setDataFiles(
                "", 
                FREELING_DATA + LANG + "/locucions.dat", 
                FREELING_DATA + LANG + "/quantities.dat",
                FREELING_DATA + LANG + "/afixos.dat", 
                FREELING_DATA + LANG + "/probabilitats.dat", 
                FREELING_DATA + LANG + "/dicc.src",
                FREELING_DATA + LANG + "/np.dat", 
                FREELING_DATA + "common/punct.dat");
    }
    
    private void createAnalyzers() {
        
        tk = new Tokenizer(FREELING_DATA + LANG + "/tokenizer.dat");
        sp = new Splitter(FREELING_DATA + LANG + "/splitter.dat");
        mf = new Maco(op);

        tg = new HmmTagger(FREELING_DATA + LANG + "/tagger.dat", true, 2);
        parser = new ChartParser(FREELING_DATA + LANG + "/chunker/grammar-chunk.dat");
        dep = new DepTxala(FREELING_DATA + LANG + "/dep/dependences.dat", parser.getStartSymbol());
        neclass = new Nec(FREELING_DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");

        sen = new Senses(FREELING_DATA + LANG + "/senses.dat"); // sense dictionary
        dis = new Ukb(FREELING_DATA + LANG + "/ukb.dat"); // sense disambiguator
    }
    
    //////////// otro metodo e impresion ////////
    public void freelingParser(String line) {
        
        ListSentence ls = analyzeSentence(line);
        
        ListSentenceIterator sIt = new ListSentenceIterator(ls);
        while (sIt.hasNext()) {
            Sentence s = sIt.next();
            TreeDepnode tree = s.getDepTree();
            printDepTree(tree);
        }
    }
    
    ///////////////////////////////////////////////

    // Analizadores
    
    public ListSentence analyzeSentence(String sentence) {
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

    public ListWord tokenize(String line) {
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
            System.out.print("  ");
        }

        nch = tr.numChildren();

        if (nch == 0) {
            if (tr.getInfo().isHead()) {
                System.out.print("+");
            }
            w = tr.getInfo().getWord();
            System.out.print("(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
            printSenses(w);
            System.out.println(")");
        } else {
            if (tr.getInfo().isHead()) {
                System.out.print("+");
            }

            System.out.println(tr.getInfo().getLabel() + "_[");

            for (int i = 0; i < nch; i++) {
                child = tr.nthChildRef(i);

                if (child != null) {
                    printParseTree(depth + 1, child);
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }
            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }

            System.out.println("]");
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
        for(int i = 0; i < level; i++) System.out.print(" ");
        Word w = deptree.getInfo().getWord();
        System.out.print(
                "(" + w.getForm() + " " + w.getLemma() + " '" + w.getTag()+"'");
        System.out.print(")");
        for(int i = 0 ; i < deptree.numChildren() ; i++){
            TreeDepnode child = deptree.nthChildRef(i);
            if(child != null){
                printDepTree(child, ++level);
            }
        }
        
    }

}
