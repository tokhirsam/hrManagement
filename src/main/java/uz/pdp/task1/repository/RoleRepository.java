package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.task1.entity.Role;
import uz.pdp.task1.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
