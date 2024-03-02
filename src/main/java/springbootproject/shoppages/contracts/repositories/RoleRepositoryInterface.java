package springbootproject.shoppages.contracts.repositories;

import springbootproject.shoppages.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryInterface extends JpaRepository<Role, Long>{
    
    Role findByName(String name);
}
