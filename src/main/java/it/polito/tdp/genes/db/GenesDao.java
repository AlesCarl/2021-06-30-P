package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(Map<String,Genes>idMap){
		
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				
				result.add(genes);
				
				
				idMap.put(res.getString("GeneID"), genes); 
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	public List<String> getAllCromosomi() {
		
		String sql = " select c.`Localization` "
				+ "from classification c  "
				+ "group by c.`Localization` "; 
		
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {  
				
				result.add(res.getString("c.Localization"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
		
	}

	public List<String> getAllGenesLocal(String local) {
		
		String sql = " select  distinct g1.`GeneID` "
				+ "from genes g1, classification c1 "
				+ "where  c1.`Localization`=?  and c1.`GeneID`= g1.`GeneID` "; 
		
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,local);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getString("GeneID"));
			}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
//	
//	// prendo i distinct type di due geni  appartenenti   a due localita 
//public String  getType2Genes ( String g1, String g2) {
//		
//		String sql = " select distinct i.`Type` "
//				+ "from interactions i, genes g1, genes g2  "
//				+ "where g1.`GeneID`= i.`GeneID1` and g2.`GeneID`= i.`GeneID2` and "
//				+ "g1.`GeneID`= ?  and g2.`GeneID`= ? "; 
//		
//	// G1 viene da localization 1
//	// G2 viene da localization 2
//		
//		String result = null; 
//		Connection conn = DBConnect.getConnection();
//
//		try {
//			
//			PreparedStatement st = conn.prepareStatement(sql);
//			st.setString(1,g1); 
//			st.setString(2,g2);
//
//			ResultSet res = st.executeQuery();
//			
////			res.first();
//			if(res.isFirst())
//				result= res.getString("i.Type");  
//			
//			
//			res.close();
//			st.close();
//			conn.close();
//			return result;
//			
//		} catch (SQLException e) {
//			throw new RuntimeException("Database error", e) ;
//		}
//	}

	
public List<Interactions> collegamentoArchi(String l1, String l2, Map<String,Genes> idMap) {
	

	String sql = "  select distinct  i.* "
			+ " from interactions i, genes g1, genes g2, classification c1, classification c2 "
			+ " where g1.`GeneID`= i.`GeneID1` and g2.`GeneID`= i.`GeneID2` and c1.`GeneID`= g1.`GeneID` "
			+ " and c2.`GeneID`= g2.`GeneID`and c1.`Localization`=? and c2.`Localization`=? "; 
	
			//  5   11
	
	List<Interactions> result = new ArrayList<Interactions>();
	//boolean result= false; 
	Connection conn = DBConnect.getConnection();

	try {
		
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, l1);
		st.setString(2, l2);

		ResultSet res = st.executeQuery();
		
	
		while (res.next()) {
			
			Interactions inter = new Interactions (idMap.get(res.getString("i.GeneID1")), idMap.get(res.getString("i.GeneID2")),res.getString("i.Type"),
				                 res.getDouble("i.Expression_Corr"));
			
			result.add(inter);
		}
		res.close();
		st.close();
		conn.close();
		return result;
		
	} catch (SQLException e) {
		throw new RuntimeException("Database error", e) ;
	}
}




	

	
}
