package it.polito.tdp.nyc.model;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private NYCDao dao;
	
	public Model() {
		super();
		this.dao = new NYCDao();
	}
	
	public void creaGrafo(String borgo) {
		
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(borgo));
		
		System.out.println(this.grafo.vertexSet().size());
		
		List<NTA> NTAList = this.dao.getNTA(borgo);
		
		//scorro tutta la lista di nta e a coppie controllo quanti ssid hanno in comune e non
		for(int i = 0; i < NTAList.size(); i++) {
			for(int j = i; j < NTAList.size(); j++) {
				if(!NTAList.get(i).equals(NTAList.get(j))) {
					int unione = NTAList.get(i).getMappaSSID().size();
					for(String s: NTAList.get(j).getMappaSSID().values()) {
						if(!NTAList.get(i).getMappaSSID().containsKey(s)) {
							unione++;
						}
					}
					//aggiungo edge e peso
					Graphs.addEdge(this.grafo, NTAList.get(i).getNTACode(),
							NTAList.get(j).getNTACode(), unione);
				}
			}	
		}
		
	}

	public List<String> getBorghi() {
		
		return this.dao.getAllBorghi();
	}

	public int numVertici() {
		return this.grafo.vertexSet().size();
	}

	public int numArchi() {
		return this.grafo.edgeSet().size();
	}

	public double getPesoMedio() {
		
		double peso = 0;
		
		for(DefaultWeightedEdge edge: this.grafo.edgeSet()) {
			peso += this.grafo.getEdgeWeight(edge);
		}
		System.out.println(peso/this.grafo.edgeSet().size() );
		return peso/this.grafo.edgeSet().size();
	}
	
	public int getArchiSopraMedia() {
		
		int speciali = 0;
		
		double peso = 0.0;
		
		for(DefaultWeightedEdge edge: this.grafo.edgeSet()) {
			peso += this.grafo.getEdgeWeight(edge);
		}
		
		double media = peso/this.grafo.edgeSet().size();
		
		for(DefaultWeightedEdge edge: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(edge) > media)
				speciali ++;
		}
		
		
		return speciali;
	}
	
	
}
