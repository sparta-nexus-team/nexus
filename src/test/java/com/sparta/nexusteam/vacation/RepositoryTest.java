//package com.sparta.nexusteam.vacation;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.sparta.nexusteam.config.QueryDslConfig;
//import com.sparta.nexusteam.employee.dto.SignupRequest;
//import com.sparta.nexusteam.employee.entity.Company;
//import com.sparta.nexusteam.employee.entity.Employee;
//import com.sparta.nexusteam.employee.entity.Position;
//import com.sparta.nexusteam.employee.entity.UserRole;
//import com.sparta.nexusteam.employee.repository.CompanyRepository;
//import com.sparta.nexusteam.employee.repository.EmployeeRepository;
//import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
//import com.sparta.nexusteam.vacation.entity.Vacation;
//import com.sparta.nexusteam.vacation.entity.VacationType;
//import com.sparta.nexusteam.vacation.repository.VacationRepository;
//import com.sparta.nexusteam.vacation.repository.VacationTypeRepository;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//@DataJpaTest
//@Import({QueryDslConfig.class,})
//public class RepositoryTest {
//
//    @Autowired
//    private VacationRepository vacationRepository;
//
//    @Autowired
//    private VacationTypeRepository vacationTypeRepository;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Nested
//    @DisplayName("휴가 리포지토리 테스트")
//    class vacation {
//
//        @Test
//        @Transactional
//        public void save() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            vacationTypeRepository.save(vacationType);
//            Employee employee = new Employee(new SignupRequest("testId", "test1234!", "testUsername", "test@test.com",
//                            "01012345678", "testAddress", "testCompany"),"test1234!", Position.ASSISTANT_MANAGER, UserRole.USER,company);
//            employeeRepository.save(employee);
//            Vacation vacation = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
//                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
//            //when
//            Vacation savedVacation = vacationRepository.save(vacation);
//
//            //then
//            assertNotNull(savedVacation.getId());
//            assertEquals(LocalDateTime.of(2024, 07, 24, 9, 00), savedVacation.getStartDate());
//            assertEquals(LocalDateTime.of(2024, 07, 25, 9, 00), savedVacation.getEndDate());
//            assertEquals(savedVacation.getVacationType().getId(), vacationType.getId());
//            assertEquals(savedVacation.getEmployee().getId(), employee.getId());
//        }
//
//        @Test
//        @Transactional
//        public void findById() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            vacationTypeRepository.save(vacationType);
//            Employee employee = new Employee(new SignupRequest("testId", "test1234!", "testUsername", "test@test.com",
//                    "01012345678", "testAddress", "testCompany"),"test1234!", Position.ASSISTANT_MANAGER, UserRole.USER,company);
//            employeeRepository.save(employee);
//            Vacation vacation = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
//                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
//            Vacation savedVacation = vacationRepository.save(vacation);
//            Long vacationId = savedVacation.getId();
//            //when
//            Vacation findVacation = vacationRepository.findById(vacationId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 휴가는 없습니다."));
//            //then
//            assertEquals(vacationId, findVacation.getId());
//            assertEquals(vacation.getStartDate(), findVacation.getStartDate());
//            assertEquals(vacation.getEndDate(), findVacation.getEndDate());
//            assertEquals(vacationType.getId(), findVacation.getVacationType().getId());
//            assertEquals(employee.getId(), findVacation.getEmployee().getId());
//        }
//
//        @Test
//        @Transactional
//        public void findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            vacationTypeRepository.save(vacationType);
//            Employee employee = new Employee(new SignupRequest("testId", "test1234!", "testUsername", "test@test.com",
//                    "01012345678", "testAddress", "testCompany"),"test1234!", Position.ASSISTANT_MANAGER, UserRole.USER,company);
//            employeeRepository.save(employee);
//            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 22, 9, 00),
//                    LocalDateTime.of(2024, 07, 23, 9, 00), vacationType, employee);
//            Vacation vacation2 = new Vacation(LocalDateTime.of(2024, 07, 23, 9, 00),
//                    LocalDateTime.of(2024, 07, 24, 9, 00), vacationType, employee);
//            Vacation vacation3 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
//                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
//            Vacation vacation4 = new Vacation(LocalDateTime.of(2024, 07, 25, 9, 00),
//                    LocalDateTime.of(2024, 07, 26, 9, 00), vacationType, employee);
//            vacationRepository.save(vacation1);
//            vacationRepository.save(vacation2);
//            vacationRepository.save(vacation3);
//            vacationRepository.save(vacation4);
//
//            //when
//            List<Vacation> vacationList = vacationRepository.findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc(
//                    LocalDateTime.of(2024, 07, 25, 10, 00), employee.getId());
//            //then
//            assertEquals(vacationList.size(), 3);
//            assertEquals(vacationList.get(0).getId(), vacation3.getId());
//            assertEquals(vacationList.get(1).getId(), vacation2.getId());
//            assertEquals(vacationList.get(2).getId(), vacation1.getId());
//        }
//
//        @Test
//        @Transactional
//        public void findByEndDateAfterAndEmployeeIdOrderByStartDateAsc() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            vacationTypeRepository.save(vacationType);
//            Employee employee = new Employee(new SignupRequest("testId", "test1234!", "testUsername", "test@test.com",
//                    "01012345678", "testAddress", "testCompany"),"test1234!", Position.ASSISTANT_MANAGER, UserRole.USER,company);
//            employeeRepository.save(employee);
//            //vacation 생성
//            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
//                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
//            Vacation vacation2 = new Vacation(LocalDateTime.of(2024, 07, 25, 9, 00),
//                    LocalDateTime.of(2024, 07, 26, 9, 00), vacationType, employee);
//            Vacation vacation3 = new Vacation(LocalDateTime.of(2024, 07, 26, 9, 00),
//                    LocalDateTime.of(2024, 07, 27, 9, 00), vacationType, employee);
//            Vacation vacation4 = new Vacation(LocalDateTime.of(2024, 07, 27, 9, 00),
//                    LocalDateTime.of(2024, 07, 28, 9, 00), vacationType, employee);
//            vacationRepository.save(vacation1);
//            vacationRepository.save(vacation2);
//            vacationRepository.save(vacation3);
//            vacationRepository.save(vacation4);
//
//            //when
//            List<Vacation> vacationList = vacationRepository.findByEndDateAfterAndEmployeeIdOrderByStartDateAsc(
//                    LocalDateTime.of(2024, 07, 25, 10, 00), employee.getId());
//            //then
//            assertTrue(vacationList.size() == 3);
//            assertEquals(vacationList.get(0).getId(), vacation2.getId());
//            assertEquals(vacationList.get(1).getId(), vacation3.getId());
//            assertEquals(vacationList.get(2).getId(), vacation4.getId());
//        }
//
//        @Test
//        @Transactional
//        public void findByApprovalStatusOrderByStartDateAsc() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            vacationTypeRepository.save(vacationType);
//            Employee employee = new Employee(new SignupRequest("testId", "test1234!", "testUsername", "test@test.com",
//                    "01012345678", "testAddress", "testCompany"),"test1234!", Position.ASSISTANT_MANAGER, UserRole.USER,company);
//            employeeRepository.save(employee);
//            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
//                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
//            Vacation vacation2 = new Vacation(LocalDateTime.of(2024, 07, 25, 9, 00),
//                    LocalDateTime.of(2024, 07, 26, 9, 00), vacationType, employee);
//            Vacation vacation3 = new Vacation(LocalDateTime.of(2024, 07, 26, 9, 00),
//                    LocalDateTime.of(2024, 07, 27, 9, 00), vacationType, employee);
//            vacation3.updateApprovalStatus(ApprovalStatus.APPROVED);
//            vacationRepository.save(vacation1);
//            vacationRepository.save(vacation2);
//            vacationRepository.save(vacation3);
//            //when
//            List<Vacation> vacationList = vacationRepository.findByCompanyIdAndApprovalStatus(company.getId(),ApprovalStatus.PENDING);
//
//            //then
//            assertTrue(vacationList.size() == 2);
//            assertEquals(vacationList.get(0).getId(), vacation1.getId());
//            assertEquals(vacationList.get(1).getId(), vacation2.getId());
//        }
//
//        @Test
//        @Transactional
//        public void delete() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            vacationTypeRepository.save(vacationType);
//            Employee employee = new Employee(new SignupRequest("testId", "test1234!", "testUsername", "test@test.com",
//                    "01012345678", "testAddress", "testCompany"),"test1234!", Position.ASSISTANT_MANAGER, UserRole.USER,company);
//            employeeRepository.save(employee);
//            Vacation vacation1 = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
//                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
//            Vacation savedVacation1 = vacationRepository.save(vacation1);
//
//            //when
//            vacationRepository.delete(savedVacation1);
//
//            //then
//            Optional<Vacation> deletedVacation = vacationRepository.findById(
//                    savedVacation1.getId());
//            assertTrue(deletedVacation.isEmpty());
//        }
//    }
//
//    @Nested
//    @DisplayName("휴가 종류 리포지토리 테스트")
//    class vacationType {
//
//        @Test
//        @Transactional
//        public void save() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType = new VacationType("연차", 1, company);
//            //when
//            vacationTypeRepository.save(vacationType);
//            //then
//            assertNotNull(vacationType.getId());
//            assertEquals(vacationType.getName(), vacationType.getName());
//            assertEquals(vacationType.getDays(), vacationType.getDays());
//        }
//
//        @Test
//        @Transactional
//        public void findAll() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType1 = new VacationType("연차", 1,company);
//            VacationType vacationType2 = new VacationType("연차", 1,company);
//            VacationType vacationType3 = new VacationType("연차", 1,company);
//            vacationTypeRepository.save(vacationType1);
//            vacationTypeRepository.save(vacationType2);
//            vacationTypeRepository.save(vacationType3);
//
//            //when
//            List<VacationType> vacationTypes = vacationTypeRepository.findByCompanyId(company.getId());
//
//            //then
//            assertEquals(3, vacationTypes.size());
//            for (VacationType vacationType : vacationTypes) {
//                assertNotNull(vacationType.getId());
//                assertNotNull(vacationType.getName());
//                assertNotNull(vacationType.getDays());
//            }
//        }
//
//        @Test
//        @Transactional
//        public void findById() {
//            //given
//            Company company = new Company("TestCompany");
//            companyRepository.save(company);
//            VacationType vacationType1 = new VacationType("연차", 1,company);
//            vacationTypeRepository.save(vacationType1);
//            //when
//            VacationType findVacationType1 = vacationTypeRepository.findById(
//                            vacationType1.getId())
//                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 휴가종류를 찾을 수 없습니다."));
//            //then
//            assertNotNull(findVacationType1);
//            assertEquals(vacationType1.getId(), findVacationType1.getId());
//            assertEquals(vacationType1.getName(), findVacationType1.getName());
//            assertEquals(vacationType1.getDays(), findVacationType1.getDays());
//        }
//    }
//
//
//}
