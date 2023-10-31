package springbootproject.shoppages.controllers;

import java.util.List;

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
public class UserManagementController {
    protected UserServiceInterface userService;

    public UserManagementController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserRequest> users = this.userService.getUsersDataList();
        model.addAttribute("users", users);

        return "pages/users";
    }

    @GetMapping("/users/create")
    public String create(Model model) {
        UserRequest user = new UserRequest();
        model.addAttribute("user", user);

        return "pages/forms/users/create_users";
    }

    // @PostMapping("/users/save")
    // public String save(
    // @Valid @ModelAttribute("user") UserRequest userRequest,
    // BindingResult result,
    // Model model,
    // HttpServletResponse response) throws IOException {

    // User checkExistedEmail =
    // this.userService.findByEmail(userRequest.getEmail());

    // if (checkExistedEmail != null) {
    // result.rejectValue("email", "409", "This email is already registed.");
    // }

    // if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
    // result.rejectValue("confirmPassword", "400",
    // "Your confirmed password should be identical to your original password.");
    // }

    // if (result.hasErrors()) {
    // model.addAttribute("user", userRequest);
    // response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request!");

    // return "pages/forms/users/create_users";
    // }

    // this.userService.saveUser(userRequest);

    // return "redirect:/users?success";
    // }

    @PostMapping("/users/save")
    public String save(
            @Valid @ModelAttribute("user") UserRequest userRequest,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) {

        User checkExistedEmail = userService.findByEmail(userRequest.getEmail());

        if (checkExistedEmail != null) {
            result.rejectValue("email", "409", "This email is already registered.");
            redirectAttributes.addFlashAttribute("error", "This email is already registered.");
            response.setStatus(HttpServletResponse.SC_CONFLICT); // Set HTTP 409 Conflict status
        }

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "400", "Password mismatch.");
            redirectAttributes.addFlashAttribute("error", "Password mismatch.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set HTTP 400 Bad Request status
        }

        if (result.hasErrors()) {
            return "pages/forms/users/create_users";
        }

        this.userService.saveUser(userRequest);
        redirectAttributes.addFlashAttribute("success", "User saved successfully.");
        response.setStatus(HttpServletResponse.SC_CREATED); // Set HTTP 201 Created status

        return "redirect:/users?success";
    }

    // @GetMapping("/users/edit/{id}")
    // public String edit(Model model) {
    //     return "pages/forms/users/update_users";
    // }

    // @PostMapping("/users/update")
    // public String update(
    //         @Valid @ModelAttribute("user") UserRequest userRequest,
    //         BindingResult result,
    //         Model model) {

    //     User checkExistedEmail = this.userService.findByEmail(userRequest.getEmail());

    //     if (checkExistedEmail != null) {
    //         result.rejectValue("email", "409", "This email is already registed.");
    //     }

    //     if (result.hasErrors()) {
    //         model.addAttribute("user", userRequest);
    //         return "pages/forms/users/update_users";
    //     }

    //     this.userService.saveUser(userRequest);

    //     return "redirect:/users?success";
    // }
}
