package com.homework.blog;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Cron_Servlet extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(Cron_Servlet.class.getName());
	public static ArrayList<User> subscribedUsers = new ArrayList<User>();
	public static ArrayList<String> posts = new ArrayList<String>();
	public static ArrayList<String> users = new ArrayList<String>();
	
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
		_logger.info("Test");
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String userName = req.getParameter("userName");
		
		// Sender's email ID needs to be mentioned
	    String from = "admin@random-thoughts8.appspotmail.com";


	    // Get system properties
	    Properties properties = new Properties();
		Session session = Session.getDefaultInstance(properties,null);
		
		for(int index = 0; index < subscribedUsers.size(); index += 1) {
			
			/**Need to get all of the emails**/
			String to = subscribedUsers.get(index).getEmail();
			_logger.info(to);
			
			try{
				
				//String htmlText;
				String text = "This is your daily update for your Subscription to the Random Thoughts Blog: \n\n";
				
				//Multipart mp = new MimeMultipart();
				
		         // Create a default MimeMessage object.
		         MimeMessage message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set To: header field of the header.
		         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		         // Set Subject: header field
		         message.setSubject("Blog Update!");

		         
		         int size = posts.size();
		         if(size == 0) {
		        	 text = "There have not been any new posts since the last update";
		         } else {
			         for(int loop = 0; loop < size; loop += 1) {
			        	 text += users.get(loop);
			        	 text += "wrote: \n";
			        	 text += posts.get(loop);
			        	 text += "\n";
			        	 //MimeBodyPart htmlPart = new MimeBodyPart();
			        	 //htmlText = " ";
			        	 //_logger.info("Checking: " + htmlText);
			        	 //htmlPart.setContent(htmlText, posts.get(loop));
			        	 //mp.addBodyPart(htmlPart);
			         }
		         }
		         
		         //message.setContent(mp);
		         //Now set the actual message
		         message.setText(text);
		         
		         // Send message
		         Transport.send(message);
		         _logger.info("Cron Job has been executed");
		         
		      }catch (MessagingException e) {
		    	  _logger.info("Cron Job has NOT been executed");
		         e.printStackTrace();
		      }
		}   
		posts.clear();
		resp.sendRedirect("/home.jsp?userName=" + userName);
	}
}

