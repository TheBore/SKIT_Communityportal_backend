package IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.intelligenta.communityportal.CommunityPortalApplication;
import io.intelligenta.communityportal.models.NAP;
import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.StatusType;
import io.intelligenta.communityportal.models.dto.NAPDto;
import io.intelligenta.communityportal.service.NAPService;
import io.intelligenta.communityportal.service.StatusService;
import org.junit.jupiter.api.*;
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
public class NAPIntegrationTest {
    MockMvc mockMvc;

    @Autowired
    NAPService napService;
    @Autowired
    StatusService statusService;

    private static NAP nap;
    private static NAP napToBeDeleted;
    private static Boolean dataInitialized = false;

    @BeforeEach
    public void setup(WebApplicationContext context){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        initData();
    }

    private void initData(){
        if(!dataInitialized) {
            StatusType statusType = StatusType.НАП;
            Status status = new Status();
            status.setStatusMk("Во тек");
            status.setIsEvaluable(false);
            status.setStatusType(statusType);
            status.setId(4L);

            statusService.createStatus(status);

            NAPDto napDto = new NAPDto();
            napDto.setNameMk("НАП мк");
            napDto.setDescriptionMk("НАП опис");
            napDto.setStatus(status.getId());
            napDto.setStartDate("11/28/2021");
            napDto.setEndDate("11/28/2024");

            nap = napService.createNAP(napDto);

            NAPDto napToBeDeletedDto = new NAPDto();
            napToBeDeletedDto.setNameMk("НАП мк");
            napToBeDeletedDto.setDescriptionMk("НАП опис");
            napToBeDeletedDto.setStatus(status.getId());
            napToBeDeletedDto.setStartDate("11/28/2021");
            napToBeDeletedDto.setEndDate("11/28/2024");


            napToBeDeleted = napService.createNAP(napToBeDeletedDto);
            napService.setActive(napToBeDeleted.getId());

            dataInitialized = true;
        }
    }

    @Test
    @Order(0)
    public void contextLoads(){

    }


    @Test
    @Order(1)
    public void testGetNapFindAll() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap/all");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(2)
    public void testGetNapFindAllRecent() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap/all-recent");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(3)
    public void testGetNapFindAllList() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap/all-list");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(4)
    public void testGetNapFindById() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap/" + nap.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(5)
    public void testPostNapCreate() throws Exception {
        NAPDto napDto = new NAPDto();
        napDto.setNameMk("НАП мк");
        napDto.setDescriptionMk("НАП опис");
        napDto.setStatus(nap.getStatus().getId());
        napDto.setStartDate("11/28/2021");
        napDto.setEndDate("11/28/2024");



        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rest/nap/create")
                .content(asJsonString(napDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(6)
    public void testPostNapUpdate() throws Exception {
        NAPDto napDto = new NAPDto();
        napDto.setNameMk("НАП мк");
        napDto.setDescriptionMk("НАП опис");
        napDto.setStatus(nap.getStatus().getId());
        napDto.setStartDate("11/28/2021");
        napDto.setEndDate("11/28/2024");



        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rest/nap/update/" + nap.getId())
                .content(asJsonString(napDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(7)
    public void testPostNapDelete() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/rest/nap/delete/" + nap.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(8)
    public void testPostNapUnDelete() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/rest/nap/delete/" + nap.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
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
