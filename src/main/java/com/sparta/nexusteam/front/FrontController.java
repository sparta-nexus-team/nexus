package com.sparta.nexusteam.front;

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
    public String home(){
        return "home.html";
    }

    @GetMapping("/work")
    public String work(){
        return "work.html";
    }

    @GetMapping("/work/myWork")
    public String myWork(){ return "mywork.html"; }

    @GetMapping("/work/companyWork")
    public String companyWork(){ return "companywork.html"; }

    @GetMapping("/vacation")
    public String vacation(){return "vacation.html";}

    @GetMapping("/vacation-manage")
    public String vacationManage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails.getEmployee().getRole().equals(UserRole.ADMIN)){
            return "vacationManageAdmin.html";
        } else if(userDetails.getEmployee().getRole().equals(UserRole.MANAGER)){
            return "vacationManageManager.html";
        } else {
           throw new RuntimeException("접근 권한이 없습니다.");
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
