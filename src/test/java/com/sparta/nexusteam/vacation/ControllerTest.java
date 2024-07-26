package com.sparta.nexusteam.vacation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nexusteam.config.WebSecurityConfig;
import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.Position;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.vacation.controller.VacationController;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.entity.Vacation;
import com.sparta.nexusteam.vacation.entity.VacationType;
import com.sparta.nexusteam.vacation.service.VacationServiceImpl;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = VacationController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        })
@DisplayName("VacationController 단위 테스트")
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @MockBean
    private VacationServiceImpl vacationServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        Department department = new Department();
        department.setId(1L);
        department.setName("departmentTest");
        Employee employee = new Employee(1L, "testAccountId", "test1234!", "testUsername",
                "test@test.com", "01012345678", "testAddress",
                Position.ASSISTANT_MANAGER, UserRole.USER, "test", department);
        department.setEmployees(List.of(employee));

        UserDetailsImpl mockUser = new UserDetailsImpl(employee);

        mockPrincipal = new UsernamePasswordAuthenticationToken(mockUser,
                null, mockUser.getAuthorities());
    }


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }


    @Test
    public void createVacationType() throws Exception {
        //given
        PostVacationTypeRequest requestDto = new PostVacationTypeRequest("연차", 1);
        VacationType vacationType = new VacationType(1L, "연차", 1);

        given(vacationServiceImpl.createVacationType(any(PostVacationTypeRequest.class))).willReturn(
                new VacationTypeResponse(vacationType));

        //when
        ResultActions resultActions = mockMvc.perform(post("/vacation-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 종류 등록 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("연차"))
                .andExpect(jsonPath("$.data.days").value(1));


    }

    @Test
    void createVacation() throws Exception {
        //given
        mockUserSetup();
        Long vacationTypeId = 1L;
        PostVacationRequest requestDto = new PostVacationRequest();
        requestDto.setStartDate(LocalDateTime.of(2024, 07, 25, 9, 0));
        requestDto.setEndDate(LocalDateTime.of(2024, 07, 26, 9, 0));
        VacationType vacationType = new VacationType(vacationTypeId,"연차",1);
        Employee employee =((UserDetailsImpl) ((UsernamePasswordAuthenticationToken) mockPrincipal).getPrincipal()).getEmployee();
        Vacation vacation = new Vacation(requestDto.getStartDate(),requestDto.getEndDate(),vacationType,employee);
        vacation.setId(1L);

        given(vacationServiceImpl.createVacation(any(Long.class), any(PostVacationRequest.class), any(Employee.class))).willReturn(new VacationResponse(vacation));

        //when
        ResultActions resultActions = mockMvc.perform(post("/vacation-type/" + vacationTypeId + "/vacation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(mockPrincipal));

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 등록 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.vacationTypeName").value("연차"))
                .andExpect(jsonPath("$.data.vacationTypeDays").value(1))
                .andExpect(jsonPath("$.data.employeeUserName").value("testUsername"))
                .andExpect(jsonPath("$.data.startDate").value("2024-07-25 09:00"))
                .andExpect(jsonPath("$.data.endDate").value("2024-07-26 09:00"))
                .andExpect(jsonPath("$.data.approvalStatus").value("PENDING"));
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
