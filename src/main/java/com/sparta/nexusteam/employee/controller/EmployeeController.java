package com.sparta.nexusteam.employee.controller;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.dto.SignupResponse;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.Invitation;
import com.sparta.nexusteam.employee.repository.InvitationRepository;
import com.sparta.nexusteam.employee.service.EmployeeService;
import com.sparta.nexusteam.employee.service.EmployeeServiceImpl;
import com.sparta.nexusteam.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.nexusteam.base.ControllerUtil.*;
import static com.sparta.nexusteam.security.JwtProvider.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    private final InvitationRepository invitationRepository;

    @PostMapping("/api/signup")
    public ResponseEntity<CommonResponse> signup(  //최초 가입자
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

    @GetMapping("/api/logout")
    public ResponseEntity<CommonResponse> logout( //로그아웃
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

    @PostMapping("/invite")
    public ResponseEntity<CommonResponse> inviteEmployee( //노동자 초대
            @RequestParam("email") String email
    ) {
        try{
            String response = employeeServiceImpl.inviteEmployee(email);
            return getResponseEntity(response, "초대 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/registerEmployee")
    public ResponseEntity<CommonResponse> confirmInvitation( //초대링크 유효성 검사
            @RequestParam("token") String token
    ) {
        try{
            Invitation invitation = invitationRepository.findByToken(token);
            if (invitation == null) {
                throw new Exception("Invalid token");
            }
            return getResponseEntity(invitation, "초대 토큰 유효");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PostMapping("/setNewEmployee")
    public ResponseEntity<CommonResponse> setNewEmployee( //초대링크 기반 가입
            @RequestParam("token") String token,
            @Valid @RequestBody SignupRequest signupRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "초대가입 실패");
        }
        try{
            SignupResponse response = employeeServiceImpl.setNewEmployee(token, signupRequest);
            return getResponseEntity(response, "초대가입 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
