package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.AirlineIdMap;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.AirportIdMap;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.FlightIdMap;

public class FlightDelaysDAO {
		
	public List<Airline> loadAllAirlines(AirlineIdMap airlineIdMap) {
	
		String sql = "SELECT id, airline from airlines";
		
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(airlineIdMap.get(new Airline(rs.getString("ID"), rs.getString("airline"))));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(AirportIdMap airportIdMap) {
		
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airportIdMap.get(airport));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights(AirlineIdMap airlineIdMap, AirportIdMap airportIdMap, FlightIdMap flightIdMap) {
		
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {				
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flightIdMap.get(flight));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	public List<Flight> getVoliMedia(Airline airline, AirportIdMap airportIdMap) {
			
		String sql = "select f.ORIGIN_AIRPORT_ID as a1, f.DESTINATION_AIRPORT_ID as a2, AVG(f.ARRIVAL_DELAY) as avg " + 
				"from flights as f " + 
				"where f.AIRLINE =? " + 
				"group by a1, a2";
		
		List<Flight> result = new ArrayList<Flight>();		
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			
			PreparedStatement st = conn.prepareStatement(sql);
					
			st.setString(1, airline.getId());
						
			ResultSet rs = st.executeQuery();
						
			while (rs.next()) {
				Airport sourceAirport = airportIdMap.get(rs.getString("a1"));
				Airport destinationAirport = airportIdMap.get(rs.getString("a2"));
				Flight flight = new Flight(airline,sourceAirport,destinationAirport,rs.getDouble("avg"));
				result.add(flight);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
		
		return result;
	}
}
