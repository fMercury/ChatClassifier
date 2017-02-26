package org.ipa;

/**
 * Clase que contiene una fila/línea del resultado del análisis de los datos
 * @author martinmineo
 *
 */
public class GroupAnalysisRow extends InfoRow{

	private String conflict;
	private String behavior;
	private int infLimit;
	private int supLimit;
	private double percentage;
	
	/**
	 * Constructor
	 * @param conflict String Conflicto
	 * @param behavior String Comportamiento
	 * @param infLimit String Límite inferior
	 * @param supLimit String Límite superior
	 * @param percentaje String Porcentaje
	 */
	public GroupAnalysisRow(String conflict, String behavior, int infLimit, int supLimit, double percentaje) {
		this.conflict = conflict;
		this.behavior = behavior;
		this.infLimit = infLimit;
		this.supLimit = supLimit;
		this.percentage = percentaje;
	}
	
	/**
	 * Genera un Object[] que se utiliza para completar la tabla que contiene los resultados del análisis
	 * Si existe un conflicto pinta la línea de rojo
	 * @return Object[] Arreglo con los datos a mostrar en la tabla
	 */
	public Object[] getDataToTable() {
		
		Object[] objects;
        objects = new Object[] {conflict, behavior,  infLimit + "%", supLimit + "%", String.format("%.2f", percentage) + "%"};

        if (percentage < infLimit || percentage > supLimit) {
            objects = paintItRed(objects);
        }
        
        return objects;
	}
	
	/**
	 * Devuelve el Conflicto
	 * @return String Conflicto
	 */
	public String getConflict() {
		return conflict;
	}

	/**
	 * Devuelve el Comportamiento
	 * @return String Comportamiento
	 */
	public String getBehavior() {
		return behavior;
	}

	/**
	 * Devuelve el Límite inferior
	 * @return String Límite inferior
	 */
	public String getInfLimit() {
		return infLimit + "%";
	}

	/**
	 * Devuelve el Límite superior
	 * @return String Límite superior
	 */
	public String getSupLimit() {
		return supLimit + "%";
	}

	/**
	 * Devuelve el Porcentaje
	 * @return String Porcentaje
	 */
	public String getPercentage() {
		
		return String.format("%.2f", percentage) + "%";
	}
    
	/**
	 * Pinta el texto de rojo
	 * @param objects Object[] Texto a ser coloreado
	 * @return Object[] Texto coloreado
	 */
    private Object[] paintItRed(Object[] objects) {
        
    	return paintIt(objects, "#FF0000");
    }
}
