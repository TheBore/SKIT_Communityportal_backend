package IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {CommunityPortalApplication.class})
public class NAPAreaIntegrationTest {
    MockMvc mockMvc;

    @Autowired
    NAPAreaService napAreaService;

    @Autowired
    NAPAreaTypeService napAreaTypeService;

    private static NAPArea napArea;
    private static Boolean dataInitialized = false;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        initData();
    }

    private void initData() {
        if (!dataInitialized) {
            NapAreaDto napAreaDto = new NapAreaDto();
            napAreaDto.setId(1L);
            napAreaDto.setNameMk("НАП област мк");
            napAreaDto.setNameAl("");
            napAreaDto.setNameEn("");
            napAreaDto.setDescriptionAl("");
            napAreaDto.setDescriptionEn("");
            napAreaDto.setDescriptionMk("");
            napAreaDto.setCode("Code");
            napAreaDto.setNapAreaTypeId(3L);

            NAPAreaType napAreaType = new NAPAreaType();
            napAreaType.setId(3L);

            napAreaTypeService.createNAPAreaType(napAreaType);

            napAreaDto.setNapAreaTypeId(napAreaType.getId());

            napArea = napAreaService.createNAPArea(napAreaDto);

            dataInitialized = true;
        }
    }

    @Test
    @Order(0)
    public void contextLoads() {

    }

    @Test
    @Order(1)
    public void testPostNapAreaCreate() throws Exception {
        NapAreaDto napAreaDto = new NapAreaDto();
        napAreaDto.setId(1L);
        napAreaDto.setNameMk("НАП област мк");
        napAreaDto.setNameAl("");
        napAreaDto.setNameEn("");
        napAreaDto.setDescriptionAl("");
        napAreaDto.setDescriptionEn("");
        napAreaDto.setDescriptionMk("");
        napAreaDto.setCode("Code");
        napAreaDto.setNapAreaTypeId(3L);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rest/nap-area/create")
                .content(asJsonString(napAreaDto))
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
    public void testGetNapAreaFindById() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap-area/" + napArea.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(3)
    public void testGetNapAreaFindAllActive() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rest/nap-area/allActive");

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(4)
    public void testPostNapAreaDelete() throws Exception {


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rest/nap-area/delete/" + napArea.getId());

        MvcResult result = this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @Order(5)
    public void testPostNapAreaUndelete() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rest/nap-area/undelete/" + napArea.getId());

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
