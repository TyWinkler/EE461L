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
	
	UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userName;
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Key userKey = KeyFactory.createKey("User", userName);
        
        
        if(user != null){
        	
        	//Need to check, just concept code currently
        	/*userName = req.getParameter("userName");
	        String content = req.getParameter("content");
	        
	        Post newPost = new Post(user,content);
	        newPost.addPost(newPost);*/
	        
	        Entity posts = new Entity("Post", userKey);
	        posts.setProperty("user", user);
	        posts.setProperty("date", date);
	        posts.setProperty("content", content);
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	        datastore.put(posts);
        }
        resp.sendRedirect("/home.jsp?userName=" + userName);
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
    	//Currently the datastore service is dependent on the userName, and the userName changes. This messes up going back to the home page
    	resp.sendRedirect("/home.jsp?userName=" + userName);
	}
}