package net.gb.knox.petopia.controller;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerIntegrationTest {

    @Autowired
    private MockMvc api;

    @Value("classpath:data/admin-create-pet-request.json")
    private Resource adminCreatePetRequestResource;

    @Value("classpath:data/admin-create-pet-response.json")
    private Resource adminCreatePetResponseResource;

    @Value("classpath:data/admin-get-all-pets-response.json")
    private Resource adminGetAllPetsResponseResource;

    @Value("classpath:data/admin-get-pet-response.json")
    private Resource adminGetPetResponseResource;

    @Value("classpath:data/admin-update-pet-request.json")
    private Resource adminUpdatePetRequestResource;

    @Value("classpath:data/admin-update-pet-response.json")
    private Resource adminUpdatePetResponseResource;

    @Value("classpath:data/get-all-unadopted-pets-response.json")
    private Resource getAllUnadoptedPetsResponseResource;

    @Value("classpath:data/get-unadopted-pet-response.json")
    private Resource getUnadoptedPetResponseResource;

    @Test
    @DirtiesContext
    @WithMockUser(roles = "admin")
    public void testCreatePet() throws Exception {
        var adminCreatePetRequestJson = adminCreatePetRequestResource.getContentAsString(Charset.defaultCharset());
        var adminCreatePetResponseJson = adminCreatePetResponseResource.getContentAsString(Charset.defaultCharset());

        var result = api
                .perform(
                        post("/admin/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(adminCreatePetRequestJson)
                )
                .andExpect(status().isCreated())
                .andReturn();

        JSONAssert.assertEquals(adminCreatePetResponseJson, result.getResponse().getContentAsString(), true);
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/pets.sql")
    @WithMockUser(roles = "admin")
    public void testGetAllPets() throws Exception {
        var adminGetAllPetsResponseJson = adminGetAllPetsResponseResource.getContentAsString(Charset.defaultCharset());

        var result = api
                .perform(get("/admin/pets"))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(adminGetAllPetsResponseJson, result.getResponse().getContentAsString(), true);
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/pets.sql")
    @WithMockUser(roles = "admin")
    public void testGetPetById() throws Exception {
        var adminGetPetResponseJson = adminGetPetResponseResource.getContentAsString(Charset.defaultCharset());

        var result = api
                .perform(get("/admin/pets/2"))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(adminGetPetResponseJson, result.getResponse().getContentAsString(), true);
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/pets.sql")
    @WithMockUser(roles = "admin")
    public void testUpdatePet() throws Exception {
        var adminUpdatePetRequestJson = adminUpdatePetRequestResource.getContentAsString(Charset.defaultCharset());
        var adminUpdatePetResponseJson = adminUpdatePetResponseResource.getContentAsString(Charset.defaultCharset());

        var result = api
                .perform(
                        patch("/admin/pets/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(adminUpdatePetRequestJson)
                )
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(adminUpdatePetResponseJson, result.getResponse().getContentAsString(), true);
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/pets.sql")
    @WithMockUser(roles = "admin")
    public void testDeletePet() throws Exception {
        api.perform(delete("/admin/pets/2")).andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/pets.sql")
    @WithMockUser
    public void testGetAllUnadoptedPets() throws Exception {
        var getAllUnadoptedPetsResponseJson = getAllUnadoptedPetsResponseResource
                .getContentAsString(Charset.defaultCharset());

        var result = api
                .perform(get("/pets"))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(
                getAllUnadoptedPetsResponseJson,
                result.getResponse().getContentAsString(),
                true
        );
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/pets.sql")
    @WithMockUser
    public void testGetUnadoptedPet() throws Exception {
        var getUnadoptedPetResponseJson = getUnadoptedPetResponseResource.getContentAsString(Charset.defaultCharset());

        var result = api
                .perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(getUnadoptedPetResponseJson, result.getResponse().getContentAsString(), true);
    }
}
