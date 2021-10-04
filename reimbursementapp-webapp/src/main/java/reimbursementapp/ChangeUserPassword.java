package reimbursementapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class ChangeUserPassword
 */
@WebServlet("/chpass")
public class ChangeUserPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserAccountDAO userAccess = new UserAccountOracleDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeUserPassword()
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
			User u = userAccess.getUserByName((String)(s.getAttribute("username")));
			if(!u.comparePassword(request.getParameter("passconfirm")))
			{
				out.write("<div class=\"error-banner\" id=\"change-pass-error\">Original password is incorrect.</div>");
			}
			else
			{
				u.setPassword(request.getParameter("newpass"));
				userAccess.updateUser(u);
				out.write("<div class=\"success-banner\">Password changed.</div>");
			}
			RequestDispatcher rd = request.getRequestDispatcher("/maincp");
			rd.include(request, response);
		}
		else 
		{
			out.write("You must log in first!");
			RequestDispatcher rd = request.getRequestDispatcher("/index.html");
			rd.include(request, response);
		}
	}

}
