package springbootproject.shoppages.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public UserService(
            UserRepositoryInterface userRepo,
            RoleRepositoryInterface roleRepo,
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

        Role adminRole = this.roleRepo.findByName("ROLE_ADMIN");

        if (adminRole == null) {
            adminRole = saveRole();
        }

        List<Role> userRoles = new ArrayList<>();
        userRoles.add(adminRole);

        user.setRoles(userRoles);

        this.userRepo.save(user);
    }

    @Override
    public void updateUser(UserRequest userRequest) {
        User targetUser = findByEmail(userRequest.getTargetEmail());

        targetUser.setName(userRequest.getName());

        if (!userRequest.getEmail().equals(targetUser.getEmail())) {
            targetUser.setEmail(userRequest.getEmail());
        }

        String pw = this.passwordEncoder.encode(userRequest.getPassword());
        targetUser.setPassword(pw);

        this.userRepo.save(targetUser);
    }

    @Override
    public void deleteUser(UserRequest userRequest) {
        User user = this.userRepo.findByEmail(userRequest.getEmail());

        user.getRoles().clear();
        this.userRepo.delete(user);
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

    @Override
    public List<UserRequest> getUsersDataList(String searchCriteria) {
        List<User> users = this.userRepo.findEmailOrNameByKeyword(searchCriteria);

        return users.stream().map(user -> convertUsers(user))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserRequest> getUsersDataList(Pageable pageable, String sortColumn, String sortDirection) {
        Sort sort = Sort.by(sortColumn);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<User> userPage = this.userRepo.findAll(pageable);

        List<UserRequest> userRequests = userPage.stream()
                .map(user -> convertUsers(user))
                .collect(Collectors.toList());

        return new PageImpl<>(userRequests, pageable, userPage.getTotalElements());
    }

    @Override
    public Page<UserRequest> getUsersDataList(Pageable pageable, String searchCriteria, String sortColumn, String sortDirection) {
        Sort sort = Sort.by(sortColumn);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<User> userPage = this.userRepo.findEmailOrNameByKeyword(pageable, searchCriteria);

        List<UserRequest> userRequests = userPage.stream()
                .map(user -> convertUsers(user))
                .collect(Collectors.toList());

        return new PageImpl<>(userRequests, pageable, userPage.getTotalElements());
    }

    private UserRequest convertUsers(User user) {
        UserRequest users = new UserRequest();
        users.setId(user.getId());
        users.setName(user.getName());
        users.setEmail(user.getEmail());

        return users;
    }
}
