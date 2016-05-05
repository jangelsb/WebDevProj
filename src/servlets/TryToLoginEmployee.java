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

public class TryToLoginEmployee extends HttpServlet
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
			response.sendRedirect("login_emp.html");
		}


		String loginUser = Credentials.admin;
		String loginPasswd = Credentials.password;
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		response.setContentType("text/html");    // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		try
		{


			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
			// Verify CAPTCHA.
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
            if (Credentials.RECAPTCHA_STATE && !valid) {
            	out.print("false");
			}
			else
			{
				String email = request.getParameter("email");
				String password = request.getParameter("password");

				String query = "select email from employees where email like '"+email+"' and password like '"+password+"'";

				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
				Statement selectStmt = db_connection.createStatement();
				ResultSet results = selectStmt.executeQuery(query);

				boolean empty = true;

				String emp_email = "";

				while(results.next()){

					empty = false;

					emp_email = results.getString("email");

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
					//this prints out to the AJAX call
					out.print(emp_email); 
					System.out.println("Logged in employee id: " + emp_email); 

					request.getSession().setAttribute("emp_email", emp_email);
				}
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