package com.pstu.dtl.server.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.pstu.dtl.shared.exception.SessionExpireSecurityException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    protected static Logger logger = Logger.getLogger(CustomAuthenticationEntryPoint.class);

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        // logger.debug("Authentication required " + request.getRequestURI() +
        // "?" +
        // request.getQueryString());

        if (request.getMethod().equalsIgnoreCase("POST") && request.getRequestURI().contains(".rpc")) {
            // Сюда попадаем если сессия закончилась, а клиент ещё работает (или
            // кто-то пытается обратиться к нашему RPC формируя запросы руками)
            // Возвращаем ответ нашему GWT клиенту на понятному ему языке (RPC)
            try {
                String s = RPC.encodeResponseForFailure(null, new SessionExpireSecurityException("Сессия закончилась"));
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(s);
            }
            catch (SerializationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Сессия закончилась");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher("/WEB-INF/jsp/deniedpage.jsp").forward(request, response);
        }
    }
}
