package tester;

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
		
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        Statement selectStmt = db_connection.createStatement();
        ResultSet results = selectStmt.executeQuery(query);
        
        
        List<Movie> movies = new ArrayList<Movie>();

        
        while(results.next()) 
        	movies.add(new Movie(results.getInt("id"), results.getString("title"), results.getInt("year"),results.getString("director"),results.getString("banner_url"),results.getString("trailer_url")));
        
        results.close();
        selectStmt.close();
        db_connection.close();
        
        for(Movie m: movies)
        {
        	
        	ArrayList<String> genres = new ArrayList<String>();
        	
	        query = "select g.name as 'genre' "
	          		+ "from movies as m, genres_in_movies as gm, genres as g where m.id = gm.movie_id and g.id = gm.genre_id and m.id = "+ m.getId() +" "
	          		+ "order by g.name, m.title, m.year, m.director;";
	  
	        	
        	db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            selectStmt = db_connection.createStatement();
            results = selectStmt.executeQuery(query);
	            
	            
	
            
            while(results.next())
            	genres.add(new String(results.getString("genre"))); 
            
            results.close();
            selectStmt.close();
            db_connection.close();
            
            m.setGenres(genres);
        }
        
        for(Movie m: movies)
        	
        {
        	
        	ArrayList<Star> stars = new ArrayList<Star>();
        	
            
	        query =     		
	              "select s.id, s.first_name, s.last_name, s.dob, s.photo_url "
	              + "from movies as m, stars as s, stars_in_movies as sm where m.id = sm.movie_id and s.id = sm.star_id and m.id = "+m.getId()+" "
	              + "order by s.first_name, s.last_name;";
	  
	        	
        	db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            selectStmt = db_connection.createStatement();
            results = selectStmt.executeQuery(query);
	            
	
            while(results.next())
            	stars.add(new Star(results.getInt("id"),results.getString("first_name"),results.getString("last_name"),results.getString("dob"), results.getString("photo_url"))); 
            
            results.close();
            selectStmt.close();
            db_connection.close();
            
            m.setStars(stars);
        }
        

        String num_of_movies = "0";		
	    
		query = "SELECT COUNT(*) as count from movies as m, genres_in_movies as gm, genres as g where m.id = gm.movie_id and g.id = gm.genre_id and g.name = '" + genre + "'";

    	db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        selectStmt = db_connection.createStatement();
        results = selectStmt.executeQuery(query);
	            
	
	    while(results.next())
	    	num_of_movies = results.getString("count"); 
	    
	    results.close();
	    selectStmt.close();
	    db_connection.close();
	    
	    System.out.print(num_of_movies);
	    
	    
	    ArrayList<String> all_genres = new ArrayList<String>();
    	
        
        query = "select name from genres order by name";
  
        	
    	db_connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        selectStmt = db_connection.createStatement();
        results = selectStmt.executeQuery(query);
            

        while(results.next())
        	all_genres.add(results.getString("name")); 
        
        results.close();
        selectStmt.close();
        db_connection.close();
        
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
