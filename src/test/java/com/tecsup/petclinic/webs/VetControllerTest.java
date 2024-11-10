package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.domain.SpecialtyTO;
import com.tecsup.petclinic.domain.VetTO;
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
public class VetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllVets() throws Exception {

        //int NRO_RECORD = 73;
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/vets"))
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
    public void testFindVetOK() throws Exception {

        String FIRST_NAME = "Sebastián";

        mockMvc.perform(get("/vets/1"))  // Object must be BASIL
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateVet() throws Exception {

        String FIRST_NAME = "Abel";
        String LAST_NAME = "Gianella";

        VetTO newVetTO = new VetTO();
        newVetTO.setFirstName(FIRST_NAME);
        newVetTO.setLastName(LAST_NAME);

        mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)));

    }


    /**
     *
     * @throws Exception
     */
    @Test
    public void testDeleteVet() throws Exception {

        String FIRST_NAME = "Abel";
        String LAST_NAME = "Gianella";

        VetTO newVetTO = new VetTO();
        newVetTO.setFirstName(FIRST_NAME);
        newVetTO.setLastName(LAST_NAME);

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/vets/" + id ))
                .andExpect(status().isOk());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testUpdateVet() throws Exception {

        String FIRST_NAME = "Abel";
        String LAST_NAME = "Gianella";

        String UP_FIRST_NAME = "Sebastián";
        String UP_LAST_NAME = "Nose:v";

        VetTO newVetTO = new VetTO();
        newVetTO.setFirstName(FIRST_NAME);
        newVetTO.setLastName(LAST_NAME);

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        VetTO upVetTO = new VetTO();
        upVetTO.setId(id);
        upVetTO.setFirstName(UP_FIRST_NAME);
        upVetTO.setLastName(UP_LAST_NAME);

        mockMvc.perform(put("/vets/"+id)
                        .content(om.writeValueAsString(upVetTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/vets/" + id))  //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)));

        // DELETE
        mockMvc.perform(delete("/vets/" + id))
                /*.andDo(print())*/
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVetById() throws Exception {
        int id = 6;
        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateVetById() throws Exception {

        int id = 1;

        String UP_FIRST_NAME = "Sebastián";
        String UP_LAST_NAME = "Nose:v";

        VetTO upVetTO = new VetTO();
        upVetTO.setId(id);
        upVetTO.setFirstName(UP_FIRST_NAME);
        upVetTO.setLastName(UP_LAST_NAME);

        mockMvc.perform(put("/vets/"+id)
                        .content(om.writeValueAsString(upVetTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/vets/" + id))  //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)));

    }
}
