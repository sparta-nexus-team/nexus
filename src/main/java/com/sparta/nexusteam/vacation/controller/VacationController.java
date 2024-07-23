package com.sparta.nexusteam.vacation.controller;


import static com.sparta.nexusteam.base.ControllerUtil.getBadRequestResponseEntity;
import static com.sparta.nexusteam.base.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.nexusteam.base.ControllerUtil.getResponseEntity;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.service.VacationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;


    /**
     * 휴가 종류 등록
     */
    @PostMapping("/vacation-type")
    public ResponseEntity<CommonResponse> createVacationType(
            @Valid @RequestBody PostVacationTypeRequest requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 종류 등록 실패");
        }
        try {
            VacationTypeResponse responseDto = vacationService.createVacationType(requestDto);
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
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 등록 실패");
        }
        try {
            VacationResponse responseDto = vacationService.createVacation(vacationTypeId,
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
    public ResponseEntity<CommonResponse> readVacationsBeforeUse() {
        try {
            List<VacationResponse> responseDtoList = vacationService.readVacationsBeforeUse();
            return getResponseEntity(responseDtoList, "휴가 사용전 리스트 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 사용후 리스트 조회
     */
    @GetMapping("/vacation/after")
    public ResponseEntity<CommonResponse> readVacationsAfterUse() {
        try {
            List<VacationResponse> responseDtoList = vacationService.readVacationsAfterUse();
            return getResponseEntity(responseDtoList, "휴가 사용후 리스트 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 특정 휴가 조회
     */
    @GetMapping("/vacation/{vacationId}")
    public ResponseEntity<CommonResponse> readVacation(@PathVariable Long vacationId) {
        try {
            VacationResponse responseDto = vacationService.readVacation(vacationId);
            return getResponseEntity(responseDto, "특정 휴가 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 승인전 휴가 리스트 조회
     */
    @GetMapping("/vacation/approval")
    public ResponseEntity<CommonResponse> readVacationsBoforeApporval() {
        try {
            List<VacationResponse> responseDtoList = vacationService.readVacationsBoforeApporval();
            return getResponseEntity(responseDtoList, "승인전 휴가 리스트 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 휴가 종류 조회
     */
    @GetMapping("/vacation-type")
    public ResponseEntity<CommonResponse> readVacationTypes() {
        try {
            List<VacationTypeResponse> responseDtoList = vacationService.readVacationTypes();
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
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "휴가 승인/거절 실패");
        }
        try {
            VacationResponse responseDto = vacationService.updateVacationApprovalStatus(vacationId,
                    requestDto);
            return getResponseEntity(responseDto, "휴가 등록 성공");
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
            vacationService.deleteVacation(vacationId);
            return getResponseEntity(null, "휴가 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
