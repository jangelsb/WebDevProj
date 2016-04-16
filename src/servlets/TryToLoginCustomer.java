package servlets;

//slight change 2
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import model.*;

public class TryToLoginCustomer extends HttpServlet
{
	public String getServletInfo()
	{
		return "Servlet connects to MySQL database and displays result of a SELECT";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		
		
		if(request.getQueryString() == null)
		{
//			RequestDispatcher dispatcher = request.getRequestDispatcher("login.html");
//			dispatcher.forward(request, response);
//			
			response.sendRedirect("login.html");
		}
		
		
		
		Integer shopping_cart_size = (Integer) request.getSession().getAttribute("shopping_cart_size");
		
		if(shopping_cart_size == null)
			request.getSession().setAttribute("shopping_cart_size", 0);
		
		
		String loginUser = Credentials.admin;
		String loginPasswd = Credentials.password;
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		response.setContentType("text/html");    // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		try
		{

			String username = request.getParameter("username");
			String password = request.getParameter("password");

			String query = "select id from customers where email like '"+username+"' and password like '"+password+"'";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			Statement selectStmt = db_connection.createStatement();
			ResultSet results = selectStmt.executeQuery(query);

			boolean empty = true;

			String user_id = "";

			while(results.next()){

				empty = false;

				user_id = results.getString("id");

				break;
			}

			results.close();
			selectStmt.close();
			db_connection.close();

			if(empty)
			{
				out.print("false");
			}
			else
			{
				out.print(user_id); 
				
				
				System.out.print("Logged in user id: " + user_id); 

				request.getSession().setAttribute("user_id", user_id);

				
//				response.sendRedirect("/home.jsp");
				//We should probably redirect to another page if they login correctly
//                RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
//                dispatcher.forward(request, response);
//				
//				request.getSession().setAttribute("user_name", user_name);
			}

		} catch (Exception e)
		{
			out.print("false");
			System.out.println("Invalid SQL Command.\n\n" + e.toString());
		}

		out.close();
	}

	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}
}