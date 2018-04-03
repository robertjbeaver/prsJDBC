package business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import db.DBUtil;

public class VendorDB {
	
	public VendorDB() {
		super();
		
	}
	public ArrayList<Vendor> list() {
		ArrayList<Vendor> vendors = new ArrayList<>();
		String sql = "SELECT * FROM vendor";
		try
		{
			ResultSet rs = getResultSet(sql);
			while (rs.next()) {
				Vendor vend = getVendorFromResultSet(rs);
				vendors.add(vend);
			}
			DBUtil.closeConnection();
		}
		catch (SQLException sqle) {
			System.out.println("Error retrieving all vendors.");
			sqle.printStackTrace();
		}

		return vendors;
	}

	public Vendor get(int i) {
		Vendor v = new Vendor();
		String sql = "Select * FROM vendor WHERE id= " + i;
		try
		{

			ResultSet rs = getResultSet(sql);
			while(rs.next()) {
				v = getVendorFromResultSet(rs);
			}

			DBUtil.closeConnection();
			return v;
		} catch (SQLException sqle) {
			System.out.println("Error retrieving vendor");
			sqle.printStackTrace();
		}
		return null;

	}

	public boolean add(Vendor v) {
		String sql = "insert into vendor (code, name, address, city, state, zip, phone, email, preapproved)" +
				"values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int rowsUpdated = 0;
		boolean success = false;

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // need the oid back after insert
			ps.setString(1, v.getCode());
			ps.setString(2, v.getName());
			ps.setString(3, v.getAddress());
			ps.setString(4, v.getCity());
			ps.setString(5, v.getState());
			ps.setString(6, v.getZip());
			ps.setString(7, v.getPhone());
			ps.setString(8, v.getEmail());
			ps.setBoolean(9, v.isPreapproved());
			rowsUpdated = ps.executeUpdate();
			//upon successful insert, get the generated key from prepared statement
			try (ResultSet generatedKey = ps.getGeneratedKeys()) {
				if (generatedKey.next()) {
					v.setId(generatedKey.getInt(1));
				}
			}
		}
		catch (SQLException sqle) {
			System.out.println("Error adding vendor!");
			sqle.printStackTrace();
		}
		if (rowsUpdated>0) success=true;
		return success;
	}

	public boolean remove(Vendor v) {
		String sql = "DELETE FROM Vendor WHERE ID = ?";
		boolean success = false;

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) { 
			ps.setInt(1, v.getId());
			ps.executeUpdate();
			success = true;


		}
		catch (SQLException sqle) {
			System.out.println("Error removing Vendor!");
			sqle.printStackTrace();
		}
		return success;
	}
	private Vendor getVendorFromResultSet(ResultSet rs) throws SQLException{
		Vendor v = new Vendor();
		v.setId(rs.getInt(1));
		v.setCode(rs.getString(2));
		v.setName(rs.getString(3));
		v.setAddress(rs.getString(4));
		v.setCity(rs.getString(5));
		v.setState(rs.getString(6));
		v.setZip(rs.getString(7));
		v.setPhone(rs.getString(8));
		v.setEmail(rs.getString(9));
		v.setPreapproved(rs.getBoolean(10));
		



		return v;
	}
	private ResultSet getResultSet(String sqlStatement) throws SQLException{
		Connection connection = DBUtil.getConnection();
		PreparedStatement ps = connection.prepareStatement(sqlStatement);
		ResultSet rs = ps.executeQuery();
		return rs;

	}
}
