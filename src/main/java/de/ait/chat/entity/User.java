package de.ait.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "Required")
    @Size(max = 30, message = "Max 30 symbols")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only letters")
    @Schema(description = "User firstName", example = "James")
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 30, message = "Max 30 symbols")
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must contain only letters")
    @Schema(description = "User lastName", example = "May")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Chat> chats;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "email")
    @NotBlank(message = "Required")
    @Size(max = 30, message = "Max 30 symbols")
    @Email(message = "Invalid email format!")
    @Schema(description = "User email", example = "may@gmail.com")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Required")
    @Size(min = 60, max = 60)
    @Schema(description = "User password", example = "Password12#")
    private String password;

    @Column(name = "registration_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime registrationDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Можно добавить логику проверки на истекший срок учетной записи
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Можно добавить логику проверки на блокировку учетной записи
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Можно добавить логику проверки на истекшие учетные данные
    }

    @Override
    public boolean isEnabled() {
        return isActive; // Можно добавить дополнительную логику активации учетной записи
    }
}
