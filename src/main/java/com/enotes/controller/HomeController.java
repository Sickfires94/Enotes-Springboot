package com.enotes.controller;

import com.enotes.entity.User;
import com.enotes.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        boolean f = userService.existEmailCheck(user.getEmail());
        if(f){
            session.setAttribute("msg","Email already exits");
        }
        else{
            User saveUser = userService.saveUser(user);
            if(saveUser != null){
                session.setAttribute("msg","Register successful");
            }
            else {
                session.setAttribute("msg", "Something wrong on server");
            }

        }
        return "redirect:/register";
    }



    @GetMapping("/signin")
    public String login() {
        return "login";
    }



}
