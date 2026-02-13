package dev.razvan.potra.demo.rooutes.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(name = "RouteResponse", description = "Route result containing ordered list of country ISO codes")
public class RouteResponse {

    @Schema(description = "Ordered list of country ISO alpha-3 codes", example = "[\"CZE\", \"AUT\", \"ITA\"]")
    private List<String> route;
}

