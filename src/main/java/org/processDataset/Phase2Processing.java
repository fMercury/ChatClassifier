package org.processDataset;

import java.util.ArrayList;

import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Crea los datos necesarios para el procesado de la Fase 2
 * @author martinmineo
 *
 */
public class Phase2Processing extends PhaseXProcessing{

    /**
     * Constructor
     * @param useFreeling boolean Determina si se hará un procesado utilizando Freeling o no
     */
    public Phase2Processing(boolean useFreeling) {
        super(useFreeling);
    }

    /**
     * Devuelve los atributos que se usan en la segunda fase de procesado
     * @param boolean includeName Usado cuando el dataset tiene el atributo Nombre
     * @return ArrayList<Attribute> Atributos de la segunda fase
     */
    protected ArrayList<Attribute> getAttributes(boolean includeName) {

        // Atributo class_reaccion
        Attribute attClassReaccion = Weka.classReaccionAttribute();
        // Atributo class_area
        Attribute attClassArea = Weka.classAreaAttribute();
        // Attributo nombre
        Attribute attNombre = new Attribute(Weka.NOMBRE, (ArrayList<String>) null);
        // Atributo mensaje
        Attribute attMensaje = new Attribute(Weka.MENSAJE, (ArrayList<String>) null);
        // Todos los atributos
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(attClassReaccion);
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
     * Crea el dataset para usar en la segunda fase a partir de otro dataset
     * @param String fileName Nombre del archivo del dataset a convertir
     * @return String Nombre del archivo donde se guardó el dataset
     */
    protected String getDataset(String fileName) {

        Instances dataset = Weka.loadDataset(fileName);
        
        boolean includeName = false;
        if (useFreeling) {
            includeName = dataset.attribute(2).name().compareTo(Weka.NOMBRE)==0;
        }
        
        ArrayList<Attribute> attributes = getAttributes(includeName);

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < dataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase2
            Instance instance = dataset.instance(i);
            
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = "";
            if (includeName)
                nombre = instance.stringValue(instanceIndex++);
            String mensaje = instance.stringValue(instanceIndex);

            int valuesIndex = 0;
            double[] values = new double[instances.numAttributes()];
            values[valuesIndex] = instances.attribute(valuesIndex++).indexOfValue(Weka.conductaToReaccion(conducta));
            values[valuesIndex] = instances.attribute(valuesIndex++).indexOfValue(Weka.conductaToArea(conducta));
            if (includeName)
                values[valuesIndex] = instances.attribute(valuesIndex++).addStringValue(nombre);
            values[valuesIndex] = instances.attribute(valuesIndex++).addStringValue(mensaje);
            
            if (useFreeling) {
                for (int j = valuesIndex; j < valuesIndex + Freeling.FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j-1);
                }
            }

            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));
            if (values[1] == -1.0)
                newInstance.setMissing(instances.attribute(1));

            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-phase2" + Constants.ARFF_FILE;
        Weka.saveDataset(instances, phaseFileName);

        return phaseFileName;
    }
    
    /**
     * Devuelve el dataset para entrenar en la segunda fase
     * @param fileName Nombre del dataset a convertir para usar en la segunda fase
     * @param boolean includeName 
     * @return Nombre del archivo del dataset de entrenamiento
     */
    public String getTrainDataset(String fileName, boolean includeName, String fileNameSufix) {

        Instances dataset = Weka.loadDataset(fileName);
        ArrayList<Attribute> attributes = getAttributes(includeName);

        Instances instances = new Instances("chat", attributes, 0);

        for (int i = 0; i < dataset.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase2
            Instance instance = dataset.instance(i);
            int instanceIndex = 0;
            String classArea = instance.stringValue(instanceIndex++);
            String nombre = "";
            if (includeName)
                nombre = instance.stringValue(instanceIndex++);
            String mensaje = instance.stringValue(instanceIndex);

            int valueIndex = 0;
            double[] values = new double[instances.numAttributes()];
            values[valueIndex++] = -1;
            values[valueIndex] = instances.attribute(valueIndex++).indexOfValue(classArea);
            if (includeName)
                values[valueIndex] = instances.attribute(valueIndex++).addStringValue(nombre);
            values[valueIndex] = instances.attribute(valueIndex++).addStringValue(mensaje);

            if (useFreeling) {
                for (int j = valueIndex; j < valueIndex + Freeling.FREELING_ATTRIBUTES; j++) {     
                    values[j] = instance.value(j-1);
                }
            }
            
            Instance newInstance = new DenseInstance(1.0, values);
            if (values[0] == -1.0)
                newInstance.setMissing(instances.attribute(0));

            instances.add(newInstance);
        }

        String phaseFileName = fileName.substring(0, fileName.lastIndexOf("-labeled-phase1")) + "-phase2" + fileNameSufix + Constants.ARFF_FILE;
        Weka.saveDataset(instances, phaseFileName);

        return phaseFileName;
    }
}
