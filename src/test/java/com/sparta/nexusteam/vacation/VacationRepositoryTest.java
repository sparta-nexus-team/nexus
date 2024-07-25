package com.sparta.nexusteam.vacation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.vacation.entity.Vacation;
import com.sparta.nexusteam.vacation.entity.VacationType;
import com.sparta.nexusteam.vacation.repository.VacationRepository;
import com.sparta.nexusteam.vacation.repository.VacationTypeRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VacationRepositoryTest {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private VacationTypeRepository vacationTypeRepository;

    @Nested
    @DisplayName("휴가 리포지토리 테스트")
    class vacation {

        @Test
        public void save() {
            //given
            VacationType vacationType = new VacationType("연차", 1);
            Employee employee = new Employee();
            Vacation vacation = new Vacation(LocalDateTime.of(2024, 07, 24, 9, 00),
                    LocalDateTime.of(2024, 07, 25, 9, 00), vacationType, employee);
            //when
            Vacation savedVacation = vacationRepository.save(vacation);
            //then
            assertEquals(vacation,savedVacation);
        }

        @Test
        public void findById() {
            //given
            Long vacationId = 1L;
            //when
            Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(()->new IllegalArgumentException("해당 휴가는 없습니다."));
            //then
            assertEquals(vacationId,vacation.getId());
        }

        @Test
        public void findByEndDateBeforeAndEmployeeIdOrderByStartDateDesc() {
            //given
            
            //when

            //then

        }

        @Test
        public void findByEndDateAfterAndEmployeeIdOrderByStartDateDesc() {
            //given
            //when
            //then
        }

        @Test
        public void findByApprovalStatusOrderByStartDateAsc() {
            //given
            //when
            //then
        }

        @Test
        public void delete() {
            //given
            //when
            //then
        }

        @Test
        public void updateVacation() {
            //given
            //when
            //then
        }
    }

    @Nested
    @DisplayName("휴가 종류 리포지토리 테스트")
    class vacationType {
        @Test
        public void save() {
            //given
            //when
            //then
        }

        @Test
        public void findAll() {
            //given
            //when
            //then
        }

        @Test
        public void findById() {
            //given
            //when
            //then
        }
    }


}
