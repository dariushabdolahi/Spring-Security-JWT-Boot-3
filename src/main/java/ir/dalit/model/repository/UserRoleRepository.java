package ir.dalit.model.repository;

import ir.dalit.model.domain.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoles, UUID> {
}
