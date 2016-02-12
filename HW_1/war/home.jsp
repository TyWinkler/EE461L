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
		<li class="topbar"><a href="hw_1" class="active">Home</a></li>
		<li class="topbar"><a href="Posts">Posts</a></li>
		<ul style="float:right; list-style-type:none;">
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
  </ul>
  
  <body>
	<div id="page-wrap">
		<%
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key userKey = KeyFactory.createKey("User", userName);
	    Query query = new Query("Post", userKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> posts = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
	    if (posts.isEmpty()) {
	        %>
	        <p>Blog '${fn:escapeXml(userName)}' has no messages.</p>
	        <%
	    } else {
	        %>
	        <%
	        for (Entity post : posts) {
	            pageContext.setAttribute("post_content", post.getProperty("content"));
                pageContext.setAttribute("post_user", post.getProperty("user"));
                %>
                <p><b>${fn:escapeXml(post_user.nickname)}</b> wrote:</p>
	            <blockquote>${fn:escapeXml(post_content)}</blockquote>
	            <%
	        }
	    }
	%>
		<p>Your Message:</p>
	    <form action="/sign" method="post">
	      <div><textarea name="content" rows="3" cols="60"></textarea></div>
	      <div><input type="submit" value="Post" /></div>
	      <input type="hidden" name="userName" value="${fn:escapeXml(userName)}"/>
	    </form>
	 </div>   
  </body>
</html>