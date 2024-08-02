package com.jnjuste.hospitalms.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeoutListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // Set the session timeout interval in seconds (e.g., 30 minutes)
        System.out.println("Session created: " + event.getSession().getId());
        event.getSession().setMaxInactiveInterval(30 * 60);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Session expired
        System.out.println("Session expired: " + event.getSession().getId());
    }
}
