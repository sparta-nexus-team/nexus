package com.sparta.nexusteam.vacation.service;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.Vacation;
import com.sparta.nexusteam.vacation.entity.VacationType;
import com.sparta.nexusteam.vacation.repository.VacationRepository;
import com.sparta.nexusteam.vacation.repository.VacationTypeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final VacationTypeRepository vacationTypeRepository;

    @Override
    public VacationTypeResponse createVacationType(PostVacationTypeRequest requestDto) {
        VacationType vacationType = new VacationType(requestDto.getName(), requestDto.getDays());
        vacationType = vacationTypeRepository.save(vacationType);
        return new VacationTypeResponse(vacationType);
    }

    @Override
    public VacationResponse createVacation(Long vacationTypeId, PostVacationRequest requestDto,
            Employee employee) {
        VacationType vacationType = vacationTypeRepository.findById(vacationTypeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 휴가 종류가 없습니다."));
        Vacation vacation = new Vacation(requestDto.getStartDate(), requestDto.getEndDate(),
                vacationType, employee);
        vacation = vacationRepository.save(vacation);
        return new VacationResponse(vacation);
    }

    @Override
    public List<VacationResponse> getVacationsBeforeUse(Employee employee) {
        List<Vacation> vacationList = vacationRepository.findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc(
                LocalDateTime.now(), employee.getId());
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
    public List<VacationResponse> getVacationsAfterUse(Employee employee) {
        List<Vacation> vacationList = vacationRepository.findByEndDateAfterAndEmployeeIdOrderByStartDateDesc(
                LocalDateTime.now(), employee.getId());
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
    public VacationResponse getVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 휴가는 없습니다."));
        return new VacationResponse(vacation);
    }

    @Override
    public List<VacationResponse> getPendingVacations() {
        List<Vacation> vacationList = vacationRepository.findByApprovalStatusOrderByStartDateAsc(
                ApprovalStatus.PENDING);
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
    public List<VacationTypeResponse> getVacationTypes() {
        List<VacationType> vacationTypes = vacationTypeRepository.findAll();
        return vacationTypes.stream().map(VacationTypeResponse::new).toList();
    }

    @Override
    @Transactional
    public VacationResponse updateVacationApprovalStatus(Long vacationId,
            PatchVacationApprovalRequest requestDto) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 휴가는 없습니다."));
        vacation.updateApprovalStatus(requestDto.getApprovalStatus());
        return new VacationResponse(vacation);
    }

    @Override
    @Transactional
    public void deleteVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 휴가는 없습니다."));
        vacationRepository.delete(vacation);
    }
}
