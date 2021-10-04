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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class TicketAdmin
 */
public class TicketAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserAccountDAO userAccess = new UserAccountOracleDAO();
	private TicketDAO ticketAccess = new TicketOracleDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TicketAdmin()
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
			JSONArray ja = new JSONArray();
			for(Ticket t: ticketAccess.getAllTickets()) 
			{
				if(t.getStatus() != Ticket.TicketStatus.pending)
				{
					continue;
				}
				JSONObject obj = new JSONObject();
				obj.put("ticketNumber", t.getTicketNumber());
				obj.put("amount", t.getAmount());
				obj.put("username", t.getOwner().getUsername());
				obj.put("type", t.getType().toString());
				obj.put("status", t.getStatus().toString());
				obj.put("description", t.getDesc());
				ja.put(obj);
				System.out.println(obj.toString());
			}
			request.setAttribute("tickets_admin", ja.toString().replaceAll("'", "&#39;"));
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
