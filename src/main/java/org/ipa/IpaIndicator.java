package org.ipa;

import org.enums.IpaBehavior;

/**
 * Contiene los indicadores de Comportamiento
 * @author martinmineo
 *
 */
public class IpaIndicator {	

	private IpaBehavior behavior;
	private int inferiorLimit;
	private int superiorLimit;
	
	/**
	 * Constructor
	 * @param behavior IpaBehavior Comportamiento
	 * @param inferiorLimit int Límite inferior
	 * @param superiorLimit int Límite superior
	 */
	public IpaIndicator(IpaBehavior behavior, int inferiorLimit, int superiorLimit) {
		
		this.behavior = behavior;
		this.inferiorLimit = inferiorLimit;
		this.superiorLimit = superiorLimit;
	}
	
	/**
	 * Devuelve el Comportamiento
	 * @return IpaBehavior Comportamiento
	 */
	public IpaBehavior getBehavior() {
	    return behavior;
	}
	
	/**
	 * Devuelve el límite interior
	 * @return int Límite inferior
	 */
	public int getInferiorLimit() {
		return inferiorLimit;
	}

	/**
	 * Devuelve el límite superior
	 * @return int Límite superior
	 */
	public int getSuperiorLimit() {
		return superiorLimit;
	}	
}
