package solution;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
public class Solution {
	/*
	 * Complete the function below.
	 */

	    static String[] findPotentialInsiderTraders(String[] datafeed) {
	    	final int ILLEGAL = 500000;
	        Map<String, Integer> sus = new TreeMap<String, Integer>();
	        Map<Integer, Integer> price = new TreeMap<Integer, Integer>();
	        
	        // index synced
	        ArrayList<Integer> day = new ArrayList<Integer>();
	        ArrayList<String> person = new ArrayList<String>();
	        ArrayList<Integer> quantity = new ArrayList<Integer>();
	        
	        boolean first = true;
	        int lines;
	        // collecting information, inefficient means
	        for (String detail : datafeed) { 
	            if (first) {
	                first = false;
	                continue;
	            }
	            String[] parts = detail.split("\\|");
	            if(parts.length == 2) { //stock price has changed
	            	// if multiple changes, keeps last for that day
	                price.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	            } else { // trade has been made
	            	day.add(Integer.parseInt(parts[0]));
	            	person.add(parts[1]);
	            	quantity.add(Integer.parseInt(parts[3]));
	            }
	        }
	        
	        // Check for inside trading
	        
	        for (int i = 0; i < day.size(); ++i) {
	        	int tradeDate = day.get(i);
	        	int datePrice = 0;
	        	for (Integer current : price.keySet()) {
	        		if (current <= tradeDate) {
	        			datePrice = price.get(current);
	        		}
	        	}
	        	
	        	if (sus.containsKey(person.get(i))) {
	        		continue;
	        	} else if (price.containsKey(tradeDate + 1) && // only one day after
	        			(quantity.get(i) * (datePrice - price.get(tradeDate + 1)) > ILLEGAL ||
	        			quantity.get(i) * (datePrice - price.get(tradeDate + 1)) < -ILLEGAL)) { 
	        		sus.put(person.get(i), tradeDate);
	        	} else if (price.containsKey(tradeDate + 2) && // two days
	        			(quantity.get(i) * (datePrice - price.get(tradeDate + 2)) > ILLEGAL ||
	        			quantity.get(i) * (datePrice - price.get(tradeDate + 2)) < -ILLEGAL)) {
	        		sus.put(person.get(i), tradeDate);
	        	}
	        }
	        
	        return mapToString(sus);
	    }
	    
	    /**
	     * Takes mapping of suspects to day and will return required string[] output.
	     * NOT WORKING FOR DUPLICATES YET!
	     * @param sus
	     * @return
	     */
	    private static String[] mapToString(Map<String, Integer> sus) {
	    	Map<Integer, String> ordered = new TreeMap<Integer, String>();
	    	for (String person : sus.keySet()) {
	    		ordered.put(sus.get(person), person);
	    	}
	    	
	    	String[] result = new String[ordered.size()];
	    	int i = 0; // index
	    	for (Integer day : ordered.keySet()) {
	    		result[i] = day + "|" + ordered.get(day);
	    	}
	    	return result;
	    }
}
