package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;

/**
 * Servlet implementation class Search
 */
public class ShowGenre extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowGenre() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
        String loginUser = Credentials.admin;
        String loginPasswd = Credentials.password;
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type
        PrintWriter out = response.getWriter();

	
    	try {
			String limit = request.getParameter("limit");
			String offset = request.getParameter("offset");
			String genre = request.getParameter("genre");
			String orderby = request.getParameter("orderby");
						
			if(limit==null)
				limit = "10";
			if(offset == null)
				offset = "0";
			if(genre == null)
				genre = "Thriller";
			
			if(orderby == null)
				orderby = "asc_t";
			
			
			

			System.out.println("genre = " + genre);
			
//
//		String query = "select m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url from movies as m, genres_in_movies as gm, genres as g where m.id = gm.movie_id and g.id = gm.genre_id and g.name = '" + genre + "' order by g.name, m.title, m.year, m.director LIMIT "+ limit +" OFFSET "+offset;
		String query = "select m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url from movies as m, genres_in_movies as gm, genres as g where m.id = gm.movie_id and g.id = gm.genre_id and g.name = '" + genre + "'";

		
		switch(orderby)
		{
		default:
			query += " order by title";
			break;
		case "desc_t":
			query += " order by title DESC";
			break;
		case "asc_y":
			query += " order by year";
			break;
		case "desc_y":
			query += " order by year DESC";
			break;
		}
		
		query += " LIMIT "+ limit +" OFFSET "+offset;
		
        List<Movie> movies = Movie.getMovies(query);

        for(Movie m: movies)
        {
            m.setGenres(Movie.getGenres(m.getId()));
            m.setStars(Movie.getStars(m.getId()));
        }
       
		query = "SELECT COUNT(*) as count from movies as m, genres_in_movies as gm, genres as g where m.id = gm.movie_id and g.id = gm.genre_id and g.name = '" + genre + "'";

	    String num_of_movies = MySQL.select(query).get(0).get("count").toString();
	
	    
	    System.out.print(num_of_movies);
	    
	    
	    ArrayList<String> all_genres = new ArrayList<String>();
    	
        query = "select name from genres order by name";
        
        ArrayList<Map<String, Object>> results = MySQL.select(query);
    	
        for(Map<String, Object> row : results)
        	all_genres.add(row.get("name").toString());
        
        request.getSession().setAttribute("movies", movies);
        request.getSession().setAttribute("offset", offset);
        request.getSession().setAttribute("limit", limit);
        request.getSession().setAttribute("genre", genre);
        request.getSession().setAttribute("all_genres", all_genres);
        request.getSession().setAttribute("num_of_movies", num_of_movies);
        request.getSession().setAttribute("orderby", orderby);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/showGenre.jsp");
        dispatcher.forward(request, response);
        
	    } catch (Exception e)
	    {
	      out.println("Invalid SQL Command.\n\n" + e.toString());
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
