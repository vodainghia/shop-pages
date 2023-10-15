package springbootproject.shoppages.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import springbootproject.shoppages.contract.services.UserServiceInterface;
import springbootproject.shoppages.requests.UserRequest;

@Controller
public class RegisterController {

    protected UserServiceInterface userService;

    public RegisterController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        UserRequest user = new UserRequest();
        model.addAttribute("user", user);

        return "authentication/register";
    }

    @PostMapping("/register/save")
    public String createAccount(
            @Valid @ModelAttribute("user") UserRequest userRequest,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", userRequest);
            return "register";
        }

        this.userService.saveUser(userRequest);

        return "redirect:/login";
    }
}
