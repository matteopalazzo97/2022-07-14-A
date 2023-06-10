package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.nyc.model.Hotspot;
import it.polito.tdp.nyc.model.NTA;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> getAllBorghi(){
		String sql = "SELECT DISTINCT ny.`Borough` "
				+ "FROM `nyc_wifi_hotspot_locations` ny";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("Borough"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> getVertici(String borgo){
		String sql = "SELECT DISTINCT ny.`NTACode` "
				+ "FROM `nyc_wifi_hotspot_locations` ny "
				+ "WHERE ny.`Borough`=?";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, borgo);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("NTACode"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	
	/*
	 * faccio come ha fatto lui perchè non sono in grado di fare una query che faccia tutto da sola
	 * 
	 * quindi importo una tabella con gli nta e gli ssid ad essi associati (uno o più ssid per ogni nta)
	 * e per ogni nuovo nta creo un oggetto nta e nella sua lista di ssid aggiungo ssid, se nta esiste 
	 * già aggiungo solo ssid nella lista di ssid di quell nta
	 * 
	 * tengo traccia dell'nta della riga precedente e lo inizializzo a null
	 */
	public List<NTA> getNTA(String borgo){
		String sql = "SELECT DISTINCT ny.`NTACode`, ny.`SSID` "
				+ "FROM `nyc_wifi_hotspot_locations` ny "
				+ "WHERE ny.`Borough`= ?"
				+ "ORDER BY ny.`NTACode`";
		List<NTA> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, borgo);
			ResultSet res = st.executeQuery();
			
			NTA prec = new NTA("null");

			while (res.next()) {
				
				if(res.getString("NTACode").equals(prec.getNTACode())) {
					prec.getMappaSSID().put(res.getString("SSID"), res.getString("SSID"));
				} else {
					prec = new NTA(res.getString("NTACode"));
					result.add(prec);
					prec.getMappaSSID().put(res.getString("SSID"), res.getString("SSID"));
					
				}
				
			}
			
			System.out.println(result.get(0).toString() + "  " + result.get(0).getMappaSSID().size());
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	
}
