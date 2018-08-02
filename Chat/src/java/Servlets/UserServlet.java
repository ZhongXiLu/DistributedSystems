package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Facades.ChatUserFacade;
import EntityClasses.ChatUser;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author zhongxilu
 */
@WebServlet(name = "ChatUserServlet", urlPatterns = {"/ChatUserServlet"})
public class UserServlet extends HttpServlet {

    @EJB
    private ChatUserFacade chatUserFacade;
	
	private CookieManager cookieManager = new CookieManager();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        if (request.getParameter("action") != null) {
            request.setAttribute("action", request.getParameter("action"));
        }

        if (request.getAttribute("action") != null) {
			
			System.out.println("Received Request " + request.getRequestURL() + ": " + request.getAttribute("action"));

			if(request.getAttribute("action").equals("register") || request.getAttribute("action").equals("login")) {

				String username = request.getParameter("username");
				String password = request.getParameter("password");
				Time initialTime = new Time(0, 0, 0);
				Integer driftValue = 0;
				Boolean parseSuccess = true;
				try {
					driftValue = Integer.valueOf(request.getParameter("driftValue"));
					if(driftValue < 0) {
						parseSuccess = false;
						request.setAttribute("errorMessage", "Drift value is wrong (must be a positive int)");
						request.getRequestDispatcher(request.getAttribute("action") + ".jsp").forward(request, response);
					}
				} catch(Exception e) {
					parseSuccess = false;
					request.setAttribute("errorMessage", "Drift value is wrong (must be a positive int)");
					request.getRequestDispatcher(request.getAttribute("action") + ".jsp").forward(request, response);
				}
				try {
					initialTime = Time.valueOf(request.getParameter("initialTime") + ":00");
				} catch (Exception e) {
					parseSuccess = false;
					request.setAttribute("errorMessage", "Initial time must be of following format: 'HH:mm'");
					request.getRequestDispatcher(request.getAttribute("action") + ".jsp").forward(request, response);
				}
				
				if(parseSuccess) {
					if (request.getAttribute("action").equals("register")) {
						// create new user and log in
						String repassword = request.getParameter("repassword");
						if (password.equals(repassword)) {
							boolean success = chatUserFacade.addUser(username, password, false);
							if (success) {
								chatUserFacade.setIsOnline(username, true);
								ChatUser user = chatUserFacade.getChatUser(username);
								cookieManager.createCookie(response, user, initialTime, driftValue);
//								request.getRequestDispatcher("index.jsp").forward(request, response);
								response.sendRedirect("index.jsp");
							} else {
								request.setAttribute("errorMessage", "Username already exists");
								request.getRequestDispatcher("register.jsp").forward(request, response);
							}
						} else {
							request.setAttribute("errorMessage", "Passwords do not match");
							request.getRequestDispatcher("register.jsp").forward(request, response);
						}

					} else {
						// Log In
						boolean success = chatUserFacade.checkAccount(username, password);
						if (success) {
							ChatUser user = chatUserFacade.getChatUser(username);
							chatUserFacade.setIsOnline(username, true);
							cookieManager.createCookie(response, user, initialTime, driftValue);
//							request.getRequestDispatcher("index.jsp").forward(request, response);
							response.sendRedirect("index.jsp");
						} else {
							request.setAttribute("errorMessage", "Username or password is wrong");
							request.getRequestDispatcher("login.jsp").forward(request, response);
						}
					}
				}
				
            } else if (request.getAttribute("action").equals("logout")) {
                chatUserFacade.setIsOnline(cookieManager.getUsernameFromCookie(request), false);
                cookieManager.clearCookie(request, response);
                request.getRequestDispatcher("index.jsp").forward(request, response);
//				response.sendRedirect("index.jsp");
				
            } else if (request.getAttribute("action").equals("getOnlineUsers")) {
				cookieManager.refreshCookie(request, response, chatUserFacade.getChatUser(cookieManager.getUsernameFromCookie(request)));
				
                chatUserFacade.refreshUser(cookieManager.getUsernameFromCookie(request));
                List<ChatUser> onlineUsers = chatUserFacade.getAllOnlineUsers();
				//request.getSession().setAttribute("user", chatUserFacade.getChatUser((String) request.getSession().getAttribute("username")));
                request.setAttribute("onlineUsers", onlineUsers);
                request.getRequestDispatcher("onlineUsers.jsp").forward(request, response);
				
            } else if (request.getAttribute("action").equals("checkLoggedIn")) {
				if(cookieManager.emptyCookie(request)) {
					// no cookies are set
					response.getWriter().write("false");
				} else {
					// check if user is actually logged in
					ChatUser user = chatUserFacade.getChatUser(cookieManager.getUsernameFromCookie(request));
					if(user.getIsOnline()) {
						response.getWriter().write("true");
					} else {
						response.getWriter().write("false");
						// also clear the cookies
						cookieManager.clearCookie(request, response);
					}
				}
			}
        }
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
