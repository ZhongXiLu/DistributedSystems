package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Facades.ChatUserFacade;
import EntityClasses.ChatUser;
import EntityClasses.Message;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
								createSession(request, user, initialTime, driftValue);
								request.getRequestDispatcher("index.jsp").forward(request, response);
							} else {
								request.setAttribute("errorMessage", "Username already exists");
								request.getRequestDispatcher("register.jsp").forward(request, response);
							}
						} else {
							request.setAttribute("errorMessage", "Passwords do not match");
							request.getRequestDispatcher("register.jsp").forward(request, response);
						}

					} else {
						boolean success = chatUserFacade.checkAccount(username, password);
						if (success) {
							ChatUser user = chatUserFacade.getChatUser(username);
							chatUserFacade.setIsOnline(username, true);
							createSession(request, user, initialTime, driftValue);
							request.getRequestDispatcher("index.jsp").forward(request, response);
						} else {
							request.setAttribute("errorMessage", "Username or password is wrong");
							request.getRequestDispatcher("login.jsp").forward(request, response);
						}
					}
				}
				
            } else if (request.getAttribute("action").equals("logout")) {
                HttpSession session = request.getSession(false);
                chatUserFacade.setIsOnline((String) session.getAttribute("username"), false);
                session.removeAttribute("user");
                session.removeAttribute("username");
                request.getRequestDispatcher("index.jsp").forward(request, response);
				
            } else if (request.getAttribute("action").equals("getOnlineUsers")) {
                chatUserFacade.refreshUser((String) request.getSession().getAttribute("username"));
                List<ChatUser> onlineUsers = chatUserFacade.getAllOnlineUsers();
				request.getSession().setAttribute("user", chatUserFacade.getChatUser((String) request.getSession().getAttribute("username")));
                request.setAttribute("onlineUsers", onlineUsers);
                request.getRequestDispatcher("onlineUsers.jsp").forward(request, response);
			}
        }
    }

    private void createSession(HttpServletRequest request, ChatUser user, Time initialTime, Integer driftValue) {
		request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("username", user.getName());
		request.getSession().setAttribute("initialTime", initialTime);
		request.getSession().setAttribute("driftValue", driftValue);
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
