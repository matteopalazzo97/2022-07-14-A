package it.polito.tdp.nyc.model;


import java.util.Map;
import java.util.HashMap;
import java.util.Objects;


public class NTA {
	
	private String NTACode;
	private Map<String, String> mappaSSID;
	
	public NTA(String nTACode) {
		super();
		NTACode = nTACode;
		mappaSSID = new HashMap<String, String>();
	}

	public String getNTACode() {
		return NTACode;
	}

	public void setNTACode(String nTACode) {
		NTACode = nTACode;
	}

	public Map<String, String> getMappaSSID() {
		return mappaSSID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(NTACode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NTA other = (NTA) obj;
		return Objects.equals(NTACode, other.NTACode);
	}

	@Override
	public String toString() {
		return "NTA [NTACode=" + NTACode + "]";
	}
	
	
	

}
