package org.main;

import org.controler.Controller;
import org.view.MainAppWindow;
import org.weka.MachineLearningClassifierTechnique;

public class ExecuteChatClassifier {
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        MainAppWindow view = new MainAppWindow();
        MachineLearningClassifierTechnique model = null;
        Controller controller = new Controller(model, view);

        view.setControler(controller);
        controller.initializeView();
    }
    
 /*   public void freeling() throws IOException {
    ////FREELING
        FreelingAnalyzer freelingAnalyzer = new FreelingAnalyzer();

        System.out.println("-------- Ingrese texto para freelingear -----------");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
//        String line = input.readLine();
        String line = "El gato come pescado. El Sr. García pasea al perro.";
        System.out.println(line);

//        while (line != null) {
            ListWord l = freelingAnalyzer.tokenize(line);

            ListSentence ls = freelingAnalyzer.split(l, false);
            freelingAnalyzer.analyzeMaco(ls);
//            freelingAnalyzer.analyzeSenses(ls);
            freelingAnalyzer.analyzeHmmTagger(ls);
            freelingAnalyzer.analyzeNec(ls);
            freelingAnalyzer.analyzeUkb(ls);
            freelingAnalyzer.analyzeChartParser(ls);
//            freelingAnalyzer.analyzeDepTxala(ls);
            
            System.out.println("=========== Print parsed: ===========");
            freelingAnalyzer.printResults( ls, "parsed" );
//            System.out.println("=========== Print dep: ===========");
//            freelingAnalyzer.printResults( ls, "dep" );
//            System.out.println("=========== Print other: ===========");
//            freelingAnalyzer.printResults( ls, "" );
            
//            freelingAnalyzer.freelingParser(line);
            
            System.out.println();
//            System.out.println("Ingrese nueva linea");
            line = input.readLine();
            
//            if (line.equals("null"))
//                line = null;
//        }
        ////FREELING_END
    }
*/
}
