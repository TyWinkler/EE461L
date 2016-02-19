package com.homework.blog;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**Used for accessing the page that has all posts on it**/

@SuppressWarnings("serial")
public class UserPosts_Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userName = req.getParameter("userName");
        
        
		
		resp.sendRedirect("/allPosts.jsp?userName=" + userName);
	}
}
