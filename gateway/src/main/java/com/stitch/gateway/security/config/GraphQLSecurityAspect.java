package com.stitch.gateway.security.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(1)
public class GraphQLSecurityAspect {

    /**
     * All graphQLResolver methods can be called only by authenticated user.
     * Exclusions are named in Pointcut expression.
     */
    @Before("isDefinedInGatewayControllers() && !isMethodAnnotatedAsUnsecured()")
    public void doSecurityCheck() {
        log.trace("Doing security check");
        if (SecurityContextHolder.getContext() == null ||
                SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
                AnonymousAuthenticationToken.class.isAssignableFrom(SecurityContextHolder.getContext().getAuthentication().getClass())) {
            throw new AccessDeniedException("User not authenticated");
        }
    }



    /**
     * Matches all beans in com.stitch.gateway.controller package
     * GraphQl Resolver controllers must be in this package
     */
    @Pointcut("within(com.stitch.gateway.controller..*)")
    private void isDefinedInGatewayControllers() {
    }


    /**
     * Any method annotated with @Unsecured
     */
    @Pointcut("@annotation(com.stitch.gateway.security.model.Unsecured)")
    private void isMethodAnnotatedAsUnsecured() {
    }
}
