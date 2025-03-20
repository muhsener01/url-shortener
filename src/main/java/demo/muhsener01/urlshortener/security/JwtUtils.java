package demo.muhsener01.urlshortener.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import demo.muhsener01.urlshortener.utils.SpringContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtUtils {

    private static final SecurityConstants securityConstants = SpringContext.getBean(SecurityConstants.class);

    public static String generateToken(UUID userId, String email, String username, Collection<GrantedAuthority> authorities) {
        Instant now = Instant.now();
        return JWT.create().withIssuedAt(now).withExpiresAt(now.plusMillis(securityConstants.getTokenExpirationTime())).withSubject(username).withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList()).withClaim("email", email).withClaim("userId", userId.toString()).sign(Algorithm.HMAC256(securityConstants.getSecretKey()));
    }


    public static void verifyToken(String token) {
        JWT.require(Algorithm.HMAC256(securityConstants.getSecretKey())).build().verify(token);
    }

    public static UserPrincipal extractUserPrincipal(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String email = jwt.getClaim("email").asString();
        String username = jwt.getSubject();
        Collection<GrantedAuthority> roles = jwt.getClaim("roles").asList(String.class).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        UUID userId = UUID.fromString(jwt.getClaim("userId").asString());

        return new UserPrincipal(userId, username, email, roles);


    }
}
