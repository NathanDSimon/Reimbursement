package reimbursementapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CreateUser
 */
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserAccountDAO userAccess = new UserAccountOracleDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUser() 
    {
        super();
    }

    /**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		userAccess.open();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() 
	{
		userAccess.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		HttpSession s = request.getSession();
		PrintWriter out = response.getWriter();
		if(s.getAttribute("username") != null && userAccess.getUserByName((String)(s.getAttribute("username"))).comparePassword((String)(s.getAttribute("authToken"))))
		{
			if(!(userAccess.getUserByName((String)s.getAttribute("username")).getAccountType() == User.UserAccountType.admin))
			{
				out.write("<div class=\"error-banner\">You don't have access to that.</div>");
				RequestDispatcher rd = request.getRequestDispatcher("/maincp");
				rd.forward(request, response);
				return;
			}
			userAccess.addUser(new User(request.getParameter("new_user_name"), request.getParameter("new_user_pass"), User.UserAccountType.decode(Integer.parseInt(request.getParameter("new_acct_type"))), request.getParameter("new_user_email")));
			out.write("<div class=\"success-banner\">Added user account.</div>");
			RequestDispatcher rd = request.getRequestDispatcher("/maincp");
			rd.forward(request, response);
		}
		else 
		{
			out.write("<div class=\"error-banner\">You must log in first!</div>");
			RequestDispatcher rd = request.getRequestDispatcher("/index.html");
			rd.include(request, response);
		}
	}

}
