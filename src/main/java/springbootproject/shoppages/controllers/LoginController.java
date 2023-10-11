package springbootproject.shoppages.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String login() {
        return "authentication/login";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "pages/dashboard";
    }

    @RequestMapping("/calendar")
    public String calendar() {
        return "pages/calendar";
    }
}
