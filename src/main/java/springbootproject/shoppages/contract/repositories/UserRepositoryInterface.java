package springbootproject.shoppages.contract.repositories;

import springbootproject.shoppages.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
