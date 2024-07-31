package com.jnjuste.hospitalms.filters;

import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.Nurse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class RoleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();
        boolean isDoctorEndpoint = requestURI.startsWith("/api/doctor/");
        boolean isNurseEndpoint = requestURI.startsWith("/api/nurse/");
        boolean isAuthEndpoint = requestURI.startsWith("/api/auth/");

        if (session == null) {
            httpResponse.sendRedirect("/api/auth/session-timeout");
            return;
        }

        Doctor doctor = (Doctor) session.getAttribute("doctor");
        Nurse nurse = (Nurse) session.getAttribute("nurse");

        if (isDoctorEndpoint) {
            if (doctor == null) {
                httpResponse.sendRedirect("/api/auth/session-timeout");
                return;
            }
        } else if (isNurseEndpoint) {
            if (nurse == null) {
                httpResponse.sendRedirect("/api/auth/session-timeout");
                return;
            }
        } else if (!isAuthEndpoint) {
            httpResponse.sendRedirect("/api/auth/session-timeout");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
