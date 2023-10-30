package springbootproject.shoppages.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import springbootproject.shoppages.contract.services.UserServiceInterface;
import springbootproject.shoppages.models.User;
import springbootproject.shoppages.requests.UserRequest;

@Controller
public class UserController {

    protected UserServiceInterface userService;

    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/users-ajax")
    public String users(Model model) {
        UserRequest user = new UserRequest();
        model.addAttribute("user", user);

        return "pages/users-ajax";
    }

    @PostMapping("/users-ajax/list-data")
    public String getUsersDataList(Model model) {
        List<UserRequest> users = this.userService.getUsersDataList();
        model.addAttribute("users", users);

        return "pages/users_table";
    }

    @PostMapping("/users-ajax/save")
    public ResponseEntity<Map<String, String>> save(
            @Valid @ModelAttribute("user") UserRequest userRequest,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) {

        String userName = userRequest.getName();
        String userEmail = userRequest.getEmail();
        String userPassword = userRequest.getPassword();
        String userConfirmPassword = userRequest.getConfirmPassword();

        Map<String, String> errors = new HashMap<>();
        User checkExistedEmail = userService.findByEmail(userEmail);

        if (checkExistedEmail != null) {
            errors.put("email", "This email is already registered.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        if (userPassword != null && userConfirmPassword != null && !userPassword.equals(userConfirmPassword)) {
            errors.put("confirmPassword", "Your confirmed password should be identical to your original password.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (userName == null || userName.isEmpty()) {
            errors.put("name", "Name is empty");
        }

        if (userEmail == null || userEmail.isEmpty()) {
            errors.put("email", "Email is empty");
        }

        if (userPassword == null || userPassword.isEmpty()) {
            errors.put("password", "Password is empty");
        }

        if (userConfirmPassword == null || userConfirmPassword.isEmpty()) {
            errors.put("confirmPassword", "Confirm Password is empty");
        }

        if (result.hasErrors() || !errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        this.userService.saveUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}
