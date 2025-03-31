package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.exception.AuthenticationRequiredException;
import demo.muhsener01.urlshortener.security.UserPrincipal;

import java.util.UUID;

public interface SecurityOperations {

    /**
     * Returns <code>id</code> of authenticated user. If user is not authenticated, then throws <code>AuthenticationRequiredException</code>
     *
     * @return Id of authenticated user.
     * @throws AuthenticationRequiredException If user is not authenticated
     */
    UUID getAuthenticatedUserId() throws AuthenticationRequiredException;

    UserPrincipal getAuthenticatedPrincipal();
}
