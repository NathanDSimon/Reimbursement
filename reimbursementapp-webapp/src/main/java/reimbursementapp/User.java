package reimbursementapp;

public class User 
{
	private String username;
	private String password;
	private UserAccountType accountType;
	private String email;
	public String getUsername()
	{
		return username;
	}
	public boolean comparePassword(String other)
	{
		return this.password.compareTo(other) == 0;
	}
	String getPass()
	{
		return this.password;
	}
	public void setPassword(String newpass)
	{
		password = newpass;
	}
	public UserAccountType getAccountType()
	{
		return accountType;
	}
	public String getEmail()
	{
		return email;
	}
	public enum UserAccountType
	{
		employee(0),
		admin(1);
		private final int m_type;
		private UserAccountType(int type)
		{
			this.m_type = type;
		}
		public int getTypeCode()
		{
			return this.m_type;
		}
		public static UserAccountType decode(int code)
		{
			if(code == 1) return admin;
			return employee;
		}
	}
	public User(String username, String password, UserAccountType accountType, String email)
	{
		this.username = username;
		this.password = password;
		this.accountType = accountType;
		this.email = email;
	}
}