package ua.charger.map.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.ChargerPointDTO;

import java.util.List;

public interface PointController {
    void add(float lat, float lng, String description, boolean notFound);

    @RequestMapping("/found")
    int found(@RequestParam("chargerPointId") int id);

    @RequestMapping("/notFound")
    int notFound(@RequestParam("chargerPointId") int id);

    List<ChargerPointDTO> list(float latStart, float lngStart, float latEnd, float lngEnd);
}
