// Her istekte JWT doğrulaması yapan Spring Security filtresi.
package com.GoDo.user_api.filter;

import com.GoDo.user_api.service.CustomUserDetailsService;
import com.GoDo.user_api.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull; // Dikkat: Bu import önemli
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    // Constructor, bağımlılıkları enjekte eder
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    // Her HTTP isteğinde JWT doğrulaması yapar
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    // lightweight debug via logger at debug level
    try {
        StringBuilder sb = new StringBuilder();
        sb.append("Incoming request: ")
            .append(request.getMethod()).append(" ").append(request.getRequestURI()).append("\n");
        String authHdr = request.getHeader("Authorization");
        sb.append("Authorization header: ")
            .append(authHdr == null ? "<none>" : authHdr).append("\n");
        logger.debug(sb.toString());
    } catch (Exception ignored) {
    }
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                // JWT Token geçersiz veya süresi dolmuş
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, AuthorityUtils.createAuthorityList(userDetails.getAuthorities().iterator().next().getAuthority()));
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}