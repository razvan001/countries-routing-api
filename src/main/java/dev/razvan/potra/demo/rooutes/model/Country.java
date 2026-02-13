package dev.razvan.potra.demo.rooutes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonProperty("cca3")
    private String code;

    @JsonProperty("borders")
    private List<String> borders;
}
