package springbootproject.shoppages.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import springbootproject.shoppages.contract.services.UserServiceInterface;
import springbootproject.shoppages.models.User;
import springbootproject.shoppages.requests.UserRequest;

@Controller
public class UserController {

    protected UserServiceInterface userService;
    protected int pageSize = 5;

    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/users-ajax")
    public String users(Model model) {
        UserRequest user = new UserRequest();
        model.addAttribute("user", user);

        return "pages/users-ajax";
    }

    @GetMapping("/users-ajax/list-data")
    public String getUsersDataList(Model model, @RequestParam("pageIndex") int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<UserRequest> userRequestPage = this.userService.getUsersDataList(pageable);

        model.addAttribute("users", userRequestPage.getContent());
        model.addAttribute("userCount", userRequestPage.getTotalElements());
        model.addAttribute("totalPages", userRequestPage.getTotalPages());
        model.addAttribute("currentPage", pageIndex);

        return "pages/users_table";
    }

    @PostMapping("/users-ajax/save")
    public ResponseEntity<Map<String, String>> save(
            @Valid @ModelAttribute("user") UserRequest userRequest,
            BindingResult bindingResult,
            HttpServletResponse response) {

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

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        } else if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        } else {
            this.userService.saveUser(userRequest);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/users-ajax/update")
    public ResponseEntity<Map<String, String>> update(
            @Valid @ModelAttribute("user") UserRequest userRequest,
            BindingResult bindingResult,
            HttpServletResponse response) {

        String userEmail = userRequest.getEmail();
        String userTargetEmail = userRequest.getTargetEmail();
        String userPassword = userRequest.getPassword();
        String userConfirmPassword = userRequest.getConfirmPassword();

        Map<String, String> errors = new HashMap<>();
        User checkExistedEmail = userService.findByEmail(userEmail);

        if (checkExistedEmail != null && !checkExistedEmail.getEmail().equals(userTargetEmail)) {
            errors.put("update-email", "This email is already registered.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        if (userPassword != null && userConfirmPassword != null && !userPassword.equals(userConfirmPassword)) {
            errors.put("update-confirmPassword",
                    "Your confirmed password should be identical to your original password.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        } else if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put("update-" + error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        } else {
            this.userService.updateUser(userRequest);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/users-ajax/delete")
    public ResponseEntity<Map<String, String>> delete(
            @RequestParam("email") String email,
            HttpServletResponse response) {

        Map<String, String> errors = new HashMap<>();
        User checkExistedEmail = this.userService.findByEmail(email);

        if (checkExistedEmail == null) {
            errors.put("delete-email", "This email is not existing.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email);
        this.userService.deleteUser(userRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/users-ajax/search-data")
    public String search(
            Model model,
            @RequestParam("searchCriteria") String searchCriteria,
            @RequestParam("pageIndex") int pageIndex) {

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<UserRequest> userRequestPage = this.userService.getUsersDataList(pageable, searchCriteria);

        model.addAttribute("users", userRequestPage.getContent());
        model.addAttribute("userCount", userRequestPage.getTotalElements());
        model.addAttribute("totalPages", userRequestPage.getTotalPages());
        model.addAttribute("currentPage", pageIndex);

        return "pages/users_table";
    }

}
