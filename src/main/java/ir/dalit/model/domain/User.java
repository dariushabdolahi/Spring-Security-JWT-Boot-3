package ir.dalit.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "TBL_USERS")
@Getter
@Setter
@Accessors(chain = true)
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    private boolean enabled;
    private boolean lockStatus;
    @CreationTimestamp
    private LocalDateTime creation;
    private LocalDateTime expiration;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "TBL_USERS_ID")
    private List<UserRoles> userRoles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Objects.nonNull(expiration)
               && LocalDateTime.now().isBefore(expiration);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !lockStatus;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
