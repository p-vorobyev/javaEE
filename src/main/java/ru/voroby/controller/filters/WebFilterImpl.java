package ru.voroby.controller.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

import static ru.voroby.controller.filters.WebFilterImpl.SecurityContextImpl.fromUsernameWithNotSecureConnection;
import static ru.voroby.controller.filters.WebFilterImpl.SecurityContextImpl.fromUsernameWithSecureConnection;

@Slf4j
@Provider
@WebFilter(urlPatterns = {"/api/users"})
public class WebFilterImpl implements Filter, ContainerRequestFilter {

    @Context
    private HttpServletRequest httpRequest;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("Filter chain check.");
        if (request instanceof HttpServletRequest req) {
            log.debug("Context: [path: {}]", req.getRequestURI());
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SecurityContext securityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(
                securityContext.isSecure() ?
                        fromUsernameWithSecureConnection(httpRequest.getMethod() + "_TEST_USER") :
                        fromUsernameWithNotSecureConnection(httpRequest.getMethod() + "_TEST_USER")
        );
    }

    record SecurityContextImpl(String user, boolean isHttps) implements SecurityContext {

        public static SecurityContext fromUsernameWithSecureConnection(String username) {
            return new SecurityContextImpl(username, true);
        }

        public static SecurityContext fromUsernameWithNotSecureConnection(String username) {
            return new SecurityContextImpl(username, false);
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> user;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public boolean isSecure() {
            return isHttps;
        }

        @Override
        public String getAuthenticationScheme() {
            return "testScheme";
        }
    }
}
