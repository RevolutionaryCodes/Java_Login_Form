/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * @author Dell
 */
public class Database extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            String name = request.getParameter("User-Name");
            String password = request.getParameter("Password");
            String button = request.getParameter("enter");
            LoginDatabase ld = new LoginDatabase();

            if (button.equals("Sign-in")) {
                ld.AddUser(name, password);
                String url = " just.jsp?username=" + name;
                response.sendRedirect(url);
            } else {
                if (button.equals("Login")) {
                    if (ld.CheckUserInfo(name, password)) {
                        String url = " just.jsp?username=" + name;
                        response.sendRedirect(url);
                    } else {
                        String url = "ERROR.jsp";
                        response.sendRedirect(url);
                    }
                }
            }

        }

    }

    class LoginDatabase {

        DatabaseTable db = new DatabaseTable();
        Connection con = db.getConnection();

        public boolean CheckUserInfo(String name, String password) {
            try {
                PreparedStatement statement = con.prepareCall("SELECT * FROM info WHERE UserName = ? AND passwords = ?");
                statement.setString(1, name);
                statement.setString(2, password);
                ResultSet result = statement.executeQuery();
                return result.next();
            } catch (SQLException e) {
                System.err.print("FAILED BECAUSE" + e.getMessage());
                return false;
            }
        }

        public void AddUser(String name, String password) {
            try {
                PreparedStatement statement = con.prepareCall("INSERT INTO info(UserName,passwords) VALUES (?,?)");
                statement.setString(1, name);
                statement.setString(2, password);
                statement.execute();
            }catch(SQLIntegrityConstraintViolationException e){
                System.err.print("FAILED BECAUSE"+e.getMessage());
            }
            catch (SQLException e) {
                System.err.print("failed because" + e.getMessage());
            }
        }
    }

    class DatabaseTable {

        public Connection getConnection() {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433; database =loginform", "Amara", "amara");
                System.out.println(" Successful Hollaaa!!!!!");
                return con;

            }
            catch (Exception e) {
                System.err.println(" Connection to database failed " + e.getMessage());
            }
            return null;
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
