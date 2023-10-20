package springbootproject.shoppages.contract.services;

import springbootproject.shoppages.models.User;
import springbootproject.shoppages.requests.UserRequest;

public interface UserServiceInterface {
    void saveUser(UserRequest userRequest);

    User findByEmail(String email);
}
