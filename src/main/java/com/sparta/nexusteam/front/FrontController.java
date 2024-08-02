package com.sparta.nexusteam.front;

import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping("/")
    public String index(){
        return "index.html";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup.html";
    }

    @GetMapping("/main")
    public String main(){
        return "main.html";
    }

    @GetMapping("/vacation")
    public String vacation(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails.getEmployee().getRole().equals(UserRole.MANAGER)){

        }
        return "vacation.html";
    }
}
