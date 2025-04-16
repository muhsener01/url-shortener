package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.exception.AuthenticationRequiredException;
import demo.muhsener01.urlshortener.exception.NoPermissionException;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.SecurityOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityOperationsImpl implements SecurityOperations {


    public UUID getAuthenticatedUserId() throws AuthenticationRequiredException {
        return getAuthenticatedPrincipal().getId();

    }

    @Override
    public UserPrincipal getAuthenticatedPrincipal() throws AuthenticationRequiredException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationRequiredException("Authentication is required!");
        }

        return ((UserPrincipal) authentication.getPrincipal());
    }

    @Override
    public boolean isUrlOwnerOrAdmin(Link link) throws AuthenticationRequiredException {
        UserPrincipal userPrincipal = getAuthenticatedPrincipal();

        return (link.getUserId().equals(userPrincipal.getId()) || userPrincipal.isAdmin());


    }

    @Override
    public void validateIsAdmin() throws AuthenticationRequiredException, NoPermissionException {
        UserPrincipal authenticatedPrincipal = getAuthenticatedPrincipal();

        if (!authenticatedPrincipal.isAdmin()) {
            throw new NoPermissionException("Forbidden access");
        }

    }
}
