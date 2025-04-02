package com.Illia.filters;

import com.Illia.service.DatabaseTableHandler;
import com.auth0.SessionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter class to check if a valid session exists. This will be true if the User Id is present.
 */
@WebFilter(urlPatterns = "/index.jsp")
public class Auth0Filter implements Filter {
    private final DatabaseTableHandler tableHandler = new DatabaseTableHandler();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String accessToken = (String) SessionUtils.get(req, "accessToken");
        String idToken = (String) SessionUtils.get(req, "idToken");
        System.out.println("Auth0Filter is running...");
        System.out.println(accessToken);
        System.out.println(idToken);

        if (accessToken == null && idToken == null) {
            System.out.println("Redirecting to login");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        tableHandler.processRequest(req);

        next.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}