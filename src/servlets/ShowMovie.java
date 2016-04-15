package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
/**
 * Servlet implementation class ShowMovie
 */
public class ShowMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowMovie() {
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
			String movie_id = request.getParameter("movie_id");
						
			if(movie_id==null)
				movie_id = "10";
			
		String query = "SELECT * FROM movies WHERE id = " + movie_id;
        
        List<Movie> movies = Movie.getMovies(query);

        for(Movie m: movies)
        {
            m.setGenres(Movie.getGenres(m.getId()));
            m.setStars(Movie.getStars(m.getId()));
        }
        
//        Movie movie = movies.get(0); //there will only be 1 movie in the list.
//
//        movie.setGenres(Movie.getGenres(movie.getId()));
//        movie.setStars(Movie.getStars(movie.getId()));
        
        request.getSession().setAttribute("movies", movies);
        
        //TODO Create MOIVE PAGE
        RequestDispatcher dispatcher = request.getRequestDispatcher("/showMovie.jsp");
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