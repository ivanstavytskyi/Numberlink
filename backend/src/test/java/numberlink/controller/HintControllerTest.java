package numberlink.controller;

import numberlink.exceptions.RestExceptionHandler;
import numberlink.game.core.CheckCellState;
import numberlink.game.core.CheckFlowHead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HintControllerTest {

    @Mock private CheckCellState checkCellState;
    @Mock private CheckFlowHead checkFlowHead;

    @InjectMocks private HintController hintController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(hintController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void hintCheck_whenNoMap_returns404() throws Exception {
        mockMvc.perform(post("/api/hint-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[[0]]"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No active map"));
    }

    @Test
    void hintCheck_whenMapSizeMismatch_returns400() throws Exception {
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("width", 7);
        session.setAttribute("height", 7);
        session.setAttribute("map_solved", new int[8][8]);

        mockMvc.perform(post("/api/hint-check")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[[0]]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error. Transfered map size, not answer original size."));
    }
}
