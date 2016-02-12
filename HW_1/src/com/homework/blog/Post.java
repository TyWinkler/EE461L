package com.homework.blog;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Index;

import com.google.appengine.api.users.User;


public class Post implements Comparable<Post>{
	/**The biggest issue that we need to solve is creating a data store that can be accessed from all classes, not just the one it is created in
	 * This shouldn't be hard, and my plan is to implement a method similar to the one used in the first lab. I want to try to avoid objectify still
	 * but if necessary will research it more and add it in
	 **/
	
	User user;
	String content;
	@Index Date date;
	
	ArrayList<Post> posts;
	
	private Post() {}
	
	public Post(User u, String c){
		user = u;
		content = c;
		date = new Date();
	}
	
	public User getUser() {
		return user;
	}
	
	public String getContent() {
		return content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void addPost(Post p) {
		posts.add(p);
	}

	@Override
	public int compareTo(Post o) {
		if (date.after(o.date)) {
            return 1;
        } else if (date.before(o.date)) {
            return -1;
        }
        return 0;
	}
}
