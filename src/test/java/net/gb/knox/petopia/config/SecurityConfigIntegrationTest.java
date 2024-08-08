package net.gb.knox.petopia.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@Import(SecurityConfig.class)
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc api;

    @Test
    public void testAuthenticatedEndpointWithAnonymousUser() throws Exception {
        api.perform(get("/test/security"))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testAuthenticatedEndpointWithBasicUser() throws Exception {
        api.perform(get("/test/security"))
           .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testAuthenticatedEndpointWithAdminUser() throws Exception {
        api.perform(get("/test/security"))
           .andExpect(status().isOk());
    }

    @Test
    public void testAdminEndpointWithAnonymousUser() throws Exception {
        api.perform(get("/admin/test/security"))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testAdminEndpointWithBasicUser() throws Exception {
        api.perform(get("/admin/test/security"))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testAdminEndpointWithAdminUser() throws Exception {
        api.perform(get("/admin/test/security"))
           .andExpect(status().isOk());
    }
}
