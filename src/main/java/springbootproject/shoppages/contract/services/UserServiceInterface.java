package springbootproject.shoppages.contract.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import springbootproject.shoppages.models.User;
import springbootproject.shoppages.requests.UserRequest;

public interface UserServiceInterface {
    void saveUser(UserRequest userRequest);

    User findByEmail(String email);

    List<UserRequest> getUsersDataList();

    List<UserRequest> getUsersDataList(String searchCriteria);
    
    Page<UserRequest> getUsersDataList(Pageable pageable);

    void updateUser(UserRequest userRequest);

    void deleteUser(UserRequest userRequest);
}
