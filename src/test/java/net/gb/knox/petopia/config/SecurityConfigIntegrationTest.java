package net.gb.knox.petopia.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc api;

    @Test
    public void testAuthenticatedEndpointWithAnonymousUser() throws Exception {
        api.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testAuthenticatedEndpointWithBasicUser() throws Exception {
        api.perform(get("/")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testAuthenticatedEndpointWithAdminUser() throws Exception {
        api.perform(get("/")).andExpect(status().isNotFound());
    }

    @Test
    public void testAdminEndpointWithAnonymousUser() throws Exception {
        api.perform(get("/admin")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testAdminEndpointWithBasicUser() throws Exception {
        api.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testAdminEndpointWithAdminUser() throws Exception {
        api.perform(get("/admin")).andExpect(status().isNotFound());
    }
}
