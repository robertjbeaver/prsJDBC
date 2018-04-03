package business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import db.DBUtil;

public class ProductDB {
	public ProductDB() {
		super();
	}
	public ArrayList<Product> list() {
		ArrayList<Product> products = new ArrayList<>();
		String sql = "SELECT * FROM product";
		try
		{
			ResultSet rs = getResultSet(sql);
			while (rs.next()) {
				Product prod = getProductFromResultSet(rs);
				products.add(prod);
			}
			DBUtil.closeConnection();
		}
		catch (SQLException sqle) {
			System.out.println("Error retrieving all vendors.");
			sqle.printStackTrace();
		}

		return products;
	}

//	public Vendor get(int i) {
//		Vendor v = new Vendor();
//		String sql = "Select * FROM vendor WHERE id= " + i;
//		try
//		{
//
//			ResultSet rs = getResultSet(sql);
//			while(rs.next()) {
//				v = getVendorFromResultSet(rs);
//			}
//
//			DBUtil.closeConnection();
//			return v;
//		} catch (SQLException sqle) {
//			System.out.println("Error retrieving vendor");
//			sqle.printStackTrace();
//		}
//		return null;
//
//	}
//
//	public boolean add(Vendor v) {
//		String sql = "insert into vendor (code, name, address, city, state, zip, phone, email, preapproved)" +
//				"values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		int rowsUpdated = 0;
//		boolean success = false;
//
//		try (Connection connection = DBUtil.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // need the oid back after insert
//			ps.setString(1, v.getCode());
//			ps.setString(2, v.getName());
//			ps.setString(3, v.getAddress());
//			ps.setString(4, v.getCity());
//			ps.setString(5, v.getState());
//			ps.setString(6, v.getZip());
//			ps.setString(7, v.getPhone());
//			ps.setString(8, v.getEmail());
//			ps.setBoolean(9, v.isPreapproved());
//			rowsUpdated = ps.executeUpdate();
//			//upon successful insert, get the generated key from prepared statement
//			try (ResultSet generatedKey = ps.getGeneratedKeys()) {
//				if (generatedKey.next()) {
//					v.setId(generatedKey.getInt(1));
//				}
//			}
//		}
//		catch (SQLException sqle) {
//			System.out.println("Error adding vendor!");
//			sqle.printStackTrace();
//		}
//		if (rowsUpdated>0) success=true;
//		return success;
//	}
//
//	public boolean remove(Vendor v) {
//		String sql = "DELETE FROM Vendor WHERE ID = ?";
//		boolean success = false;
//
//		try (Connection connection = DBUtil.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql)) { 
//			ps.setInt(1, v.getId());
//			ps.executeUpdate();
//			success = true;
//
//
//		}
//		catch (SQLException sqle) {
//			System.out.println("Error removing Vendor!");
//			sqle.printStackTrace();
//		}
//		return success;
//	}
	private Product getProductFromResultSet(ResultSet rs) throws SQLException{
		Product p = new Product();
		p.setId(rs.getInt(1));
		p.setVendorID(rs.getInt(2));
		p.setVendorPartNumber(rs.getString(3));
		p.setName(rs.getString(4));
		p.setPrice(rs.getDouble(5));
		p.setUnit(rs.getString(6));
		p.setPhotoPath(rs.getString(7));
		



		return p;
	}
	private ResultSet getResultSet(String sqlStatement) throws SQLException{
		Connection connection = DBUtil.getConnection();
		PreparedStatement ps = connection.prepareStatement(sqlStatement);
		ResultSet rs = ps.executeQuery();
		return rs;

	}
}
