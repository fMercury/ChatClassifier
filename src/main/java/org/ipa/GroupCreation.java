package org.ipa;

public class GroupCreation  extends InfoRow{

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
	
	public GroupCreation(String name, String c1, String c2, String c3, String c4, String c5, String c6, String c7,
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
	
	public Object[] getDataToTable() {
		
		Object[] objects = new Object[] {name, c1 + "%", c2 + "%", c3 + "%", c4 + "%", c5 + "%", c6 + "%", c7 + "%", c8 + "%", c9 + "%", c10 + "%", c11 + "%", c12 + "%", totalDeviation + "%"};
		if (paintItBlack)
			return paintItBlack(objects);
		else 
			return objects;
	}
	
    public String getName() {
		return name;
	}

	public String getC1() {
		return c1 + "%";
	}

	public String getC2() {
		return c2 + "%";
	}

	public String getC3() {
		return c3 + "%";
	}

	public String getC4() {
		return c4 + "%";
	}

	public String getC5() {
		return c5 + "%";
	}

	public String getC6() {
		return c6 + "%";
	}

	public String getC7() {
		return c7 + "%";
	}

	public String getC8() {
		return c8 + "%";
	}

	public String getC9() {
		return c9 + "%";
	}

	public String getC10() {
		return c10 + "%";
	}

	public String getC11() {
		return c11 + "%";
	}

	public String getC12() {
		return c12 + "%";
	}

	public String getTotalDeviation() {
		return totalDeviation + "%";
	}

	private Object[] paintItBlack(Object[] objects) {
    	return paintIt(objects, "#000000");
    }
	
	
}
