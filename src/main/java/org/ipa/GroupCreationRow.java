package org.ipa;

/**
 * Clase que contiene una fila/línea del resultado de la creación del grupo
 * @author martinmineo
 *
 */
public class GroupCreationRow  extends InfoRow{

	private String name;
	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;
	private String c6;
	private String c7;
	private String c8;
	private String c9;
	private String c10;
	private String c11;
	private String c12;
	private String totalDeviation;
	private boolean paintItBlack;
	
	/**
	 * Constructor
	 * @param name String Nombre del grupo
	 * @param c1 String C1
	 * @param c2 String C2
	 * @param c3 String C3
	 * @param c4 String C4
	 * @param c5 String C5
	 * @param c6 String C6
	 * @param c7 String C7
	 * @param c8 String C8
	 * @param c9 String C9
	 * @param c10 String C10
	 * @param c11 String C11
	 * @param c12 String C12
	 * @param totalDeviation String Desviación total 
	 * @param paintItBlack boolean Determina si la fila se debe pintar de negro o no
	 */
	public GroupCreationRow(String name, String c1, String c2, String c3, String c4, String c5, String c6, String c7,
			String c8, String c9, String c10, String c11, String c12, String totalDeviation, boolean paintItBlack) {
		
		if (paintItBlack)
			this.name = "TOTAL";
		else
			this.name = name;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.c5 = c5;
		this.c6 = c6;
		this.c7 = c7;
		this.c8 = c8;
		this.c9 = c9;
		this.c10 = c10;
		this.c11 = c11;
		this.c12 = c12;
		this.totalDeviation = totalDeviation;
		this.paintItBlack = paintItBlack;
	}
	
    /**
     * Genera un Object[] que se utiliza para completar la tabla que contiene los resultados de la creación del grupo
     * Si es necesario, pinta la fila de negro
     * @return Object[] Arreglo con los datos a mostrar en la tabla
     */
	public Object[] getDataToTable() {
		
		Object[] objects = new Object[] {name, c1 + "%", c2 + "%", c3 + "%", c4 + "%", c5 + "%", c6 + "%", c7 + "%", c8 + "%", c9 + "%", c10 + "%", c11 + "%", c12 + "%", totalDeviation + "%"};
		if (paintItBlack)
			return paintItBlack(objects);
		else 
			return objects;
	}
	
	/**
	 * Devuelve el nombre del grupo
	 * @return String Nombre del grupo
	 */
    public String getName() {
		return name;
	}

    /**
     * Devuelde el valor de C1
     * @return String C1
     */
	public String getC1() {
		return c1 + "%";
	}

    /**
     * Devuelde el valor de C2
     * @return String C2
     */
	public String getC2() {
		return c2 + "%";
	}

    /**
     * Devuelde el valor de C3
     * @return String C3
     */
	public String getC3() {
		return c3 + "%";
	}

    /**
     * Devuelde el valor de C4
     * @return String C4
     */
	public String getC4() {
		return c4 + "%";
	}

    /**
     * Devuelde el valor de C5
     * @return String C5
     */
	public String getC5() {
		return c5 + "%";
	}

    /**
     * Devuelde el valor de C6
     * @return String C6
     */
	public String getC6() {
		return c6 + "%";
	}

    /**
     * Devuelde el valor de C7
     * @return String C7
     */
	public String getC7() {
		return c7 + "%";
	}

    /**
     * Devuelde el valor de C8
     * @return String C8
     */
	public String getC8() {
		return c8 + "%";
	}

    /**
     * Devuelde el valor de C9
     * @return String C9
     */
	public String getC9() {
		return c9 + "%";
	}

    /**
     * Devuelde el valor de C10
     * @return String C10
     */
	public String getC10() {
		return c10 + "%";
	}

    /**
     * Devuelde el valor de C11
     * @return String C11
     */
	public String getC11() {
		return c11 + "%";
	}
    
	/**
     * Devuelde el valor de C12
     * @return String C12
     */
	public String getC12() {
		return c12 + "%";
	}

    /**
     * Devuelde la desviación total
     * @return String C1
     */
	public String getTotalDeviation() {
		return totalDeviation + "%";
	}

	 /**
     * Pinta el texto de negro
     * @param objects Object[] Texto a ser coloreado
     * @return Object[] Texto coloreado
     */
	private Object[] paintItBlack(Object[] objects) {
    	return paintIt(objects, "#000000");
    }
	
	
}
