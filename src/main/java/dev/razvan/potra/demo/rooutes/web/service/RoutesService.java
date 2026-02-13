package dev.razvan.potra.demo.rooutes.web.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.razvan.potra.demo.rooutes.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class RoutesService {

    private final CountriesFetcher countriesFetcher;
    private final ObjectMapper objectMapper;
    private final String countriesDataUrl;

    public RoutesService(CountriesFetcher countriesFetcher,
                         ObjectMapper objectMapper,
                         @Value("${countries.data.url}") String countriesDataUrl) {
        this.countriesFetcher = countriesFetcher;
        this.objectMapper = objectMapper;
        this.countriesDataUrl = countriesDataUrl;
    }

    public List<String> computeRoute(String origin, String destination) {
        var countries = fetchCountries();
        var graph = buildGraph(countries);
        return bfs(graph, origin, destination);
    }

    private List<Country> fetchCountries() {
        var responseBody = countriesFetcher.fetch(countriesDataUrl);
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Failed to parse countries data", e);
            throw new IllegalStateException("Failed to parse countries data", e);
        }
    }

    private Map<String, List<String>> buildGraph(List<Country> countries) {
        var graph = new HashMap<String, List<String>>();
        for (var country : countries) {
            graph.put(country.getCode(), Optional.ofNullable(country.getBorders()).orElseGet(List::of));
        }
        return graph;
    }

    private List<String> bfs(Map<String, List<String>> graph, String origin, String destination) {
        if (Objects.equals(origin, destination)) {
            return List.of(origin);
        }
        var queue = new ArrayDeque<String>();
        var visited = new HashSet<String>();
        var parent = new HashMap<String, String>();

        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            for (var neighbor : graph.getOrDefault(current, List.of())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    if (neighbor.equals(destination)) {
                        return reconstructPath(parent, origin, destination);
                    }
                    queue.add(neighbor);
                }
            }
        }
        return List.of();
    }

    private List<String> reconstructPath(Map<String, String> parent, String origin, String destination) {
        var path = new ArrayList<String>();
        var current = destination;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        if (!path.isEmpty() && path.getFirst().equals(origin)) {
            return path;
        }
        return List.of();
    }
}
