package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	GenesDao dao; 
    private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;  // SEMPLICE, PESATO, NON ORIENTATO
    private List<String> allLocalization ; 
    
    Map <String,Genes>idMapGenes= new HashMap<>();
    private List<String> bestCammino ; 
   
    

    

    public Model() {
    	
    	this.dao= new GenesDao();  
    	this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    	this.allLocalization= new ArrayList<>();
    	
    	
    }
    
    
    
    
    public void creaGrafo() {
		 
		 
    	allLocalization= dao.getAllCromosomi(); 

		 /** VERTICI */
	    	Graphs.addAllVertices(this.graph, allLocalization);
	 		System.out.println("NUMERO vertici GRAFO: " +this.graph.vertexSet().size());
	 		
	 		
	 		/*
 esiste un  arco se e solo  se esiste almeno una interazione che coinvolge due geni, 
 rispettivamente della prima  della seconda localizzazione (o nell’ordine inverso).  
	 		
	 		
PESO:  dovrà essere un numero intero, pari al numero di ** TYPE diversi di interazioni **
       tra i geni associati alle due localizzazioni.
	 		 
	 		 
	 		 */
	 		
	 		
	 		for(String l1: allLocalization) {
	 			for(String l2: allLocalization) {
	 				
	 				if(l1.compareTo(l2)!=0) { 
	 					
	 					List<String> listGenesLocal1= dao.getAllGenesLocal(l1);
	 					List<String> listGenesLocal2= dao.getAllGenesLocal(l2);
	 				

	 					
	 				int peso= getPesoType(listGenesLocal1,listGenesLocal2); 
	 				if(peso>0) {
	 					System.out.println("\n peso creato\n");
 						Graphs.addEdgeWithVertices(this.graph, l1, l2, peso);
 						//this.listPesi.add(peso); //abbastanza inutile, usa i GRAFI
	 				}
	 			}
	 				
	 		  }
	 		}
			System.out.println("\nnumero ARCHI: "+ this.graph.edgeSet().size());
 
	 		
    }


    // data una localizzazione, ho un insieme di geni.
    // se esiste almeno una interazione che coinvolge due geni di due localz diverse
    
    
	private int getPesoType(List<String> listGenesLocal1, List<String> listGenesLocal2) {
	
		List<String> temp= new ArrayList<>(); 
		
		
		for(String g1:listGenesLocal1 ) {
			for(String g2:listGenesLocal2 ) {
				
				for(String c1:  dao.getPeso(g1, g2)) {
					if(!temp.contains(c1)) {
						temp.add(c1);
						
					}
				}
				
			}
		}
		
		
		return temp.size();
	}
	
	
	
	
	
	
}