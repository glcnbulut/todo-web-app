package com.GoDo.user_api.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SecurityDebugHandlers {

    public static class FileAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            String msg = "[AccessDeniedHandler] Request=" + request.getMethod() + " " + request.getRequestURI() + " cause=" + accessDeniedException.getMessage() + "\n";
            try { Files.writeString(Path.of("/tmp/security_debug.log"), msg, StandardOpenOption.CREATE, StandardOpenOption.APPEND); } catch (Exception ignored) {}
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }
    }

    public static class FileAuthEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
            String msg = "[AuthEntryPoint] Request=" + request.getMethod() + " " + request.getRequestURI() + " cause=" + authException.getMessage() + "\n";
            try { Files.writeString(Path.of("/tmp/security_debug.log"), msg, StandardOpenOption.CREATE, StandardOpenOption.APPEND); } catch (Exception ignored) {}
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
}
