package org.preprocessDataset;

import java.util.ArrayList;

import org.commons.Constants;
import org.weka.Weka;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Hace todo el procesado de datos relacionado con Freeling
 * @author martinmineo
 *
 */
public class Freeling {
    
    // Cantidad de atributos de Freeling
    public static final int FREELING_ATTRIBUTES = 12;

    /**
     * Devuelve en un arreglo todos los atributos de Freeling
     * @return ArrayList<Attribute> Atributos de Freeling 
     */
    public static ArrayList<Attribute> getFreelingAttributes() {

        // Atributos de freeling
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();

        attributes.add(new Attribute("adjectives"));
        attributes.add(new Attribute("adverbs"));
        attributes.add(new Attribute("determinants"));
        attributes.add(new Attribute("names"));
        attributes.add(new Attribute("verbs"));
        attributes.add(new Attribute("pronouns"));
        attributes.add(new Attribute("conjuctions"));
        attributes.add(new Attribute("interjections"));
        attributes.add(new Attribute("prepositions"));
        attributes.add(new Attribute("punctuation"));
        attributes.add(new Attribute("numerals"));
        attributes.add(new Attribute("dates_times"));

        return attributes;
    }
    
    /**
     * Devuelve un arreglo de double con la cantidad de veces que aparece cada categoría EAGLES en una interacción
     * @param values double[] Arreglo con los valores
     * @param index int Índice
     * @param freelingAnalyzer FreelingAnalyzer Analizador Freeling
     * @return double[] arreglo actualizado
     */
    private double[] getFreelingValues(double[] values, int index, FreelingAnalyzer freelingAnalyzer) {
        
        // Atributo adjectives
        values[index++] = freelingAnalyzer.getAdjectivesCount();
        // Atributo adverbs
        values[index++] = freelingAnalyzer.getAdverbsCount();
        // Atributo determinants
        values[index++] = freelingAnalyzer.getDeterminantsCount();
        // Atributo names
        values[index++] = freelingAnalyzer.getNamesCount();
        // Atributo verbs
        values[index++] = freelingAnalyzer.getVerbsCount();
        // Atributo pronouns
        values[index++] = freelingAnalyzer.getPronounsCount();
        // Atributo conjuctions
        values[index++] = freelingAnalyzer.getConjunctionsCount();
        // Atributo interjections
        values[index++] = freelingAnalyzer.getInterjectionsCount();
        // Atributo prepositions
        values[index++] = freelingAnalyzer.getPrepositionsCount();
        // Atributo punctuation
        values[index++] = freelingAnalyzer.getPunctuationCount();
        // Atributo numerals
        values[index++] = freelingAnalyzer.getNumeralsCount();
        // Atributo dates_times
        values[index++] = freelingAnalyzer.getDatesAndTimesCount();
        
        return values;
    }
    
    /**
     * Crea un nuevo archivo con el análisis de Freeling. Convierte todas las palabras a su palabra raíz (lema),
     * si una palabra no aparece en el diccionario la reemplaza por la más cercana y cuenta la cantidad de veces
     * que aparece cada categoría EAGLES en la interacción
     * @param fileName String Nombre del archivo a procesar
     * @return String Nombre del archivo donde se guardó la información procesada
     */
    public String freelingAnalisys(String fileName) {
        
        Instances dataset = Weka.loadDataset(fileName);
        FreelingAnalyzer freelingAnalyzer = FreelingAnalyzer.getInstance();

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(Weka.classConductaAttribute());
        
        // Si la cantidad de atributos es mayor que 2 el dataset incluye el nombre
        if (dataset.numAttributes() > 2) {
            Attribute attNombre = new Attribute(Weka.NOMBRE, (ArrayList<String>) null);
            attributes.add(attNombre);
        }
        
        Attribute attMensaje = new Attribute(Weka.MENSAJE, (ArrayList<String>) null);
        attributes.add(attMensaje);
        attributes.addAll(getFreelingAttributes());
        
        Instances freelingDataset = new Instances("chat", attributes, 0);

        for (int i = 0; i < dataset.numInstances(); i++) {

            Instance instance = dataset.instance(i);
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = "";
            
            if (instance.numAttributes() > 2)
                nombre = instance.stringValue(instanceIndex++);
            
            String mensaje = instance.stringValue(instanceIndex);
            mensaje = freelingAnalyzer.getLemmas(freelingAnalyzer.analyze(mensaje));
            
            int valuesIndex = 0;
            double[] values = new double[freelingDataset.numAttributes()];
            values[valuesIndex] = freelingDataset.attribute(valuesIndex++).indexOfValue(conducta);
            if (instance.numAttributes() > 2)
                values[valuesIndex] = freelingDataset.attribute(valuesIndex++).addStringValue(nombre);
            values[valuesIndex] = freelingDataset.attribute(valuesIndex++).addStringValue(mensaje);
            
            values = getFreelingValues(values, valuesIndex, freelingAnalyzer);

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(freelingDataset.attribute(0));

            freelingDataset.add(newInstance);
        }
        
        String freelingFileName = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-freeling" + Constants.ARFF_FILE;
        Weka.saveDataset(freelingDataset, freelingFileName);
        
        return freelingFileName;
    }
    
    /**
     * Separa cada sentencia en oraciones para que el procesado sea más efectivo
     * @param fileName String Nombre del archivo a procesar
     * @return String Nombre del archivo donde se guardó la información procesada 
     */
    public static String splitSentences(String fileName) {
        
        FreelingAnalyzer freelingAnalyzer = FreelingAnalyzer.getInstance();
        
        Instances dataset = Weka.loadDataset(fileName);
        Instances sentencesDataset = new Instances(dataset);
        
        sentencesDataset = new Instances(dataset,0);

        for (int i = 0; i < dataset.numInstances(); i++) {

            Instance instance = dataset.instance(i);
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = "";
            if (instance.numAttributes() > 2)
                nombre = instance.stringValue(instanceIndex++);
            String mensaje = instance.stringValue(instanceIndex);
            
            ArrayList<String> sentences = freelingAnalyzer.getSentences(mensaje);
            
            for (String sentence: sentences) {
                
                int valuesIndex = 0;
                double[] values = new double[sentencesDataset.numAttributes()];
                values[valuesIndex] = sentencesDataset.attribute(valuesIndex++).indexOfValue(conducta);
                if (instance.numAttributes() > 2)
                    values[valuesIndex] = sentencesDataset.attribute(valuesIndex++).addStringValue(nombre);
                values[valuesIndex] = sentencesDataset.attribute(valuesIndex).addStringValue(sentence);
                
                Instance newInstance = new DenseInstance(1.0, values);
                if (values[0] == -1.0)
                    newInstance.setMissing(sentencesDataset.attribute(0));
    
                sentencesDataset.add(newInstance);
            }
        }
            
        String sentencesFileName = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-sentences" + Constants.ARFF_FILE;
        Weka.saveDataset(sentencesDataset, sentencesFileName);
        
        return sentencesFileName;
    }
}
