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
	
	public List<Genes> getAllGenes(){
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
//
//				Genes genes = new Genes(res.getString("GeneID"), 
//						res.getString("Essential"), 
//						res.getInt("Chromosome"));
				
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
	
public List<String>  getPeso (String gId1, String gId2) {
		
		String sql = " select distinct  i.`Type` "
				+ "from interactions i, genes g1, genes g2 "
				+ "where (g1.`GeneID`= i.`GeneID1` and g2.`GeneID`= i.`GeneID2`) or (g1.`GeneID`= i.`GeneID2` and g2.`GeneID`= i.`GeneID1`) and "
				+ "g1.`GeneID`= ? and g2.`GeneID`= ? ";
		
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,gId1); 
			st.setString(2,gId1);

			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getString("i.Type"));
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
