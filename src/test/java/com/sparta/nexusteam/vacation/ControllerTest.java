package com.sparta.nexusteam.vacation;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nexusteam.vacation.controller.VacationController;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.service.VacationServiceImpl;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = VacationController.class)
@DisplayName("VacationController 단위 테스트")
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacationServiceImpl vacationServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void createVacationType() throws Exception {
        //given
        PostVacationTypeRequest requestDto = new PostVacationTypeRequest("연차",1);
        VacationTypeResponse responseDto = new VacationTypeResponse(1L,"연차",1);

        given(vacationServiceImpl.createVacationType(requestDto)).willReturn(responseDto);

        String JsonString = objectMapper.writeValueAsString(requestDto);

        //when-then
        mockMvc.perform(post("/vacation-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonString))
                .andExpect(status().isOk());


    }

    @Test
    void createVacation() {

    }

    @Test
    void getVacationsBeforeUse() {

    }

    @Test
    void getVacationsAfterUse() {

    }

    @Test
    void getVacation() {

    }

    @Test
    void getPendingVacations() {

    }

    @Test
    void getVacationTypes() {

    }

    @Test
    void updateVacationApprovalStatus() {

    }

    @Test
    void deleteVacation() {

    }
}
