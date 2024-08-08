package com.stitch.gateway.security.config;

import com.stitch.commons.exception.StitchException;
import com.stitch.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Slf4j
//@Component
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final TokenAuthenticationProvider tokenAuthenticationProvider;
//
//
//    public TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider) {
//        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//
//        String header = httpServletRequest.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            log.trace("User attempting access without authentication token");
//        } else {
//
//            String token = header.substring(7);
//
//            log.info("my token :{}", token);
//
//            try {
//                System.out.println("hello world 1");
//                Authentication customerAuth = tokenAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList()));
//
//                log.info("customerAuth : {}", customerAuth);
//                Authentication vendorAuth = tokenAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList()));
//
//                System.out.println("hello world 1" + (Vendor) vendorAuth.getPrincipal());
//
//                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//                SecurityContextHolder.getContext().setAuthentication(vendorAuth);
//
//                logger.trace("User token is valid and is authenticated");
//            }
//            catch (Exception ex) {
//                logger.error("Token authentication error: " + ex.getMessage());
//                // guarantees the user is not authenticated at all
//                SecurityContextHolder.clearContext();
//                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
//                return;
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//
//}



@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    public TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String header = httpServletRequest.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("no header");
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            log.trace("User attempting access without authentication token");
//            return;
        } else {

            String token = header.substring(7);

            try {
//                Authentication authentication = tokenAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList()));

                Authentication authentication = tokenAuthenticationProvider.authenticate(token);

//                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.trace("User token is valid and is authenticated");

            } catch (AuthenticationException ex) {
                logger.error("Token authentication error: " + ex.getMessage());
                SecurityContextHolder.clearContext();
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
                throw new UserException("Token authentication error: " + ex.getMessage());
//                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }



//    @Autowired
//    private TokenUtils jwtService;
//
//    @Autowired
//    CustomUserDetailsServiceImpl userDetailsServiceImpl;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            token = authHeader.substring(7);
//            username = jwtService.extractUsername(token);
//        }
//
//        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
//            if(jwtService.validateToken(token, userDetails)){
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//
//        }
//
//        filterChain.doFilter(request, response);
//    }
}
