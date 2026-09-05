package lk.sunrise.clinic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAuthorizationIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void unauthenticatedStaffCannotOpenProtectedClinicPages() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "reception", roles = "RECEPTIONIST")
    void receptionistCannotOpenAdministratorFunctions() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = "ADMIN")
    void administratorCanOpenStaffManagement() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));
    }
}
