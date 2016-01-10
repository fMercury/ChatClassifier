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

    private static final String DATA = "/opt/local/share/freeling/";
    private static final String LANG = "es";
    private static final String FREELINGDIR = "/Users/martinmineo/Downloads/freeling-3.1";

    String line;
    String linout;
    Tokenizer tk;
    Splitter sp;
    Maco mf;
    HmmTagger tg;
    ChartParser parser;
    DepTxala dep;
    Nec neclass;
    Senses sen;
    Ukb dis;

    public FreelingAnalyzer() {

        System.load(FREELINGDIR + "/APIs/java/" + "libfreeling_javaAPI.dylib");

        Util.initLocale("default");

        MacoOptions op = new MacoOptions(LANG);

        op.setActiveModules(false, true, true, true, true, true, true, true, true, true);

        op.setDataFiles("", 
                DATA + LANG + "/locucions.dat", 
                DATA + LANG + "/quantities.dat",
                DATA + LANG + "/afixos.dat", 
                DATA + LANG + "/probabilitats.dat", 
                DATA + LANG + "/dicc.src",
                DATA + LANG + "/np.dat", 
                DATA + "common/punct.dat");

        tk = new Tokenizer(DATA + LANG + "/tokenizer.dat");
        sp = new Splitter(DATA + LANG + "/splitter.dat");
        mf = new Maco(op);

        tg = new HmmTagger(DATA + LANG + "/tagger.dat", true, 2);
        parser = new ChartParser(DATA + LANG + "/chunker/grammar-chunk.dat");
        dep = new DepTxala(DATA + LANG + "/dep/dependences.dat", parser.getStartSymbol());
        neclass = new Nec(DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");

        sen = new Senses(DATA + LANG + "/senses.dat"); // sense dictionary
        dis = new Ukb(DATA + LANG + "/ukb.dat"); // sense disambiguator
    }

    public void setLine(String text) {
        line = text;
    }

    public void setLinOut(String text) {
        linout = text;
    }

    public String getLine() {
        String text = line;
        line = null;
        return text;
    }

    public String getLinOut() {
        String text = linout;
        linout = null;
        return text;
    }

    public String ejecutar() {
        System.out.println(line);

        ListWord l = tk.tokenize(line);
        System.out.print("|");
        ListSentence ls = sp.split(l, false);
        System.out.print("!");
        mf.analyze(ls);
        System.out.print("/");
        tg.analyze(ls);
        System.out.print("{");
        neclass.analyze(ls);
        System.out.print("[");
        sen.analyze(ls);
        System.out.print("]");
        dis.analyze(ls);
        System.out.print("�");
        parser.analyze(ls);
        System.out.print("�");
        dep.analyze(ls);
        System.out.print("?");
        printResults(ls, "dep");
        System.out.print("}");
        line = null;
        return this.getLinOut();
    }

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
        setLinOut(auxiliar);
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
    
    // Getters y setters

    public Tokenizer getTk() {
        return tk;
    }

    public void setTk(Tokenizer tk) {
        this.tk = tk;
    }

    public Splitter getSp() {
        return sp;
    }

    public void setSp(Splitter sp) {
        this.sp = sp;
    }

    public Maco getMf() {
        return mf;
    }

    public void setMf(Maco mf) {
        this.mf = mf;
    }

    public HmmTagger getTg() {
        return tg;
    }

    public void setTg(HmmTagger tg) {
        this.tg = tg;
    }

    public ChartParser getParser() {
        return parser;
    }

    public void setParser(ChartParser parser) {
        this.parser = parser;
    }

    public DepTxala getDep() {
        return dep;
    }

    public void setDep(DepTxala dep) {
        this.dep = dep;
    }

    public Nec getNeclass() {
        return neclass;
    }

    public void setNeclass(Nec neclass) {
        this.neclass = neclass;
    }

    public Senses getSen() {
        return sen;
    }

    public void setSen(Senses sen) {
        this.sen = sen;
    }

    public Ukb getDis() {
        return dis;
    }

    public void setDis(Ukb dis) {
        this.dis = dis;
    }
}
