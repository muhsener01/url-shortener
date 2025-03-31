package demo.muhsener01.urlshortener.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import demo.muhsener01.urlshortener.security.JwtUtils;
import demo.muhsener01.urlshortener.security.SecurityConstants;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final SecurityConstants securityConstants;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
        super(authenticationManager);
        this.securityConstants = securityConstants;
    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, SecurityConstants securityConstants) {
        super(authenticationManager, authenticationEntryPoint);
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!authenticationIsRequired(request)) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = extractToken(request).substring(securityConstants.getTokenPrefix().length()); // Bearer
        log.trace("Authenticating user with token.");

        try {
            JwtUtils.verifyToken(token);
            UserPrincipal userPrincipal = JwtUtils.extractUserPrincipal(token);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, "", userPrincipal.getRoles());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.trace("SecurityContext is set to {}", authenticationToken);

            filterChain.doFilter(request, response);

        } catch (JWTVerificationException exception) {
            getAuthenticationEntryPoint().commence(request, response, new BadCredentialsException(exception.getMessage(), exception));
            return;
        }


    }

    protected boolean authenticationIsRequired(HttpServletRequest request) {
        String header = extractToken(request);
        if (header == null || header.isBlank() || !header.startsWith(securityConstants.getTokenPrefix())) return false;

        return true;
    }

    protected String extractToken(HttpServletRequest request) {
        return request.getHeader(securityConstants.getHeaderName());
    }
}
