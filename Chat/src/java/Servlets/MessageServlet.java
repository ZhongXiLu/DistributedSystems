/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import EntityClasses.Channel;
import EntityClasses.ChatUser;
import EntityClasses.Message;
import Facades.ChannelFacade;
import Facades.ChatUserFacade;
import Facades.MessageFacade;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "MessageServlet", urlPatterns = {"/MessageServlet"})
public class MessageServlet extends HttpServlet {

	@EJB
	private ChatUserFacade chatUserFacade;
    @EJB
    private ChannelFacade channelFacade;
    @EJB
    private MessageFacade messageFacade;

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
            if (request.getAttribute("action").equals("getLatestMessages")) {
				// TODO: Before login: User == null
				String username = (String) request.getSession().getAttribute("username");
				Channel myChannel = chatUserFacade.getChannelOfUser(username);
                request.setAttribute("messages", messageFacade.getLatestMessagesOfChannel(myChannel));
                request.getRequestDispatcher("messages.jsp").forward(request, response);
				
			} else if (request.getAttribute("action").equals("getUserInfo")) {
				String username = (String) request.getParameter("user");
				List<Message> messages = messageFacade.getLatestMessagesOfUser(chatUserFacade.getChatUser(username));
				request.setAttribute("username", username);
				request.setAttribute("messages", messages);
				request.getRequestDispatcher("userProfile.jsp").forward(request, response);

            } else if (request.getAttribute("action").equals("sendMessage")) {
                HttpSession session = request.getSession(false);
                String message = request.getParameter("message");
                ChatUser user = (ChatUser) request.getSession().getAttribute("user");
				Channel channel = chatUserFacade.getChannelOfUser(user.getName());
                messageFacade.addMessage(message, user, channel);
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
        protected void doGet
        (HttpServletRequest request, HttpServletResponse response)
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
        protected void doPost
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            processRequest(request, response);
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo
        
            () {
        return "Short description";
        }// </editor-fold>

    }
