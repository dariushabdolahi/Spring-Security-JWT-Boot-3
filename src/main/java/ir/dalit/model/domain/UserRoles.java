package ir.dalit.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@Accessors(chain = true)
public class UserRoles implements GrantedAuthority,Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
