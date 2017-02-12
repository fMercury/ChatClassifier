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
    public double getTotalDeviation() {
  
        double totalDeviation = 0;
    
        for(IpaBehavior behavior : IpaBehavior.values()){
            totalDeviation += Math.abs(getBehaviorDeviation(behavior));
        }
  
        return totalDeviation;
    }    
    

    @Override
    public int compareTo(IpaPerson o) {
//      if (totalDeviation < p.getTotalDeviation()) return -1;
//      if (totalDeviation == p.getTotalDeviation()) return 0;
//      if (totalDeviation > p.getTotalDeviation()) return 1;
      
      return 0;
    }
}
//
//import java.util.Hashtable;
//import java.util.Set;
//
//import org.enums.IpaBehavior;
//
//public class IpaPerson extends IpaItem implements Comparable<IpaPerson> {
//
//	private Hashtable<IpaBehavior, Integer> interactions;
//	private Hashtable<IpaBehavior, Double> percentages;
//	private double totalDeviation;
//	
//	public IpaPerson(String name) {
//		super(name);
//		interactions = new Hashtable<IpaBehavior, Integer>();
//		percentages = new Hashtable<IpaBehavior, Double>();	
//		totalDeviation = 0;
//	}
//	
//	public int getInteractions(IpaBehavior behavior) {
//		
//		if (interactions.containsKey(behavior))
//			return interactions.get(behavior).intValue();
//		
//		return 0;
//	}
//	
//	public void addInteraction(IpaBehavior behavior, int amount) {
//        
//        if (interactions.containsKey(behavior)) {
//            int prevAmount = interactions.get(behavior);
//            interactions.put(behavior, prevAmount + amount);
//        }
//        else {
//            interactions.put(behavior, amount);
//        }
//        
//        updatePercentages();
//    }
//    
//    private int getTotalInteractions() {        
//        
//        int total = 0;
//        
//        Set<IpaBehavior> keys = interactions.keySet();      
//        for(IpaBehavior key: keys){         
//            total += interactions.get(key).intValue();
//        }
//        
//        return total;
//    }
//    
//	public double getPercentage(IpaBehavior behavior) {
//		
//		if (percentages.containsKey(behavior))
//			return percentages.get(behavior).doubleValue();
//		
//		return 0;
//	}
//	
//	private void updatePercentages() {
//        
//        int total = getTotalInteractions();
//        for(IpaBehavior key : IpaBehavior.values()){
//            
//            int interactionAmount = 0;
//            if (interactions.containsKey(key))
//                interactionAmount = interactions.get(key);
//            
//            double value = (double)interactionAmount * (double)100 / (double)total;
//            percentages.put(key, value);
//        }   
//    }
//    
//	public double getTotalDeviation() {
//		
//		totalDeviation = 0;
//        
//        for(IpaBehavior key : IpaBehavior.values()){
//            
//            double percentage = percentages.get(key);
//            
//            double inferiorLimit, superiorLimit;
//            inferiorLimit = IpaEdgeValues.getInferiorLimit(key);
//            superiorLimit = IpaEdgeValues.getSuperiorLimit(key);
//            
//            if (percentage < inferiorLimit) {
//                totalDeviation += inferiorLimit - percentage;
//            }
//            else {
//                if (percentage > superiorLimit) {
//                    totalDeviation += percentage - superiorLimit;
//                }
//            }
//        }   
//		
//		return totalDeviation;
//	}
//	
//	public double getDeviation(IpaBehavior behavior) {
//		
//	    double percentage = percentages.get(behavior);
//        
//        double inferiorLimit, superiorLimit;
//        inferiorLimit = IpaEdgeValues.getInferiorLimit(behavior);
//        superiorLimit = IpaEdgeValues.getSuperiorLimit(behavior);
//        
//        if (percentage < inferiorLimit) {
//            return inferiorLimit - percentage;
//        }
//        else {
//            if (percentage > superiorLimit) {
//                return percentage - superiorLimit;
//            }
//        }
//	    
//		return 0;
//	}
//	
//	@Override
//	public int compareTo(IpaPerson p) {
//
//		if (totalDeviation < p.getTotalDeviation()) return -1;
//		if (totalDeviation == p.getTotalDeviation()) return 0;
//		if (totalDeviation > p.getTotalDeviation()) return 1;
//		
//		return 0;
//	}
//	
//}
