package com.sparta.nexusteam.vacation.service;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.employee.repository.CompanyRepository;
import com.sparta.nexusteam.vacation.dto.AnnualLeaveInfoResponse;
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
        Vacation vacation;
        Duration vacationDuration = Duration.between(requestDto.getStartDate(),
                requestDto.getEndDate());
        // 0이 아니면 휴가 종류를 가지는 일반 휴가로 저장
        if (vacationTypeId != 0) {
            VacationTypeHistory vacationTypeHistory = vacationTypeHistoryRepository.findTopByVacationTypeIdOrderByIdDesc(
                            vacationTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가 종류가 없습니다."));
            if (vacationTypeHistory.getDays() * 24 < vacationDuration.toHours()) {
                throw new IllegalArgumentException(("신청시 지급일 보다 많은 휴가를 사용할 수 없습니다."));
            }
            vacation = new Vacation(requestDto.getStartDate(), requestDto.getEndDate(),
                    vacationTypeHistory, employee);
        } else { // 0이면 연차 휴가로 저장
            if (getAnnualLeaveInfo(employee).getRemainingAnnualLeave() < vacationDuration.toDays() + (
                    vacationDuration.toHours() % 24 / 8)) {
                throw new IllegalArgumentException("남은 연차 보다 많은 휴가를 사용할 수 없습니다.");
            }
            vacation = new Vacation(requestDto.getStartDate(), requestDto.getEndDate(), employee);
        }

        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new IllegalArgumentException("시작일시는 종료일시 전이여야 합니다");
        }
        vacation = vacationRepository.save(vacation);
        return new VacationResponse(vacation);
    }
    @Override
    public List<VacationResponse> getVacationsRequest(Employee employee) {
        List<Vacation> vacations = vacationRepository.findByApprovalStatusAndEmployeeId(ApprovalStatus.PENDING, employee.getId());
        return vacations.stream().map(VacationResponse::new).toList();
    }
    @Override
//    @Cacheable(value = "vacationsBeforeUse", key = "#employee.id")
    public List<VacationResponse> getVacationsBeforeUse(Employee employee) {
        List<Vacation> vacationList = vacationRepository.findByEndDateAfterAndEmployeeIdAndApprovalStatusOrderByStartDateAsc(
                LocalDateTime.now(), employee.getId(), ApprovalStatus.APPROVED);
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
//    @Cacheable(value = "vacationsAfterUse", key = "#employee.id")
    public List<VacationResponse> getVacationsAfterUse(Employee employee) {
        List<Vacation> vacationList = vacationRepository.findByEndDateBeforeAndEmployeeIdAndApprovalStatusOrderByStartDateDesc(
                LocalDateTime.now(), employee.getId(), ApprovalStatus.APPROVED);
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
    public List<VacationResponse> getPendingVacations(Employee employee) {
        UserRole userRole = switch(employee.getRole()) {
            case ADMIN -> UserRole.MANAGER;
            default -> UserRole.USER;
        };
        List<Vacation> vacationList = vacationRepository.findByCompanyIdAndApprovalStatus(employee.getCompany().getId(),
                ApprovalStatus.PENDING,userRole);
        return vacationList.stream().map(VacationResponse::new).toList();
    }

    @Override
//    @Cacheable(value = "vacationTypes", key = "#companyId")
    public List<VacationTypeResponse> getVacationTypes(Long companyId) {
        List<VacationType> vacationTypes = vacationTypeRepository.findByCompanyIdAndIsDeletedFalse(
                companyId);
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
        if (vacation.getEndDate().isBefore(LocalDateTime.now())) {
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


    public AnnualLeaveInfoResponse getAnnualLeaveInfo(Employee employee) {
        //변수 선언
        Period hirePeriod = Period.between(employee.getHireDate(), LocalDate.now());
        float totalAnnualLeave;
        float usedAnnualLeave = 0f;
        float remainingAnnualLeave;
        LocalDateTime AnnualLeaveGrantDateTime;
        //입사 1년 미만 총 연차 및 지급일 계산
        if (hirePeriod.getYears() < 1) {
            totalAnnualLeave = hirePeriod.getMonths();
            AnnualLeaveGrantDateTime = employee.getHireDate()
                    .plusMonths(hirePeriod.getMonths()).atStartOfDay();
        } else { //입사 1년 이상 총 연차 및 지급일 계산
            totalAnnualLeave = 15 + (hirePeriod.getYears() - 1) / 2;
            AnnualLeaveGrantDateTime = employee.getHireDate()
                    .plusYears(hirePeriod.getYears()).atStartOfDay();
        }
        //유효 연차 리스트 조회
        List<Vacation> annualLeaveList = vacationRepository.findUsedAnnualLeaveAfterGrantDate(
                AnnualLeaveGrantDateTime, employee.getId());
        //사용한 연차 계산
        for (Vacation annualLeave : annualLeaveList) {
            Duration leaveDuration = Duration.between(annualLeave.getStartDate(), annualLeave.getEndDate());
            usedAnnualLeave += leaveDuration.toDays() + ((float)leaveDuration.toHours() % 24 / 8);
        }

        //남은 연차 계산
        remainingAnnualLeave = totalAnnualLeave - usedAnnualLeave;

        return new AnnualLeaveInfoResponse(employee.getId(), employee.getUserName(),
                totalAnnualLeave, usedAnnualLeave, remainingAnnualLeave);
    }


}