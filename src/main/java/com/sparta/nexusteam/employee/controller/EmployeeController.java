package com.sparta.nexusteam.employee.controller;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.dto.SignupResponse;
import com.sparta.nexusteam.employee.service.EmployeeService;
import com.sparta.nexusteam.employee.service.EmployeeServiceImpl;
import com.sparta.nexusteam.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.nexusteam.base.ControllerUtil.*;
import static com.sparta.nexusteam.security.JwtProvider.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
        }
        try{
            SignupResponse response = employeeServiceImpl.signup(request);
            return getResponseEntity(response, "회원 가입 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpResponse
    ) {
        try{
            Long response = employeeServiceImpl.logout(userDetails.getEmployee(), httpResponse);
            httpResponse.setHeader(AUTHORIZATION_HEADER, "");
            return getResponseEntity(response, "로그아웃 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
