package com.sparta.nexusteam.vacation.service;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.PutVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import java.util.List;

public interface VacationService {

    VacationTypeResponse createVacationType(PostVacationTypeRequest requestDto,Long companyId);

    VacationResponse createVacation(Long vacationTypeId, PostVacationRequest requestDto, Employee employee);

    List<VacationResponse> getVacationsBeforeUse(Employee employee);

    List<VacationResponse> getVacationsAfterUse(Employee employee);

    VacationResponse getVacation(Long vacationId);

    List<VacationResponse> getPendingVacations(Long companyId);

    List<VacationTypeResponse> getVacationTypes(Long companyId);

    VacationResponse updateVacationApprovalStatus(Long vacationId, PatchVacationApprovalRequest requestDto);

    void deleteVacation(Long vacationId);

    VacationTypeResponse updateVacationType(Long vacationTypeId, PutVacationTypeRequest requestDto);

    void deleteVacationType(Long vacationTypeId);
}
