package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
/**
 * Servlet implementation class Search
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	
        response.setContentType("text/html");    // Response mime type
        PrintWriter out = response.getWriter();
  
    	try {
			String limit = request.getParameter("limit");
			String offset = request.getParameter("offset");
			String orderby = request.getParameter("orderby");

			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String fName = request.getParameter("first_name");
			String lName = request.getParameter("last_name");

			String advance =  request.getParameter("adv");
					
			if(limit==null)
				limit = "10";
			if(offset == null)
				offset = "0";
			if(title == null)
				title = "";
			if(orderby == null)
				orderby = "asc_t";

		String query = "";
		String count_query= "";
		String wordsToSearch[] = title.split(" ");
		String partsOfTitles = "";
		String fuzzyCheck = request.getParameter("fuzzy_search");
		for(int k = 0; k < wordsToSearch.length; k++){	
			if((k != wordsToSearch.length -1)||(wordsToSearch.length == 1))
				partsOfTitles += "%"+wordsToSearch[k] +"%";
			else
				partsOfTitles += " "+wordsToSearch[k] +"%";
    	}
		if(advance == null || !advance.equals("true") || "".equals(wordsToSearch) || (wordsToSearch.length == 1 && wordsToSearch[0].length() == 1))
		{
			query = "SELECT * FROM movies as m WHERE title like '"+title+"%'";
		}
		else
		{	
			if(fuzzyCheck == null){
				System.out.println(fuzzyCheck);
				query = "SELECT distinct m.id, title, year, director, banner_url, trailer_url "
							+ "FROM movies as m, stars_in_movies as sm, stars as s "
							+ "WHERE sm.star_id = s.id AND m.id = sm.movie_id "
							+ "AND m.title like '" + partsOfTitles + "'";
				if (!"".equals(year))
					query += " AND m.year like '%" + year + "'";
				if (!"".equals(director))
					query += " AND m.director like '%" + director + "%'";
				if (!"".equals(fName)) 
					query += " AND s.first_name like '%" + fName + "%'";
				if (!"".equals(lName))
					query += " AND s.last_name like '%" + lName + "%'";
			}else{
				query += "DROP FUNCTION IF EXISTS edth; ";
				MySQL.createFunction(query);
				query = "CREATE FUNCTION edth RETURNS INTEGER SONAME 'libedth.so';";
				MySQL.createFunction(query);
				query = "SELECT distinct m.id, title, year, director, banner_url, trailer_url "
						+ "FROM movies as m, stars_in_movies as sm, stars as s "
						+ "WHERE sm.star_id = s.id AND m.id = sm.movie_id ";
				if (!"".equals(title))
					query += "AND edth(title, '" + title + "',3)";
				else
					query += "AND title like '" + partsOfTitles + "'";;
				if (!"".equals(year))
					query += " AND m.year like '%" + year + "'";
				if (!"".equals(director))
					query += " AND edth(m.director, '" + director + "',2)";
				if (!"".equals(fName)) 
					query += " AND edth(s.first_name, '" + fName + "',2)";
				if (!"".equals(lName))
					query += " AND edth(s.last_name, '" + lName + "',2)";
			}
			
		}
		count_query = query;
		switch(orderby)
		{
		default:
			query += " order by title";
			break;
		case "desc_t":
			query += " order by title DESC";
			break;
		case "asc_y":
			query += " order by year, title";
			break;
		case "desc_y":
			query += " order by year DESC, title";
			break;
		}
		query += " LIMIT "+ limit +" OFFSET "+offset;
		
        List<Movie> movies = new ArrayList<Movie>();
        movies = Movie.getMovies(query);
        if(advance == null)
    		count_query = "SELECT COUNT(*) as count " + count_query.substring(count_query.indexOf("FROM"));
        else
    		count_query = "SELECT COUNT(distinct m.id) as count " + count_query.substring(count_query.indexOf("FROM"));
        String num_of_movies = MySQL.select(count_query).get(0).get("count").toString();        
        request.getSession().setAttribute("movies", movies);
        request.getSession().setAttribute("offset", offset);
        request.getSession().setAttribute("limit", limit);
        request.getSession().setAttribute("num_of_movies", num_of_movies);
        request.getSession().setAttribute("orderby", orderby);
        
        request.getSession().setAttribute("title", title);

		request.getSession().setAttribute("year", year);
		request.getSession().setAttribute("director", director);
		request.getSession().setAttribute("first_name", fName);
		request.getSession().setAttribute("last_name", lName);
		request.getSession().setAttribute("adv", advance);
		request.getSession().setAttribute("fuzzy_search", fuzzyCheck);

        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
        dispatcher.forward(request, response);
        
	    } catch (Exception e)
	    {
			request.getSession().setAttribute("error_message", "Invalid SQL Command [Search].\n\n" + e.toString());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

//package servlets;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import model.*;
//
///**
// * Servlet implementation class Search
// */
//public class Search extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public Search() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		
//		
//        response.setContentType("text/html");    // Response mime type
//        PrintWriter out = response.getWriter();
//  
//    	try {
//			String limit = request.getParameter("limit");
//			String offset = request.getParameter("offset");
//			String orderby = request.getParameter("orderby");
//
//			String title = request.getParameter("title");
//			String year = request.getParameter("year");
//			String director = request.getParameter("director");
//			String fName = request.getParameter("first_name");
//			String lName = request.getParameter("last_name");
//
//			String advance =  request.getParameter("adv");
//			
////			System.out.println(advance);
//					
//			if(limit==null)
//				limit = "10";
//			if(offset == null)
//				offset = "0";
//			if(title == null)
//				title = "";
//			if(orderby == null)
//				orderby = "asc_t";
//
//		String query = "";
//		ArrayList<String> queryList = new ArrayList<String>();
//		if(advance == null || !advance.equals("true"))
//		{
//			query = "SELECT * FROM movies WHERE title like '"+title+"%'";
//		}
//		else
//		{
//			
//			query = "SELECT distinct m.id, title, year, director, banner_url, trailer_url "
//					+ "FROM movies as m, stars_in_movies as sm, stars as s "
//					+ "WHERE sm.star_id = s.id AND m.id = sm.movie_id "
//					+ "AND m.title like '%" + title + "%'";
//
//			if (year != null)
//				query += " AND m.year like '%" + year + "'";
//			if (director != null)
//				query += " AND m.director like '%" + director + "%'";
//			if (fName != null) 
//				query += " AND s.first_name like '%" + fName + "%'";
//			if (lName != null)
//				query += " AND s.last_name like '%" + lName + "%'";
//		}
//
//
//		String count_query = query;
//		
//		switch(orderby)
//		{
//		default:
//			query += " order by title";
//			break;
//		case "desc_t":
//			query += " order by title DESC";
//			break;
//		case "asc_y":
//			query += " order by year, title";
//			break;
//		case "desc_y":
//			query += " order by year DESC, title";
//			break;
//		}
//		
//		query += " LIMIT "+ limit +" OFFSET "+offset;
//		
//        List<Movie> movies = null;
//        List<Movie> allMovies = null;
//        if(advance == null){
//        	allMovies = Movie.getMovies(queryList.get(0));
//        	for(int i = 1; i < queryList.size(); i++ ){
//        		List<Movie> copyMovies = new ArrayList<Movie>(allMovies);
//        		List<Movie> nextWord = Movie.getMovies(queryList.get(i));
//        		copyMovies.removeAll(nextWord);
//        		allMovies.removeAll(copyMovies);
//        	}
//        	/*
//        	for(int j = Integer.parseInt(offset); j < ((Integer.parseInt(limit) + Integer.parseInt(offset))-1); j++){
//        		try{
//        			movies.add(allMovies.get(j));
//        		}catch(Exception e){
//        			out.print("Wtf " + j);
//        			break;
//        		}
//        	}*/
//        	movies = new ArrayList<Movie>(allMovies);
//        	
//        }else
//        	movies = Movie.getMovies(query);
//        String num_of_movies = "";
//        //if(advance == null)
//    	//	count_query = "SELECT COUNT(distinct m.id) as count " + count_query.substring(count_query.indexOf("FROM"));
//		/*
//        if(advance == null)
//			num_of_movies = Integer.toString(movies.size());
//		else
//        	num_of_movies = MySQL.select(count_query).get(0).get("count").toString();
//        */
//        request.getSession().setAttribute("movies", movies);
//        request.getSession().setAttribute("offset", offset);
//        request.getSession().setAttribute("limit", limit);
//        request.getSession().setAttribute("num_of_movies", num_of_movies);
//        request.getSession().setAttribute("orderby", orderby);
//        
//        request.getSession().setAttribute("title", title);
//
//		request.getSession().setAttribute("year", year);
//		request.getSession().setAttribute("director", director);
//		request.getSession().setAttribute("first_name", fName);
//		request.getSession().setAttribute("last_name", lName);
//
//		request.getSession().setAttribute("adv", advance);
//
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
//		dispatcher.forward(request, response);
//        //out.print(new Gson().toJson(movies));
//
//	    } catch (Exception e)
//	    {
//	    	out.print("false");
//	    }
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
//
//}


