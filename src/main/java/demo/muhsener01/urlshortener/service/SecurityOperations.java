package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.security.UserPrincipal;

import java.util.UUID;

public interface SecurityOperations {

    UUID    getAuthenticatedUserId();

    UserPrincipal getAuthenticatedPrincipal();
}
