package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.domain.SpecialtyTO;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class SpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllSpecialties() throws Exception {

        //int NRO_RECORD = 73;
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                //		    .andExpect(jsonPath("$", hasSize(NRO_RECORD)))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }


    /**
     *
     * @throws Exception
     *
     */
    @Test
    public void testFindSpecialtyOK() throws Exception {

        String NAME = "Immunology";

        mockMvc.perform(get("/specialties/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateSpecialty() throws Exception {
        String NAME = "Biology";
        String OFFICE = "Santa Anita";
        int OPEN = 7;
        int CLOSE = 19;

        SpecialtyTO newSpecialtyTO = new SpecialtyTO();
        newSpecialtyTO.setName(NAME);
        newSpecialtyTO.setOffice(OFFICE);
        newSpecialtyTO.setHOpen(OPEN);
        newSpecialtyTO.setHClose(CLOSE);

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.office", is(OFFICE)))
                .andExpect(jsonPath("$.hopen", is(OPEN)))
                .andExpect(jsonPath("$.hclose", is(CLOSE)));
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDeleteSpecialty() throws Exception {

        String NAME = "Biology";
        String OFFICE = "Santa Anita";
        int HOPEN = 7;
        int HCLOSE = 19;

        SpecialtyTO newSpecialtyTO = new SpecialtyTO();
        newSpecialtyTO.setName(NAME);
        newSpecialtyTO.setOffice(OFFICE);
        newSpecialtyTO.setHOpen(HOPEN);
        newSpecialtyTO.setHClose(HCLOSE);

        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/specialties/" + id ))
                .andExpect(status().isOk());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testUpdateSpecialty() throws Exception {

        String NAME = "Biology";
        String OFFICE = "Santa Anita";
        int HOPEN = 7;
        int HCLOSE = 19;

        String UP_NAME = "Immunology";
        String UP_OFFICE = "San juan";
        int UP_HOPEN = 8;
        int UP_HCLOSE = 20;

        SpecialtyTO newSpecialtyTO = new SpecialtyTO();
        newSpecialtyTO.setName(NAME);
        newSpecialtyTO.setOffice(OFFICE);
        newSpecialtyTO.setHOpen(HOPEN);
        newSpecialtyTO.setHClose(HCLOSE);

        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        SpecialtyTO upSpecialtyTO = new SpecialtyTO();
        upSpecialtyTO.setId(id);
        upSpecialtyTO.setName(UP_NAME);
        upSpecialtyTO.setOffice(UP_OFFICE);
        upSpecialtyTO.setHOpen(UP_HOPEN);
        upSpecialtyTO.setHClose(UP_HCLOSE);

        mockMvc.perform(put("/specialties/"+id)
                        .content(om.writeValueAsString(upSpecialtyTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/specialties/" + id))  //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(UP_NAME)))
                .andExpect(jsonPath("$.office", is(UP_OFFICE)))
                .andExpect(jsonPath("$.hopen", is(UP_HOPEN)))
                .andExpect(jsonPath("$.hclose", is(UP_HCLOSE)));

        // DELETE
        mockMvc.perform(delete("/specialties/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteSpecialtyById() throws Exception {
        int id = 3;
        mockMvc.perform(delete("/specialties/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSpecialtyById() throws Exception {

        int id = 1;

        String UP_NAME = "Immunology";
        String UP_OFFICE = "San juan";
        int UP_HOPEN = 8;
        int UP_HCLOSE = 20;

        SpecialtyTO upSpecialtyTO = new SpecialtyTO();
        upSpecialtyTO.setId(id);
        upSpecialtyTO.setName(UP_NAME);
        upSpecialtyTO.setOffice(UP_OFFICE);
        upSpecialtyTO.setHOpen(UP_HOPEN);
        upSpecialtyTO.setHClose(UP_HCLOSE);

        mockMvc.perform(put("/specialties/"+id)
                        .content(om.writeValueAsString(upSpecialtyTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/specialties/" + id))  //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(UP_NAME)))
                .andExpect(jsonPath("$.office", is(UP_OFFICE)))
                .andExpect(jsonPath("$.hopen", is(UP_HOPEN)))
                .andExpect(jsonPath("$.hclose", is(UP_HCLOSE)));

    }
}
