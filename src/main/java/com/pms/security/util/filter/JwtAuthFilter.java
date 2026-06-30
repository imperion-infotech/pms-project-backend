/**
 *
 */
package com.pms.security.util.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.RolePermissionCacheService;
import com.pms.security.util.CustomUserPrincipal;
import com.pms.security.util.JwtUtil;
import com.pms.usersession.service.IUserSessionLogService;
import com.pms.util.ConstantUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	public static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RolePermissionCacheService rolePermissionCacheService;
    
//    @Autowired
//    private IUserSessionLogService sessionLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {
        	
        	logger.info("=== JWT FILTER START ===");
        	logger.info("URI: {}", request.getRequestURI());
        	
        	logger.info("Method: {}", request.getMethod());
        	logger.info("RequestURI: {}", request.getRequestURI());
        	logger.info("ServletPath: {}", request.getServletPath());
        	logger.info("RequestURL: {}", request.getRequestURL());
        	logger.info("QueryString: {}", request.getQueryString());
        	logger.info("Authorization: {}", request.getHeader("Authorization"));
        	
        	logger.info("======================================");
        	logger.info("Method      : {}", request.getMethod());
        	logger.info("Request URI : {}", request.getRequestURI());
        	logger.info("URL         : {}", request.getRequestURL());
        	logger.info("Header Auth : {}", request.getHeader("Authorization"));
        	logger.info("======================================");
        	
        	

            String path = request.getServletPath();

            // Skip public APIs
            if (path.startsWith("/auth/")
                    || path.startsWith("/api/role-permissions/")
                    || path.startsWith("/swagger-ui/")
                    || path.startsWith("/v3/api-docs/")
                    || path.equals("/swagger-ui.html")) {

                chain.doFilter(request, response);
                return;
            }

            String header = request.getHeader("Authorization");
            
            logger.info("Authorization Header: {}", header);

            if (header == null || !header.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);
            
            logger.info("Token extracted");

            String username = jwtUtil.extractUsername(token);
            
            logger.info("Username: {}", username);
            
            Long userId = jwtUtil.extractUserId(token);
            Long hotelId = jwtUtil.extractHotelId(token);
            List<Long> roleIds = jwtUtil.extractRoleIds(token);
            logger.info("RoleIds: {}", roleIds);

            if (roleIds == null || roleIds.isEmpty()) {
                chain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                Collection<? extends GrantedAuthority> authorities;

                if (roleIds.contains(ConstantUtils.SUPER_ADMIN_ID)) {
                    authorities = rolePermissionCacheService.getAllAuthorities();
                } else {
                    authorities = rolePermissionCacheService.getAuthoritiesByRoleIds(roleIds);
                }
                
                logger.info("Authorities: {}", authorities);

                CustomUserPrincipal principal = new CustomUserPrincipal(
                        userId,
                        username,
                        authorities
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

//                logger.debug("Authorities ::::"+auth.getAuthorities().toString());
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("Authentication successfully set");
                logger.info("Authenticated user: {}", username);
                logger.info("Authorities: {}", auth.getAuthorities());
            }

            HotelContext.setHotelId(hotelId);
            UserContext.setUserId(userId);

            chain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {

            Long userId = jwtUtil.extractUserId(ex.getClaims().getSubject());

//            sessionLogService.markSessionExpired(userId);

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token Expired");
        } 
        
        
        catch (Exception e) {
            logger.error("Exception in JwtAuthFilter", e);
            throw e;
        } 
        
        finally {
//            HotelContext.clear();
//            UserContext.clear();
        }
    }
}