package org.ipa;

public class AnalysisResult extends InfoRow{

	private String conflict;
	private String behavior;
	private int infLimit;
	private int supLimit;
	private double percentage;
	
	public AnalysisResult(String conflict, String behavior, int infLimit, int supLimit, double percentaje) {
		this.conflict = conflict;
		this.behavior = behavior;
		this.infLimit = infLimit;
		this.supLimit = supLimit;
		this.percentage = percentaje;
	}
	
	public Object[] getDataToTable() {
		
		Object[] objects;
        objects = new Object[] {conflict, behavior,  infLimit + "%", supLimit + "%", String.format("%.2f", percentage) + "%"};

        if (percentage < infLimit || percentage > supLimit) {
            objects = paintItRed(objects);
        }
        
        return objects;
	}
	
	public String getConflict() {
		return conflict;
	}

	public String getBehavior() {
		return behavior;
	}

	public String getInfLimit() {
		return infLimit + "%";
	}

	public String getSupLimit() {
		return supLimit + "%";
	}

	public String getPercentage() {
		
		return String.format("%.2f", percentage) + "%";
	}
    
    private Object[] paintItRed(Object[] objects) {
        
    	return paintIt(objects, "#FF0000");
    }
}
