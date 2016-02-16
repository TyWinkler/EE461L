package com.homework.blog;

import java.io.IOException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**Used for accessing the page that has all posts on it**/


@SuppressWarnings("serial")
public class Cron_Servlet extends HttpServlet {
	
	public static ArrayList<User> subscribedUsers = new ArrayList<User>();
	
	public static void addUser(User user){
		subscribedUsers.add(user);
	}
	
	public static void removeUser(User user){
		subscribedUsers.remove(user);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String userName = req.getParameter("userName");
		
		if (subscribedUsers.contains(user)){
			removeUser(user);
		} else {
			addUser(user);
		}
		resp.sendRedirect("/home.jsp?userName=" + userName);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String userName = req.getParameter("userName");
		
		// Sender's email ID needs to be mentioned
	    String from = "web@gmail.com";

	    // Assuming you are sending email from localhost
	    String host = "localhost";

	    // Get system properties
	    Properties properties = System.getProperties();

     	// Setup mail server
	     properties.setProperty("mail.smtp.host", host);
	      
		Session session = Session.getDefaultInstance(properties);
		
		
		for(int index = 0; index < subscribedUsers.size(); index += 1) {
			
			/**Need to get all of the emails**/
			String to = subscribedUsers.get(index).getEmail();
			
			try{
		         // Create a default MimeMessage object.
		         MimeMessage message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set To: header field of the header.
		         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		         // Set Subject: header field
		         message.setSubject("Blog Update!");

		         // Now set the actual message
		         message.setText("This is actual message");

		         // Send message
		         Transport.send(message);
		         
		      }catch (MessagingException mex) {
		         mex.printStackTrace();
		      }
		}   
		
		resp.sendRedirect("/home.jsp?userName=" + userName);
	}
}

