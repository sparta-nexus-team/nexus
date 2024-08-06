package com.sparta.nexusteam.front;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/home")
    public String home(){
        return "home.html";
    }

    @GetMapping("/vacation")
    public String vacation(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails.getEmployee().getRole().equals(UserRole.MANAGER)){
            return "vacationManage.html";
        }
        else {
            return "vacation.html";
        }
    }

    // 등록 양식 페이지 처리
    @GetMapping("/employee/registerEmployee")
    public String showRegistrationForm(@RequestParam("token") String token, Model model) {
        // 토큰을 사용하여 사용자 인증 또는 상태 확인
        // 예: 토큰 유효성 검사 및 사용자 정보 로드
        model.addAttribute("token", token);
        return "registerEmployee.html";
    }

}
