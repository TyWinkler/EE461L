package com.homework.blog;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class BlogPost_Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, again");
	}
}
