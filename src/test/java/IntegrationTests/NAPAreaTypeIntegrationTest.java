package IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.intelligenta.communityportal.CommunityPortalApplication;
import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.NAPAreaType;
import io.intelligenta.communityportal.models.dto.NapAreaDto;
import io.intelligenta.communityportal.service.NAPAreaService;
import io.intelligenta.communityportal.service.NAPAreaTypeService;
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

import java.time.LocalDateTime;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {CommunityPortalApplication.class})
public class NAPAreaTypeIntegrationTest {
    MockMvc mockMvc;

    @Autowired
    NAPAreaTypeService napAreaTypeService;

    private static NAPAreaType napAreaType;
    private static Boolean dataInitialized = false;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        initData();
    }

    private void initData() {
        if (!dataInitialized) {
            napAreaType = new NAPAreaType();
            napAreaType.setActive(true);
            napAreaType.setDescriptionMk("Desc mk");
            napAreaType.setNameMk("Name mk");
            napAreaType.setId(3L);
            napAreaType.setDateCreated(LocalDateTime.now());
            
            napAreaTypeService.createNAPAreaType(napAreaType);

            dataInitialized = true;
        }
    }

    @Test
    @Order(0)
    public void contextLoads() {

    }

    @Test
    @Order(1)
    public void testPostNapAreaTypeCreate() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rest/nap-area-type/create")
                .content(asJsonString(napAreaType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(2)
    public void testGetNAPAreaTypeFindById() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/rest/nap-area-type/" + napAreaType.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(3)
    public void testGetNAPAreaTypeGetAllPaged() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap-area-type/all?keyword=");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    public void testGetNAPAreaTypeGetAllActive() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap-area-type/allActive");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(4)
    public void testPostNapAreaTypeUpdate() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rest/nap-area-type/update")
                .content(asJsonString(napAreaType))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(5)
    public void testPostNapAreaTypeDelete() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/rest/nap-area-type/delete/" + napAreaType.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(6)
    public void testPostNapAreaTypeUndelete() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/rest/nap-area-type/undelete/" + napAreaType.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
