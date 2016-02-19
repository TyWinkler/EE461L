package com.homework.blog;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.apphosting.utils.config.ClientDeployYamlMaker.Request;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Delete_Servlet extends HttpServlet {
    
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Key key = (Key) req.getAttribute("deletekey");
		if(key != null){
			BlogSubmit_Servlet.datastore.delete(key);
		}
        resp.sendRedirect("/userPosts.jsp");
    }
}