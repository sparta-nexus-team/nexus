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
    public String vacation(){return "vacation.html";}

    @GetMapping("/vacation-type-manage")
    public String vacationManage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(!userDetails.getEmployee().getRole().equals(UserRole.ADMIN)){
            throw new RuntimeException("Admin권한만 접근 가능합니다.");
        }
        return "vacationTypeManage.html";
    }

    @GetMapping("/vacation-apporval")
    public String vacationApproval(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getEmployee().getRole().equals(UserRole.USER)) {
            throw new RuntimeException("ADMIN,MANAGER권한만 접근 가능합니다.");
        }
        return "vacationApproval.html";
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
