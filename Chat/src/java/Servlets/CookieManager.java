/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import EntityClasses.ChatUser;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
	
	/*
	* Creates a cookie where the username is stored
	* also stores the initial time and drift value in the database.
	*/
	public void createCookie(HttpServletResponse response, ChatUser user, Time initialTime, Integer driftValue) {
		response.addCookie(new Cookie("username", user.getName()));
		response.addCookie(new Cookie("isModerator", user.getIsModerator().toString()));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		response.addCookie(new Cookie("initialTime", initialTime.toLocalTime().format(dtf)));
		response.addCookie(new Cookie("driftValue", driftValue.toString()));
	}

	public String getUsernameFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("username")) {
				return cookie.getValue();
			}
		}
		System.out.println("Empty cookie!");
		return "";
	}
	
	public void refreshCookie(HttpServletRequest request, HttpServletResponse response, ChatUser user) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("isModerator")) {
				cookie.setValue(user.getIsModerator().toString());
				response.addCookie(cookie);
			}
		}
	}
	
	public boolean emptyCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("isModerator")) {
				return false;
			}
		}
		return true;
	}
	
	public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("username") || cookie.getName().equals("initialTime")
					|| cookie.getName().equals("driftValue") || cookie.getName().equals("isModerator")) {
				cookie.setMaxAge(0);	// equivalent to removing the cookie
				cookie.setValue(null);
				response.addCookie(cookie);
			}
		}
	}
}