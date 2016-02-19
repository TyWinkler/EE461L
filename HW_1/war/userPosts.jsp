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
					<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>"> Sign out</a>
					<a href="userPosts.jsp"> Your Posts</a>
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
		<h1>The Blog of All Blogs!!!</h1>
	  	<p>Yours and only yours: </p>
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
		                %>
		                <div id="post-box">
			                <p><b>${fn:escapeXml(post_user.nickname)}</b> wrote:</p>
			                <h3>${fn:escapeXml(post_title)}</h3>
				            <blockquote>${fn:escapeXml(post_content)}</blockquote>
				            <p>On: ${fn:escapeXml(post_date)}</p>
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