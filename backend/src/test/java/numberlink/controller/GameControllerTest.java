package numberlink.controller;

import numberlink.exceptions.RestExceptionHandler;
import numberlink.game.core.CheckSolution;
import numberlink.game.core.CreateMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock private CreateMap createMap;
    @Mock private CheckSolution checkSolution;

    @InjectMocks private GameController gameController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(gameController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void createMap_whenWidthTooSmall_returns400() throws Exception {
        mockMvc.perform(get("/api/create-map")
                        .param("width", "5")
                        .param("height", "7"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error generating map. Incorrect map size."));
    }

    @Test
    void createMap_whenGenerateFails_returns400() throws Exception {
        when(createMap.generateParallel(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Map generation failed"));

        mockMvc.perform(get("/api/create-map")
                        .param("width", "7")
                        .param("height", "7"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error generating map"));
    }

    @Test
    void createMap_whenSizeValid_returns200AndStoresSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        int[][] solved = new int[7][7];
        int[][] unsolved = new int[7][7];
        unsolved[0][0] = -1;

        when(createMap.generateParallel(7, 7)).thenReturn(solved);
        when(createMap.convertToUnsolved(solved)).thenReturn(unsolved);
        when(checkSolution.check(any(), any())).thenReturn(true);

        mockMvc.perform(get("/api/create-map")
                .param("width", "7")
                .param("height", "7")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(Arrays.deepToString(unsolved)));

        mockMvc.perform(get("/api/width").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));

        String emptyRow = "[0,0,0,0,0,0,0]";
        String gridJson = "[" + String.join(",", emptyRow, emptyRow, emptyRow,
                emptyRow, emptyRow, emptyRow, emptyRow) + "]";

        mockMvc.perform(post("/api/map-check")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gridJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getWidth_whenNoMap_returns404() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/api/width").session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No active map"));
    }

    @Test
    void mapCheck_whenNoMapInSession_returnsFalse() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/api/map-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[[0]]"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void mapCheck_whenSolutionMatches_returnsTrue() throws Exception{
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("width", 7);
        session.setAttribute("height", 7);
        session.setAttribute("map_solved", new int[7][7]);

        String emptyRow = "[0,0,0,0,0,0,0]";
        String gridJson = "[" + String.join(",", emptyRow, emptyRow, emptyRow,
                emptyRow, emptyRow, emptyRow, emptyRow) + "]";

        when(checkSolution.check(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/map-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gridJson)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
