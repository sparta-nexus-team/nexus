package com.sparta.nexusteam.vacation.service;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import java.util.List;

public interface VacationService {

    VacationTypeResponse createVacationType(PostVacationTypeRequest requestDto);

    VacationResponse createVacation(Long vacationTypeId, PostVacationRequest requestDto, Employee employee);

    List<VacationResponse> getVacationsBeforeUse(Employee employee);

    List<VacationResponse> getVacationsAfterUse(Employee employee);

    VacationResponse getVacation(Long vacationId);

    List<VacationResponse> getPendingVacations();

    List<VacationTypeResponse> getVacationTypes();

    VacationResponse updateVacationApprovalStatus(Long vacationId, PatchVacationApprovalRequest requestDto);

    void deleteVacation(Long vacationId);
}
