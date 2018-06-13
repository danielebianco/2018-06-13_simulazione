package it.polito.tdp.flightdelays.model;

import java.util.HashMap;

public class AirportIdMap extends HashMap<String,Airport>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Airport get(Airport airport) {
		Airport old = super.get(airport.getId());
		
		if(old!=null) {
			return old;
		}
		super.put(airport.getId(), airport);
		
		return airport;
	}

}
