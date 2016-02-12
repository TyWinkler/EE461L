package com.homework.blog;

import java.io.IOException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**Used for accessing the page that has all posts on it**/

@SuppressWarnings("serial")
public class Cron_Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		/**Need to get all of the emails**/
		String to = "abcd@gmail.com";
		 
		// Sender's email ID needs to be mentioned
	    String from = "web@gmail.com";

	    // Assuming you are sending email from localhost
	    String host = "localhost";

	    // Get system properties
	    Properties properties = System.getProperties();

     	// Setup mail server
	     properties.setProperty("mail.smtp.host", host);
	      
		Session session = Session.getDefaultInstance(properties);
		
		try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("This is the Subject Line!");

	         // Now set the actual message
	         message.setText("This is actual message");

	         // Send message
	         Transport.send(message);
	         
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }   
	}
}
