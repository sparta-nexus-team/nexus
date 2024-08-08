package com.sparta.nexusteam.vacation.service;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.repository.CompanyRepository;
import com.sparta.nexusteam.vacation.dto.AnnualLeaveResponseDto;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.PutVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.Vacation;
import com.sparta.nexusteam.vacation.entity.VacationType;
import com.sparta.nexusteam.vacation.entity.VacationTypeHistory;
import com.sparta.nexusteam.vacation.repository.VacationRepository;
import com.sparta.nexusteam.vacation.repository.VacationTypeHistoryRepository;
import com.sparta.nexusteam.vacation.repository.VacationTypeRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final CompanyRepository companyRepository;
    private final VacationRepository vacationRepository;
    private final VacationTypeRepository vacationTypeRepository;
    private final VacationTypeHistoryRepository vacationTypeHistoryRepository;
    private final CacheManager cacheManager;

    @Override
    @Transactional
//    @CacheEvict(value = "vacationTypes", key = "#companyId")
    public VacationTypeResponse createVacationType(PostVacationTypeRequest requestDto,
            Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회사가 없습니다"));
        VacationType vacationType = new VacationType(requestDto.getName(), requestDto.getDays(),
                company);
        VacationTypeHistory vacationTypeHistory = new VacationTypeHistory(vacationType);
        vacationType = vacationTypeRepository.save(vacationType);
        vacationTypeHistoryRepository.save(vacationTypeHistory);
        return new VacationTypeResponse(vacationType);
    }

    @Override
    @Transactional
//    @CacheEvict(value = "vacations", key = "#employee.id")
    public VacationResponse createVacation(Long vacationTypeId, PostVacationRequest requestDto,
            Employee employee) {
        VacationTypeHistory vacationTypeHistory = vacationTypeHistoryRepository.findTopByVacationTypeIdOrderByIdDesc(
                        vacationTypeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가 종류가 없습니다."));
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new IllegalArgumentException("시작일시는 종료일시 보다 이르면 안됩니다.");
        }
        if (vacationTypeHistory.getDays() * 24 < Duration.between(requestDto.getStartDate(),
                requestDto.getEndDate()).toHours()) {
            throw new IllegalArgumentException(("시작일시와 종료일시의 차이는 휴가 종류 일수보다 크면 안 됩니다."));
        }
        Vacation vacation = new Vacation(requestDto.getStartDate(), requestDto.getEndDate(),
                vacationTypeHistory, employee);
        vacation = vacationRepository.save(vacation);
        return new VacationResponse(vacation);
    }

    @Override
//    @Cacheable(value = "vacationsBeforeUse", key = "#employee.id")
    public List<VacationResponse> getVacationsBeforeUse(Employee employee) {
        List<Vacation> vacationList = vacationRepository.findByEndDateAfterAndEmployeeIdOrderByStartDateAsc(
                LocalDateTime.now(), employee.getId());
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
//    @Cacheable(value = "vacationsAfterUse", key = "#employee.id")
    public List<VacationResponse> getVacationsAfterUse(Employee employee) {
        List<Vacation> vacationList = vacationRepository.findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc(
                LocalDateTime.now(), employee.getId());
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
//    @Cacheable(value = "vacation", key = "#vacationId")
    public VacationResponse getVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가는 없습니다."));
        return new VacationResponse(vacation);
    }

    @Override
//    @Cacheable(value = "pendingVacations", key = "#companyId")
    public List<VacationResponse> getPendingVacations(Long companyId) {
        List<Vacation> vacationList = vacationRepository.findByCompanyIdAndApprovalStatus(companyId,
                ApprovalStatus.PENDING);
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
//    @Cacheable(value = "vacationTypes", key = "#companyId")
    public List<VacationTypeResponse> getVacationTypes(Long companyId) {
        List<VacationType> vacationTypes = vacationTypeRepository.findByCompanyIdAndIsDeletedFalse(companyId);
        return vacationTypes.stream().map(VacationTypeResponse::new).toList();
    }

    @Override
    @Transactional
//    @CacheEvict(value = "vacation", key = "#vacationId")
    public VacationResponse updateVacationApprovalStatus(Long vacationId,
            PatchVacationApprovalRequest requestDto) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가는 없습니다."));
        vacation.updateApprovalStatus(requestDto.getApprovalStatus());
        return new VacationResponse(vacation);
    }

    @Override
    @Transactional
//    @CacheEvict(value = "vacation", key = "#vacationId")
    public void deleteVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가는 없습니다."));
        if (vacation.getEndDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("사용한 휴가는 삭제 할수 없습니다.");
        }
        vacationRepository.delete(vacation);
    }

    @Override
    @Transactional
    public VacationTypeResponse updateVacationType(Long vacationTypeId,
            PutVacationTypeRequest requestDto) {
        VacationType vacationType = vacationTypeRepository.findById(vacationTypeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가 종류는 없습니다."));
        vacationType.updateVacationType(requestDto);
        VacationTypeHistory vacationTypeHistory = new VacationTypeHistory(vacationType);
        vacationTypeHistoryRepository.save(vacationTypeHistory);
        return new VacationTypeResponse(vacationType);
    }

    @Override
    @Transactional
    public void deleteVacationType(Long vacationTypeId) {
        VacationType vacationType = vacationTypeRepository.findById(vacationTypeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가 종류는 없습니다."));
        vacationType.softDelete();
    }


    public AnnualLeaveResponseDto getAnnualLeave(Employee employee) {
        Period hirePeriod = Period.between(employee.getHireDate(), LocalDate.now());
        int totalAnnualLeave;
        int usedAnnualLeave;
        int remainingAnnualLeave;
        //총 연차 지급일 계산
        if(hirePeriod.getYears()<1) {
          totalAnnualLeave = hirePeriod.getMonths();
        } else {
            totalAnnualLeave = 15+(hirePeriod.getYears()-1)/2;
        }
        //유효 연차 사용일 계산
        LocalDate AnnualLeaveGrantDate = employee.getHireDate().plusYears(hirePeriod.getYears());
//        usedAnnualLeave = vacationRepository.findByStartDate

        //남은 연차 계산
        return new AnnualLeaveResponseDto();
    }
}