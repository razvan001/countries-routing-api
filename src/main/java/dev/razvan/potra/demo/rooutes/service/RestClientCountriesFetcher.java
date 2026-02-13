package dev.razvan.potra.demo.rooutes.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientCountriesFetcher implements CountriesFetcher {

    private final RestClient restClient;

    public RestClientCountriesFetcher(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public String fetch(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}

