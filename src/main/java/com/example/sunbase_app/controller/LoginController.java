package com.example.sunbase_app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.sunbase_app.service.AuthService;
@Controller
public class LoginController {

    private final AuthService authService;
    @Autowired
    public LoginController(AuthService authService){
        this.authService = authService;
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Return the name of your HTML login page (login.html)
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        System.out.println(username+" "+password);
        String token = authService.authenticateUser(username, password);

        if (token != null) {
            // Authentication succeeded, redirect to a customer-list page
            return "redirect:/customer/list-login";
        } else {
            // Authentication failed, show an error message on the login page
            return "login"; // Return to the login page with an error message
        }
    }
    @GetMapping("/customer/list-login")
    public String CustomerList() {
        return "customer-list";
    }
}
