package reimbursementapp;

public interface UserAccountDAO 
{
	public void open();
	public User getUserByName(String username);
	public void updateUser(User u);
	public void deleteUser(User u);
	public void addUser(User u);
	public void close();
}
