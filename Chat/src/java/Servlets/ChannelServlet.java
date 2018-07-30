package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Facades.ChannelFacade;
import EntityClasses.Channel;  // TODO: Remove reference to entityclasses. Replace with calls to Facade instead.
import EntityClasses.ChatUser;
import Facades.ChatUserFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet(urlPatterns = {"/ChannelServlet"})
public class ChannelServlet extends HttpServlet {

	@EJB
	private ChatUserFacade chatUserFacade;
	@EJB
	private ChannelFacade channelFacade;
	
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

			if (request.getAttribute("action").equals("getChannels")) {
                List<Channel> publicChannels = channelFacade.getActivePublicChannels();
                request.setAttribute("publicChannels", publicChannels);
				String username = cookieManager.getUsernameFromCookie(request);
				Channel myChannel = chatUserFacade.getChannelOfUser(username);
                request.setAttribute("myChannel", myChannel);
				request.getRequestDispatcher("channels.jsp").forward(request, response);
				
			} else if(request.getAttribute("action").equals("addPublicChannel")) {
				String channelName = request.getParameter("channelName");
				Boolean success = channelFacade.addPublicChannel(channelName);
				// TODO: return error message?
				request.getRequestDispatcher("chat.jsp").forward(request, response);
				
			} else if(request.getAttribute("action").equals("deleteChannel")) {
				String channelName = (String) request.getParameter("channelName");
				Boolean success = channelFacade.removeChannel(channelName);
				request.getRequestDispatcher("chat.jsp").forward(request, response);
			
			} else if (request.getAttribute("action").equals("joinChannel")) {
				String channelName = (String) request.getParameter("channelName");
				ChatUser user = chatUserFacade.getChatUser(cookieManager.getUsernameFromCookie(request));
				channelFacade.switchChannel(user, channelName);
				request.getRequestDispatcher("chat.jsp").forward(request, response);
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
