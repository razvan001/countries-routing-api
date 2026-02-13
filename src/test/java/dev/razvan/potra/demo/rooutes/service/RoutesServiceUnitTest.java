package dev.razvan.potra.demo.rooutes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutesServiceUnitTest {

    @Mock
    private CountriesFetcher countriesFetcher;

    private RoutesService service;

    @BeforeEach
    void setUp() {
        service = new RoutesService(countriesFetcher, new ObjectMapper(), "http://example/countries.json");
    }

    @Test
    void should_computeRoute_when_neighborsConnect() {
        var json = "[{" +
                "\"cca3\":\"CZE\",\"borders\":[\"AUT\"]},{" +
                "\"cca3\":\"AUT\",\"borders\":[\"ITA\"]},{" +
                "\"cca3\":\"ITA\",\"borders\":[]}]";

        when(countriesFetcher.fetch("http://example/countries.json")).thenReturn(json);

        var route = service.computeRoute("CZE", "ITA");

        assertThat(route).containsExactly("CZE", "AUT", "ITA");
    }

    @Test
    void should_returnEmpty_when_noPathFound() {
        var json = "[{\"cca3\":\"AAA\",\"borders\":[]},{\"cca3\":\"BBB\",\"borders\":[]}]";

        when(countriesFetcher.fetch("http://example/countries.json")).thenReturn(json);

        var route = service.computeRoute("AAA", "BBB");

        assertThat(route).isEmpty();
    }
}
