package com.sparta.nexusteam.vacation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nexusteam.config.WebSecurityConfig;
import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.Position;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.vacation.controller.VacationController;
import com.sparta.nexusteam.vacation.dto.PatchVacationApprovalRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationRequest;
import com.sparta.nexusteam.vacation.dto.PostVacationTypeRequest;
import com.sparta.nexusteam.vacation.dto.VacationResponse;
import com.sparta.nexusteam.vacation.dto.VacationTypeResponse;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
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

@WebMvcTest(controllers = VacationController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)})
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
        Company company = new Company("testCompany");
        Employee employee = new Employee(1L, "testAccountId", "test1234!", "testUsername",
                "test@test.com", "01012345678", "testAddress", Position.ASSISTANT_MANAGER,
                UserRole.USER, "test", department, company);
        department.setEmployees(List.of(employee));
        UserDetailsImpl mockUser = new UserDetailsImpl(employee);
        mockPrincipal = new UsernamePasswordAuthenticationToken(mockUser, null,
                mockUser.getAuthorities());
    }


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter())).build();
    }


    @Test
    public void createVacationType() throws Exception {
        //given
        PostVacationTypeRequest requestDto = new PostVacationTypeRequest("연차", 1);
        Long companyId = 1L;
        given(vacationServiceImpl.createVacationType(any(PostVacationTypeRequest.class),
                any(Long.class))).willReturn(new VacationTypeResponse(1L, "연차", 1));
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/company/"+companyId+"/vacation-type").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        resultActions.andDo(print()).andExpect(status().isOk())
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
        PostVacationRequest requestDto = new PostVacationRequest(
                LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0));
        given(vacationServiceImpl.createVacation(any(Long.class), any(PostVacationRequest.class),
                any(Employee.class))).willReturn(new VacationResponse(1L, "연차", 1, "testUsername",
                LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0),
                ApprovalStatus.PENDING));
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/vacation-type/" + vacationTypeId + "/vacation").contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(mockPrincipal));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
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
    void getVacationsBeforeUse() throws Exception {
        //given
        mockUserSetup();
        given(vacationServiceImpl.getVacationsBeforeUse(any(Employee.class))).willReturn(
                List.of(new VacationResponse(1L, "연차", 1, "testUsername",
                        LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0),
                        ApprovalStatus.PENDING), new VacationResponse(2L, "연차", 1, "testUsername2",
                        LocalDateTime.of(2024, 07, 27, 9, 0), LocalDateTime.of(2024, 07, 28, 9, 0),
                        ApprovalStatus.PENDING), new VacationResponse(3L, "연차", 1, "testUsername3",
                        LocalDateTime.of(2024, 07, 29, 9, 0), LocalDateTime.of(2024, 07, 30, 9, 0),
                        ApprovalStatus.PENDING)));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/vacation/before").principal(mockPrincipal));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 사용전 리스트 조회 성공"));
    }

    @Test
    void getVacationsAfterUse() throws Exception {
        //given
        mockUserSetup();
        given(vacationServiceImpl.getVacationsAfterUse(any(Employee.class))).willReturn(
                List.of(new VacationResponse(1L, "연차", 1, "testUsername",
                        LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0),
                        ApprovalStatus.PENDING), new VacationResponse(2L, "연차", 1, "testUsername2",
                        LocalDateTime.of(2024, 07, 27, 9, 0), LocalDateTime.of(2024, 07, 28, 9, 0),
                        ApprovalStatus.PENDING), new VacationResponse(3L, "연차", 1, "testUsername3",
                        LocalDateTime.of(2024, 07, 29, 9, 0), LocalDateTime.of(2024, 07, 30, 9, 0),
                        ApprovalStatus.PENDING)));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/vacation/after").principal(mockPrincipal));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 사용후 리스트 조회 성공"));
    }

    @Test
    void getVacation() throws Exception {
        //given
        Long vacationId = 1L;
        given(vacationServiceImpl.getVacation(any(Long.class))).willReturn(
                new VacationResponse(1L, "연차", 1, "testUsername",
                        LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0),
                        ApprovalStatus.PENDING));
        //when
        ResultActions resultActions = mockMvc.perform(get("/vacation/" + vacationId));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("특정 휴가 조회 성공"));
    }

    @Test
    void getPendingVacations() throws Exception {
        //given
        Long companyId = 1L;
        given(vacationServiceImpl.getPendingVacations(any(Long.class))).willReturn(
                List.of(new VacationResponse(1L, "연차", 1, "testUsername",
                        LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0),
                        ApprovalStatus.PENDING), new VacationResponse(2L, "연차", 1, "testUsername2",
                        LocalDateTime.of(2024, 07, 27, 9, 0), LocalDateTime.of(2024, 07, 28, 9, 0),
                        ApprovalStatus.PENDING), new VacationResponse(3L, "연차", 1, "testUsername3",
                        LocalDateTime.of(2024, 07, 29, 9, 0), LocalDateTime.of(2024, 07, 30, 9, 0),
                        ApprovalStatus.PENDING)));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/company/" + companyId + "/vacation/approval"));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("승인전 휴가 리스트 조회 성공"));
    }

    @Test
    void getVacationTypes() throws Exception {
        //given
        Long companyId = 1L;
        given(vacationServiceImpl.getVacationTypes(any(Long.class))).willReturn(
                List.of(new VacationTypeResponse(1L, "연차", 1),
                        new VacationTypeResponse(2L, "연차", 1),
                        new VacationTypeResponse(3L, "연차", 1)));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/company/" + companyId + "/vacation-type"));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 종류 조회 성공"));
    }

    @Test
    void updateVacationApprovalStatus() throws Exception {
        //given
        Long vacationId = 1L;
        String JsonRequestBody = "{\"approvalStatus\":\"APPROVED\"}";
        given(vacationServiceImpl.updateVacationApprovalStatus(any(Long.class),
                any(PatchVacationApprovalRequest.class))).willReturn(
                new VacationResponse(1L, "연차", 1, "testUsername",
                        LocalDateTime.of(2024, 07, 25, 9, 0), LocalDateTime.of(2024, 07, 26, 9, 0),
                        ApprovalStatus.APPROVED));
        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/vacation/" + vacationId + "/approval").contentType(
                        MediaType.APPLICATION_JSON).content(JsonRequestBody));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 승인/거절 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.approvalStatus").value("APPROVED"));
    }

    @Test
    void deleteVacation() throws Exception {
        //given
        Long vacationId = 1L;
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/vacation/" + vacationId).contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("휴가 삭제 성공"));
    }
}
