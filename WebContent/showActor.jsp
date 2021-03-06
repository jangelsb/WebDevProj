<!DOCTYPE html>
<html>
<head>
    <title>Actor Page</title>
    <script src="https://code.jquery.com/jquery-2.2.3.min.js" integrity="sha256-a23g1Nt4dtEYOj7bR+vTu7+T8VP13humZFBJNIYoEJo=" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js" integrity="sha256-xNjb53/rY+WmG+4L6tTl9m6PpqknWZvRt0rO1SRnJzw=" crossorigin="anonymous"></script>
    <link href="https://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="./css/shared.css">
    <link rel="stylesheet" type="text/css" href="./css/header.css">
    <link rel="stylesheet" type="text/css" href="./css/search.css">
    <link rel="stylesheet" type="text/css" href="./css/footer.css">
    <script src="./javascript/shared.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <style type="text/css">

        #spacer {
            height: 40px;
        }

    </style>
    <script>
    	<%
    	int offset = Integer.parseInt(Objects.toString(session.getAttribute("offset"), "10"));
        int limit = Integer.parseInt(Objects.toString(session.getAttribute("limit"), "0"));
        int num_of_movies = Integer.parseInt(Objects.toString(session.getAttribute("num_of_movies"), "0"));
    	int cart_counter = Integer.parseInt(Objects.toString(session.getAttribute("shopping_cart_size"), "0"));
    	
    	String orderby = Objects.toString(session.getAttribute("orderby"), "asc_t");
    	%>
    	
    	var orderby = "<%=orderby%>";
    	var limit = <%=limit%>;
    	var cartCounter = <%=cart_counter%>;
    </script>

</head>

<body>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"

import="java.sql.*, java.util.*, javax.sql.*, java.io.IOException, javax.servlet.http.*, javax.servlet.*, model.*" 
       
%>

    <div id="wrapper">

        <div id="navBarTop">
                <%@ include file="header.jsp" %>
        </div>

        <div id="spacer"></div>

        <div id="moviesList">


<%

            Star actor = (session.getAttribute("actor") == null) ? new Star() : (Star) session.getAttribute("actor");

                /*  this takes into account if they are null */
                String a_photo_url = Objects.toString(actor.getPhoto_url(), "");
                String a_name = Objects.toString(actor.getName(), "");
                String a_dob = Objects.toString(actor.getDob(), "");
                String a_id = actor.getId() == -1 ? "" :  Integer.toString(actor.getId());

 %>         

            <div class="movieBox">
                <div class="imageAndBuy">
                    <div class="movieImage" style="background-image: url('<%=a_photo_url%>');">
                        <img src='<%=a_photo_url%>' onerror= "this.src = './images/no-image.jpg';">
                    </div>
                </div>
                <div id="movieInfo">
                    <div class="info first">
                        <div class="infoTitle">Star Name:</div>
                        <div class="infoDetail"><%=a_name%> </div>
                    </div>
                     <div class="info">
                        <div class="infoTitle">DOB:</div>
                        <div class="infoDetail"><%=a_dob%></div>
                    </div>
                    <div class="info">
                        <div class="infoTitle">Star ID:</div>
                        <div class="infoDetail"><%=a_id%></div>
                    </div> 
                    <div class="info">
                        <div class="infoTitle">Starred in:</div>
                        <div class="infoDetail">
<%                  
                        List<Movie> movies_starred_in = (ArrayList<Movie>) actor.getStarred_in();
                        for(int i = 0; i < movies_starred_in.size(); ++i){
%>
                            <%if(i < movies_starred_in.size()-1){%>
                                <a class="tt" title="<%=movies_starred_in.get(i).getId()%>" href="./ShowMovie?movie_id=<%=movies_starred_in.get(i).getId()%>"> <%=movies_starred_in.get(i).getTitle()%> <br></a>
                            <%}
                            else{%>
                                <a class="tt" title="<%=movies_starred_in.get(i).getId()%>" href="./ShowMovie?movie_id=<%=movies_starred_in.get(i).getId()%>"><%=movies_starred_in.get(i).getTitle()%></a>
                            <%}%>
                    
                        <%}%>   
                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>