package springbootproject.shoppages.contract.services;

import java.util.List;

import springbootproject.shoppages.models.User;
import springbootproject.shoppages.requests.UserRequest;

public interface UserServiceInterface {
    void saveUser(UserRequest userRequest);

    User findByEmail(String email);

    List<UserRequest> getUsersDataList();

    List<UserRequest> getUsersDataList(String searchCriteria);

    void updateUser(UserRequest userRequest);

    void deleteUser(UserRequest userRequest);
}
