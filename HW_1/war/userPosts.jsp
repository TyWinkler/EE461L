<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>




<html>
  <head>
  	<link rel="stylesheet" href="resources/css/index.css">
  	<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
  	<link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
  </head>
  	<%
    String userName = request.getParameter("userName");
    if (userName == null) {
    	userName = "default";
    }
    pageContext.setAttribute("userName", userName);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    %>
    
  <ul class="border">
		<li class="topbar"><a href="home.jsp">Home</a></li>
		<li class="topbar"><a href="allPosts.jsp">Posts</a></li>
		
		
		<ul style="float:right; list-style-type:none;">
			<li class="topbar">
				<%
				if (user != null) {
					pageContext.setAttribute("user", user);
					%>
					<a href="userPosts.jsp">Your Posts</a>
					<%
	    		}
				%>
			</li>
			
			<li class="topbar">
				<%
				if (user != null) {
					pageContext.setAttribute("user", user);
					%>
					<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a>
					<%
	    		} else {
					%>
					<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
					<%
	    		}
				%>
			</li>
  		</ul>
  		<ul style="float:right; list-style-type:none;">
  			<%
			if(user != null)
			{
				%>
			    <form action="/cronjob" method="post">
			    <%
			    if(com.homework.blog.Cron_Servlet.subscribedUsers.contains(user)) {
			    	%>
			    	<div><input type="submit" class="submitlink" value="Unsubscribe" /></div>
			    	<%
			    }
			    else {
			    	%>
			    	<div><input type="submit" class="submitlink" value="Subscribe" /></div>
			    	<%
			    }
			    %>
			      <input type="hidden" name="userName" value="${fn:escapeXml(userName)}"/>
			    </form>
			 <%
			}
			%>
  		</ul>
  </ul>
  
  <body>
	<div id="page-wrap">
	  	<p>Your Posts: </p>
		<%
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key userKey = KeyFactory.createKey("User", userName);
	    Query query = new Query("Post", userKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> posts = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(200));
	    if (posts.isEmpty()) {
	        %>
	        <p>Blog '${fn:escapeXml(userName)}' has no messages.</p>
	        <%
	    } else {
	        %>
	        <%
	        int numPosts = 0;
	        for (Entity post : posts) { 
	        	User compare = (User)post.getProperty("user");
		            if(compare.equals(user)) {
		            	pageContext.setAttribute("post_content", post.getProperty("content"));
		                pageContext.setAttribute("post_user", post.getProperty("user"));
		                pageContext.setAttribute("post_date", post.getProperty("date"));
		                pageContext.setAttribute("post_title", post.getProperty("title"));
		                pageContext.setAttribute("post_key", post.getKey());
		                %>
		                <div style="padding-bottom: 20px;">
			                <div class="w3-card-4" style="width:100%; padding-bottom: 0px;">
				                <header class="w3-container w3-red">
								  <h3>${fn:escapeXml(post_title)}</h3>
								</header>
				                <div class="w3-container">
					                <p><b>${fn:escapeXml(post_user.nickname)}</b> wrote:</p>
						            <blockquote>${fn:escapeXml(post_content)}</blockquote>
						            <p style="color:grey;">On: ${fn:escapeXml(post_date)}</p>
						            <p>${fn:escapeXml(post_key)}</p>
					            </div>
					            <form action="/delete" method="Post" style="padding: 20px; padding-top: 0px;">
						      		<div><input style="margin-top: 10px;" type="submit" class="original" value="Delete" /></div>
						      		<input type="hidden" name="deletekey" value="woot"/>
						   	 	</form>
				            </div>
				        </div>
			            <%
			            numPosts += 1;
		            }
	        }
	        if(numPosts == 0) {
				%>
				<p>You have not posted anything yet!</p>
				<%
	        }
	    }
		%>
	 </div>   
  </body>
</html>