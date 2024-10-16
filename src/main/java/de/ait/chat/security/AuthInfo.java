package de.ait.chat.security;

import de.ait.chat.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


public class AuthInfo implements Authentication {

    private boolean authenticated;
    private String username;
    private Set<Role> roles;

    public AuthInfo(Set<Role> roles, String username) {
        this.roles = roles;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthInfo authInfo = (AuthInfo) o;
        return authenticated == authInfo.authenticated && Objects.equals(username, authInfo.username) && Objects.equals(roles, authInfo.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticated, username, roles);
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "authenticated=" + authenticated +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
