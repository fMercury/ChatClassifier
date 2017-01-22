package org.enums;

public enum IpaBehavior {
	
	C1("C1", "Muestra soliradidad", "Reintegración"),
	C2("C2", "Muestra relajamiento", "Reducción de tensión"),
	C3("C3", "Muestra acuerdo o aprueba", "Decisión"),
	C4("C4", "Da sugerencia u orientación", "Control"),
	C5("C5", "Da opiniones", "Evaluación"),
	C6("C6", "Da información", "Comunicación"),
	C7("C7", "Pide información", "Comunicación"),
	C8("C8", "Pide opinión", "Evaluación"),
	C9("C9", "Pide sugerencia u orientación", "Control"),
	C10("C10", "Muestra desacuerdo", "Decisión"),
	C11("C11", "Muestra tensión o molestia", "Reducción de tensión"),
	C12("C12", "Muestra antagonismo", "Reintegración");
	
	private String code;
	private String description;
	private String conflict;
	
	
	IpaBehavior(String code, String description, String conflict) {		
		this.code = code;
		this.description = description;
		this.conflict = conflict;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription() {		
		return description;
	}
	
	public String getConflict() {
	    return conflict;
	}
	
	public static IpaBehavior stringToIpaBehavior(String string) {
		
		switch (string) {
		case "1":
			return IpaBehavior.C1;
		case "2":
			return IpaBehavior.C2;
		case "3":
			return IpaBehavior.C3;
		case "4":
			return IpaBehavior.C4;
		case "5":
			return IpaBehavior.C5;
		case "6":
			return IpaBehavior.C6;
		case "7":
			return IpaBehavior.C7;
		case "8":
			return IpaBehavior.C8;
		case "9":
			return IpaBehavior.C9;
		case "10":
			return IpaBehavior.C10;
		case "11":
			return IpaBehavior.C11;
		case "12":
			return IpaBehavior.C12;
		default:
			return IpaBehavior.C1; 
		}
		
	}

}
