package org.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.controler.WekaControler;
import org.freeling.FreelingAnalyzer;
import org.view.MainAppWindow;
import org.weka.MachineLearningClassifierTechnique;

import edu.upc.freeling.LangIdent;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Util;

public class ExecuteChatClassifier {
    
    private static final String DATA = "/opt/local/share/freeling/";
    private static final String LANG = "es";
    private static final String FREELINGDIR="/Users/martinmineo/Downloads/freeling-3.1";
    
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        ////FREELING
        FreelingAnalyzer a = new FreelingAnalyzer();
        
        System.load(FREELINGDIR + "/APIs/java/" + "libfreeling_javaAPI.dylib" );

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

        LangIdent lgid = new LangIdent(DATA + "/common/lang_ident/ident-few.dat");

        System.out.println("-------- Ingrese texto para freelingear -----------");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
        String line = input.readLine();

        String lg = lgid.identifyLanguage(line);
        System.out.println("Idioma identificado: " + lg);
        System.out.println("-------- LANG_IDENT results -----------");

        while (line != null) {
            ListWord l = a.getTk().tokenize(line);

            ListSentence ls = a.getSp().split(l, false);
            a.getMf().analyze(ls);
            a.getTg().analyze(ls);
            a.getNeclass().analyze(ls);
            a.getSen().analyze(ls);
            a.getDis().analyze(ls);
            a.getParser().analyze(ls);
            a.printResults( ls, "parsed" );
            a.getDep().analyze(ls);

            System.out.println("Ingrese nueva linea");
            line = input.readLine();
        }
        ////FREELING_END
        
        MainAppWindow view = new MainAppWindow();
        MachineLearningClassifierTechnique model = null;
        WekaControler controler = new WekaControler(model, view);

        view.setControler(controler);
        controler.initializeView();
    }

}
