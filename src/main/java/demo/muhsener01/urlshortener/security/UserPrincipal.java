package demo.muhsener01.urlshortener.security;

import demo.muhsener01.urlshortener.domain.entity.MyUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements UserDetails {

    private final UUID id;
    private final String username;
    private final String email;
    private final Collection<GrantedAuthority> roles;
    private String password;

    public UserPrincipal(MyUser myUser) {
        this.id = myUser.getId();
        this.username = myUser.getUsername();
        this.email = myUser.getEmail();
        this.password = myUser.getPassword();

        this.roles = myUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

    }


    public UserPrincipal(UUID id, String username, String email, Collection<GrantedAuthority> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return roles.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }


}
