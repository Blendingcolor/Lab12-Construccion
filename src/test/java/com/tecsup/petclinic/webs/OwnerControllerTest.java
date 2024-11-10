package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.domain.OwnerTO;
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
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllOwners() throws Exception {

        //int NRO_RECORD = 73;
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/owners"))
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
    public void testFindOwnerOK() throws Exception {

        String FIRST_NAME = "Carlos";
        String LAST_NAME = "Basil";
        String ADDRESS = "Calle 1";
        String CITY = "Santo Domingo";
        String TELEPHONE = "123456789";

        mockMvc.perform(get("/owners/1"))  // Object must be BASIL
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
    public void testCreateOwner() throws Exception {

        String FIRST_NAME = "Leo";
        String LAST_NAME = "Basil";
        String ADDRESS = "Calle 1";
        String CITY = "Santo Domingo";
        String TELEPHONE = "123456789";

        OwnerTO newOwnerTO = new OwnerTO();
        newOwnerTO.setFirstName(FIRST_NAME);
        newOwnerTO.setLastName(LAST_NAME);
        newOwnerTO.setAddress(ADDRESS);
        newOwnerTO.setCity(CITY);
        newOwnerTO.setTelephone(TELEPHONE);

        mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.address", is(ADDRESS)))
                .andExpect(jsonPath("$.city", is(CITY)))
                .andExpect(jsonPath("$.telephone", is(TELEPHONE)));

    }


    /**
     *
     * @throws Exception
     */
    @Test
    public void testDeleteOwner() throws Exception {

        String FIRST_NAME = "Leo";
        String LAST_NAME = "Basil";
        String ADDRESS = "Calle 1";
        String CITY = "Santo Domingo";
        String TELEPHONE = "123456789";

        OwnerTO newOwnerTO = new OwnerTO();
        newOwnerTO.setFirstName(FIRST_NAME);
        newOwnerTO.setLastName(LAST_NAME);
        newOwnerTO.setAddress(ADDRESS);
        newOwnerTO.setCity(CITY);
        newOwnerTO.setTelephone(TELEPHONE);

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/owners/" + id ))
                .andExpect(status().isOk());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testUpdateOwner() throws Exception {

        String FIRST_NAME = "Leo";
        String LAST_NAME = "Basil";
        String ADDRESS = "Calle 1";
        String CITY = "Santo Domingo";
        String TELEPHONE = "123456789";

        String UP_FIRST_NAME = "Carlos";
        String UP_LAST_NAME = "Jose";
        String UP_ADDRESS = "Calle 2";
        String UP_CITY = "San Isidro";
        String UP_TELEPHONE = "987654321";

        OwnerTO newOwnerTO = new OwnerTO();
        newOwnerTO.setFirstName(FIRST_NAME);
        newOwnerTO.setLastName(LAST_NAME);
        newOwnerTO.setAddress(ADDRESS);
        newOwnerTO.setCity(CITY);
        newOwnerTO.setTelephone(TELEPHONE);

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        OwnerTO upOwnerTO = new OwnerTO();
        upOwnerTO.setId(id);
        upOwnerTO.setFirstName(UP_FIRST_NAME);
        upOwnerTO.setLastName(UP_LAST_NAME);
        upOwnerTO.setAddress(UP_ADDRESS);
        upOwnerTO.setCity(UP_CITY);
        upOwnerTO.setTelephone(UP_TELEPHONE);

        mockMvc.perform(put("/owners/"+id)
                        .content(om.writeValueAsString(upOwnerTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/owners/" + id))  //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
                .andExpect(jsonPath("$.address", is(UP_ADDRESS)))
                .andExpect(jsonPath("$.city", is(UP_CITY)))
                .andExpect(jsonPath("$.telephone", is(UP_TELEPHONE)));

        // DELETE
        mockMvc.perform(delete("/owners/" + id))
                /*.andDo(print())*/
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwnerById() throws Exception {
        int id = 10;
        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSpecialtyById() throws Exception {

        int id =1;

        String UP_FIRST_NAME = "Carlos";
        String UP_LAST_NAME = "Jose";
        String UP_ADDRESS = "Calle 2";
        String UP_CITY = "San Isidro";
        String UP_TELEPHONE = "987654321";

        OwnerTO upOwnerTO = new OwnerTO();
        upOwnerTO.setId(id);
        upOwnerTO.setFirstName(UP_FIRST_NAME);
        upOwnerTO.setLastName(UP_LAST_NAME);
        upOwnerTO.setAddress(UP_ADDRESS);
        upOwnerTO.setCity(UP_CITY);
        upOwnerTO.setTelephone(UP_TELEPHONE);

        mockMvc.perform(put("/owners/"+id)
                        .content(om.writeValueAsString(upOwnerTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/owners/" + id))  //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
                .andExpect(jsonPath("$.address", is(UP_ADDRESS)))
                .andExpect(jsonPath("$.city", is(UP_CITY)))
                .andExpect(jsonPath("$.telephone", is(UP_TELEPHONE)));

    }
}
