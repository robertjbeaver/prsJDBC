package business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import db.DBUtil;

public class UserDB {
	public UserDB() {
		super();
	}
	public ArrayList<User> list() {
		ArrayList<User> users = new ArrayList<>();
		String sql = "SELECT * FROM user";
		try
		{
			ResultSet rs = getResultSet(sql);
			while (rs.next()) {
				User usr = getUserFromResultSet(rs);
				users.add(usr);
			}
			DBUtil.closeConnection();
		}
		catch (SQLException sqle) {
			System.out.println("Error retrieving all users.");
			sqle.printStackTrace();
		}

		return users;
	}

	public User get(int i) {
		User u = new User();
		String sql = "Select * From user WHERE id= " + i;
		try
		{

			ResultSet rs = getResultSet(sql);
			while(rs.next()) {
				u = getUserFromResultSet(rs);
			}

			DBUtil.closeConnection();
			return u;
		} catch (SQLException sqle) {
			System.out.println("Error retrieving user");
			sqle.printStackTrace();
		}
		return null;

	}

	public boolean add(User u) {
		String sql = "insert into user (UserName,Password,FirstName,LastName,Phone,Email,IsReviewer,IsAdmin)" +
				"values (?, ?, ?, ?, ?, ?, ?, ?)";
		int rowsUpdated = 0;
		boolean success = false;

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // need the oid back after insert
			ps.setString(1, u.getUserName());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getFirstName());
			ps.setString(4, u.getLastName());
			ps.setString(5, u.getPhoneNumber());
			ps.setString(6, u.getEmail());
			ps.setBoolean(7, u.isReviewer());
			ps.setBoolean(8, u.isAdmin());
			rowsUpdated = ps.executeUpdate();
			//upon successful insert, get the generated key from prepared statement
			try (ResultSet generatedKey = ps.getGeneratedKeys()) {
				if (generatedKey.next()) {
					u.setId(generatedKey.getInt(1));
				}
			}
		}
		catch (SQLException sqle) {
			System.out.println("Error adding user!");
			sqle.printStackTrace();
		}
		if (rowsUpdated>0) success=true;
		return success;
	}

	public boolean remove(User u) {
		String sql = "DELETE FROM User WHERE ID = ?";
		boolean success = false;

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) { 
			ps.setInt(1, u.getId());
			ps.executeUpdate();
			success = true;


		}
		catch (SQLException sqle) {
			System.out.println("Error removing user!");
			sqle.printStackTrace();
		}
		return success;
	}
	private User getUserFromResultSet(ResultSet rs) throws SQLException{
		User u = new User();
		u.setId(rs.getInt(1));
		u.setUserName(rs.getString(2));
		u.setPassword(rs.getString(3));
		u.setFirstName(rs.getString(4));
		u.setLastName(rs.getString(5));
		u.setPhoneNumber(rs.getString(6));
		u.setEmail(rs.getString(7));
		u.setReviewer(rs.getBoolean(8));
		u.setAdmin(rs.getBoolean(9));
		String dateTimeString = rs.getString(11);
		StringBuilder dateTimeStringT = new StringBuilder(dateTimeString);
		dateTimeStringT.setCharAt(10, 'T');
		System.out.println(dateTimeStringT);
		//LocalDateTime createDateT = LocalDateTime.parse(dateTimeStringT);
		//u.setDateCreated(createDateT);



		return u;
	}
	private ResultSet getResultSet(String sqlStatement) throws SQLException{
		Connection connection = DBUtil.getConnection();
		PreparedStatement ps = connection.prepareStatement(sqlStatement);
		ResultSet rs = ps.executeQuery();
		return rs;

	}
}
