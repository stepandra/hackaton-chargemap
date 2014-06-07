package ua.charger.map.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.Answer;
import ua.charger.map.dto.ChargerPointDTO;

import java.util.List;

public interface PointController {
    /**
     * @param notFound not required
     */
    void add(float lat, float lng, String description, boolean notFound);

    Answer<List<ChargerPointDTO>> list(float latStart, float lngStart, float latEnd, float lngEnd);

    Answer<Integer> found(int id);

    Answer<Integer> notFound(int id);
}
