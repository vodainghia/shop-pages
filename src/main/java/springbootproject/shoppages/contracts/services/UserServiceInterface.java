package springbootproject.shoppages.contracts.services;

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
    
    Page<UserRequest> getUsersDataList(Pageable pageable, String sortColumn, String sortDirection);

    Page<UserRequest> getUsersDataList(Pageable pageable, String searchCriteria, String sortColumn, String sortDirection);

    void updateUser(UserRequest userRequest);

    void deleteUser(UserRequest userRequest);
}
