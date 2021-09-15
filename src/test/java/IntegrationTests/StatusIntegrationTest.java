package IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.intelligenta.communityportal.CommunityPortalApplication;
import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.StatusType;
import io.intelligenta.communityportal.service.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes={CommunityPortalApplication.class})
public class StatusIntegrationTest {
    MockMvc mockMvc;

    @Autowired
    StatusService statusService;

    private static Status status;
    private static Boolean dataInitialized = false;

    @BeforeEach
    public void setup(WebApplicationContext context){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        initData();
    }

    private void initData(){
        if(!dataInitialized) {
            StatusType statusType = StatusType.НАП;
            status = new Status();
            status.setStatusMk("Во тек");
            status.setIsEvaluable(false);
            status.setStatusType(statusType);

            statusService.createStatus(status);

            dataInitialized = true;
        }
    }

    @Test
    @Order(0)
    public void testPostStatusCreate() throws Exception {
        StatusType statusType = StatusType.НАП;
        Status newStatus = new Status();
        newStatus.setStatusMk("Во тек");
        newStatus.setIsEvaluable(false);
        newStatus.setStatusType(statusType);
        newStatus.setId(4L);

        status = newStatus;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rest/status/create")
                .content(asJsonString(newStatus))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(1)
    public void testGetStatusFindById() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/status/" + status.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(2)
    public void testGetStatusFindAllWithKeyword() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/status/all?keyword=");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(3)
    public void testPostStatusUpdate() throws Exception {
        status.setStatusMk("New status");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rest/status/update")
                .content(asJsonString(status))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(4)
    public void testDeleteStatusDelete() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete("/rest/status/delete/" + status.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
