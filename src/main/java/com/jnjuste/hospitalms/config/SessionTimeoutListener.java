package com.jnjuste.hospitalms.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeoutListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // Session created
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Session expired
        System.out.println("Session expired: " + event.getSession().getId());
    }
}
