package springbootproject.shoppages.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    protected PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryInterface userRepo, RoleRepositoryInterface roleRepo,
            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserRequest userRequest) {
        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        String pw = this.passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(pw);

        Role role = this.roleRepo.findByName("ROLE_ADMIN");

        if (role == null) {
            role = saveRole();
        }

        user.setRoles(Arrays.asList(role));
        this.userRepo.save(user);
    }

    private Role saveRole() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        return this.roleRepo.save(role);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepo.findByEmail(email);
    }

    @Override
    public List<UserRequest> getUsersDataList() {
        List<User> users = this.userRepo.findAll();
        return users.stream().map(user -> convertUsers(user))
                .collect(Collectors.toList());
    }

    private UserRequest convertUsers(User user) {
        UserRequest users = new UserRequest();
        users.setId(user.getId());
        users.setName(user.getName());
        users.setEmail(user.getEmail());

        return users;
    }
}
