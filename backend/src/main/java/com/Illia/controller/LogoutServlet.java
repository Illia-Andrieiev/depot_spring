package com.Illia.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


public class LogoutServlet extends HttpServlet {

    private String domain;
    private String clientId;

    @Override
    public void init(ServletConfig config) {
        domain = config.getServletContext().getInitParameter("com.auth0.domain");
        clientId = config.getServletContext().getInitParameter("com.auth0.clientId");
    }

@Override
protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    if (request.getSession() != null) {
        request.getSession().invalidate();
    }
    
    String returnUrl = "http://localhost:3000"; 
    String logoutUrl = String.format(
            "https://%s/v2/logout?client_id=%s&returnTo=%s",
            domain,
            clientId,
            URLEncoder.encode(returnUrl, "UTF-8") 
    );

    response.sendRedirect(logoutUrl);
}

}
