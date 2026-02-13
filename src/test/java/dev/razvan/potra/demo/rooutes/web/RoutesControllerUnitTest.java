package dev.razvan.potra.demo.rooutes.web;

import dev.razvan.potra.demo.rooutes.web.service.RoutesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutesControllerUnitTest {

    @Mock
    private RoutesService routesService;

    private RoutesController controller;

    @BeforeEach
    void setUp() {
        controller = new RoutesController(routesService);
    }

    @Test
    void should_returnRoute_when_pathExists() {
        when(routesService.computeRoute("CZE", "ITA")).thenReturn(java.util.List.of("CZE", "AUT", "ITA"));

        var response = controller.getRoute("CZE", "ITA");

        assertThat(response).isNotNull();
        assertThat(response.getRoute()).containsExactly("CZE", "AUT", "ITA");
    }

    @Test
    void should_throwBadRequest_when_noLandCrossing() {
        when(routesService.computeRoute("AAA", "BBB")).thenReturn(java.util.List.of());

        try {
            controller.getRoute("AAA", "BBB");
            fail("Expected ResponseStatusException");
        } catch (ResponseStatusException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(ex.getReason()).isEqualTo("No land crossing between origin and destination");
        }
    }
}

