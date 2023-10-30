package com.mycompany.crudweb.servlet;

import com.mycompany.cruddbapp.dao.context.Context;
import com.mycompany.cruddbapp.dao.inter.UserDaoInter;
import com.mycompany.cruddbapp.entity.User;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {
    private final UserDaoInter userDaoImpl = Context.instanceOfUserDaoImpl();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        switch (servletPath) {
            case "/new":
                showNewForm(request, response);
                break;
            case "/insert":
                insertUser(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/update":
                updateUser(request, response);
                break;
            case "/delete":
                deleteUser(request, response);
                break;
            default:
                getUsers(request, response);
                break;
        }
    }
    
    private void getUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> usersList = userDaoImpl.getUsers();
        request.setAttribute("usersList", usersList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("users-list.jsp");
        requestDispatcher.forward(request, response);
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
        requestDispatcher.forward(request, response);
    }
    
    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String emailAddress = request.getParameter("email_address");
        double monthlySalary = Double.parseDouble(request.getParameter("monthly_salary"));
        User newUser = new User(firstName, lastName, emailAddress, monthlySalary);
        userDaoImpl.insertUser(newUser);
        response.sendRedirect("users-list.jsp");
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDaoImpl.getUserById(id);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
        request.setAttribute("user", existingUser);
        requestDispatcher.forward(request, response);
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String emailAddress = request.getParameter("email_address");
        double monthlySalary = Double.parseDouble(request.getParameter("monthly_salary"));
        User user = new User(id, firstName, lastName, emailAddress, monthlySalary);
        userDaoImpl.updateUser(user);
        response.sendRedirect("users-list.jsp");
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDaoImpl.deleteUser(id);
        response.sendRedirect("users-list.jsp");
    }
}