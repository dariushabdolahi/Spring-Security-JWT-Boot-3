package ir.dalit.model.repository;

import ir.dalit.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserDetails> findByUsernameAndPassword(String username, String password);

    Optional<UserDetails> findByUsername(String username);
}
