/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
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
@WebServlet(name = "ChatUserServlet", urlPatterns = {"/ChatUserServlet"})
public class UserServlet extends HttpServlet {

    @EJB
    private ChatUserFacade chatUserFacade;

    private static int _id = 0;
    
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
        
        if (request.getParameter("action") != null) {
            if (request.getAttribute("action").equals("register")) {
                // create new user and log in
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String repassword = request.getParameter("repassword");
                if (password.equals(repassword)) {
                    int id = _id;
                    _id++;
                    boolean isModerator = false;    // TODO: make first user the moderator
                    boolean success = chatUserFacade.addUser(id, username, password, isModerator);
                    request.getRequestDispatcher("chat.jsp").forward(request, response);
                } else {
                    // TODO: return error: passwords do not match
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            } else if (request.getAttribute("action").equals("login")) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                boolean success = chatUserFacade.checkAccount(username, password);
                if(success) {
                    request.getRequestDispatcher("chat.jsp").forward(request, response);
                } else {
                    // TODO: return error: username or password is wrong
                    request.getRequestDispatcher("login.jsp").forward(request, response);
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
