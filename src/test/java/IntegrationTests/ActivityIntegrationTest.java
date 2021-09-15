// IMPORTANT These tests do not work!
//
//
// package IntegrationTests;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.intelligenta.communityportal.CommunityPortalApplication;
//import io.intelligenta.communityportal.models.Activity;
//import io.intelligenta.communityportal.models.NAP;
//import io.intelligenta.communityportal.models.Status;
//import io.intelligenta.communityportal.models.StatusType;
//import io.intelligenta.communityportal.models.dto.ActivityDto;
//import io.intelligenta.communityportal.models.dto.MeasureDto;
//import io.intelligenta.communityportal.models.dto.NAPDto;
//import io.intelligenta.communityportal.models.dto.ProblemDto;
//import io.intelligenta.communityportal.service.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {CommunityPortalApplication.class})
//public class ActivityIntegrationTest {
//    MockMvc mockMvc;
//
//    @Autowired
//    ActivityService activityService;
//
//    @Autowired
//    MeasureService measureService;
//
//    @Autowired
//    ProblemService problemService;
//
//    @Autowired
//    NAPService napService;
//
//    @Autowired
//    StatusService statusService;
//
//    private static Activity activity;
//    private static Boolean dataInitialized = false;
//
//    @BeforeEach
//    public void setup(WebApplicationContext context) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        initData();
//    }
//    todo fix initData()
//    private void initData() {
//        if (!dataInitialized) {
//            ActivityDto activityDto = new ActivityDto();
//            activityDto.setNameMk("Активност мк");
//            activityDto.setNameAl("");
//            activityDto.setNameEn("Activity en");
//            activityDto.setActive(true);
//            activityDto.setContinuously(true);
//            activityDto.setMeasure(1L);
//            activityDto.setStatus(1L);
//
//            MeasureDto measureDto = new MeasureDto();
//            measureDto.setNameMk("Measure mk");
//            measureDto.setId(1L);
//            measureDto.setProblem(1L);
//
//            ProblemDto problemDto = new ProblemDto();
//            problemDto.setNameMk("Problem mk");
//            problemDto.setId(1L);
//            problemDto.setStrategyGoals(new ArrayList<>());
//
//            NAPDto napDto = new NAPDto();
//            napDto.setNameMk("NAP mk");
//            NAP nap = napService.createNAP(napDto);
//
//
//            Status status = new Status();
//            status.setStatusMk("Во тек");
//            status.setIsEvaluable(false);
//            status.setStatusType(StatusType.НАП);
//            status.setId(4L);
//
//            statusService.createStatus(status);
//
//
//            problemDto.setNap(nap.getId());
//
//            problemService.createProblem(problemDto);
//
//            measureService.createMeasure(measureDto);
//
//            activity = activityService.createActivity(activityDto);
//
//            dataInitialized = true;
//        }
//    }
//
//    @Test
//    public void testGetActivityFindAll() throws Exception {
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/activity/all");
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andReturn();
//    }
//
//    @Test
//    public void testGetActivityFindAllList() throws Exception {
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/activity/all-list");
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andReturn();
//    }
//
//    @Test
//    public void testGetActivityFindById() throws Exception {
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/activity/" + activity.getId());
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andReturn();
//    }
//
//    @Test
//    public void testPostActivityCreate() throws Exception {
//        ActivityDto activityDto = new ActivityDto();
//        activityDto.setNameMk("Активност мк created");
//        activityDto.setNameAl("");
//        activityDto.setNameEn("Activity en created");
//        activityDto.setActive(true);
//        activityDto.setContinuously(true);
//        activityDto.setMeasure(1L);
//        activityDto.setStatus(1L);
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rest/activity/create")
//                .content(asJsonString(activityDto))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andReturn();
//    }
//
//    @Test
//    public void testPostActivityUpdate() throws Exception {
//        activity.setNameMk("Activity mk update");
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rest/activity/update/" + activity.getId())
//                .content(asJsonString(activity))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andReturn();
//    }
//
//    @Test
//    public void testPostActivityDelete() throws Exception {
//        MockHttpServletRequestBuilder request =
//                MockMvcRequestBuilders.post("/rest/activity/delete/" + activity.getId());
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//    }
//
//    @Test
//    public void testPostActivityUndelete() throws Exception {
//        MockHttpServletRequestBuilder request =
//                MockMvcRequestBuilders.post("/rest/activity/undelete/" + activity.getId());
//
//        MvcResult result = this.mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//    }
//
//    private static String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper mapper = new ObjectMapper();
//            final String jsonContent = mapper.writeValueAsString(obj);
//            return jsonContent;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
