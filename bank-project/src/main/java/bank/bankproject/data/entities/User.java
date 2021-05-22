package bank.bankproject.data.entities;

import bank.bankproject.data.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Entity(name = "user")
@Data
@NoArgsConstructor
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authorities", nullable = false)
    private String authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "requestingUser", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Request> sentRequests = new HashSet<>();

    @OneToMany(mappedBy = "sendingUser", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Request> receivedRequests = new HashSet<>();

    public User(String username, String authorities, String password) {
        this.username = username;
        this.authorities = authorities;
        this.password = password;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return stream(authorities.split(";")).map(Role::new).collect(toSet());
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
