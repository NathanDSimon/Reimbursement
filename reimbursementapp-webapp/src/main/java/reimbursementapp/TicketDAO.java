package reimbursementapp;

import java.util.List;

public interface TicketDAO 
{
	public void open();
	public List<Ticket> getAllTickets();
	public List<Ticket> getTicketsByUser(User u);
	public Ticket getTicketByNumber(long num);
	public void addTicket(Ticket t);
	public void updateTicket(Ticket t);
	public long lowestUniqueTicketNumber();
	public void close();
}
