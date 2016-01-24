package org.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.controler.WekaControler;
import org.freeling.FreelingAnalyzer;
import org.view.MainAppWindow;
import org.weka.MachineLearningClassifierTechnique;

import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListWord;

public class ExecuteChatClassifier {
    
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        ////FREELING
        FreelingAnalyzer freelingAnalyzer = new FreelingAnalyzer();

        System.out.println("-------- Ingrese texto para freelingear -----------");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
        String line = input.readLine();

        System.out.println("-------- LANG_IDENT results -----------");

        while (line != null) {
            ListWord l = freelingAnalyzer.getTk().tokenize(line);

            ListSentence ls = freelingAnalyzer.getSp().split(l, false);
            freelingAnalyzer.getMf().analyze(ls);
            freelingAnalyzer.getTg().analyze(ls);
            freelingAnalyzer.getNeclass().analyze(ls);
            freelingAnalyzer.getSen().analyze(ls);
            freelingAnalyzer.getDis().analyze(ls);
            freelingAnalyzer.getParser().analyze(ls);
            freelingAnalyzer.getDep().analyze(ls);
            
            System.out.println("===================== Print parsed:");
            freelingAnalyzer.printResults( ls, "parsed" );
            System.out.println("===================== Print dep:");
            freelingAnalyzer.printResults( ls, "dep" );
            System.out.println("===================== Print other:");
            freelingAnalyzer.printResults( ls, "" );
            
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
