package com.Illia.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Illia.dto.UserDTO;
import com.Illia.service.UserService;
import com.google.gson.Gson;

@WebServlet("/users")
public class UserController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<UserDTO> users = userService.getAllUsers();
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(users));
    }
}
