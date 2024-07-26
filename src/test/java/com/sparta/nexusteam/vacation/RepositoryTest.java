package com.sparta.nexusteam.vacation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.Position;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.employee.repository.EmployeeRepository;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.Vacation;
import com.sparta.nexusteam.vacation.entity.VacationType;
import com.sparta.nexusteam.vacation.repository.VacationRepository;
import com.sparta.nexusteam.vacation.repository.VacationTypeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class RepositoryTest {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private VacationTypeRepository vacationTypeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Nested
    @DisplayName("휴가 리포지토리 테스트")
    class vacation {

        @Test
        @Transactional
        public void save() {
            //given
            //employee 생성
            String accountId = "test";
            String password = "test12345!";
            String username = "test";
            String email = "test@test.com";
            String phoneNumber = "01012345678";
            String address = "test";
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setAccountId(accountId);
            signupRequest.setPassword(password);
            signupRequest.setUserName(username);
            signupRequest.setEmail(email);
            signupRequest.setPhoneNumber(phoneNumber);
            signupRequest.setAddress(address);
            Employee employee = new Employee(signupRequest, password, Position.ASSISTANT_MANAGER,
                    UserRole.USER);
            Employee savedEmployee = employeeRepository.save(employee);
            //vacationType 생성
            VacationType vacationType = new VacationType("연차", 1);
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //vacation 생성
            Vacation vacation = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), savedVacationType, savedEmployee);
            //when
            Vacation savedVacation = vacationRepository.save(vacation);
            //then
            assertNotNull(savedVacation.getId());
            assertEquals(LocalDateTime.of(2024, 07, 24, 9, 00), savedVacation.getStartDate());
            assertEquals(LocalDateTime.of(2024, 07, 25, 9, 00), savedVacation.getEndDate());
            assertEquals(savedVacationType.getId(), savedVacation.getVacationType().getId());
            assertEquals(savedEmployee.getId(), savedVacation.getEmployee().getId());
        }

        @Test
        @Transactional
        public void findById() {
            //given
            //employee 생성
            String accountId = "test";
            String password = "test12345!";
            String username = "test";
            String email = "test@test.com";
            String phoneNumber = "01012345678";
            String address = "test";
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setAccountId(accountId);
            signupRequest.setPassword(password);
            signupRequest.setUserName(username);
            signupRequest.setEmail(email);
            signupRequest.setPhoneNumber(phoneNumber);
            signupRequest.setAddress(address);
            Employee employee = new Employee(signupRequest, password, Position.ASSISTANT_MANAGER,
                    UserRole.USER);
            Employee savedEmployee = employeeRepository.save(employee);
            //vacationType 생성
            VacationType vacationType = new VacationType("연차", 1);
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //vacation 생성
            Vacation vacation = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), savedVacationType, savedEmployee);
            Vacation savedVacation = vacationRepository.save(vacation);
            Long vacationId = savedVacation.getId();
            //when
            Vacation findVacation = vacationRepository.findById(vacationId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 휴가는 없습니다."));
            //then
            assertEquals(vacationId, findVacation.getId());
            assertEquals(savedVacation.getStartDate(), findVacation.getStartDate());
            assertEquals(savedVacation.getEndDate(), findVacation.getEndDate());
            assertEquals(savedVacationType.getId(), findVacation.getVacationType().getId());
            assertEquals(savedEmployee.getId(), findVacation.getEmployee().getId());
        }

        @Test
        @Transactional
        public void findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc() {
            //given
            //employee 생성
            String accountId = "test";
            String password = "test12345!";
            String username = "test";
            String email = "test@test.com";
            String phoneNumber = "01012345678";
            String address = "test";
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setAccountId(accountId);
            signupRequest.setPassword(password);
            signupRequest.setUserName(username);
            signupRequest.setEmail(email);
            signupRequest.setPhoneNumber(phoneNumber);
            signupRequest.setAddress(address);
            Employee employee = new Employee(signupRequest, password, Position.ASSISTANT_MANAGER,
                    UserRole.USER);
            Employee savedEmployee = employeeRepository.save(employee);
            //vacationType 생성
            VacationType vacationType = new VacationType("연차", 1);
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //vacation 생성
            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 22, 9, 00),
                    LocalDateTime.of(2024, 07, 23, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation2 = new Vacation(LocalDateTime.of(2024, 07, 23, 9, 00),
                    LocalDateTime.of(2024, 07, 24, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation3 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation4 = new Vacation(LocalDateTime.of(2024, 07, 25, 9, 00),
                    LocalDateTime.of(2024, 07, 26, 9, 00), savedVacationType, savedEmployee);
            Vacation savedVacation1 = vacationRepository.save(vacation1);
            Vacation savedVacation2 = vacationRepository.save(vacation2);
            Vacation savedVacation3 = vacationRepository.save(vacation3);
            Vacation savedVacation4 = vacationRepository.save(vacation4);

            //when
            List<Vacation> vacationList = vacationRepository.findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc(
                    LocalDateTime.now(), employee.getId());
            //then
            assertTrue(vacationList.size() == 3);
            assertEquals(vacationList.get(0).getId(), savedVacation3.getId());
            assertEquals(vacationList.get(1).getId(), savedVacation2.getId());
            assertEquals(vacationList.get(2).getId(), savedVacation1.getId());
        }

        @Test
        @Transactional
        public void findByEndDateAfterAndEmployeeIdOrderByStartDateAsc() {
            //given
            //employee 생성
            String accountId = "test";
            String password = "test12345!";
            String username = "test";
            String email = "test@test.com";
            String phoneNumber = "01012345678";
            String address = "test";
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setAccountId(accountId);
            signupRequest.setPassword(password);
            signupRequest.setUserName(username);
            signupRequest.setEmail(email);
            signupRequest.setPhoneNumber(phoneNumber);
            signupRequest.setAddress(address);
            Employee employee = new Employee(signupRequest, password, Position.ASSISTANT_MANAGER,
                    UserRole.USER);
            Employee savedEmployee = employeeRepository.save(employee);
            //vacationType 생성
            VacationType vacationType = new VacationType("연차", 1);
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //vacation 생성
            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation2 = new Vacation(LocalDateTime.of(2024, 07, 25, 9, 00),
                    LocalDateTime.of(2024, 07, 26, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation3 = new Vacation(LocalDateTime.of(2024, 07, 26, 9, 00),
                    LocalDateTime.of(2024, 07, 27, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation4 = new Vacation(LocalDateTime.of(2024, 07, 27, 9, 00),
                    LocalDateTime.of(2024, 07, 28, 9, 00), savedVacationType, savedEmployee);
            Vacation savedVacation1 = vacationRepository.save(vacation1);
            Vacation savedVacation2 = vacationRepository.save(vacation2);
            Vacation savedVacation3 = vacationRepository.save(vacation3);
            Vacation savedVacation4 = vacationRepository.save(vacation4);

            //when
            List<Vacation> vacationList = vacationRepository.findByEndDateAfterAndEmployeeIdOrderByStartDateAsc(
                    LocalDateTime.now(), employee.getId());
            //then
            assertTrue(vacationList.size() == 3);
            assertEquals(vacationList.get(0).getId(), savedVacation2.getId());
            assertEquals(vacationList.get(1).getId(), savedVacation3.getId());
            assertEquals(vacationList.get(2).getId(), savedVacation4.getId());
        }

        @Test
        @Transactional
        public void findByApprovalStatusOrderByStartDateAsc() {
            //given
            //employee 생성
            String accountId = "test";
            String password = "test12345!";
            String username = "test";
            String email = "test@test.com";
            String phoneNumber = "01012345678";
            String address = "test";
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setAccountId(accountId);
            signupRequest.setPassword(password);
            signupRequest.setUserName(username);
            signupRequest.setEmail(email);
            signupRequest.setPhoneNumber(phoneNumber);
            signupRequest.setAddress(address);
            Employee employee = new Employee(signupRequest, password, Position.ASSISTANT_MANAGER,
                    UserRole.USER);
            Employee savedEmployee = employeeRepository.save(employee);
            //vacationType 생성
            VacationType vacationType = new VacationType("연차", 1);
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //vacation 생성
            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation2 = new Vacation(LocalDateTime.of(2024, 07, 25, 9, 00),
                    LocalDateTime.of(2024, 07, 26, 9, 00), savedVacationType, savedEmployee);
            Vacation vacation3 = new Vacation(LocalDateTime.of(2024, 07, 26, 9, 00),
                    LocalDateTime.of(2024, 07, 27, 9, 00), savedVacationType, savedEmployee);
            vacation3.updateApprovalStatus(ApprovalStatus.APPROVED);
            Vacation savedVacation1 = vacationRepository.save(vacation1);
            Vacation savedVacation2 = vacationRepository.save(vacation2);
            Vacation savedVacation3 = vacationRepository.save(vacation3);

            //when
            List<Vacation> vacationList = vacationRepository.findByApprovalStatusOrderByStartDateAsc(
                    ApprovalStatus.PENDING);

            //then
            assertTrue(vacationList.size() == 2);
            assertEquals(vacationList.get(0).getId(), savedVacation1.getId());
            assertEquals(vacationList.get(1).getId(), savedVacation2.getId());
        }

        @Test
        @Transactional
        public void delete() {
            //given
            //employee 생성
            String accountId = "test";
            String password = "test12345!";
            String username = "test";
            String email = "test@test.com";
            String phoneNumber = "01012345678";
            String address = "test";
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setAccountId(accountId);
            signupRequest.setPassword(password);
            signupRequest.setUserName(username);
            signupRequest.setEmail(email);
            signupRequest.setPhoneNumber(phoneNumber);
            signupRequest.setAddress(address);
            Employee employee = new Employee(signupRequest, password, Position.ASSISTANT_MANAGER,
                    UserRole.USER);
            Employee savedEmployee = employeeRepository.save(employee);
            //vacationType 생성
            VacationType vacationType = new VacationType("연차", 1);
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //vacation 생성
            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), savedVacationType, savedEmployee);
            Vacation savedVacation1 = vacationRepository.save(vacation1);

            //when
            vacationRepository.delete(savedVacation1);

            //then
            Optional<Vacation> deletedVacation = vacationRepository.findById(
                    savedVacation1.getId());
            assertTrue(deletedVacation.isEmpty());
        }
    }

    @Nested
    @DisplayName("휴가 종류 리포지토리 테스트")
    class vacationType {

        @Test
        @Transactional
        public void save() {
            //given
            VacationType vacationType = new VacationType("연차", 1);
            //when
            VacationType savedVacationType = vacationTypeRepository.save(vacationType);
            //then
            assertNotNull(savedVacationType.getId());
            assertEquals(vacationType.getName(), savedVacationType.getName());
            assertEquals(vacationType.getDays(), savedVacationType.getDays());
        }

        @Test
        @Transactional
        public void findAll() {
            //given
            VacationType vacationType1 = new VacationType("연차", 1);
            VacationType vacationType2 = new VacationType("연차", 1);
            VacationType vacationType3 = new VacationType("연차", 1);
            vacationTypeRepository.save(vacationType1);
            vacationTypeRepository.save(vacationType2);
            vacationTypeRepository.save(vacationType3);

            //when
            List<VacationType> vacationTypes = vacationTypeRepository.findAll();

            //then
            assertEquals(3, vacationTypes.size());
            for (VacationType vacationType : vacationTypes) {
                assertNotNull(vacationType.getId());
                assertNotNull(vacationType.getName());
                assertNotNull(vacationType.getDays());
            }
        }

        @Test
        @Transactional
        public void findById() {
            //given
            VacationType vacationType1 = new VacationType("연차", 1);
            VacationType savedVacationType1 = vacationTypeRepository.save(vacationType1);
            //when
            VacationType findVacationType1 = vacationTypeRepository.findById(
                            savedVacationType1.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가종류를 찾을 수 없습니다."));
            //then
            assertNotNull(findVacationType1);
            assertEquals(savedVacationType1.getId(), findVacationType1.getId());
            assertEquals(savedVacationType1.getName(), findVacationType1.getName());
            assertEquals(savedVacationType1.getDays(), findVacationType1.getDays());
        }
    }


}
