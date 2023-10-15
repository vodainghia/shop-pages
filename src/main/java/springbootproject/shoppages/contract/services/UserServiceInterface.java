package springbootproject.shoppages.contract.services;

import springbootproject.shoppages.requests.UserRequest;

public interface UserServiceInterface {
    void saveUser(UserRequest userRequest);
}
