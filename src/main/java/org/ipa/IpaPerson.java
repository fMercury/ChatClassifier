package org.ipa;

import java.util.Hashtable;

import org.enums.IpaBehavior;

/**
 * Contiene la información relacionada a una persona
 * @author martinmineo
 *
 */
public class IpaPerson  extends IpaItem {
    Hashtable<IpaBehavior, Integer> interactions;

    /**
     * Constructor
     * @param name String Nombre de la persona
     */
    public IpaPerson(String name) {
        super(name);
     
        initializeInteractions();
    }
    
    /**
     * Inicializador de 'interactions'
     */
    private void initializeInteractions() {
        
        interactions = new Hashtable<IpaBehavior, Integer>();
        for (IpaBehavior behavior : IpaBehavior.values()) {
            interactions.put(behavior, 0);
        }
    }
    
    /**
     * Agrega cierta cantidad de interacciones a la persona
     * @param behavior IpaBehavior Comportamiento
     * @param amount int Cantidad de interacciones para el comportamiento
     */
    public void addInteraction(IpaBehavior behavior, int amount) {
      
        interactions.put(behavior, interactions.get(behavior) + amount);
    }
    
    /**
     * Devuelve el total de interacciones de la persona
     * @return int Total de interacciones
     */
    public int getTotalInteractions() {
        
        int total = 0;
        
        for (IpaBehavior behavior : IpaBehavior.values()) {
            total += interactions.get(behavior);
        }
        
        return total;      
    }
    
    /**
     * Devuelve el total de interacciones de la persona para un Comportamiento determinado
     * @param behavior IpaBehavior Comportamiento
     * @return int Total de interacciones 
     */
    public int getBehaviorInteractions(IpaBehavior behavior) {
        
        return interactions.get(behavior);
    }
    
    /**
     * Devuelve el porcentaje de interacciones para un Comportamiento dado
     * @param behavior IpaBehavior Comportamiento
     * @return double Porcentaje 
     */
    private double getBehaviorPercentage(IpaBehavior behavior) {
        
        return (double)interactions.get(behavior) * 100.0 / (double)getTotalInteractions();
    }
    
    /**
     * Devuelve la desviación para un Comportamiento dado
     * @param behavior IpaBehavior Comportamiento
     * @return double Desviación
     */
    public double getBehaviorDeviation(IpaBehavior behavior) {
          
        double percentage = getBehaviorPercentage(behavior);

        double inferiorLimit = IpaEdgeValues.getInferiorLimit(behavior);
        double superiorLimit = IpaEdgeValues.getSuperiorLimit(behavior);
          
        if (percentage < inferiorLimit) {
            return percentage - inferiorLimit;
        }
        else {
            if (percentage > superiorLimit) {
                return percentage - superiorLimit;
            }
        }
        return 0;
    }
   
    /**
     * Devuelve si este objeto se debe manejar con una desviación absoluta o relativa
     * @return boolean Desviación absoluta?
     */
    @Override
	protected boolean absoluteDeviation() {
		return true;
	}
}