package com.sparta.nexusteam.employee.controller;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.employee.dto.*;
import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Invitation;
import com.sparta.nexusteam.employee.repository.InvitationRepository;
import com.sparta.nexusteam.employee.service.DepartmentServiceImpl;
import com.sparta.nexusteam.employee.service.EmployeeServiceImpl;
import com.sparta.nexusteam.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.nexusteam.base.ControllerUtil.*;
import static com.sparta.nexusteam.security.JwtProvider.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    private final InvitationRepository invitationRepository;

    private final DepartmentServiceImpl departmentServiceImpl;

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

    @PostMapping("/employee/invite")
    public ResponseEntity<CommonResponse> inviteEmployee( //사원 초대
            @RequestParam("email") String email,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            String response = employeeServiceImpl.inviteEmployee(email, userDetails.getEmployee());
            return getResponseEntity(response, "초대 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/employee/registerEmployee")
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

    @PostMapping("/employee/setNewEmployee")
    public ResponseEntity<CommonResponse> setNewEmployee( //초대링크 기반 가입
            @RequestParam("token") String token,
            @Valid @RequestBody InviteSignupRequest invitesignupRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "초대가입 실패");
        }
        try{
            SignupResponse response = employeeServiceImpl.setNewEmployee(token, invitesignupRequest);
            return getResponseEntity(response, "초대가입 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/employee/list")
    public ResponseEntity<CommonResponse> getAllEmployees( //모든 사원 조회 (id, 이름, 직급, 부서)
             @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            List<EmployeeResponse> response = employeeServiceImpl.getAllEmployees(userDetails.getEmployee());
            return getResponseEntity(response, "전체사원 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/employee/list/byDepartment")
    public ResponseEntity<CommonResponse> getEmployeesByDepartment( //특정 부서 사원 조회
            @RequestParam("departmentName") String departmentName,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            List<EmployeeResponse> response = employeeServiceImpl.getEmployeesByDepartment(departmentName, userDetails.getEmployee());
            return getResponseEntity(response, "특정부서 사원 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/employee/list/byUserName")
    public ResponseEntity<CommonResponse> getEmployeesByUserName( //특정 이름 사원 조회
            @RequestParam("userName") String userName,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            List<EmployeeResponse> response = employeeServiceImpl.getEmployeesByUserName(userName, userDetails.getEmployee());
            return getResponseEntity(response, "특정이름 사원 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> getEmployeeById( //특정 사원 조회
            @PathVariable Long id
    ) {
        try{
            EmployeeResponse response = employeeServiceImpl.getEmployeeById(id);
            return getResponseEntity(response, "특정사원 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> updateEmployee( // 사원 정보 수정
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest employeeDetails,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "사원 정보 수정 실패");
        }
        try{
            EmployeeResponse response = employeeServiceImpl.updateEmployee(id, employeeDetails, userDetails.getEmployee());
            return getResponseEntity(response, "사원 정보 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> deleteEmployee( // 사원 삭제
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = employeeServiceImpl.deleteEmployee(id, userDetails.getEmployee());
            return getResponseEntity(response, "사원 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }


    @GetMapping("/employee/departments")
    public ResponseEntity<CommonResponse> getAllDepartments( // 모든 부서 조회
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            List<DepartmentResponse> response = departmentServiceImpl.getAllDepartments(userDetails.getEmployee());
            return getResponseEntity(response, "모든 부서 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PostMapping("/employee/departments")
    public ResponseEntity<CommonResponse> createDepartment( // 부서 생성
            @Valid @RequestBody Department department,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "부서 추가 실패");
        }
        try{
            Department response = departmentServiceImpl.createDepartment(department, userDetails.getEmployee());
            return getResponseEntity(response, "부서 추가 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PutMapping("/employee/departments/{id}")
    public ResponseEntity<CommonResponse> updateDepartment( // 부서 이름 변경
            @PathVariable Long id,
            @Valid @RequestBody Department departmentDetails,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "부서 수정 실패");
        }
        try{
            Department response = departmentServiceImpl.updateDepartment(id, departmentDetails, userDetails.getEmployee());
            return getResponseEntity(response, "부서 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @DeleteMapping("/employee/departments/{id}")
    public ResponseEntity<CommonResponse> deleteDepartment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = departmentServiceImpl.deleteDepartment(id, userDetails.getEmployee());
            return getResponseEntity(response, "부서 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
