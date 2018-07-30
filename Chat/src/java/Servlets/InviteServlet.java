/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import EntityClasses.ChatUser;
import EntityClasses.Invite;
import Facades.ChannelFacade;
import Facades.ChatUserFacade;
import Facades.InviteFacade;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zhongxilu
 */
@WebServlet(name = "InviteServlet", urlPatterns = {"/InviteServlet"})
public class InviteServlet extends HttpServlet {

	@EJB
	private InviteFacade inviteFacade;
	
	@EJB
    private ChatUserFacade chatUserFacade;
	
	private CookieManager cookieManager = new CookieManager();

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
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

			if (request.getAttribute("action").equals("acceptInvite")) {
				Integer inviteId = Integer.parseInt(request.getParameter("inviteId"));
				inviteFacade.acceptInvite(inviteId);
				request.getRequestDispatcher("chat.jsp").forward(request, response);
				
			} else if (request.getAttribute("action").equals("declineInvite")) {
				Integer inviteId = Integer.parseInt(request.getParameter("inviteId"));
				inviteFacade.declineInvite(inviteId);
				request.getRequestDispatcher("chat.jsp").forward(request, response);
				
			} else if (request.getAttribute("action").equals("createInvite")) {
				String channelName = request.getParameter("channelName");
				ChatUser user = chatUserFacade.getChatUser(cookieManager.getUsernameFromCookie(request));
				String otherUser = (String) request.getParameter("user");
				inviteFacade.addInvite(channelName, user, otherUser);
				request.getRequestDispatcher("chat.jsp").forward(request, response);
				
			} else if (request.getAttribute("action").equals("getOpenInvite")) {
				ChatUser user = chatUserFacade.getChatUser(cookieManager.getUsernameFromCookie(request));
				Invite invite = inviteFacade.getOpenInvite(user);
				request.setAttribute("invite", invite);
				request.getRequestDispatcher("invite.jsp").forward(request, response);
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
