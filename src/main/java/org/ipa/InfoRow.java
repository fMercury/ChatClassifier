package org.ipa;

/**
 * Clase que reune código en común para las clases AnalysisResult y GroupResult
 * @author martinmineo
 *
 */
public class InfoRow {

    /**
     * Pinta un Object[] del color deseado
     * @param objects Object[] Texto a colorear
     * @param color String Color que se utilizará para colorear
     * @return Object[] Texto coloreado
     */
    protected Object[] paintIt(Object[] objects, String color) {
        
        Object[] newObjects = objects;
        for (int i = 0; i < newObjects.length; i++) {
            newObjects[i] = "<html><b style=\"color:" + color + "\">" + newObjects[i] + "</b><html>";
        }

        return newObjects;
    }
}
