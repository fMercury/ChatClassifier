package org.ipa;

import java.util.Hashtable;

import org.enums.IpaBehavior;

public class IpaPerson  extends IpaItem implements Comparable<IpaPerson> {
    Hashtable<IpaBehavior, Integer> interactions;

    public IpaPerson(String name) {
        super(name);
     
        initializeInteractions();
    }
    
    private void initializeInteractions() {
        
        interactions = new Hashtable<IpaBehavior, Integer>();
        for (IpaBehavior behavior : IpaBehavior.values()) {
            interactions.put(behavior, 0);
        }
    }
    
    public void addInteraction(IpaBehavior behavior, int amount) {
      
        interactions.put(behavior, interactions.get(behavior) + amount);
    }
    
    public int getTotalInteractions() {
        
        int total = 0;
        
        for (IpaBehavior behavior : IpaBehavior.values()) {
            total += interactions.get(behavior);
        }
        
        return total;      
    }
    
    public int getBehaviorInteractions(IpaBehavior behavior) {
        
        return interactions.get(behavior);
    }
    
    private int getBehaviorPercentage(IpaBehavior behavior) {
        
        return interactions.get(behavior) * 100 / getTotalInteractions();
    }
    
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
   
    @Override
    public int compareTo(IpaPerson o) {
//      if (totalDeviation < p.getTotalDeviation()) return -1;
//      if (totalDeviation == p.getTotalDeviation()) return 0;
//      if (totalDeviation > p.getTotalDeviation()) return 1;
      
      return 0;
    }

	
    @Override
	protected boolean absoluteDeviation() {
		return true;
	}
}