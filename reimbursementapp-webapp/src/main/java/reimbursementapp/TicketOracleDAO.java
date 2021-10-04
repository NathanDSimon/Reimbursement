package reimbursementapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketOracleDAO implements TicketDAO {

	private Connection conn;
	private UserAccountDAO users = new UserAccountOracleDAO();
	public void open() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@javareact1.caw9n80kj8lb.us-east-2.rds.amazonaws.com:1521:ORCL", "admin", "12345678");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		users.open();
	}

	public List<Ticket> getAllTickets() {
		List<Ticket> ret = new ArrayList<Ticket>();
		try {
			PreparedStatement s = conn.prepareStatement("SELECT * FROM REIMBURSEMENT.TICKETS");
			ResultSet rs = s.executeQuery();
			while(rs.next())
			{
				Ticket t = new Ticket(Ticket.TicketType.decode(rs.getInt("TYPE")), 
						rs.getDouble("AMOUNT"),
						users.getUserByName(rs.getString("USERNAME")),
						rs.getString("DESCRIPTION"),
						rs.getLong("TICKETNUM"));
				t.setStatus(Ticket.TicketStatus.decode(rs.getInt("STATUS")));
				ret.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<Ticket> getTicketsByUser(User u) 
	{
		List<Ticket> ret = new ArrayList<Ticket>();
		try {
			PreparedStatement s = conn.prepareStatement("SELECT * FROM REIMBURSEMENT.TICKETS WHERE USERNAME=?");
			s.setString(1, u.getUsername());
			ResultSet rs = s.executeQuery();
			while(rs.next())
			{
				Ticket T = new Ticket(Ticket.TicketType.decode(rs.getInt("TYPE")), 
						rs.getDouble("AMOUNT"),
						u,
						rs.getString("DESCRIPTION"),
						rs.getLong("TICKETNUM"));
				T.setStatus(Ticket.TicketStatus.decode(rs.getInt("STATUS")));
				ret.add(T);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public void addTicket(Ticket t) 
	{
		try {
			PreparedStatement s = conn.prepareStatement("INSERT INTO REIMBURSEMENT.TICKETS VALUES (?,?,?,?,?,?)");
			s.setString(1, t.getOwner().getUsername());
			s.setDouble(2, t.getAmount());
			s.setInt(3, t.getType().getTypeCode());
			s.setInt(4, t.getStatus().getStatusCode());
			s.setString(5, t.getDesc());
			s.setLong(6, t.getTicketNumber());
			s.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTicket(Ticket t) 
	{
		try {
			PreparedStatement s = conn.prepareStatement("UPDATE REIMBURSEMENT.TICKETS SET AMOUNT=?,STATUS=?,TYPE=?,DESCRIPTION=? WHERE TICKETNUM=?");
			s.setDouble(1, t.getAmount());
			s.setInt(2, t.getStatus().getStatusCode());
			s.setInt(3, t.getType().getTypeCode());
			s.setString(4, t.getDesc());
			s.setLong(5, t.getTicketNumber());
			s.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		users.close();
	}
	public long lowestUniqueTicketNumber() 
	{
		try {
			PreparedStatement s = conn.prepareStatement("SELECT MAX(TICKETNUM) FROM REIMBURSEMENT.TICKETS");
			ResultSet rs = s.executeQuery();
			if(rs.next())
			{
				return rs.getLong("MAX(TICKETNUM)") + 1l;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Ticket getTicketByNumber(long num) {
		try {
			PreparedStatement s = conn.prepareStatement("SELECT * FROM REIMBURSEMENT.TICKETS WHERE TICKETNUM=?");
			s.setLong(1, num);
			ResultSet rs = s.executeQuery();
			if(rs.next())
			{
				Ticket t = new Ticket(Ticket.TicketType.decode(rs.getInt("TYPE")),
						rs.getDouble("AMOUNT"),
						users.getUserByName(rs.getString("USERNAME")),
						rs.getString("DESCRIPTION"),
						num
				);
				t.setStatus(Ticket.TicketStatus.decode(rs.getInt("STATUS")));
				return t;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
