package dev.razvan.potra.demo.rooutes.web;

import dev.razvan.potra.demo.rooutes.web.dto.RouteResponse;
import dev.razvan.potra.demo.rooutes.web.service.RoutesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Tag(name = "Routing")
@RestController
@RequestMapping(path = "/routing", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoutesController {

    private final RoutesService routesService;

    public RoutesController(RoutesService routesService) {
        this.routesService = routesService;
    }

    @Operation(summary = "Get route between two countries",
            description = "Returns a list of country codes representing the route between origin and destination.")
    @GetMapping("/{origin}/{destination}")
    public RouteResponse getRoute(
            @Parameter(description = "Origin country ISO code", example = "CZE")
            @PathVariable String origin,
            @Parameter(description = "Destination country ISO code", example = "ITA")
            @PathVariable String destination) {
        var route = routesService.computeRoute(origin, destination);
        if (route.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No land crossing between origin and destination");
        }
        return new RouteResponse(route);
    }
}
