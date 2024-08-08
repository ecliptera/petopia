package net.gb.knox.petopia.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionController.class)
@Import({GlobalExceptionHandler.class})
public class GlobalExceptionHandlerUnitTest {

    @Autowired
    private MockMvc api;

    @Test
    @WithMockUser
    public void testResourceNotFound() throws Exception {
        api.perform(get("/test/exception/resource-not-found"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.code").value("ResourceNotFound"))
           .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    @WithMockUser
    public void testInvalidStatusAction() throws Exception {
        api.perform(get("/test/exception/invalid-status-action"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.code").value("InvalidStatusAction"))
           .andExpect(jsonPath("$.message").value("Invalid status action"));
    }

    @Test
    @WithMockUser
    public void testUnsupportedStatusAction() throws Exception {
        api.perform(get("/test/exception/unsupported-status-action"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.code").value("UnsupportedStatusAction"))
           .andExpect(jsonPath("$.message").value("Unsupported status action"));
    }

    @Test
    @WithMockUser
    public void testException() throws Exception {
        api.perform(get("/test/exception"))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.code").value("General"))
           .andExpect(jsonPath("$.message").value("Something went wrong"));
    }
}
