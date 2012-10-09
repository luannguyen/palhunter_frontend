/**
 * 
 */
package edu.usc.cs587.examples.objects;

import edu.usc.cs587.examples.Constants;


/**
 * @author Ling
 *
 */
public class UserMessage {
	private String username;
	private String message;
	private long pubdate;
	public String getUsername() {
		return username;
	}
	public String getMessage() {
		return message;
	}
	public long getPubdate() {
		return pubdate;
	}
	/**
	 * @param username
	 * @param message
	 * @param pubdate
	 */
	public UserMessage(String username, String message, long pubdate) {
		super();
		this.username = username;
		this.message = message;
		this.pubdate = pubdate;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "user = " + username + " message = "+message
		+ " pubDate = "+Constants.getTimeAsString(pubdate);
	}
	
	
}
