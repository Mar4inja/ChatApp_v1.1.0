package de.ait.chat.security;

import de.ait.chat.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class AuthInfo implements Authentication {

    private boolean authenticated;
    private String username;
    private Set<Role> roles;
    private LocalDate birthDate; // Changed to LocalDate

    // Constructor with LocalDate
    public AuthInfo(Set<Role> roles, String username, LocalDate birthDate) {
        this.roles = roles;
        this.username = username;
        this.birthDate = birthDate;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthInfo authInfo = (AuthInfo) o;
        return authenticated == authInfo.authenticated &&
                Objects.equals(username, authInfo.username) &&
                Objects.equals(roles, authInfo.roles) &&
                Objects.equals(birthDate, authInfo.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticated, username, roles, birthDate);
    }

    // Set birthDate from String if needed
    public void setBirthDateFromString(String birthDateString) {
        this.birthDate = LocalDate.parse(birthDateString); // Convert string to LocalDate
    }

    // Implement the Authentication interface methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;  // Assuming that Role implements GrantedAuthority
    }

    @Override
    public Object getCredentials() {
        return null;  // Credentials are typically the password, but we're not dealing with it here
    }

    @Override
    public Object getDetails() {
        return null;  // Additional details can be provided if necessary
    }

    @Override
    public Object getPrincipal() {
        return this;  // The principal is the AuthInfo itself (can be used to identify the user)
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;  // Return whether the user is authenticated
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;  // Set authentication status
    }

    @Override
    public String getName() {
        return username;  // Return the username
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }

    // toString method for debugging and logging purposes
    @Override
    public String toString() {
        return "AuthInfo{" +
                "authenticated=" + authenticated +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", birthDate=" + birthDate +
                '}';
    }
}
