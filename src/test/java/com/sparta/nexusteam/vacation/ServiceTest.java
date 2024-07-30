package com.sparta.nexusteam.vacation;

import com.sparta.nexusteam.vacation.repository.VacationRepository;
import com.sparta.nexusteam.vacation.repository.VacationTypeRepository;
import com.sparta.nexusteam.vacation.service.VacationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("vacationService 단위 테스트")
public class ServiceTest {

    @Mock
    VacationRepository vacationRepository;

    @Mock
    VacationTypeRepository vacationTypeRepository;

    @Test
    void createVacationType() {
        //given

        //then

        //when

    }

    @Test
    void createVacation() {
        //given

        //then

        //when

    }

    @Test
    void getVacationsBeforeUse() {
        //given

        //then

        //when

    }

    @Test
    void getVacationsAfterUse() {
        //given

        //then

        //when

    }

    @Test
    void getVacation() {
        //given

        //then

        //when

    }

    @Test
    void getPendingVacations() {
        //given

        //then

        //when

    }

    @Test
    void getVacationTypes() {
        //given

        //then

        //when

    }

    @Test
    void updateVacationApprovalStatus() {
        //given

        //then

        //when

    }

    @Test
    void deleteVacation() {
        //given

        //then

        //when

    }

}
