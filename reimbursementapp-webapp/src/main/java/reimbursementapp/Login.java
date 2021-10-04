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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger log = LogManager.getLogger(Login.class.getName());
	private UserAccountDAO userAccess = new UserAccountOracleDAO();
;
    /**
     * Default constructor. 
     */
    public Login() 
    {
    	super();
    	log.info("Initialize Login servlet...");
    	System.out.println("initialize...");
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		userAccess.open();
		log.info("Completed call to Login.init()");
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Service request");
		System.out.println("service request");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("userName");
		String pass = request.getParameter("pass");
		HttpSession session = request.getSession(true);
		log.info("Got session");
		System.out.println("Got session");
		if(userAccess.getUserByName(username) != null && userAccess.getUserByName(username).comparePassword(pass))
		{
			session.setAttribute("username", username);
			session.setAttribute("authToken", pass);
			session.setAttribute("authenticated", true);
			session.setAttribute("isAdmin", userAccess.getUserByName(username).getAccountType() == User.UserAccountType.admin);
			RequestDispatcher rd = request.getRequestDispatcher("/maincp");
			rd.forward(request, response);
		}
		else
		{
			out.write("<div class=\"error-banner\" id=\"login-error\">Incorrect username or password.</div>");
			RequestDispatcher rd=request.getRequestDispatcher("/index.html");  
	        rd.include(request, response);  
		}
	}

}
