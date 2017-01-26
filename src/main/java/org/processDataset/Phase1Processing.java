package org.processDataset;

import java.util.ArrayList;

import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Phase1Processing extends PhaseXProcessing{
    
    public Phase1Processing(boolean useFreeling) {

        super(useFreeling);
    }

    /**
     * Devuelve los atributos que se usan en la primera fase de procesado
     * @param boolean includeName Usado cuando el dataset tiene el atributo Nombre
     * @return ArrayList<Attribute> Atributos de la primera fase
     */
    protected ArrayList<Attribute> getAttributes(boolean includeName) {

        // Atributo class_area
        Attribute attClassArea = Weka.classAreaAttribute();
        // Attributo nombre
        Attribute attNombre = new Attribute(Weka.NOMBRE, (ArrayList<String>) null);
        // Atributo mensaje
        Attribute attMensaje = new Attribute(Weka.MENSAJE, (ArrayList<String>) null);
        // Todos los atributos
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(attClassArea);
        if (includeName)
            attributes.add(attNombre);
        attributes.add(attMensaje);

        if (useFreeling) {
            attributes.addAll(Freeling.getFreelingAttributes());
        }
            
        return attributes;
    }

    /**
     * Crea el dataset para usar en la primera fase a partir de otro dataset
     * @param String fileName Nombre del archivo del dataset a convertir
     * @return String Nombre del archivo donde se guardó el dataset
     */
    protected String getDataset(String fileName) {

        Instances dataset = Weka.loadDataset(fileName);

        boolean includeName = dataset.attribute(1).name().compareTo(Weka.NOMBRE) == 0;
        ArrayList<Attribute> attributes = getAttributes(includeName);

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < dataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase1
            Instance instance = dataset.instance(i);
            
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = "";
            if (includeName)
                nombre = instance.stringValue(instanceIndex++);
            String mensaje = instance.stringValue(instanceIndex);

            int valuesIndex = 0;
            double[] values = new double[instances.numAttributes()];
            values[valuesIndex] = instances.attribute(valuesIndex++).indexOfValue(Weka.conductaToArea(conducta));
            if (includeName)
                values[valuesIndex] = instances.attribute(valuesIndex++).addStringValue(nombre);
            values[valuesIndex] = instances.attribute(valuesIndex++).addStringValue(mensaje);
            
            if (useFreeling) {
                for (int j = valuesIndex; j < valuesIndex + Freeling.FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j);
                }
            }

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));
            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-phase1" + Constants.ARFF_FILE;
        Weka.saveDataset(instances, phaseFileName);

        return phaseFileName;
    }

    /**
     * No es usado en el procesado de la primera fase
     */
    protected String getTrainDataset(String fileName, boolean includeName, String fileNameSufix) {
        // TODO Auto-generated method stub
        return null;
    }
}
