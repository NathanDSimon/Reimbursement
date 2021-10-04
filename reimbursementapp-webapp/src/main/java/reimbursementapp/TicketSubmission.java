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
 * Servlet implementation class TicketSubmission
 */
public class TicketSubmission extends HttpServlet {
	private static final long serialVersionUID = 1L;
    UserAccountDAO userAccess = new UserAccountOracleDAO();
    TicketDAO ticketAccess = new TicketOracleDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TicketSubmission() 
    {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		userAccess.open();
		ticketAccess.open();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy()
	{
		userAccess.close();
		ticketAccess.close();
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
			try {
				Ticket t = new Ticket(Ticket.TicketType.decode(Integer.parseInt(request.getParameter("ticketType"))), Double.parseDouble(request.getParameter("amount")), userAccess.getUserByName((String)s.getAttribute("username")), request.getParameter("desc"));
				ticketAccess.addTicket(t);
				out.write("The ticket has been submitted for review.");
			} catch(NumberFormatException e) {
				e.printStackTrace();
				out.write("There was an error: one of the required inputs was improperly formatted.");
			} finally {
				RequestDispatcher rd = request.getRequestDispatcher("/maincp");
				rd.include(request, response);
			}
		}
		else {
			out.write("You must log in first!");
			RequestDispatcher rd = request.getRequestDispatcher("/index.html");
			rd.include(request, response);
		}
	}

}
