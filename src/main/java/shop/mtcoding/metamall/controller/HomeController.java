package shop.mtcoding.metamall.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
    @RequestMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/productmanagement")
    public String productmanagement()
    {
        return "productmanagement";
    }

    @GetMapping("/ordermanagement")
    public String ordermanagement()
    {
        return "ordermanagement";
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken()
    {
        return ResponseEntity.ok().build();
    }
}
