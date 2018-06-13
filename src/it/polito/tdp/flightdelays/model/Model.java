package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private List<Airline> airlines;
	private List<Airport> airports;
	private List<Flight> flights;
	private FlightDelaysDAO fdao;
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private AirlineIdMap airlineIdMap;
	private AirportIdMap airportIdMap;
	private FlightIdMap flightIdMap; 
	
	public Model() {
		this.fdao = new FlightDelaysDAO();
		this.airlineIdMap = new AirlineIdMap();
		this.airportIdMap = new AirportIdMap();
		this.flightIdMap = new FlightIdMap();
		this.airlines = fdao.loadAllAirlines(airlineIdMap);
		this.airports = fdao.loadAllAirports(airportIdMap);
		this.flights = fdao.loadAllFlights(airlineIdMap,airportIdMap, flightIdMap);
	}

	public List<Airline> getAirlines() {
		if(this.airlines==null) {
			return new ArrayList<Airline>();
		}
		return airlines;
	}

	public List<Flight> getFlights() {
		if(this.flights==null) {
			return new ArrayList<Flight>();
		}
		return flights;
	}
	
	public List<Airport> getAirport() {
		if(this.airports==null) {
			return new ArrayList<Airport>();
		}
		return airports;
	}
	
	public void creaGrafo(Airline airline) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, airports);
		for(Flight f : fdao.getVoliMedia(airline,this.airportIdMap)) {
			if(f.getA1()!=null && f.getA2()!=null) {
				double distanza = LatLngTool.distance(new LatLng(f.getA1().getLatitude(), f.getA1().getLongitude()),
						new LatLng(f.getA2().getLatitude(), f.getA2().getLongitude()), LengthUnit.KILOMETER);
				double peso = f.getAvg()/distanza;
				Graphs.addEdge(grafo, f.getA1(), f.getA2(), peso);
			}
						
		}		
	}
	
	public List<Rotta> stampaRisultato(Airline airline) {
		
		List<DefaultWeightedEdge> list = new ArrayList<DefaultWeightedEdge>(grafo.edgeSet());
		
		if(grafo!=null) {
			this.creaGrafo(airline);
		}
		
		Collections.sort(list, new Comparator<DefaultWeightedEdge>() {
			
			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				return - Double.compare(grafo.getEdgeWeight(o1), grafo.getEdgeWeight(o2)) ;
			}
		});
		
		List<Rotta> result = new ArrayList<Rotta>();
		
		for(int i =0 ; i<10; i++) {
			DefaultWeightedEdge arco = list.get(i);
			Airport a1 = grafo.getEdgeSource(arco);
			Airport a2 = grafo.getEdgeTarget(arco);
			double avg = grafo.getEdgeWeight(arco);
			Rotta r = new Rotta(a1,a2,avg);
			result.add(r);
		}
		
		return result;
	}

}
