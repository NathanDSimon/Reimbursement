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
 * Servlet implementation class TicketView
 */
public class TicketView extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserAccountDAO userAccess = new UserAccountOracleDAO();
    private TicketDAO ticketAccess = new TicketOracleDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TicketView() 
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
			JSONArray ja = new JSONArray();
			for(Ticket t: ticketAccess.getTicketsByUser(userAccess.getUserByName((String)(s.getAttribute("username"))))) 
			{
				JSONObject obj = new JSONObject();
				obj.put("ticketNumber", t.getTicketNumber());
				obj.put("amount", t.getAmount());
				obj.put("username", t.getOwner().getUsername());
				obj.put("type", t.getType().toString());
				obj.put("status", t.getStatus().toString());
				obj.put("description", t.getDesc());
				ja.put(obj);
			}
			request.setAttribute("tickets_view", ja.toString().replaceAll("'", "&#39;"));
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
