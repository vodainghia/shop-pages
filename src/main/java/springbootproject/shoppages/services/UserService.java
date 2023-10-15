package springbootproject.shoppages.services;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import springbootproject.shoppages.contract.repositories.RoleRepositoryInterface;
import springbootproject.shoppages.contract.repositories.UserRepositoryInterface;
import springbootproject.shoppages.contract.services.UserServiceInterface;
import springbootproject.shoppages.models.Role;
import springbootproject.shoppages.models.User;
import springbootproject.shoppages.requests.UserRequest;

@Service
public class UserService implements UserServiceInterface {

    protected UserRepositoryInterface userRepo;
    protected RoleRepositoryInterface roleRepo;

    public UserService(UserRepositoryInterface userRepo, RoleRepositoryInterface roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public void saveUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        Role role = saveRole();

        user.setRoles(Arrays.asList(role));
        this.userRepo.save(user);
    }

    private Role saveRole() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return this.roleRepo.save(role);
    }
}
