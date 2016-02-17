package com.homework.blog;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlogSubmit_Servlet extends HttpServlet {
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
    	String userName = req.getParameter("userName");
    	boolean subscribed = false;
    	
        if(user != null){
        	Key userKey = KeyFactory.createKey("User", userName);
	        String content = req.getParameter("content");
	        com.homework.blog.Cron_Servlet.posts.add(content);
	        Date date = new Date();
	        
	        try {
	        	datastore.get(userKey);
	        }catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
		        Entity posts = new Entity("Post", userKey);
		        posts.setProperty("user", user);
		        posts.setProperty("date", date);
		        posts.setProperty("content", content);
	        	posts.setProperty("sub", subscribed);
	        	datastore.put(posts);
			}
        }
        resp.sendRedirect("/home.jsp?userName=" + userName);
    }
}