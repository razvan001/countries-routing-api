package dev.razvan.potra.demo.rooutes.web;

import dev.razvan.potra.demo.rooutes.service.RoutesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoutesController.class)
class RoutesControllerApiTest {

  private static final String ROUTING_URL = "/routing/{origin}/{destination}";

  @MockitoBean
  private RoutesService routesService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void should_returnRoute_when_pathExists() throws Exception {
    given(routesService.computeRoute("CZE", "ITA"))
        .willReturn(java.util.List.of("CZE", "AUT", "ITA"));

    mockMvc.perform(get(ROUTING_URL, "CZE", "ITA")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.route[0]").value("CZE"))
        .andExpect(jsonPath("$.route[1]").value("AUT"))
        .andExpect(jsonPath("$.route[2]").value("ITA"));
  }

  @Test
  void should_returnBadRequest_when_noLandCrossing() throws Exception {
    given(routesService.computeRoute("AAA", "BBB"))
        .willReturn(java.util.List.of());

    mockMvc.perform(get(ROUTING_URL, "AAA", "BBB")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
