package reimbursementapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import reimbursementapp.Ticket.TicketStatus;

/**
 * Servlet implementation class TicketApproveDeny
 */
public class TicketApproveDeny extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserAccountDAO userAccess = new UserAccountOracleDAO();
	private TicketDAO ticketAccess = new TicketOracleDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TicketApproveDeny() 
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
			if(!(userAccess.getUserByName((String)s.getAttribute("username")).getAccountType() == User.UserAccountType.admin))
			{
				out.write("<div class=\"error-banner\">You don't have access to that.</div>");
				RequestDispatcher rd = request.getRequestDispatcher("/maincp");
				rd.forward(request, response);
				return;
			}
			Map<String, String[]> params = request.getParameterMap();
			for(Map.Entry<String, String[]> e : params.entrySet())
			{
				//key is in format "ticket_<NUMBER>_action
				//value is action "approve", "deny", or "do nothing"
				String k = e.getKey();
				long num = Long.parseLong(k.split("_")[1]);
				Ticket t = ticketAccess.getTicketByNumber(num);
				if(e.getValue()[0].compareTo("approve") == 0)
				{
					t.setStatus(TicketStatus.approved);
				}
				else if (e.getValue()[0].compareTo("deny") == 0)
				{
					t.setStatus(TicketStatus.denied);
				}
				ticketAccess.updateTicket(t);
			}
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
