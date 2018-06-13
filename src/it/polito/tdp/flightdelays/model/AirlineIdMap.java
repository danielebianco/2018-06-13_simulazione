package it.polito.tdp.flightdelays.model;

import java.util.HashMap;

public class AirlineIdMap extends HashMap<String,Airline>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Airline get(Airline airline) {
		Airline old = super.get(airline.getId());
		
		if(old!=null) {
			return old;
		}
		super.put(airline.getId(), airline);
		
		return airline;
	}
	

}
