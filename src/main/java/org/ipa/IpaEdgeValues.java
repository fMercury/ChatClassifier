package org.ipa;

import java.util.Hashtable;

import org.enums.IpaBehavior;

/**
 * Contiene los valores límites de IPA 
 * @author martinmineo
 *
 */
public class IpaEdgeValues {

	private static Hashtable<IpaBehavior, IpaIndicator> indicators;
	
	/**
	 * Inicializador
	 */
	private static void initialize() {
		
		indicators = new Hashtable<IpaBehavior, IpaIndicator>();
		
		IpaIndicator indicator;
		
		indicator = new IpaIndicator(IpaBehavior.C1, 0, 5);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C2, 3, 14);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C3, 6, 20);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C4, 4, 11);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C5, 21, 40);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C6, 14, 30);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C7, 2, 11);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C8, 1, 9);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C9, 0, 5);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C10, 3, 13);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C11, 1, 10);
		indicators.put(indicator.getBehavior(), indicator);
		
		indicator = new IpaIndicator(IpaBehavior.C12, 0, 7);
		indicators.put(indicator.getBehavior(), indicator);
	}
	
	/**
	 * Devuelve el límite inferior para un Comportamiento 
	 * @param behavior IpaBehavior Comportamiento
	 * @return int Límite inferior
	 */
	public static int getInferiorLimit(IpaBehavior behavior) {
		
	    if (indicators == null) 
	        initialize();
	    
		return indicators.get(behavior).getInferiorLimit();
	}
	
	 /**
     * Devuelve el límite superior para un Comportamiento 
     * @param behavior IpaBehavior Comportamiento
     * @return int Límite superior
     */
	public static int getSuperiorLimit(IpaBehavior behavior) {
		
	    if (indicators == null) 
	        initialize();
		return indicators.get(behavior).getSuperiorLimit();
	}
}
