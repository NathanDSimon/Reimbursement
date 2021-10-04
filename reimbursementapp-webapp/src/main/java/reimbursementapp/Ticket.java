package reimbursementapp;

public class Ticket 
{
	public enum TicketType
	{
		lodging(0),
		travel(1),
		food(2),
		other(3);
		
		private final int type;
		private TicketType(int type)
		{
			this.type = type;
		}
		public int getTypeCode()
		{
			return this.type;
		}
		public static TicketType decode(int code)
		{
			switch(code)
			{
			case 1:
				return travel;
			case 2:
				return food;
			case 0:
				return lodging;
			default:
				return other;	
			}
		}
		public String toString()
		{
			switch(type)
			{
			case 0:
				return "lodging";
			case 1:
				return "travel";
			case 2:
				return "food";
			default:
				return "other";	
			}
		}
	}
	public enum TicketStatus
	{
		pending(0),
		approved(1),
		denied(-1);
		
		private final int statusCode;
		private TicketStatus(int code)
		{
			this.statusCode = code;
		}
		public int getStatusCode()
		{
			return this.statusCode;
		}
		public static TicketStatus decode(int code)
		{
			if(code == -1) return denied;
			if(code == 1) return approved;
			return pending;
		}
		public String toString()
		{
			switch(statusCode)
			{
			case 1:
				return "approved";
			case -1:
				return "denied";
			default:
				return "pending";
			}
		}
	}
	private TicketType type;
	private double amount;
	private User owner;
	private TicketStatus status;
	private String desc;
	private long ticketNumber;
	public long getTicketNumber()
	{
		return ticketNumber;
	}
	public TicketType getType()
	{
		return type;
	}
	public double getAmount()
	{
		return this.amount;
	}
	public User getOwner()
	{
		return this.owner;
	}
	public TicketStatus getStatus()
	{
		return this.status;
	}
	public void setStatus(TicketStatus s)
	{
		this.status = s;
	}
	public String getDesc()
	{
		return desc;
	}
	public Ticket(TicketType type, double amount, User owner, String desc, long number)
	{
		this.type = type;
		this.amount = amount;
		this.owner = owner;
		this.status = TicketStatus.pending;
		this.desc = desc;
		this.ticketNumber = number;
	}
	public Ticket(TicketType type, double amount, User owner, String desc)
	{
		TicketDAO temp = new TicketOracleDAO();
		temp.open();
		this.ticketNumber = temp.lowestUniqueTicketNumber();
		temp.close();
		this.type = type;
		this.amount = amount;
		this.owner = owner;
		this.desc = desc;
		this.status = TicketStatus.pending;
	}
}
