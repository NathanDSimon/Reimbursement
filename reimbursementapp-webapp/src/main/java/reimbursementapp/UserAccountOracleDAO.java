package reimbursementapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class UserAccountOracleDAO implements UserAccountDAO 
{
	static final Logger log = LogManager.getLogger(UserAccountOracleDAO.class.getName());
	private Connection conn;
	public void open() {
		log.info("Opening connection to database server.");
		System.out.println("open db connection");
		try {
			System.out.println("get driver");
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			System.out.println("get connection");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@javareact1.caw9n80kj8lb.us-east-2.rds.amazonaws.com:1521:ORCL", "admin", "12345678");
			System.out.println("got connection");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("connection error");
			log.error(e);
		}
	}

	public User getUserByName(String username) 
	{
		try {
			System.out.println("Username is: ");
			System.out.println(username);
			PreparedStatement s = conn.prepareStatement("SELECT * FROM REIMBURSEMENT.USERS WHERE USERNAME=?");
			s.setString(1, username);
			ResultSet rs = s.executeQuery();
			log.info("Executed query: ".concat(s.toString()));
			if(rs.next())
			{
				return new User(rs.getString("USERNAME"), rs.getString("PASS"), User.UserAccountType.decode(rs.getInt("TYPE")), rs.getString("EMAIL"));
			}
			else 
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateUser(User u) 
	{
		try {
			PreparedStatement s = conn.prepareStatement("UPDATE TABLE REIMBURSEMENT.USERS SET EMAIL=?, PASS=?, TYPE=? WHERE USERNAME=?");
			s.setString(1, u.getEmail());
			s.setString(2, u.getPass());
			s.setInt(3, u.getAccountType().getTypeCode());
			s.setString(4, u.getUsername());
			s.executeUpdate();
			log.info("Executed query: ".concat(s.toString()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteUser(User u) 
	{
		try {
			PreparedStatement s = conn.prepareStatement("DELETE FROM REIMBURSEMENT.USERS WHERE USERNAME=?");
			s.setString(1, u.getUsername());
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addUser(User u) 
	{
		try {
			PreparedStatement s = conn.prepareStatement("INSERT INTO REIMBURSEMENT.USERS VALUES (?,?,?,?)");
			s.setString(1, u.getUsername());
			s.setString(2, u.getPass());
			s.setString(3, u.getEmail());
			s.setInt(4, u.getAccountType().getTypeCode());
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
