package org.ipa;

import org.enums.IpaBehavior;

/**
 * Clase abstracta que reune funcionalidad para las clases IpaPerson e IpaGroup
 * @author martinmineo
 *
 */
public abstract class IpaItem {
	private String name;
	
	/**
	 * Constructor
	 * @param name String Nombre
	 */
	public IpaItem(String name) {
		
		this.name = name;
	}
	
	/**
	 * Devuelve el nombre del grupo o la persona
	 * @return String Nombre
	 */
	public String getName() {
		return name;
	}
	
	public abstract int getBehaviorInteractions(IpaBehavior behavior);
	public abstract double getBehaviorDeviation(IpaBehavior behavior);
	protected abstract boolean absoluteDeviation();
	
	/**
	 * Devuelve la desviación total del grupo o de la persona
	 * @return double Desviación total
	 */
    public double getTotalDeviation() {
    	  
        double totalDeviation = 0;
    
        for(IpaBehavior behavior : IpaBehavior.values()){
        	if (absoluteDeviation())
        		totalDeviation += Math.abs(getBehaviorDeviation(behavior));
        	else
        		totalDeviation += getBehaviorDeviation(behavior);
        }
  
        return totalDeviation;
    }   
}
