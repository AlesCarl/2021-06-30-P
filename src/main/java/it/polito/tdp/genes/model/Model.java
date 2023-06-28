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
    	dao.getAllGenes(idMapGenes); 
    	

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
	 					
//	 				 int peso= getPesoType(l1,l2); 
	 					int peso = getNewPesoType(l1,l2); 
	 				
	 				 if(peso>0) {
 						Graphs.addEdgeWithVertices(this.graph, l1, l2, peso);
	 				}
	 			}
	 				
	 		  }
	 		}
			System.out.println("\nnumero ARCHI: "+ this.graph.edgeSet().size());
 
	 		
    }


    // data una localizzazione, ho un insieme di geni.
    // se esiste almeno una interazione che coinvolge due geni di due localz diverse
    
    
    
    
    
    // do in input due localita
	private int getNewPesoType(String l1, String l2) {

		 List<Interactions> allInteractions2Location= dao.collegamentoArchi(l1,l2, idMapGenes); 
		 Map<String,Integer> temp= new HashMap<>(); 

		 
		 for(Interactions ii: allInteractions2Location) {
			 if(temp.get(ii.getType())==null) {
					temp.put(ii.getType(), 0);  // qui metto tutti i TYPE differenti	
				}		
		 }
		 
		return temp.size();
	}



/** CON QUESTO METODO NON SI TROVA **/ 
//	private int getPesoType(String l1, String l2) {
//
//	 List<String> listGenesLocal1= dao.getAllGenesLocal(l1);  // tutti i geni appartenenti a localizzazione 1
//	 List<String> listGenesLocal2= dao.getAllGenesLocal(l2);  // tutti i geni appartenenti a localizzazione 2
//	
//	 Map<String,Integer> temp= new HashMap<>(); 
//	 
//
//	 /** calcolo le interazioni tra due diverse localita' -->  **/
//	 
//	 for(String g1:listGenesLocal1 ) {
//			for(String g2:listGenesLocal2 ) {
//				
//				if( g1.compareTo(g2)!=0 ) {
//					String tt= dao.getType2Genes(g1,g2);
//					
//					if(temp.get(tt)==null) {
//						temp.put(tt, 0);  // qui metto tutti i TYPE differenti	
//					}		
//				}		
//			}
//	 }
//				
//		return temp.size(); 
//	}
	
	
	

	
	
	
	
	
	
		
	
	
}