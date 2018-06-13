package it.polito.tdp.flightdelays.model;

import java.util.HashMap;

public class FlightIdMap extends HashMap<Integer,Flight>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Flight get(Flight flight) {
		Flight old = super.get(flight.getId());
		
		if(old!=null) {
			return old;
		}
		super.put(flight.getId(), flight);
		
		return flight;
	}

}
