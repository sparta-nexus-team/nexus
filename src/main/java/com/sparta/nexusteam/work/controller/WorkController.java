package com.sparta.nexusteam.work.controller;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.base.ControllerUtil;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.work.dto.WorkRequest;
import com.sparta.nexusteam.work.dto.WorkResponse;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.service.WorkServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work")
public class WorkController {
    private final WorkServiceImpl workService;

    public WorkController(WorkServiceImpl workService) {
        this.workService = workService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> saveWork(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody WorkRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return ControllerUtil.getFieldErrorResponseEntity(bindingResult,"근무 생성 실패");
        }
        try{
            Long id = workService.saveWork(userDetails.getEmployee(),request);
            return ControllerUtil.getResponseEntity(id,"근무 승인 요청 완료");
        }catch(Exception e){
            return ControllerUtil.getBadRequestResponseEntity(e);
        }

    }

    @GetMapping("/week")
    public ResponseEntity<CommonResponse> getWeekWork(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "work_date") String sortBy
            ){

        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
            Page<WorkResponse> pages = workService.getWeekWork(userDetails.getEmployee(),pageable);
            return ControllerUtil.getResponseEntity(pages,"근무 주간 조회 성공");
        }catch (Exception e){
            return ControllerUtil.getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/{company_id}/today")
    public ResponseEntity<CommonResponse> getEmployeeWorkToday(
            @PathVariable Long company_id ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "work_date") String sortBy
    ){
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
            Page<WorkResponse> pages = workService.getMemberDayWork(company_id,pageable);
            return ControllerUtil.getResponseEntity(pages,"회사 고용자 하루 근무 조회");
        }catch (Exception e){
            return ControllerUtil.getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/{company_id}/week")
    public ResponseEntity<CommonResponse> getEmployeeWorkWeek(
            @PathVariable Long company_id ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "work_date") String sortBy
    ){
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
            Page<WorkResponse> pages = workService.getMemberWeekWork(company_id,pageable);
            return ControllerUtil.getResponseEntity(pages,"회사 고용자 주간 근무 조회");
        }catch (Exception e){
            return ControllerUtil.getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/{company_id}/month")
    public ResponseEntity<CommonResponse> getEmployeeWorkMonth(
            @PathVariable Long company_id ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "work_date") String sortBy
    ){
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
            Page<WorkResponse> pages = workService.getMemberMonthWork(company_id,pageable);
            return ControllerUtil.getResponseEntity(pages,"회사 고용자 월간 근무 조회");
        }catch (Exception e){
            return ControllerUtil.getBadRequestResponseEntity(e);
        }
    }

}
