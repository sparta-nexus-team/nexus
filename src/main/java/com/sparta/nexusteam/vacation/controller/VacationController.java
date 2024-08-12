package com.sparta.nexusteam.vacation.controller;


import static com.sparta.nexusteam.base.ControllerUtil.getBadRequestResponseEntity;
import static com.sparta.nexusteam.base.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.nexusteam.base.ControllerUtil.getResponseEntity;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.vacation.dto.AnnualLeaveInfoResponse;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.PutVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.service.VacationServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VacationController {

    private final VacationServiceImpl vacationServiceImpl;


    /**
     * 휴가 종류 등록
     */
    @PostMapping("/vacation-type")
    public ResponseEntity<CommonResponse> createVacationType(
            @Valid @RequestBody PostVacationTypeRequest requestDto, BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 종류 등록 실패");
        }
        try {
            VacationTypeResponse responseDto = vacationServiceImpl.createVacationType(requestDto,
                    userDetails.getEmployee().getCompany().getId());
            return getResponseEntity(responseDto, "휴가 종류 등록 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 등록
     */
    @PostMapping("/vacation-type/{vacationTypeId}/vacation")
    public ResponseEntity<CommonResponse> createVacation(@PathVariable Long vacationTypeId,
            @Valid @RequestBody PostVacationRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 등록 실패");
        }
        try {
            VacationResponse responseDto = vacationServiceImpl.createVacation(vacationTypeId,
                    requestDto, userDetails.getEmployee());
            return getResponseEntity(responseDto, "휴가 등록 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 사용전 리스트 조회
     */
    @GetMapping("/vacation/before")
    public ResponseEntity<CommonResponse> getVacationsBeforeUse(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<VacationResponse> responseDtoList = vacationServiceImpl.getVacationsBeforeUse(
                    userDetails.getEmployee());
            return getResponseEntity(responseDtoList, "휴가 사용전 리스트 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 사용후 리스트 조회
     */
    @GetMapping("/vacation/after")
    public ResponseEntity<CommonResponse> getVacationsAfterUse(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<VacationResponse> responseDtoList = vacationServiceImpl.getVacationsAfterUse(
                    userDetails.getEmployee());
            return getResponseEntity(responseDtoList, "휴가 사용후 리스트 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 특정 휴가 조회
     */
    @GetMapping("/vacation/{vacationId}")
    public ResponseEntity<CommonResponse> getVacation(@PathVariable Long vacationId) {
        try {
            VacationResponse responseDto = vacationServiceImpl.getVacation(vacationId);
            return getResponseEntity(responseDto, "특정 휴가 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 승인전 휴가 리스트 조회
     */
    @GetMapping("/vacation/approval")
    public ResponseEntity<CommonResponse> getPendingVacations(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<VacationResponse> responseDtoList = vacationServiceImpl.getPendingVacations(
                    userDetails.getEmployee());
            return getResponseEntity(responseDtoList, "승인전 휴가 리스트 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 종류 조회
     */
    @GetMapping("/vacation-type")
    public ResponseEntity<CommonResponse> getVacationTypes(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<VacationTypeResponse> responseDtoList = vacationServiceImpl.getVacationTypes(
                    userDetails.getEmployee().getCompany().getId());
            return getResponseEntity(responseDtoList, "휴가 종류 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 승인/거절
     */
    @PatchMapping("/vacation/{vacationId}/approval")
    public ResponseEntity<CommonResponse> updateVacationApprovalStatus(
            @PathVariable Long vacationId,
            @Valid @RequestBody PatchVacationApprovalRequest requestDto,
            BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 승인/거절 실패");
        }
        try {
            if (userDetails.getEmployee().getRole().equals(UserRole.USER)) {
                throw new IllegalArgumentException("USER권한은 휴가 승인 권한이 없습니다.");
            }
            VacationResponse responseDto = vacationServiceImpl.updateVacationApprovalStatus(
                    vacationId, requestDto);
            return getResponseEntity(responseDto, "휴가 승인/거절 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 삭제
     */
    @DeleteMapping("/vacation/{vacationId}")
    public ResponseEntity<CommonResponse> deleteVacation(@PathVariable Long vacationId) {
        try {
            vacationServiceImpl.deleteVacation(vacationId);
            return getResponseEntity(null, "휴가 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 종류 삭제
     */
    @DeleteMapping("/vacation-type/{vacationTypeId}")
    public ResponseEntity<CommonResponse> deleteVacationType(@PathVariable Long vacationTypeId) {
        try {
            vacationServiceImpl.deleteVacationType(vacationTypeId);
            return getResponseEntity(null, "휴가 종류 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 종류 수정
     */
    @PutMapping("/vacation-type/{vacationTypeId}")
    public ResponseEntity<CommonResponse> updateVacationType(
            @PathVariable Long vacationTypeId,
            @Valid @RequestBody PutVacationTypeRequest requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 종류 수정 실패");
        }
        try {
            VacationTypeResponse responseDto = vacationServiceImpl.updateVacationType(
                    vacationTypeId,
                    requestDto);
            return getResponseEntity(responseDto, "휴가 종류 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 연차 정보 조회
     */
    @GetMapping("/annual-leave")
    public ResponseEntity<CommonResponse> getAnnualLeaveInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            AnnualLeaveInfoResponse responseDto = vacationServiceImpl.getAnnualLeaveInfo(userDetails.getEmployee());
            return getResponseEntity(responseDto, "연차 정보 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}