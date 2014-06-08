package ua.charger.map.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.Answer;
import ua.charger.map.dto.ChargerPointDTO;

import java.util.List;

public interface PointController {

    void add(ChargerPointDTO chargerPointDTO);

    Answer<List<ChargerPointDTO>> list(double latStart, double lngStart, double latEnd, double lngEnd);

    Answer<Integer> found(int id);

    Answer<Integer> notFound(int id);
}
